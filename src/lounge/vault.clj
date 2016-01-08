(ns lounge.vault
  (:require [lounge.api.core   :as api]
            [lounge.vault.sys  :as sys]
            [lounge.vault.auth :as auth]
            [org.httpkit.client :as http]
            [cheshire.core :as json]
            [castra.core :refer [defrpc ex *session*]]
            [environ.core :refer [env]]
            [cemerick.friend.credentials :as creds]))

(defn init! [keyshards minshards]
  (let [res (sys/put-init {:body (json/generate-string {:secret_shares keyshards :secret_threshold minshards})})
        res (-> res :body (json/parse-string true))]
    (swap! *session* assoc ::keys (:keys res))
    (swap! *session* assoc ::token (:root_token res))))

(defn enable-auth! [authtype]
  (let []
    (-> (sys/post-auth authtype {:body (json/generate-string {:type authtype})}) :body (json/parse-string true))))

(defn get-sealstatus []
  (-> (sys/get-seal-status) :body (json/parse-string true)))

(defn is-sealed? []
  (:sealed (get-sealstatus)))

(defn unseal! [keyshards]
  (let [unseal* #(-> (sys/put-unseal {:body (json/generate-string {:key %})}) :body (json/parse-string true))]
    (doall (map #(if (is-sealed?) (unseal* %) (get-sealstatus)) (vec keyshards)))))

(defn reg-rootadmin! [username password]
  (auth/post-userpass-users username {:body (json/generate-string {:password password :policies "root"})}))

(defn authenticated? []
  (or (get @*session* ::token)
      (throw (ex "Unauthenticated." {:token (::token @*session*)}))))

(defn auth! [username password]
  (let [res   (auth/post-userpass-login username {:body (json/generate-string {:password password})})
        token (-> res :body (json/parse-string true) :auth :client_token)]
    (swap! *session* assoc ::token token)))

(defrpc auth [username password]
  {:rpc/pre [(auth! username password)]}
  (get @*session* ::token))

(defn unauth! []
  (swap! *session* dissoc ::token))

(defrpc unauth []
  {:rpc/pre [(unauth!)]}
  nil)

(defrpc is-auth []
  {:rpc/pre []}
  (get @*session* ::token))

(defrpc status []
  {:rpc/pre []}
  (-> (sys/get-health) :body (json/parse-string true)))

(defrpc init [keyshardsnum keyshardsreq username pass confpass]
  {:rpc/pre [(api/assert (= pass confpass) "Passwords do not match.")
             (api/assert (<= keyshardsreq keyshardsnum) "Cannot require more keyshards than creating.")
             (init! keyshardsnum keyshardsreq)
             (unseal! (take keyshardsreq (get-in @*session* [::keys])))
             (enable-auth! "userpass")
             (reg-rootadmin! username pass)]}
  (get-in @*session* [::keys]))
