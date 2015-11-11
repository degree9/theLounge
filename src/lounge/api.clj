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
            [ring.middleware.webjars :refer [wrap-webjars]]
            [hiccup.core :as hiccup]
            [silicone.util :as silicone]))
(compojure/defroutes app-routes
  (compojure/GET "/" req (hiccup/html [:head [:meta {:charset "utf-8"}]
                                       (silicone/import-polymer "bower_components")]
                                      [:body [:script {:type "text/javascript" :src "index.html.js"}]]))
  (route/resources "/" {:root ""}))

(defn handler [dburi]
  (let [{:keys [conn db]} (mg/connect-via-uri dburi)]
    (-> app-routes
        (wrap-webjars)
        (castra/wrap-castra 'lounge.api.auth)
        (wrap-session {:store (session-store db "sessions")})
        (mwdefaults/wrap-defaults mwdefaults/api-defaults)
        )))

(def app (handler "mongodb://flyboarder:17pali46@ds042908.mongolab.com:42908/thelounge"))

(defn -main []
  (run-jetty 'app {:port (System/getenv "PORT")}))

