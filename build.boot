(set-env!
  :dependencies  '[[org.clojure/clojure       "1.7.0"]
                   [org.clojure/clojurescript "1.7.48"]
                   [adzerk/boot-cljs          "1.7.48-3"]
                   [adzerk/boot-cljs-repl     "0.1.10-SNAPSHOT"]
                   [adzerk/boot-reload        "0.3.2"]
                   [pandeiro/boot-http        "0.6.3"]
                   [hoplon/boot-hoplon        "0.1.7"]
                   [hoplon/hoplon             "6.0.0-alpha10"]
                   [hoplon/javelin            "3.8.2"]
                   [jeluard/boot-notify       "0.2.0"]
                   [boot/core                 "2.2.0"]
                   [buddy                     "0.7.1"]
                   [cljsjs/material           "1.0.4-0"]]

  :source-paths   #{"src"}
  :resource-paths #{"resources/assets"}
  :target-path    "resources/public")

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload :refer :all]
  '[pandeiro.boot-http :refer :all]
  '[hoplon.boot-hoplon :refer :all]
  '[jeluard.boot-notify :refer [notify]]
  '[lounge.boot :refer :all])

(swap! boot.repl/*default-dependencies*
       concat '[[lein-light-nrepl "0.1.0"]
                [org.clojure/clojurescript "1.7.48"]])

(swap! boot.repl/*default-middleware*
       conj 'lighttable.nrepl.handler/lighttable-ops)

(deftask dev
  "Build hoplon.io for local development."
  []
  (comp
    (watch)
    (hoplon :pretty-print true)
    (cljs   :optimizations :none
            :source-map    true)
    (serve  :dir (get-env :target-path))
    (notify)
    (speak)))

(deftask prod
  "Build hoplon.io for production deployment."
  []
  (comp
    (hoplon :pretty-print true)
    (cljs   :optimizations :advanced :source-map true)
    (prerender)))
