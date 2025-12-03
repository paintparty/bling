(ns bling.browser
  (:require [clojure.string :as string]
            [fireworks.core :refer [? !? ?> !?>]]
            [fireworks.color]))

(def ansi-esc "\u001b\\[")

(def sgr-color-base
  ";(?:(5);([0-9]{1,3})|(2);([0-9]{1,3});([0-9]{1,3});([0-9]{1,3});?)")

(def sgr-fgc-re
  (re-pattern (str "38" sgr-color-base)))

(def sgr-bgc-re
  (re-pattern (str "48" sgr-color-base)))

(def ansi-sgr-x256-or-rgb-foreground-color-re
  "38;(?:5;[0-9]{1,3}|2;[0-9]{1,3};[0-9]{1,3};[0-9]{1,3});?m")

(def ansi-sgr-reset
  "(?:0;?m|m)")

(def ansi-sgr-font-style
  "3;?m")

(def ansi-sgr-font-weight
  "[01];?m")

(def ansi-sgr-text-decoration-base
  "(?:9|4(?::[1-5])?)")

(def ansi-sgr-text-decoration
  (str ansi-sgr-text-decoration-base ";?m"))

(def ansi-sgr-freeform
  (? (str "[0-9;" ansi-sgr-text-decoration-base "]*m")))

(defn- capture-group [s]
  (str "(" s ")"))

(defn- find-sgr-pattern2 [s]
  (let [[_
         reset
         just-color
         just-italic
         just-weight
         just-decoration
         freeform]
        (re-find
         (re-pattern 
          (str ansi-esc
               "(?:"
               (capture-group ansi-sgr-reset)
               "|"
               (capture-group ansi-sgr-x256-or-rgb-foreground-color-re)
               "|"
               (capture-group ansi-sgr-font-style)
               "|"
               (capture-group ansi-sgr-font-weight)
               "|"
               (capture-group ansi-sgr-text-decoration)
               "|"
               (capture-group ansi-sgr-freeform)
               ")"))
         s)]
    (or (some->> reset (vector "reset"))
        (some->> just-color (vector "color"))
        (some->> just-italic (vector "font-style"))
        (some->> just-weight (vector "font-weight"))
        (some->> just-decoration (vector "text-decoration"))
        (some->> freeform (vector "freeform")))))


(defn- sgr-color->map [s k]
  (let [fgc? (= k :fgc)
        [_ x256? x256 rgb? r g b :as color]
        (re-find (re-pattern 
                  (if fgc? sgr-fgc-re sgr-bgc-re))
                 s)]
   (when color
     (hash-map
      (if fgc? "color" "background-color") 
      (if x256?
        (nth fireworks.color/xterm-colors-by-index 
             #?(:cljs (js/parseInt x256) :clj (Integer/parseInt x256)))
        (str "rgb(" r ", " g ", " b ")"))))))


(def sgr-style-by-id
  {"1"   ["font-weight" "bold"]
   "0"   ["font-weight" "normal"]
   "3"   ["font-style" "italic"]
   "9"   ["text-decoration" "line-through"]
   "4" ["text-decoration" "underline" "text-decoration-style" "straight"]
   "4:1" ["text-decoration" "underline" "text-decoration-style" "straight"]
   "4:2" ["text-decoration" "underline" "text-decoration-style" "double"]
   "4:3" ["text-decoration" "underline" "text-decoration-style" "wavy"]
   "4:4" ["text-decoration" "underline" "text-decoration-style" "dotted"]
   "4:5" ["text-decoration" "underline" "text-decoration-style" "dashed"]})


(defn- sgr-text-styling-coll [s]
  (-> s 
      (string/replace #"m$" "")
      (string/split #";")))


(defn- sgr-text-styling [s fgc bgc]
  (let [s (if fgc (string/replace s sgr-fgc-re "") s)
        s (if bgc (string/replace s sgr-bgc-re "") s)]
    (sgr-text-styling-coll s)))


(defn- text-style-map [s fgc bgc]
  (->> (sgr-text-styling s fgc bgc)
       (reduce (fn [acc v]
                 (apply conj acc (get sgr-style-by-id v)))
               [])
       (apply hash-map)
       (merge fgc bgc)))


(defn- ansi-sgr->style-map [s]
  (let [[tag v] (find-sgr-pattern2 s)]
    ;; (? :- [tag v])
    (cond (= "freeform" tag)
          (let [fgc       (sgr-color->map v :fgc)
                bgc       (sgr-color->map v :bgc)
                style-map (text-style-map v fgc bgc)]
            style-map)

          (= "color" tag)
          (sgr-color->map v :fgc)

          (contains? #{"font-style" "text-decoration" "font-weight"} tag)
          (text-style-map v nil nil)

          :else
          {"line-height" "1.4"
           "color"       "default"})))


(defn- style-map->css-style-str [m]
  (string/join "; " (mapv (fn [[k v]] (str k ": " v)) m)))

(def ansi-sgr-re (re-pattern (str ansi-esc ansi-sgr-freeform)))
(def ansi-sgr-unstyled-spaces-re (re-pattern (str ansi-esc "m +" ansi-esc "0m")))

(defn ansi-sgr-string->browser-dev-console-array
  "Intended to convert an ANSI SGR-tagged string to a format specifier-tagged
  string, with a corresponding vector of css style strings. The resulting string
  and styles can be supplied to a browser development console to format
  messages. See https://developer.chrome.com/docs/devtools/console/format-style
  for more background.
  
  Replaces all ANSI SGR tags in supplied string with CSS format specifier tag,
  \"%c\". Analyzes all ANSI SGR tags, and produces a vector of corresponding
  css style strings. Returns a vector consiting of the format-specifier-tagged
  string, followed by the contents of the styles array. In ClojureScript,
  returns an array.
   
  Example usage in ClojureScript:

  ```Clojure
  (def console-array
    (->> :foo
         bling.hifi/hifi
         ansi-sgr-string->browser-dev-console-array))
  ;; =>
  ;; [\"%c:foo%c\",
  ;;  \"color: rgb(122, 62, 157)\",
  ;;  \"line-height: 1.4; color: default\"]

  ;; Print the value, with formatting, to dev console:
  (.apply js/console.log js/console console-array)
  ```
  "
  [s]
  (let [
        ;; This removes redundant unstyled spaces
        ;; TODO - test perf with and without
        ;; s             
        ;; (string/replace s ansi-sgr-unstyled-spaces-re "")

        ;; Replaces all ANSI SGR tags with CSS format specifier tag
        with-format-specifiers 
        (string/replace s ansi-sgr-re "%c")

        ;; This analyzes all ansi-sgr escape sequences and produces vector of
        ;; css style strings
        vc                     
        (->> s
             (re-seq ansi-sgr-re)
             (reduce (fn [vc s]
                       (->> s
                            ansi-sgr->style-map
                            style-map->css-style-str
                            (conj vc)))
                     [with-format-specifiers]))]

    #?(:cljs (into-array vc) :clj vc)))
