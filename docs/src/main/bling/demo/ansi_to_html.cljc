(ns bling.demo.ansi-to-html
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            [bling.util :as util :refer [when->]]
            [bling.browser :as browser]
            [fireworks.color]
            [taipei-404.html :refer [html->hiccup]]))

(def ^:private sr string/replace)

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


(defn space->nbsp [s]
  (if (string/index-of s " ")
    (sr s #" " "&nbsp;")
    s))

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

;; (def ^:private html-string-ahref-style
;;   "color:inherit;text-decoration:underline;text-underline-offset:3px;")


(defn ^:no-doc browser-dev-console-array->html
  "Takes a browser console array and converts it to an html string."
  ([vc]
   (browser-dev-console-array->html vc nil))
  ([vc {:keys [html-escape?]}]
   (let [html-escape? (if (false? html-escape?) false true)
         [s & vc]     vc
         escaped      (if (false? html-escape?) s (html-escape s))
         escaped      (-> escaped
                          space->nbsp
                          ;; (sr #"〠_〠AHREF〠_〠([^〠]+)〠_〠"
                          ;;     #(str "<a style=\""
                          ;;           html-string-ahref-style
                          ;;           "\" href=\""
                          ;;           (second %)
                          ;;           "\">"))
                          ;; (sr #"〠_〠/A〠_〠" "</a>")
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


;; (defn ^:no-doc with-hyperlinks [args]
;;   (walk/postwalk
;;    (fn [x]
;;      (if-let [href (and (vector? x)
;;                         (< 1 (count x))
;;                         (some-> x
;;                                 first
;;                                 (when-> map?)
;;                                 :href))]
;;        (let [text (second x)]
;;          [{:color                 :red
;;            :text-decoration-color :blue}
;;           (str "〠_〠AHREF〠_〠" href "〠_〠")
;;           text
;;           "〠_〠/A〠_〠"])
;;        x))
;;    args))


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


;; (defn ^:public ^:no-doc html-hyperlink
;;   "Converts a hyperlink in Bling's hiccup syntax to an escaped string
;;    representation which will get converted to html in 
;;    `bling.core/ansi-sgr-string->html`. This should only be used in the
;;    rare case when the intended output (of `bling.core/*` public fn) is
;;    not a system or browser console, but rather html on a webpage.
   
;;    Basic example
;;    ```clojure
;;    (->> (bling.core/bling [{:href \"www.pets.com\"} pets.com])
;;         bling.browser/ansi-sgr-string->browser-dev-console-array
;;         bling.browser/browser-dev-console-array->html-string)
;;    ```"
;;   {:desc    "Converts a hyperlink in Bling's hiccup syntax to an escaped string
;;              representation which will get converted to html in 
;;              `bling.core/ansi-sgr-string->html`. This should only be used in the
;;              rare case when the intended output (of `bling.core/*` public fn) is
;;              not a system or browser console, but rather html on a webpage."
;;    :examples [{:desc  "Basic example"
;;                :forms "(->> (bling.core/bling [{:href \"www.pets.com\"} pets.com])
;;                       |     bling.browser/ansi-sgr-string->browser-dev-console-array
;;                       |     bling.browser/browser-dev-console-array->html-string)"}]}
;;   [x]
;;   (if-let [href (and (vector? x)
;;                      (< 1 (count x))
;;                      (some-> x
;;                              first
;;                              (when-> map?)
;;                              :href))]
;;     (let [text (second x)]
;;       [{:color                 :red
;;         :text-decoration-color :blue}
;;        (str "〠_〠AHREF〠_〠" href "〠_〠")
;;        text
;;        "〠_〠/A〠_〠"])
;;     x))


(defn ^:public ^:no-doc ->html
  "Converts an ANSI SGR tagged string into html with inline styles."
  ([s]
   (->html s nil))
  ([s opts]
   (-> s
       browser/ansi-sgr-string->browser-dev-console-array
       (browser-dev-console-array->html opts))))


(defn ^:public ^:no-doc ->hiccup
  "Converts an ANSI SGR tagged string into hiccup."
  [s]
  (walk/postwalk (comp maybe-inline-style->map
                       maybe-replace-escaped-html)
                 (-> s
                     (->html #_{:html-escape? false})
                     nbsp->unicode
                     html->hiccup)))
