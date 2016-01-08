(ns lounge.vault.core
  (:require [castra.core :refer [defrpc ex *session*]]
            [environ.core :refer [env]]))

(def vault-url   (env :vault-url))

(def api-version (or (env :vault-api) "v1"))

(def api-url     (clojure.string/join "/" [vault-url api-version]))

(defn default-reqmap []
  {:headers {"X-Vault-Token" (or (get-in @*session* [:lounge.vault/token]) "")}})
