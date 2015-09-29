(ns lounge.lang.algo)

; can't figure out how to include algo.generic.functor
(defn fmap [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))
