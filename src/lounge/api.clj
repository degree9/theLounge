(ns lounge.api
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :as compojure]
            [compojure.route :as route]
            [ring.middleware.defaults :as mwdefaults]
            [ring.middleware.session :refer [wrap-session]]
            [ring.util.response :as response]
            [castra.middleware :as castra]
            [monger.core :as mg]
            [monger.ring.session-store :refer [session-store]]
            [castra.core :refer [ex]]
            [hiccup.core :as hiccup]
            [silicone.util :as silicone]))


(compojure/defroutes app-routes
  (compojure/GET "/" req (hiccup/html [:head [:meta {:charset "utf-8"}]
                                       (silicone/import-polymer "bower_components")]
                                      [:body [:script {:type "text/javascript" :src "index.html.js"}]]))
  (route/resources "/" {:root ""}))

(def app
  (-> app-routes
        (castra/wrap-castra 'lounge.api.auth)
        (castra/wrap-castra-session "a 16-byte secret")
        (mwdefaults/wrap-defaults mwdefaults/api-defaults)))

(defn -main [& args]
  (let [port (or (Integer. (System/getenv "PORT")) 8080)]
    (run-jetty app {:port port})))

