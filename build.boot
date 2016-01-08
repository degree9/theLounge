(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./resources/dependencies.edn"))
(defn get-devdeps []     (read-file "./resources/dev_dependencies.edn"))

(set-env!
 :dependencies (get-deps)
 :resource-paths #{"src"}
 :asset-paths #{"resources"})

(require
 '[adzerk.bootlaces :refer :all]
 '[adzerk.boot-cljs :refer :all]
 '[pandeiro.boot-http :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[boot-semver.core :refer :all]
; '[jeluard.boot-notify :refer [notify]]
 '[degree9.boot-bower :refer [bower]]
 '[clj-commons-exec :as exec]
 '[environ.boot :refer [environ]]
 )

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
   ;(environ :env {:vault-url "http://192.168.99.100:8200"})
   (watch)
   (checkout :dependencies (get-devdeps))
   (dev)
   (speak)))

(deftask prod
  "Build theLounge for production deployment."
  []
  (comp
   (bower-deps)
   (build)
   (version)
   (pack)))
