(set-env!
 :dependencies  '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.48"]
                  [adzerk/boot-cljs          "1.7.48-6"]
                  [adzerk/boot-cljs-repl     "0.1.10-SNAPSHOT"]
                  [adzerk/boot-reload        "0.3.3-SNAPSHOT"]
                  [boot/core                 "2.2.0"]
                  [compojure                 "1.4.0"]
                  [com.cemerick/friend       "0.2.1"]
                  [com.novemberain/monger    "3.0.0-rc2"]
                  [cljsjs/material           "1.0.4-0"]
                  [danlentz/clj-uuid         "0.1.6"]
                  [hiccup                    "1.0.5"]
                  [hoplon/boot-hoplon        "0.1.9"]
                  [hoplon/hoplon             "6.0.0-alpha10"]
                  [hoplon/castra             "3.0.0-SNAPSHOT"]
                  [hoplon/javelin            "3.8.4"]
                  [jeluard/boot-notify       "0.2.0"]
                  [ring                      "1.4.0"]
                  [ring/ring-defaults        "0.1.5"]
                  [ring-webjars              "0.1.1"]
                  [pandeiro/boot-http        "0.7.0-SNAPSHOT"]
                  [degree9/boot-bower        "0.2.2"]]
 :source-paths   #{"src"}
 :asset-paths #{"resources/assets"})

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload :refer :all]
  '[pandeiro.boot-http :refer :all]
  '[hoplon.boot-hoplon :refer :all]
  '[jeluard.boot-notify :refer [notify]]
  '[degree9.boot-bower :refer [bower]])

(deftask run-test
  "Test"
  []
  clojure.core/identity)

(deftask build
  "Build theLounge for basic deployment"
  []
  (comp
   (hoplon :pretty-print true)
   (cljs   :optimizations :advanced :source-map true)))

(deftask dev
  "Build theLounge for local development within Docker."
  []
  (comp
    (bower :install {:iron-elements  "PolymerElements/iron-elements#^1.0.4"
                     :paper-elements "PolymerElements/paper-elements#^1.0.5"
                     :neon-elements  "PolymerElements/neon-elements#^1.0.0"})
    (watch)
    (hoplon :pretty-print true)
    (reload)
    (cljs   :optimizations :none
            :source-map    true)
    (serve
      :handler 'lounge.api/app
      :reload true
      :port 80)))

(deftask dev-osx
  "Build theLounge for local development on OS X."
  []
  (comp
    (dev)
    (notify)
    (speak)))

(deftask prod
  "Build theLounge for production deployment."
  [p port VAL int "Production Port number."]
  (build)
  (serve :handler 'lounge.api/app :port (or (:port *opts*) 80))
  (wait))
