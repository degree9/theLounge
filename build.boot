(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./dependencies.edn"))
(defn get-devdeps []     (read-file "./dev_dependencies.edn"))

(set-env!
 :dependencies   (get-deps)
 ;:checkouts      (get-devdeps)
 ;:source-paths   #{"src"}
 :resource-paths #{"src" "resources/assets"}
 ;:asset-paths    #{}
 )

(require
 '[adzerk.bootlaces :refer :all]
 '[adzerk.boot-cljs :refer :all]
 '[pandeiro.boot-http :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[boot-semver.core :refer :all]
 '[degree9.boot-bower :refer :all]
 '[tolitius.boot-check :as check]
 '[clj-commons-exec :as exec]
 )

(task-options!
  bower    {:install          {:animate-css    "animate.css#master"
                               :semantic-ui    "semantic-ui#next"}}
  checkout {:dependencies     (get-devdeps)}
  cljs     {:source-map       true
            :compiler-options {:pseudo-names true
                               :pretty-print true
                               :language-in :ecmascript5
                               :parallel-build true}}
  hoplon   {:pretty-print     true}
  target   {:dir              #{"dist"}}
  sift     {:include          #{#"bower_components/semantic-ui/examples"}
            :invert           true}
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

(deftask tests
  "Run code tests."
  []
  (comp
    (check/with-kibit)
    ;(check/with-yagni)
    ))

(deftask dev
  "Build theLounge for local development."
  []
  (comp
    (bower)
    (sift)
    (watch)
    (checkout)
    (hoplon)
    (cljs :optimizations :none)
    (target)
    (serve)
    (speak)
    (tests)))

(deftask prod
  "Build theLounge for production deployment."
  []
  (comp
    (bower)
    (sift)
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)
    (target)
    (version)))
