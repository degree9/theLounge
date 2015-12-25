(set-env!
 :dependencies  '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.170"]
                  [adzerk/bootlaces          "0.1.13" :scope "test"]
                  [adzerk/boot-cljs          "1.7.170-3"]
                  [compojure                 "1.4.0"]
                  [com.cemerick/friend       "0.2.1"]
                  [com.novemberain/monger    "3.0.1"]
                  [danlentz/clj-uuid         "0.1.6"]
                  [hiccup                    "1.0.5"]
                  [hoplon/boot-hoplon        "0.1.10"]
                  [hoplon/hoplon             "6.0.0-alpha11"]
                  [hoplon/castra             "3.0.0-alpha1"]
                  [hoplon/javelin            "3.8.4"]
                  [jeluard/boot-notify       "0.2.0"]
                  [ring                      "1.4.0"]
                  [ring/ring-defaults        "0.1.5"]
                  [pandeiro/boot-http        "0.7.0"]
                  [org.clojars.hozumi/clj-commons-exec "1.2.0"]
                  [http-kit                  "2.1.19"]
                  [cheshire                  "5.5.0"]
                  [degree9/boot-bower        "0.2.3"]
                  [degree9/lounge.api        "0.2.0-SNAPSHOT"]
                  [degree9/lounge.ui         "0.2.0-SNAPSHOT"]
                  [degree9/lounge.pages      "0.1.0"]
                  [degree9/lounge.org        "0.1.0"]
                  [degree9/lounge.infra      "0.1.0"]
                  [degree9/lounge.report     "0.1.0"]
                  [degree9/lounge.drawer     "0.1.0"]
                  [degree9/lounge.setup      "0.2.0-SNAPSHOT"]
                  [degree9/lounge.state      "0.2.0-SNAPSHOT"]
                  [degree9/lounge.toolbar    "0.1.0"]
                  [degree9/lounge.workflow   "0.2.0-SNAPSHOT"]
                  [degree9/silicone          "0.4.0-SNAPSHOT"]
                  [degree9/lounge.boot       "0.3.0"  :scope "test"]
                  [degree9/boot-semver       "1.2.0"  :scope "test"]]
 :resource-paths #{"src"}
 :asset-paths #{"resources/assets"})

(require
 '[adzerk.bootlaces :refer :all]
 '[adzerk.boot-cljs :refer :all]
 '[pandeiro.boot-http :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[boot-semver.core :refer :all]
; '[jeluard.boot-notify :refer [notify]]
 '[degree9.boot-bower :refer [bower]]
 '[clj-commons-exec :as exec])

(task-options!
 pom {:project 'degree9/thelounge
      :description ""
      :url         ""
      :scm {:url ""}}
 aot {:namespace #{'lounge.api}}
 jar {:main 'lounge.api})

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

(deftask bower-deps
  "Fetch bower deps."
  []
  (bower :install {:iron-elements  "PolymerElements/iron-elements#master"
                   :paper-elements "PolymerElements/paper-elements#master"
                   :neon-elements  "PolymerElements/neon-elements#master"}))

(deftask build
  "Build theLounge for deployment"
  []
  (comp
   (hoplon :pretty-print  true)
   (cljs   :optimizations :none
           :source-map    true)
   (target :dir #{"target"})))

(deftask pack
  "Pack theLounge for deployment"
  []
  (comp
   (pom)
   (aot)
   (uber)
   (jar)))

(deftask dev
  "Build theLounge for local development."
  []
  (comp
   (build)
   (serve :handler 'lounge.api/app
          :reload true
          :port 8080)))

(deftask dev-osx
  "Build theLounge for local development on OS X."
  []
  (comp
   (bower-deps)
   (watch)
   (dev)
   ;(notify)
   (speak)))

(deftask prod
  "Build theLounge for production deployment."
  []
  (comp
   (bower-deps)
   (build)
   (version)
   (pack)))
