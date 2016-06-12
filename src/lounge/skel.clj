(ns lounge.skel)

(defmacro with-breakpoint [bp f]
  (let [events (str bp)]
    `(on ~events ~f)))
