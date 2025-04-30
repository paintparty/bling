(ns bling.defs
  (:require
    [clojure.string :as string]
    #?(:cljs [bling.js-env :refer [node?]])))

(def gutter-char "█")
(def gutter-char-lower-seven-eighths "▆")
(def orange-tag-open "\033[38;5;208;1m")
(def bold-tag-open "\033[1m")
(def sgr-tag-close "\033[0;m")

(def bling-theme-names-set #{"light" "dark" "medium"})

;; TODO this is manually hack of public callout fn b/c having problems
;; with declaring it above this and then using it ... investigate.
(defn invalid-bling-theme-warning!
  [s]
  (println
   (apply str 
          (str "\n"
               orange-tag-open
               gutter-char-lower-seven-eighths
               sgr-tag-close
               "  "
               bold-tag-open "WARNING" sgr-tag-close
               "\n")
          (mapv 
           #(str orange-tag-open
                 gutter-char
                 sgr-tag-close
                 "  "
                 % 
                 "\n")
           [""
            "bling.core/bling-theme"
            ""
            "Invalid BLING_THEME environmental variable:"
            ""
            (str "BLING_THEME=" 
                 bold-tag-open 
                 "\"" s "\""
                 sgr-tag-close)
            (str "            "
                 orange-tag-open
                 (apply str (repeat (+ 2 (count s)) "^"))
                 sgr-tag-close)
            ""
            "Valid values for BLING_THEME:"
            (str bling-theme-names-set "")
            ""
            "\"light\" will increase the contrast of bling-styled"
            "messages on terminals with a light background."
            ""
            "\"dark\" will increase the contrast of bling-styled"
            "messages on terminals with a dark background."
            ""
            "The default value of \"medium\" will be used, which"
            "provides reasonable contrast on both light and"
            "dark terminal backgrounds."]))))

(def ^:public BLING_THEME
  #?(:clj  (System/getenv "BLING_THEME")
     :cljs bling.js-env/BLING_THEME))

(def ^:public bling-theme 
 (if (contains? bling-theme-names-set BLING_THEME)
   BLING_THEME
   (do
     (when (string? BLING_THEME)
       (invalid-bling-theme-warning! BLING_THEME))
     "medium")))
