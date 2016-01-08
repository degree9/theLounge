(ns lounge.vault.sys
  (:require [lounge.api.core :as api]
            [lounge.vault.core :as vault]
            [org.httpkit.client :as http]
            [cheshire.core :as json]))

(def base-url          (clojure.string/join "/" [vault/api-url "sys"]))

(def sys-init          (clojure.string/join "/" [base-url       "init"]))

(def sys-seal-status   (clojure.string/join "/" [base-url       "seal-status"]))

(def sys-seal          (clojure.string/join "/" [base-url       "seal"]))

(def sys-unseal        (clojure.string/join "/" [base-url       "unseal"]))

(def sys-mounts        (clojure.string/join "/" [base-url       "mounts"]))

(def sys-remount       (clojure.string/join "/" [base-url       "remount"]))

(def sys-auth          (clojure.string/join "/" [base-url       "auth"]))

(def sys-auth-userpass (clojure.string/join "/" [sys-auth      "userpass"]))

(def sys-policy        (clojure.string/join "/" [base-url       "policy"]))

(def sys-audit         (clojure.string/join "/" [base-url       "audit"]))

(def sys-audit-hash    (clojure.string/join "/" [base-url       "audit-hash"]))

(def sys-renew         (clojure.string/join "/" [base-url       "renew"]))

(def sys-revoke        (clojure.string/join "/" [base-url       "revoke"]))

(def sys-revoke-prefix (clojure.string/join "/" [base-url       "revoke-prefix"]))

(def sys-leader        (clojure.string/join "/" [base-url       "leader"]))

(def sys-key-status    (clojure.string/join "/" [base-url       "key-status"]))

(def sys-rekey         (clojure.string/join "/" [base-url       "rekey"]))

(def sys-rekey-init    (clojure.string/join "/" [sys-rekey     "init"]))

(def sys-rekey-update  (clojure.string/join "/" [sys-rekey     "update"]))

(def sys-rotate        (clojure.string/join "/" [base-url       "rotate"]))

(def sys-raw           (clojure.string/join "/" [base-url       "raw"]))

(def sys-health        (clojure.string/join "/" [base-url       "health"]))

(defn get-init []
  (let [res @(http/get sys-init (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-init [reqmap]
  (let [res @(http/put sys-init (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-seal-status []
  (let [res @(http/get sys-seal-status (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-seal []
  (let [res @(http/put sys-seal (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-unseal [reqmap]
  (let [res @(http/put sys-unseal (merge (vault/default-reqmap) reqmap))]
    (api/assert (or (nil? (:error res)) (= "503" (:status res))) (:cause (:error res)))
    res))

(defn get-mounts []
  (let [res @(http/get sys-mounts (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-mounts-tune [mountpoint]
  (let [url (clojure.string/join "/" [sys-mounts mountpoint "tune"])
        res @(http/get url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-mounts [mountpoint reqmap]
  (let [url (clojure.string/join "/" [sys-mounts mountpoint])
        res @(http/put url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-mounts [mountpoint reqmap]
  (let [url (clojure.string/join "/" [sys-mounts mountpoint "tune"])
        res @(http/put url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn delete-mounts [mountpoint]
  (let [url (clojure.string/join "/" [sys-mounts mountpoint])
        res @(http/delete url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn post-remount [reqmap]
  (let [res @(http/post sys-remount (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-auth []
  (let [res @(http/get sys-auth (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn post-auth [mountpoint reqmap]
  (let [url (clojure.string/join "/" [sys-auth mountpoint])
        res @(http/post url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn delete-auth [mountpoint]
  (let [url (clojure.string/join "/" [sys-auth mountpoint])
        res @(http/delete url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-policy []
  (let [res @(http/get sys-policy (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-policy [name reqmap]
  (let [url (clojure.string/join "/" [sys-policy name])
        res @(http/put url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn delete-policy [name]
  (let [url (clojure.string/join "/" [sys-policy name])
        res @(http/delete url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-audit []
  (let [res @(http/get sys-audit (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-audit [name reqmap]
  (let [url (clojure.string/join "/" [sys-audit name])
        res @(http/put url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn delete-audit [name]
  (let [url (clojure.string/join "/" [sys-audit name])
        res @(http/delete url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn post-audit-hash [name]
  (let [url (clojure.string/join "/" [sys-audit-hash name])
        res @(http/delete url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-renew [leaseid reqmap]
  (let [url (clojure.string/join "/" [sys-renew leaseid])
        res @(http/put url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-revoke [leaseid]
  (let [url (clojure.string/join "/" [sys-revoke leaseid])
        res @(http/put url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-revoke-prefix [prefixpath]
  (let [url (clojure.string/join "/" [sys-revoke-prefix prefixpath])
        res @(http/put url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-leader []
  (let [res @(http/get sys-leader (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-key-status []
  (let [res @(http/get sys-key-status (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-rekey-init []
  (let [res @(http/get sys-rekey-init (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-rekey-init [reqmap]
  (let [res @(http/put sys-rekey-init (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn delete-rekey-init []
  (let [res @(http/delete sys-rekey-init (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-rekey-update [reqmap]
  (let [res @(http/put sys-rekey-init (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-rotate []
  (let [res @(http/put sys-rotate (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-raw [path]
  (let [url (clojure.string/join "/" [sys-raw path])
        res @(http/get sys-audit (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn put-raw [path reqmap]
  (let [url (clojure.string/join "/" [sys-raw path])
        res @(http/put url (merge (vault/default-reqmap) reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn delete-audit [path]
  (let [url (clojure.string/join "/" [sys-raw path])
        res @(http/delete url (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))

(defn get-health []
  (let [res @(http/get sys-health (vault/default-reqmap))]
    (api/assert (= nil (:error res)) (:cause (:error res)))
    res))
