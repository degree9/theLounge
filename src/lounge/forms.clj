(ns lounge.forms
  (:require [javelin.core :refer [defc=]]))

(defmacro defv [name cell validation]
  `(defc= ~name [(if (~validation ~cell) true false) (~validation ~cell)]))

(defmacro defv= [name & validations]
  `(defc= ~name (empty? (filter false? (map first (list ~@validations))))))

(defmacro deferr [name validation message]
  `(defc= ~name (if-not (first ~validation) [~message ~validation] nil)))

(defmacro deferr= [name & errors]
  `(defc= ~name (filterv string? (map first (list ~@errors)))))
