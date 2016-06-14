(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./dependencies.edn"))
(defn get-devdeps []     (read-file "./dev_dependencies.edn"))

(set-env!
 :dependencies   (get-deps)
 ;:checkouts      (get-devdeps)
 :source-paths   #{"src"}
 :resource-paths #{"resources/assets"}
 ;:asset-paths    #{}
 )

(require
 '[adzerk.bootlaces :refer :all]
 '[adzerk.boot-cljs :refer :all]
 '[pandeiro.boot-http :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[boot-semver.core :refer :all]
 '[degree9.boot-bower :refer :all]
 '[clj-commons-exec :as exec]
 )

(task-options!
  bower    {:install          {:animate-css    "animate.css#master"
                               :semantic-ui    "semantic-ui#next"}}
  checkout {:dependencies     (get-devdeps)}
  cljs     {:optimizations    :advanced
            :source-map       true
            :compiler-options {:pseudo-names true
                               :pretty-print true
                               :language-in :ecmascript5
                               :parallel-build true}}
  hoplon   {:pretty-print     true}
  target   {:dir              #{"dist"}}
  serve    {:reload           true
            :port             8080})

(deftask exec
  "Apache Commons Exec wrapper task."
  [c cmd CMD [str] "Cmd to exec with args."]
  (let [cmd (or (:cmd *opts*) ["bash"])]
    (with-pre-wrap fileset
      (let [cmdresult @(exec/sh (into [] cmd))]
        (assert (= 0 (:exit cmdresult)) (:err cmdresult)))
      fileset)))

(deftask run-tests
  "Test"
  []
  clojure.core/identity)

(deftask build
  "Build theLounge for deployment"
  []
  (comp
    (hoplon)
    ;(from-cljsjs :profile :development)
    (cljs)
    ;(prerender)
    (target)))

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
    (serve)))

(deftask dev-osx
  "Build theLounge for local development on OS X."
  []
  (comp
    (bower)
    (sift :include #{#"bower_components/semantic-ui/dist/semantic.css"
                     #"bower_components/animate-css/animate.css"})
    (watch)
    (checkout)
    (dev)
    (speak)))

(deftask prod
  "Build theLounge for production deployment."
  []
  (comp
    (bower)
    (build)
    (version)
    (pack)))
