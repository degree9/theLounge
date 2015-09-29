(ns lounge.boot
  {:boot/export-tasks true}
  (:require [boot.core          :as boot]
            [boot.util          :as util]
            [boot.task.built-in :as tasks]))

(defn- rm-prev-output [fileset]
  (comp (println(tasks/sift :include #{ #"(?i)(/.*).index.html.hl$"})
                (tasks/show))
  ))

(boot/deftask lounge-ext
  "Include external css and js files into a lounge entity."
  []
  (boot/with-pre-wrap fileset
    (let [tmp (boot/tmp-dir!)]
      (util/info "Compiling external files as index.html.hl ...\n")
        ;; If there are any output files from other instances of the lounge-ext
        ;; task in the pipeline we need to remove them from the classpath.
      ;(-> fileset rm-prev-output boot/commit!)

      (-> fileset boot/commit!))))
