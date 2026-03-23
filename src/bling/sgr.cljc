(ns bling.sgr
  (:require [clojure.string :as string]
            [bling.hifi :refer [hifi]]))

(defn ^:no-doc sgr-highlighting-tags
  {:desc     "Given a style map and a `supports-color-level` int, produces a
              vector of opening and closing ansi-sgr tags for that style, when
              used with bling.hifi/hifi printing with highlighting via `:find`
              option"
   :examples [^:no-print
              {:desc  "foo"
               :forms '[[(let [m {:background-color "#670013"
                                  :color            "#ffe0e0"
                                  :font-weight      :bold}]
                           (sgr-highlighting-tags
                            (hifi {:a 1 :b 3}
                                  {:find {:path [:b] :style m}})
                            m))
                         ["\033[38;2;255;224;224;1;48;2;103;0;19m"
                          "\033m"]]]}]}
  [m n]
  (let [s (hifi '_
                {:find                 {:pred #(= % '_) :style m}
                 :supports-color-level n})
        i (string/index-of s "_")]
    [(subs s 0 i)
     (subs s (inc i))]))

            