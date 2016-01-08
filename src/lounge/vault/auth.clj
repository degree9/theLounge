(ns lounge.vault.auth
  (:require [lounge.api.core :as api]
            [lounge.vault.core :as vault]
            [org.httpkit.client :as http]
            [cheshire.core :as json]))

(def base-url                 (clojure.string/join "/" [vault/api-url "auth"]))

(def auth-token               (clojure.string/join "/" [base-url      "token"]))

(def auth-token-create        (clojure.string/join "/" [auth-token    "create"]))

(def auth-token-create-orphan (clojure.string/join "/" [auth-token    "create-orphan"]))

(def auth-token-lookup        (clojure.string/join "/" [auth-token    "lookup"]))

(def auth-token-lookup-self   (clojure.string/join "/" [auth-token    "lookup-self"]))

(def auth-token-revoke        (clojure.string/join "/" [auth-token    "revoke"]))

(def auth-token-revoke-self   (clojure.string/join "/" [auth-token    "revoke-self"]))

(def auth-token-revoke-orphan (clojure.string/join "/" [auth-token    "revoke-orphan"]))

(def auth-token-revoke-prefix (clojure.string/join "/" [auth-token    "revoke-prefix"]))

(def auth-token-renew         (clojure.string/join "/" [auth-token    "renew"]))

(def auth-token-renew-self    (clojure.string/join "/" [auth-token    "renew-self"]))

(def auth-userpass            (clojure.string/join "/" [base-url      "userpass"]))

(def auth-userpass-login      (clojure.string/join "/" [auth-userpass "login"]))

(def auth-userpass-users      (clojure.string/join "/" [auth-userpass "users"]))

(defn post-token-create [reqmap]
  (let [res (-> @(http/post auth-token-create (merge (vault/default-reqmap) reqmap)) (json/parse-string true))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-create-orphan [reqmap]
  (let [res @(http/post auth-token-create-orphan (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn get-token-lookup-self []
  (let [res @(http/get auth-token-lookup-self (vault/default-reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn get-token-lookup [token]
  (let [url (clojure.string/join "/" [auth-token-lookup token])
        res @(http/get url (vault/default-reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-revoke [token]
  (let [url (clojure.string/join "/" [auth-token-revoke token])
        res @(http/post url (vault/default-reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-revoke-self []
  (let [res @(http/post auth-token-revoke-self (vault/default-reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-revoke-orphan [token]
  (let [url (clojure.string/join "/" [auth-token-revoke-orphan token])
        res @(http/post url (vault/default-reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-revoke-prefix [prefix]
  (let [url (clojure.string/join "/" [auth-token-revoke-prefix prefix])
        res @(http/post url (vault/default-reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-renew-self [reqmap]
  (let [res @(http/post auth-token-renew-self (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-token-renew [token reqmap]
  (let [url (clojure.string/join "/" [auth-token-renew token])
        res @(http/post url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))

(defn post-userpass-login [username reqmap]
  (let [url (clojure.string/join "/" [auth-userpass-login username])
        res @(http/post url (merge (vault/default-reqmap) reqmap))]
    (api/assert (nil? (:errors res)) (clojure.string/join (:errors res)))
    res))

(defn post-userpass-users [username reqmap]
  (let [url (clojure.string/join "/" [auth-userpass-users username])
        res @(http/post url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:errors res)) (:cause (:errors res)))
    res))
