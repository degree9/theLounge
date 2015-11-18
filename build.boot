(set-env!
 :dependencies  '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.48"]
                  [adzerk/bootlaces          "0.1.13" :scope "test"]
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
                  [pandeiro/boot-http        "0.7.0-SNAPSHOT"]
                  [org.clojars.hozumi/clj-commons-exec "1.2.0"]
                  [degree9/boot-bower        "0.2.2"]]
 :source-paths   #{"src"}
 :asset-paths #{"resources/assets"})

(require
 '[adzerk.bootlaces :refer :all]
 '[adzerk.boot-cljs :refer :all]
 '[adzerk.boot-cljs-repl :refer :all]
 '[adzerk.boot-reload :refer :all]
 '[pandeiro.boot-http :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[jeluard.boot-notify :refer [notify]]
 '[degree9.boot-bower :refer [bower]]
 '[ring.adapter.jetty :refer [run-jetty]]
 '[clj-commons-exec :as exec])

(def +version+ "0.1.0")

(task-options!
 pom {:project 'degree9/thelounge
      :version +version+
      :description ""
      :url         ""
      :scm {:url ""}}
 aot {:namespace #{'lounge.api}}
 jar {:main 'lounge.api}
 )

(deftask exec
  "Apache Commons Exec wrapper task."
  [c cmd CMD [str] "Cmd to exec with args."]
  (let [cmd (or (:cmd *opts*) ["bash"])]
    (with-pre-wrap fileset
      (let [cmdresult @(exec/sh (into [] cmd))]
        (assert (= 0 (:exit cmdresult)) (:err cmdresult)))
      fileset)))

(deftask run-test
  "Test"
  []
  clojure.core/identity)

(deftask build-bower
  "Fetch bower deps."
  []
  (bower :install {:iron-elements  "PolymerElements/iron-elements#^1.0.4"
                    :paper-elements "PolymerElements/paper-elements#^1.0.6"
                    :neon-elements  "PolymerElements/neon-elements#^1.0.0"}))

(deftask build
  "Build theLounge for basic deployment"
  []
  (comp
   (build-bower)
   (hoplon :pretty-print true)
   (cljs   :optimizations :advanced)
   (pom)
   (aot)
   (uber)
   (jar)))

(deftask dev
  "Build theLounge for local development. (within Docker)"
  []
  (comp
    (build-bower)
    (watch)
    (hoplon :pretty-print true)
    (cljs   :optimizations :none
            :source-map    true)
    (serve
      :handler 'lounge.api/app
      :reload true
      :port 8000)))

(deftask dev-osx
  "Build theLounge for local development on OS X."
  []
  (comp
    (dev)
    (notify)
    (speak)))
