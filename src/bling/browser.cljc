(ns bling.browser
  (:require [clojure.string :as string]
            [bling.ansi :as ansi]
            [fireworks.core :refer [? !? ?> !?>]]
            [fireworks.color]))

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
          (str ansi/esc
               "(?:"
               (capture-group ansi/sgr-reset)
               "|"
               (capture-group ansi/sgr-x256-or-rgb-foreground-color-re)
               "|"
               (capture-group ansi/sgr-font-style)
               "|"
               (capture-group ansi/sgr-font-weight)
               "|"
               (capture-group ansi/sgr-text-decoration)
               "|"
               (capture-group ansi/sgr-freeform)
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
                  (if fgc? ansi/sgr-fgc-re ansi/sgr-bgc-re))
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
  (let [s (if fgc (string/replace s ansi/sgr-fgc-re "") s)
        s (if bgc (string/replace s ansi/sgr-bgc-re "") s)]
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
           "color"       "initial"})))


(defn- style-map->css-style-str [m]
  (string/join "; " (mapv (fn [[k v]] (str k ": " v)) m)))

(defn ansi-sgr-string->browser-dev-console-array
  "Intended to convert an ANSI SGR-tagged string to a format specifier-tagged
  string, with a corresponding vector of css style strings. The resulting string
  and styles can be supplied to a browser development console to format
  messages. See https://developer.chrome.com/docs/devtools/console/format-style
  for more background.
  
  Replaces all ANSI SGR tags in supplied string with CSS format specifier tag,
  \"%c\". Analyzes all ANSI SGR tags, and produces a vector of corresponding
  css style strings. Returns a vector of the format-specifier-tagged strings,
  followed by the contents of the styles array. In ClojureScript, returns an
   array.
   
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
        (string/replace s ansi/sgr-re "%c")

        ;; This analyzes all ansi-sgr escape sequences and produces vector of
        ;; css style strings
        vc                     
        (->> s
             (re-seq ansi/sgr-re)
             (reduce (fn [vc s]
                       (->> s
                            ansi-sgr->style-map
                            style-map->css-style-str
                            (conj vc)))
                     [with-format-specifiers]))]

    #?(:cljs (into-array vc) :clj vc)))



;; Cruft from core
;; TODO - Does this reordering need to be incorparated?
;; What about the reset?

#_(defn- reorder-text-decoration-shorthand [style]
  (if-let [td (or (get style :text-decoration)
                  (get style "text-decoration"))]
    (->> (reduce-kv (fn [acc k v]
                      (conj acc k v))
                    [:text-decoration td]
                    (dissoc style :text-decoration))
         (apply array-map))
    style))


#_(defn- updated-css [css-styles x]
  (if-let [style (some-> x
                         (maybe et-vec?)
                         enriched-text
                         :style)]
    (let [style  (->> (select-keys style browser-dev-console-props)
                      (reduce-colors-to-sgr-or-css :css)
                      (reorder-text-decoration-shorthand))
          ks     (keys style)
          resets (reduce (fn [acc k]
                           (assoc acc k "initial"))
                         {}
                         ks)]

      ;; (prn {:style style :ks ks :resets ks})

      (conj css-styles
            (css-stylemap->str style)
            (css-stylemap->str resets)))
    css-styles))
