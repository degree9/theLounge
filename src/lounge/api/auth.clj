(ns lounge.api.auth
  (:require [lounge.api.core :as api]
            [lounge.database.users :as users]
            [castra.core :refer [defrpc ex *session*]]
            (cemerick.friend [credentials :as creds])))

(def verify-creds (partial creds/bcrypt-credential-fn users/find-user-by-username))

(defn reg! [username email password]
  (users/new-user username email password))

(defn auth! [username password]
  (let [user (api/assert (verify-creds {:username username :password password}) "Bad username/password.")]
    (swap! *session* assoc ::identity user)))

(defn unauth! [] (swap! *session* assoc ::identity nil))

(defn authenticated? []
  (or (get @*session* ::identity)
      (throw (ex "Unauthenticated." {:identity (::identity @*session*)}))))

(defrpc auth [username password]
  {:rpc/pre [(auth! username password)
             (authenticated?)
             ]}
  true)

(defrpc unauth []
  {:rpc/pre [(unauth!)]}
  nil)

(defrpc reg [username email password password-conf]
  {:rpc/pre [(api/assert (= password password-conf) "Passwords don't match.")
             (api/assert (empty? (users/find-user-by-username username)) "Username not available.")
             (api/assert (empty? (users/find-user-by-email email)) "Email address has already been registered.")
             (reg! username email password)]}
  (auth username password))
