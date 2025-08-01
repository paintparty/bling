(ns bling.stubs.core
  (:require [clojure.string :as string]))


;; TODO - Maybe this be in colors ns
(def ^:public system-colors-source
  {"system-black"   {:sgr 0}
   "system-maroon"  {:sgr 1}
   "system-green"   {:sgr 2}
   "system-olive"   {:sgr 3}
   "system-navy"    {:sgr 4}
   "system-purple"  {:sgr 5}
   "system-teal"    {:sgr 6}
   "system-silver"  {:sgr 7}
   "system-grey"    {:sgr 8}
   "system-red"     {:sgr 9}
   "system-lime"    {:sgr 10}
   "system-yellow"  {:sgr 11}
   "system-blue"    {:sgr 12}
   "system-fuchsia" {:sgr 13}
   "system-aqua"    {:sgr 14}
   "system-white"   {:sgr 15}})


;; TODO - Maybe this be in colors ns
(def ^:public bling-colors*
  (apply
   array-map
   ["red"        {:sgr      196
                  :css      "#ff0000"
                  :semantic "negative"}
    "orange"     {:sgr      208
                  :css      "#ff8700"
                  :semantic "warning"}
    "yellow"     {:sgr 178 :css "#d7af00"}
    "olive"      {:sgr 106 :css "#87af00"}
    "green"      {:sgr      40
                  :css      "#00d700"
                  :semantic "positive"}
    "blue"       {:sgr      39
                  :css      "#00afff"
                  :semantic "accent"}
    "purple"     {:sgr 141 :css "#af87ff"}
    "magenta"    {:sgr 201 :css "#ff00ff"}
    "gray"       {:sgr      247
                  :css      "#9e9e9e"
                  :semantic "subtle"}
    "black"      {:sgr 16 :css "#000000"}
    "white"      {:sgr 231 :css "#ffffff"}]))

;; skips the printing
(defn ^:public ?sgr [s] s)

(defn ^:public stack-trace-preview [])

;; TODO - Maybe this should just be in black and white?
(defn ^:public point-of-interest [_] "")

;; TODO - Maybe this should just be in black and white?
(defn ^:public callout [_])

;; TODO - Maybe this should just de-hiccupize input, and return a normal string?
;; (bling [:italic "Hey"] " " [:red "you"]) => "Hey you"
(defn ^:public bling
  "De-hiccupizes input, and returns a string with no ansi sgr decoration."
  [& coll] 
  (string/join (mapv #(cond 
                        (and (vector? %)
                             (= 2 (count %))
                             (let [el (first %)] (or (map? el) (keyword? el))))
                        (str (second %))
                        (not (coll? %))
                        %
                        :else 
                        nil)
                     coll)))

(defn ^:public print-bling [& coll]
  (println (apply bling coll)))
