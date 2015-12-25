(ns lounge.vault
  (:require [lounge.api.core :as api]
            [org.httpkit.client :as http]
            [cheshire.core :as json]
            [castra.core :refer [defrpc ex *session*]]
            (cemerick.friend [credentials :as creds])))

(defn online? []
  (let [res @(http/get "http://192.168.99.100:8200/v1/sys/init")]
    (api/assert (= nil (:error res)) (:cause (:error res)))))

(defrpc init? []
  {:rpc/pre [(online?)]}
  (let [res @(http/get "http://192.168.99.100:8200/v1/sys/init")]
    (:initialized (-> (:body res) (json/parse-string true)))))

(defrpc init! [username pass confpass]
  {:rpc/pre [(online?)
             (assert (= pass confpass) "Passwords do not match.")]}
  (let [res @(http/get "http://192.168.99.100:8200/v1/sys/init")]
    (:initialized (-> (:body res) (json/parse-string true)))))
