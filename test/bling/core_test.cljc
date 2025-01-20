(ns bling.core-test
  (:require [clojure.test :as test]
            [clojure.string :as string]
            [bling.core :refer [bling callout point-of-interest]]
            [bling.sample]
            [bling.macros :refer [blingf]]
            #?(:cljs [bling.core :refer [print-bling]])
            #?(:cljs [bling.js-env :refer [node?]])))


;; Experimental template literal
(let [css-str "\".wtf{color: red;}\""
      guess   "hihi"
      flags   ["--minify" "--bundle"]]
  (println (blingf "
[The].bold.blue.italic css str is:

[${css-str}].bold.red

${(str (reverse flags))}

Yes!")))



(def printer
  #?(:cljs (if node? println print-bling) :clj println))

#_(println (bling [:bold.system-maroon "HI"]))

#_(doseq [[nm] bling.core/system-colors-source]
  (println (bling [{:color nm :font-weight :bold} nm])))

;; (println (bling [{:color "red" :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color "red"} "Clean it all up"]))
;; (println (bling [{:color "system-red" :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color "system-red"} "Clean it all up"]))

;; (println (bling [{:color :red :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color :red} "Clean it all up"]))

;; ;; Orange?
;; (println (bling [{:color 208 :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color 208} "Clean it all up"]))

;; (println (bling [{:color :yellow :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color :yellow} "Clean it all up"]))

;; ;; Olive?
;; (println (bling [{:color 106 :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color 106} "Clean it all up"]))

;; (println (bling [{:color :green :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color :green} "Clean it all up"]))

;; (println (bling [{:color :blue :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color :blue} "Clean it all up"]))

;; ;; Purple?
;; (println (bling [{:color 141 :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color 141} "Clean it all up"]))

;; (println (bling [{:color :magenta :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color :magenta} "Clean it all up"]))

;; (println (bling [{:color :gray :font-weight :bold} "Clean it all up"]))
;; (println (bling [{:color :gray} "Clean it all up"]))


;; All the colors
;; (doseq [nm bling.core/colors-ordered]
;;   (println (bling [{:color nm :font-weight :bold} nm])))

(defn bling-basics
  ([]
   (bling-basics false))
  ([extras?]

   ;; PRIMITIVES ------------------------------------------------------------------
   (printer (bling [:bold "bold "]
                   [:italic "italic "]
                   [:underline "underline "]
                   [:strikethrough "strikethrough"]))

   (println)

   ;; Colors
   (printer (bling [:bold.red "Red"]
                   " "
                   [:bold.orange "Orange"]
                   " "
                   [:bold.yellow "Yellow"]
                   " "
                   [:bold.olive "Olive"]
                   " "
                   [:bold.green "Green"]
                   " "
                   [:bold.blue "Blue"]
                   " "
                   [:bold.purple "Purple"]
                   " "
                   [:bold.magenta "Magenta"]
                   " "
                   [:bold.gray "Gray"]
                   " "
                   [:bold.black "Black"]
                   " "
                   [:bold.white "White"] ))

   (println)

   ;; Background colors
   (printer (bling [:bold.red-bg.white " Red "]
                   " "
                   [:bold.orange-bg.white " Orange "]
                   " "
                   [:bold.yellow-bg.white " Yellow "]
                   " "
                   [:bold.olive-bg.white " Olive "]
                   " "
                   [:bold.green-bg.white " Green "]
                   " "
                   [:bold.blue-bg.white " Blue "]
                   " "
                   [:bold.blue-bg.white " Purple "]
                   " "
                   [:bold.magenta-bg.white " Magenta "]
                   " "
                   [:bold.gray-bg.white " Gray "]
                ;; " "
                ;; [:bold.black-bg.white " Black "]
                ;; " "
                ;; [:bold.white-bg.black " White "]
                   ))

   (println)

   ;; Semantic colors
   (printer (bling [:bold.negative "Negative"]
                   " "
                   [:bold.error "Error"]
                   " "
                   [:bold.warning "Warning"]
                   " "
                   [:bold.positive "Positive"]
                   " "
                   [:bold.info "Info"]
                   " "
                   [:bold.subtle "Subtle"]
                   " "
                   [:bold.neutral "Neutral"] ))

   (println)

   ;; Combo
   (printer (bling [:bold.italic "bold & italic"]
                   ", "
                   [:italic.blue "italic & colored"]
                   ", "
                   [:bold.italic.white.blue-bg
                    "bold & italic & colored & colored-bg"]
                   ", "
                   [:bold.italic.blue.underline
                    "bold & italic & colored & underline"]
                   ", "
                   [:bold.italic.blue.strikethrough 
                    "bold & italic & colored & strikethrough"]))


   ;; CALLOUT examples with light border ------------------------------------------
   
   (callout {:type :info}
            "Example callout, with :type of :info")

   (callout {:type  :info
             :label "My custom label"}
            "Example callout, with :type of :info and custom :label")

   (callout {:type :warning}
            "Example callout, with :type of :warning")

   (callout {:type :error}
            "Example callout, with :type of :error")

   (callout {:type  :positive
             :label "SUCCESS!"}
            "Example callout, with :type of :positive, and custom :label")

   ;; Off for readme banner
   (when extras? 
     (callout {:type :subtle}
              "Example callout, with :type of :subtle (or :gray)")

     (callout {:type :magenta}
              "Example callout, with :type of :magenta")

     (callout "Example callout, default")

     (callout
      {:label "HEY"}
      "Example callout, default with custom label"))

   (callout
    {:label       (bling [:magenta-bg.white.bold " HEY "])
     :padding-top 1}
    (bling "Example callout, styled-label, :padding-top of 1"))


   ;; CALLOUT examples with medium border ----------------------------------------
   
   (callout {:type          :info
             :border-weight :medium}
            "Example callout, with :type of :info")

   (callout {:type          :warning
             :border-weight :medium}
            "Example callout, with :type of :warning")

   (callout {:type          :error
             :border-weight :medium}
            "Example callout, with :type of :error")

   (callout {:type          :positive
             :label         "SUCCESS!"
             :border-weight :medium}
            "Example callout, with :type of :positive, and custom :label")


   ;; Off for readme banner
   (when extras?
     (callout {:type          :subtle
               :border-weight :medium}
              "Example callout, with :type of :subtle (or :gray)")

     (callout {:type          :magenta
               :border-weight :medium}
              "Example callout, with :type of :magenta")

     (callout {:border-weight :medium} "Example callout, default"))



   ;; CALLOUT examples with heavy border ------------------------------------------
   
   (callout {:type          :info
             :border-weight :heavy}
            "Example callout, with :type of :info")

   (callout {:type          :info
             :border-weight :heavy
             :label         "My custom label"}
            "Example callout, with :type of :info and custom :label")

   (callout {:type          :warning
             :border-weight :heavy}
            "Example callout, with :type of :warning")

   (callout {:type          :error
             :border-weight :heavy}
            "Example callout, with :type of :error")

   (callout {:type          :positive
             :label         "SUCCESS!"
             :border-weight :heavy}
            "Example callout, with :type of :positive, and custom :label")

   ;; Off for readme banner
   (when extras?
     (callout {:type          :subtle
               :border-weight :heavy}
              "Example callout, with :type of :subtle (or :gray)")

     (callout {:type          :magenta
               :border-weight :heavy}
              "Example callout, with :type of :magenta")

     (callout {:border-weight :heavy} "Example callout, default"))

   (callout
    {:type          :warning
     :border-weight :heavy
     :padding-left  4}
    "Example callout, with :type of :warning, padding-left of 4.")))



(defn example-custom-callout
  [{:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header "Your header message goes here."
                             :body   (str "The body of your template goes here."
                                          "\n"
                                          "Second line of copy."
                                          "\n"
                                          "Another line.")}
                            point-of-interest-opts)
        message      (point-of-interest poi-opts)
        callout-opts (merge callout-opts
                            {:padding-top 1})]
    (callout callout-opts message)))




;; CALLOUTS with poi -----------------------------------------------------------

(defn visual-test-suite []

   (callout {:label "Callout, no body"} "")

   (callout {:border-weight :heavy :label "Callout, no body"} "")
   
   (callout {:label "Callout, no body"} nil)

   (callout {:border-weight :heavy :label "Callout, no body"} nil)
   
   (callout {:label "Callout, no body"})

   (callout {:border-weight :heavy :label "Callout, no body"})
   
   (callout "Callout, only body")

   (callout (bling [:magenta "Callout, only body"]))

   (callout {:border-weight :heavy}  "Callout, only body")

   (callout {:label "Callout, no body"})

   (callout {:label "Callout, blank body"} "")

   (callout {:label ""})

   (callout "")

   (callout "just body")

   ;; This should issue a warning callout with point-of-interest
   (callout nil)

   ;; This should issue a warning callout with point-of-interest
   (callout [1 2 3])

   (callout "Default callout, no options")

   (callout {:padding-left 2} "Default callout, no label, left padding")

   (callout {:label "My label"} "Default callout, custom label")

   (callout {:type :warning} "Callout, type :warning, default label")

   (callout {:type  :warning
             :label "My warning"}
            "Callout, type :warning, custom label")

   (callout {:type  :warning
             :label (bling [:bold.yellow-bg.black " My warning "])}
            "Callout, type :warning, custom enriched label")

   (callout {:type           :warning
             :padding-top    1
             :padding-bottom 1
             :padding-left   2} 
            "Callout, type :warning, default label, custom padding")

   (callout {:type  :positive
             :label (bling [:positive-bg.white.bold " YES "])}
            "Callout, type :positve, enriched label")

   (callout {:label "YES"
             :type  "positive"}
            "Callout, type :positve, user label")

   (callout {:type :warning}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :type   :warning}))


   (callout {:type :magenta}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :type   :magenta}))

;; poi with (default) :margin-block of 1
   (callout {:type :error}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :type   :error}))
   (callout {:type          :magenta
             :border-weight :heavy}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :type   :magenta}))

;; poi with :margin-block of 0
   (callout {:type          :magenta
             :border-weight :heavy}
            (point-of-interest {:line         11
                                :column       2
                                :form         '(+ 1 1)
                                :file         "myfile.cljs"
                                :type         :magenta
                                :margin-block 0}))


   (callout {:type          :magenta
             :border-weight :heavy}
            (bling [:blue "One line."]
                   #_"\n"
                   #_[:red "red"]))

   (callout {:type          :magenta
             :border-weight :heavy
          ;; :label "foo"
             }
            (bling [:blue "1 of 2 lines."]
                   "\n"
                   [:red "2nd line"]))

;; ;; ;; Basics
   (printer (bling [:bold "bold"] ", " [:italic "italic"] ", or " [:blue "colored"]))


;; Callouts
   (callout
    {:type  :info
     :label (bling 
             [:magenta-bg.white.bold " "]
             [:red-bg.white.bold "R"]
             [:yellow-bg.white.bold "A"]
             [:green-bg.white.bold "I"]
             [:blue-bg.white.bold "N"]
             [:magenta-bg.white.bold "B"]
             [:red-bg.white.bold "O"]
             [:yellow-bg.white.bold "W"]
             [:blue-bg.white.bold " "])}
    (bling "Example callout, :type of :info, rainbow-bg label" ))

   (callout
    {:type  :info
     :label (bling 
             [:magenta.bold "R"]
             [:red.bold "A"]
             [:yellow.bold "I"]
             [:green.bold "N"]
             [:blue.bold "B"]
             [:magenta.bold "O"]
             [:red.bold "W"])}
    (bling "Example callout, :type of :info, rainbow-bg label" ))

   (callout
    {:type          :info
     :border-weight :heavy
     :label         (bling 
                     [:magenta.bold "R"]
                     [:red.bold "A"]
                     [:yellow.bold "I"]
                     [:green.bold "N"]
                     [:blue.bold "B"]
                     [:magenta.bold "O"]
                     [:red.bold "W"])}
    (bling "Example callout, :type of :info, rainbow-bg label" ))


   (callout
    {:type  :warning
     :label {:a :b}}
    "Example callout, with :type of :warning, and custom :label." )


   (println "\n\n\n")
  
  (bling-basics)

;; Default callout w/ poi, regex-form
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type :warning}})

;; Medium callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type          :warning
                             :border-weight :medium}})

;; Heavy callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type          :warning
                             :border-weight :heavy}})


;; Default callout w/ poi, regex-form
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type :error}})

;; Medium callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type          :error
                             :border-weight :medium}})

;; Heavy callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type          :error
                             :border-weight :heavy}})

;; Normal callout w/ poi, label enriched and regex form
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   #"All of this regex should underlined"
                             :type   :error}
    :callout-opts           {:label (bling [:red-bg.bold.white " ERROR "])
                             :type  :error}})

;; callout w/ enriched header and body on poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error
                             :header (bling [:blue.italic "Enriched header."])
                             :body   (bling [:green.italic "Enriched body."]
                                            "\n"
                                            [:yellow.italic "Body line 2."])}
    :callout-opts           {:type :error}})
  #_(callout
     {:type           :error
      :margin-top     3 ; default is 1
      :margin-bottom  3 ; default is 1
      :padding-top    2 ; default is 0
      :padding-bottom 2 ; default is 0
      }
     "Example callout, with :type of :error, and custom spacing")

  #_(callout {
          ;; :border-weight :medium
          ;; :label         "My label"
              :margin-left   2
              :margin-top    0
              :margin-bottom 0}
             (bling [:bold "Hi"]))

  #_(callout {:border-weight :medium
              :label         "My label"
              :margin-left   4}
             "Hi")


  #_(callout {:border-weight :medium
              :type          :positive
              :margin-top    0
              :data?         true}
             (str "line 1"
                  "\n"
                  
                  "\n"
                  "line 3"))

  #_(callout {:border-weight :medium
              :type          :blue
              :margin-top    0
              :data?         true}
             (string/join "\n" (range 10)))


  #_(callout {
              :border-weight :heavy
              :label         "my label"}
             (callout {
                       :border-weight :medium
                       :type          :positive
                       :margin-top    0
                       :data?         true}
                      (str "line 1"
                           "\n"
                           (callout {
                                     :border-weight :medium
                                     :type          :blue
                                     :margin-top    0
                                     :data?         true}
                                    (string/join "\n" (range 10)))
                           "\n"
                           "line 3")))

  (callout {:type :neutral
            :border-weight :heavy}
          (bling [:bold.neutral 
                  "The following two callouts are issued by"]
                  "\n"
                  [:bold.neutral "bling.core/callout, because of malformed args..."]))

  ;; This should issue a warning callout with point-of-interest
  (callout nil)

  ;; This should issue a warning callout with point-of-interest
  (callout [1 2 3])
)
#_(visual-test-suite)
#_(bling-basics)
