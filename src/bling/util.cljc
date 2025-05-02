(ns bling.util
  (:require [clojure.string :as string]))

(defn sjr [n s] (string/join (repeat n s)))
