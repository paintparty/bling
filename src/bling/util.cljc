(ns bling.util
  (:require [clojure.string :as string]))

(defn maybe [x pred]
  (when (if (set? pred)
          (contains? pred x)
          (pred x))
    x))

(defn as-str [x]
  (str (if (or (keyword? x) (symbol? x)) (name x) x)))

(defn- regex? [v]
  #?(:clj  (-> v type str (= "class java.util.regex.Pattern"))
     :cljs (-> v type str (= "#object[RegExp]"))))

(defn- surround-with-quotes [x]
  (str "\"" x "\""))

(defn shortened
  "Stringifies a collection and truncates the result with ellipsis 
   so that it fits on one line."
  [v limit]
  (let [as-str                        (str v)
        regex?                        (regex? v)
        double-quotes?                (or (string? v) regex?)
        regex-pound #?(:cljs nil :clj (when regex? "#"))]
    (if (> limit (count as-str))
      (if double-quotes?
        (str regex-pound (surround-with-quotes as-str))
        as-str)
      (let [ret* (-> as-str
                     (string/split #"\n")
                     first)
            ret  (if (< limit (count ret*))
                   (let [ret (->> ret*
                                  (take limit)
                                  string/join)]
                     (str (if double-quotes?
                            (str regex-pound (surround-with-quotes ret))
                            ret)
                          (when-not double-quotes? " ")
                          "..."))
                   ret*)]
        ret))))

(defn sjr [n s] (string/join (repeat n s)))

(defn concatv
  "Concatenate `xs` and return the result as a vector."
  [& xs]
  (into [] cat xs))
