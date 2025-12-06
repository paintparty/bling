(ns bling.ansi)

(def esc "\u001b\\[")

(def sgr-color-base
  ";(?:(5);([0-9]{1,3})|(2);([0-9]{1,3});([0-9]{1,3});([0-9]{1,3});?)")

(def sgr-fgc-re
  (re-pattern (str "38" sgr-color-base)))

(def sgr-bgc-re
  (re-pattern (str "48" sgr-color-base)))

(def sgr-x256-or-rgb-foreground-color-re
  "38;(?:5;[0-9]{1,3}|2;[0-9]{1,3};[0-9]{1,3};[0-9]{1,3});?m")

(def sgr-reset
  "(?:0;?m|m)")

(def sgr-font-style
  "3;?m")

(def sgr-font-weight
  "[01];?m")

(def sgr-text-decoration-base
  "(?:9|4(?::[1-5])?)")

(def sgr-text-decoration
  (str sgr-text-decoration-base ";?m"))

(def sgr-freeform
  (str "(?:[0-9]|;|" sgr-text-decoration-base ")*m"))

(def sgr-re (re-pattern (str esc sgr-freeform)))

(def sgr-unstyled-spaces-re (re-pattern (str esc "m +" esc "0m")))

(defn sgr-count
  "Given a string containing ANSI SGR tags, returns the character count of the
   ANSI SGR tags"
  [s]
  (some->> s
           (re-seq sgr-re)
           (reduce (fn [n s] (+ n (count s))) 0)))

(defn adjusted-char-count
  "Given a string containing ANSI SGR tags, returns the character count of the
   string minus the char count of all the ANSI SGR tags."
  [s]
  (- (count s) (or (when-not (= s "\n") (sgr-count s)) 0)))
