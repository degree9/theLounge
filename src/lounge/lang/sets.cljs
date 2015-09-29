(ns lounge.lang.sets
  (:require [clojure.set :as sets]))

(defn intersects? [set1 set2]
  (not (empty? (sets/intersection set1 set2))))
