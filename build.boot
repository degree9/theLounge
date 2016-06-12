(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./dependencies.edn"))
(defn get-devdeps []     (read-file "./dev_dependencies.edn"))

(set-env!
 :dependencies   (get-deps)
 ;:checkouts      (get-devdeps)
 ;:source-paths   #{"resources/sass"}
 :resource-paths #{"src" "resources/assets"}
 ;:asset-paths    #{}
  )

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
      :scm {:url ""}})

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
  (bower :install {:animate-css    "animate.css#master"
                   :semantic-ui    "semantic-ui#next";"semantic-ui#2.1.8"
                   :iron-elements  "PolymerElements/iron-elements#1.0.8"
                   :paper-elements "PolymerElements/paper-elements#1.0.7"
                   :neon-elements  "PolymerElements/neon-elements#1.0.0"}))

(deftask build
  "Build theLounge for deployment"
  []
  (comp
   (hoplon :pretty-print   true)
   ;(from-cljsjs :profile :development)
   (cljs   :optimizations  :advanced
           :source-map     true
           :compiler-options {:pseudo-names true
                              :pretty-print true
                              :language-in :ecmascript5
                              :parallel-build true}
           )
   (target :dir #{"target"})
   ))

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
   (serve :reload true
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
