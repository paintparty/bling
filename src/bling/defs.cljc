(ns bling.defs
  (:require
    [bling.macros :as macros :refer [keyed start-dbg! stop-dbg! nth-not-found]]
    [clojure.string :as string]))

(def banner-fonts-vec
  '[miniwi
    ansi-shadow
    drippy
    big
    big-money
    rounded
    isometric-1])

(def banner-fonts-set
  (into #{} banner-fonts-vec))

(def gutter-char "█")
(def gutter-char-lower-seven-eighths "▆")

(def sideline-bold-char-top "┏")
(def sideline-bold-char "┃")
(def sideline-bold-char-bottom "┗")

(def internal-warning-border-chars
  {:gutter        [gutter-char-lower-seven-eighths
                   gutter-char 
                   gutter-char]
   :sideline-bold [sideline-bold-char-top
                   sideline-bold-char 
                   sideline-bold-char-bottom]})

(def internal-warning-border-style :gutter)


(def bling-mood-names-set #{"light" "dark" "medium"})

(def ^:public NO_COLOR
  (macros/no-color-env-var))

(def ^:public FORCE_COLOR
  (macros/force-color-env-var))

(def ^:public no-color? 
 (boolean
  (when-not (and (string? FORCE_COLOR)
                 (not (string/blank? FORCE_COLOR)))
    (and (string? NO_COLOR)
         (not (string/blank? NO_COLOR))))))

(def orange-tag-open
  (when-not no-color? "\033[38;5;208;1m"))

(def bold-tag-open "\033[1m")

(def sgr-tag-close "\033[0;m")

(def orange-tag-close
  (when-not no-color? sgr-tag-close))

;; Maybe move this to fireworks, as fireworks needs to check for BLING_MOOD as well?
;; TODO - Refactor with warning template in bling.core
(defn invalid-bling-mood-warning!
  [s]
  (let [[border-char-top
         border-char
         border-char-bottom]
        (get internal-warning-border-chars
             internal-warning-border-style)
        ] 
    (println
     (str (apply str 
                 (str "\n"
                      orange-tag-open
                      border-char-top
                      orange-tag-close
                      "  "
                      bold-tag-open "WARNING" sgr-tag-close
                      "\n")
                 (mapv 
                  #(str orange-tag-open
                        border-char
                        orange-tag-close
                        "  "
                        % 
                        "\n")
                  [""
                   "bling.core/bling-mood"
                   ""
                   "Invalid BLING_MOOD environmental variable:"
                   ""
                   (str "BLING_MOOD=" 
                        bold-tag-open 
                        "\"" s "\""
                        sgr-tag-close)
                   (str "            "
                        orange-tag-open
                        (apply str (repeat (+ 2 (count s)) "^"))
                        orange-tag-close)
                   ""
                   "Valid values for BLING_MOOD:"
                   (str bling-mood-names-set "")
                   ""
                   "\"light\" will increase the contrast of bling-styled"
                   "messages on terminals with a light background."
                   ""
                   "\"dark\" will increase the contrast of bling-styled"
                   "messages on terminals with a dark background."
                   ""
                   "The default value of \"medium\" will be used, which"
                   "provides reasonable contrast on both light and"
                   "dark terminal backgrounds."]))
          orange-tag-open
          border-char-bottom
          orange-tag-close))))

(def ^:public BLING_MOOD
  (macros/bling-mood-env-var))


(def ^:public bling-mood 
 (if (contains? bling-mood-names-set BLING_MOOD)
   BLING_MOOD
   (do
     (when (string? BLING_MOOD)
       (invalid-bling-mood-warning! BLING_MOOD))
     "medium")))
