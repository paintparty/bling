(ns bling.browser
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            [fireworks.core :refer [? !? ?> !?>]]
            [bling.util :as util :refer [when->]]
            [bling.ansi :as ansi]
            [fireworks.color]))

(def ^:private sr string/replace)

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

(def ^:no-doc sgr-style-by-id
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
      (sr #"m$" "")
      (string/split #";")))

(defn- sgr-text-styling [s fgc bgc]
  (let [s (if fgc (sr s ansi/sgr-fgc-re "") s)
        s (if bgc (sr s ansi/sgr-bgc-re "") s)]
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
    (assoc (cond (= "freeform" tag)
                 (let [fgc       (sgr-color->map v :fgc)
                       bgc       (sgr-color->map v :bgc)
                       style-map (text-style-map v fgc bgc)]
                   style-map)

                 (= "color" tag)
                 (sgr-color->map v :fgc)

                 (contains? #{"font-style" "text-decoration" "font-weight"} tag)
                 (text-style-map v nil nil)

                 :else
                 {})
           "line-height"
           1.45)))

(defn- style-map->css-style-str [m]
  (string/join "; " (mapv (fn [[k v]] (str k ": " v)) m)))

(defn ^:public ansi-sgr-string->browser-dev-console-array
  "Converts an ANSI SGR-tagged string to a format specifier-tagged string, with
   a corresponding vector of css style strings. The resulting string and styles
   can be supplied to a browser development console to format messages.
   See https://developer.chrome.com/docs/devtools/console/format-style for more
   background.
   
   Replaces all ANSI SGR tags in supplied string with CSS format specifier tag,
   \"%c\". Analyzes all ANSI SGR tags, and produces a vector of corresponding
   css style strings. Returns a vector of the format-specifier-tagged strings,
   followed by the contents of the styles array. In ClojureScript, returns an
   array.
   
   To print the value, with formatting, to dev console:
   (.apply js/console.log js/console console-array)
   ```
   
   Usage in ClojureScript
   ```clojure
   (def
    console-array
    (ansi-sgr-string->browser-dev-console-array (bling [:red :foo])))
   ;; =>
   [\"%c:foo%c\"
    \"color: #bb4747\"
    \"line-height: 1.45; color: default\"]
   ```"
  {:desc
   "Converts an ANSI SGR-tagged string to a format specifier-tagged string, with
    a corresponding vector of css style strings. The resulting string and styles
    can be supplied to a browser development console to format messages.
    See https://developer.chrome.com/docs/devtools/console/format-style for more
    background.
    
    Replaces all ANSI SGR tags in supplied string with CSS format specifier tag,
    \"%c\". Analyzes all ANSI SGR tags, and produces a vector of corresponding
    css style strings. Returns a vector of the format-specifier-tagged strings,
    followed by the contents of the styles array. In ClojureScript, returns an
    array.

    To print the value, with formatting, to dev console:
    (.apply js/console.log js/console console-array)
    ```"

   :examples
   [{:desc  "Usage in ClojureScript"
     :forms '[[(def console-array
                 (ansi-sgr-string->browser-dev-console-array
                  (bling [:red :foo])))
               ["%c:foo%c",
                "color: #bb4747",
                "line-height: 1.45; color: default"]]]}]}
  [s]
  (let [;; This removes redundant unstyled spaces
        ;; TODO - test perf with and without
        ;; s             
        ;; (sr s ansi-sgr-unstyled-spaces-re "")

        ;; Replaces all ANSI SGR tags with CSS format specifier tag
        with-format-specifiers
        (sr s ansi/sgr-re "%c")

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

(defn- html-escape
  {:tldr    "Escapes &, <, >, \", ', and null characters in `s` for safe HTML inclusion."
   :options [:map
             [:likely?
              {:optional true
               :desc     "When true, applies all replacements unconditionally —
                          faster when the string is expected to contain many HTML
                          chars. Defaults to false (uses indexOf-guarded path)."}
              :boolean]
             [:detect-double-escape?
              {:optional? true
               :desc      "When true, also escapes lowercase 'e' as &#101; to
                           help detect double-escaping during development.
                           Defaults to false."}]]}
  ([s] (html-escape s {}))
  ([s {:keys [likely? detect-double-escape?]
       :or   {likely?               false
              detect-double-escape? false}}]
   (if likely?
     ;; Fast path: replace all candidates unconditionally.
     (cond-> s
       true                  (sr #"&"    "&amp;")
       true                  (sr #"<"    "&lt;")
       true                  (sr #">"    "&gt;")
       true                  (sr #"\""   "&quot;")
       true                  (sr #"'"    "&#39;")
       true                  (sr #"\x00" "&#0;")
       detect-double-escape? (sr #"e"    "&#101;"))

     ;; Optimised path: skip replacement if the char isn't present.
     (let [all-re (if detect-double-escape?
                    #"[\x00&<>\"'e]"
                    #"[\x00&<>\"']")]
       (if-not (re-find all-re s)
         s
         (cond-> s
           (string/index-of s "&")    (sr #"&"    "&amp;")
           (string/index-of s "<")    (sr #"<"    "&lt;")
           (string/index-of s ">")    (sr #">"    "&gt;")
           (string/index-of s "\"")   (sr #"\""   "&quot;")
           (string/index-of s "'")    (sr #"'"    "&#39;")
           (string/index-of s "\u0000") (sr #"\x00" "&#0;")
           (and detect-double-escape?
                (string/index-of s "e")) (sr #"e" "&#101;")))))))

(defn- inline-style->map
  ([v]
   (inline-style->map v nil))
  ([v {:keys [keywordize-keys?]}]
   (as-> v $
     (string/split $ #";")
     (map #(let [kv    (-> % string/trim (string/split #":"))
                 [k v] (map string/trim kv)]
             [(if keywordize-keys? (keyword k) k)
              v])
          $)
     (into {} $))))

(defn ^:no-doc maybe-inline-style->map [x]
  (or (some-> x
              (when-> map?)
              :style
              (when-> string?)
              (inline-style->map {:keywordize-keys? true}))
      x))

(def ^:private html-string-ahref-style
  "color:inherit;text-decoration:underline;text-underline-offset:3px;")

(def ^:private html-string-ahref-style-map
  (inline-style->map html-string-ahref-style))

(defn ^:no-doc browser-dev-console-array->html-string
  ([vc]
   (browser-dev-console-array->html-string vc nil))
  ([vc {:keys [html-escape?]}]
   (let [html-escape? (if (false? html-escape?) false true)
         [s & vc]     vc
         escaped      (if (false? html-escape?) s (html-escape s))
         escaped      (-> escaped
                          (sr #"〠_〠AHREF〠_〠([^〠]+)〠_〠"
                              #(str "<a style=\""
                                    html-string-ahref-style
                                    "\" href=\""
                                    (second %)
                                    "\">"))
                          (sr #"〠_〠/A〠_〠" "</a>")
                          (sr #"\n" "<br>"))
         html         (reduce-kv (fn [s i style]
                                   (string/replace-first
                                    s
                                    #"%c"
                                    (if (odd? i)
                                      "</span>"
                                      (str "<span style=\"" style "\">"))))
                                 escaped
                                 (vec vc))]
     html)))

(defn ^:no-doc with-hyperlinks [args]
  (walk/postwalk
   (fn [x]
     (if-let [href (and (vector? x)
                        (< 1 (count x))
                        (some-> x
                                first
                                (when-> map?)
                                :href))]
       (let [text (second x)]
         [{:color                 :red
           :text-decoration-color :blue}
          (str "〠_〠AHREF〠_〠" href "〠_〠")
          text
          "〠_〠/A〠_〠"])
       x))
   args))

(defn ^:no-doc maybe-replace-escaped-html [x]
  (if (string? x)
    (-> x
        ;; (string/replace #"&nbsp;" "\u00A0")
        (string/replace #"&#39;" "'")
        (string/replace #"&amp;" "&")
        (string/replace #"&lt;" "\u003C")
        (string/replace #"&gt;" ">")
        (string/replace #"&quot;" "\""))
    x))

(defn ^:no-doc nbsp->unicode [s]
  (string/replace s #"&nbsp;" "\u00A0"))
