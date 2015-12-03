(ns lounge.api.core
  (:refer-clojure :exclude [assert])
  (:require [castra.core :refer [defrpc ex *session*]]))

(defmacro assert [expr & [msg]]
  `(if-not ~expr (throw (ex (or ~msg "Server error.") {:from ::assert}))
     ~expr))

(defn allow [] (constantly true))
(defn deny  [] nil)

