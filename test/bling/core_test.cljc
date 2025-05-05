;; Namespace for visual testing and sandbox dev during development

(ns bling.core-test
  (:require
   #?(:cljs [bling.core :refer [print-bling]])
   #?(:cljs [bling.js-env :refer [node?]])
   [bling.core :refer [bling callout point-of-interest]]
   [bling.sample]
   ))


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

(declare example-custom-callout)

;; TODO - write a fn to generate these, similar to bling.samples/sample
(defn bling-basics
  ([]
   (bling-basics false))
  ([extras?]

   ;; PRIMITIVES ------------------------------------------------------------------
   (printer (bling [:bold "bold "]
                   [:italic "italic "]
                   [:strikethrough "strikethrough"]
                   [:underline "underline"]
                   [:double-underline "underline"]
                   [:wavy-underline "underline"]
                   [:dotted-underline "underline"]
                   [:dashed-underline "underline"]))

   (println)

   ;; Hyperlink
   (printer (bling [{:text-decoration :underline
                     :href "http://example.com"}
                    "cmd (or ctrl) + click on me to follow hypelink"]))

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
                   [:bold.purple-bg.white " Purple "]
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
                   "\n"
                   [:italic.blue "italic & colored"]
                   "\n"
                   [:bold.italic.white.blue-bg
                    "bold & italic & colored & colored-bg"]
                   "\n"
                   [:bold.italic.blue.underline
                    "bold & italic & colored & underline"]
                   "\n"
                   [:bold.italic.blue.strikethrough 
                    "bold & italic & colored & strikethrough"]))


   ;; CALLOUT examples with light border ------------------------------------------
   
   (println)
   (println (bling [:italic.subtle ";; callout examples, {:label-theme :minimal}"]))
   (callout {:type        :info
             :label-theme :minimal}
            "Example callout, with :type of :info")

   (callout {:type  :info
             :label-theme :minimal
             :label "My custom label"}
            "Example callout, with :type of :info and custom :label")


   (callout {:type :warning
             :label-theme :minimal}
            "Example callout, with :type of :warning")


   (callout {:type :error
             :label-theme :minimal}
            "Example callout, with :type of :error")


   (callout {:colorway :positive
             :label-theme :minimal
             :label    "SUCCESS!"}
            "Example callout, with :colorway of :positive, and custom :label")

   ;; CALLOUT examples with bold border ------------------------------------------
   
   (println)
   (println (bling [:italic.subtle ";; callout examples, {:theme :sideline-bold :label-theme :minimal}"]))
   (callout {:type        :info
             :theme :sideline-bold
             :label-theme :minimal}
            "Example callout, with :type of :info")

   (callout {:type  :info
             :theme :sideline-bold
             :label-theme :minimal
             :label "My custom label"}
            "Example callout, with :type of :info and custom :label")


   (callout {:type :warning
             :theme :sideline-bold
             :label-theme :minimal}
            "Example callout, with :type of :warning")


   (callout {:type :error
             :theme :sideline-bold
             :label-theme :minimal}
            "Example callout, with :type of :error")


   (callout {:colorway :positive
             :theme :sideline-bold
             :label-theme :minimal
             :label    "SUCCESS!"}
            "Example callout, with :colorway of :positive, and custom :label")

   

   ;; CALLOUT examples {:theme :gutter} ----------------------------------------
   
   (println)
   (println (bling [:italic.subtle ";; callout examples, {:theme :gutter}"]))
   (callout {:type  :info
             :theme :gutter}
            "Example callout, with :type of :info")

   (callout {:type  :info
             :theme :gutter
             :label "My custom label"}
            "Example callout, with :type of :info and custom :label")

   (callout {:type  :warning
             :theme :gutter}
            "Example callout, with :type of :warning")

   (callout {:type  :error
             :theme :gutter}
            "Example callout, with :type of :error")

   (callout {:colorway :positive
             :label    "SUCCESS!"
             :theme    :gutter}
            "Example callout, with :colorway of :positive, and custom :label")


   ;; CALLOUT examples {:theme :gutter :margin-left 2} -------------------------
   
   (println)
   (println)
   (println (bling [:italic.subtle ";; callout examples, {:theme :gutter :margin-left 2}"]))
   (callout {:type        :info
             :margin-left 2
             :theme       :gutter}
            "Example callout, with :type of :info")

   (callout {:type        :info
             :margin-left 2
             :theme       :gutter
             :label       "My custom label"}
            "Example callout, with :type of :info and custom :label")

   (callout {:type        :warning
             :margin-left 2
             :theme       :gutter}
            "Example callout, with :type of :warning")

   (callout {:type        :error
             :margin-left 2
             :theme       :gutter}
            "Example callout, with :type of :error")

   (callout {:colorway    :positive
             :margin-left 2
             :label       "SUCCESS!"
             :theme       :gutter}
            "Example callout, with :colorway of :positive, and custom :label")  


   ;; CALLOUT examples {:theme :sideline} ----------------------------------------
   (println)
   (println)
   (println (bling [:italic.subtle
                    ";; callout examples, :sideline theme (default)"]))
   (callout {:type        :info}
            "Example callout, with :type of :info")

   (callout {:type        :info
             :label       "My custom label"}
            "Example callout, with :type of :info and custom :label")

   (callout {:type :warning}
            "Example callout, with :type of :warning")

   (callout {:type :error}
            "Example callout, with :type of :error")

   (callout {:colorway :positive
             :label    "SUCCESS!"}
            "Example callout, with :colorway of :positive, and custom :label")

   ;; Off for readme banner
   (do
     (callout {:colorway    :subtle
               :label       "My label"}
              "Example callout, with :colorway of :subtle (or :gray)")

     (callout {:colorway    :magenta
               :label       "My label"}
              "Example callout, with :colorway of :magenta")

     (callout {:label       "My label"}
              "Example callout, default"))
   

   ;; CALLOUT examples {:theme :sideline-bold} ----------------------------------------
   (println)
   (println)
   (println (bling [:italic.subtle
                    ";; callout examples, :sideline theme (default)"]))
   (callout {:type        :info
             :theme       :sideline-bold
             }
            "Example callout, with :type of :info")

   (callout {:type        :info
             :theme       :sideline-bold
             :label       "My custom label"}
            "Example callout, with :type of :info and custom :label")

   (callout {:type :warning
             :theme       :sideline-bold}
            "Example callout, with :type of :warning")

   (callout {:type  :error
             :theme :sideline-bold}
            "Example callout, with :type of :error")

   (callout {:colorway :positive
             :theme    :sideline-bold
             :label    "SUCCESS!"}
            "Example callout, with :colorway of :positive, and custom :label")

   ;; Off for readme banner
   (do
     (callout {:colorway :subtle
               :theme    :sideline-bold
               :label    "My label"}
              "Example callout, with :colorway of :subtle (or :gray)")

     (callout {:colorway :magenta
               :theme    :sideline-bold
               :label    "My label"}
              "Example callout, with :colorway of :magenta")

     (callout {:label       "My label"
               :theme    :sideline-bold
               }
              "Example callout, default"))
   


   ;; CALLOUT examples {:theme :sideline :label-theme :marquee} ----------------------------------------
   (println)
   (println)
   (println (bling [:italic.subtle ";; callout examples, {:label-theme :marquee}"]))
   (callout {:type        :info
             :label-theme :marquee}
            "Example callout, with :type of :info")

   (callout {:type        :info
             :label       "My custom label"
             :label-theme :marquee}
            "Example callout, with :type of :info and custom :label")

   (callout {:type :warning
             :label-theme :marquee}
            "Example callout, with :type of :warning")

   (callout {:type :error
             :label-theme :marquee}
            "Example callout, with :type of :error")

   (callout {:colorway :positive
             :label-theme :marquee
             :label    "SUCCESS!"}
            "Example callout, with :colorway of :positive, and custom :label")

   ;; Off for readme banner
   (do
     (callout {:colorway    :subtle
               :label-theme :marquee
               :label       "My label"}
              "Example callout, with :colorway of :subtle (or :gray)")

     (callout {:colorway    :magenta
               :label-theme :marquee
               :label       "My label"}
              "Example callout, with :colorway of :magenta")

     (callout {:label-theme :marquee
               :label       "My label"}
              "Example callout, default"))


   ;; CALLOUT examples {:theme :sideline-bold :label-theme :marquee} ---------------------------------
   
   (println)
   (println)
   (println (bling [:italic.subtle ";; callout examples, {:theme :sideline-bold :label-theme :marquee}"]))
   (callout {:type        :info
             :theme       :sideline-bold
             :label-theme :marquee}
            "Example callout, with :type of :info")

   (callout {:type        :info
             :theme       :sideline-bold
             :label       "My custom label"
             :label-theme :marquee}
            "Example callout, with :type of :info and custom :label")

   (callout {:type :warning
             :theme       :sideline-bold
             :label-theme :marquee}
            "Example callout, with :type of :warning")

   (callout {:type :error
             :theme       :sideline-bold
             :label-theme :marquee}
            "Example callout, with :type of :error")

   (callout {:colorway :positive
             :theme       :sideline-bold
             :label-theme :marquee
             :label    "SUCCESS!"}
            "Example callout, with :colorway of :positive, and custom :label")

   ;; Off for readme banner
   (do
     (callout {:colorway    :subtle
               :theme       :sideline-bold
               :label-theme :marquee
               :label       "My label"}
              "Example callout, with :colorway of :subtle (or :gray)")

     (callout {:colorway    :magenta
               :theme       :sideline-bold
               :label-theme :marquee
               :label       "My label"}
              "Example callout, with :colorway of :magenta")

     (callout {:theme       :sideline-bold
               :label-theme :marquee
               :label       "My label"}
              "Example callout, default"))
   

   ;; TODO make custom error and warning examples
   
   (println)
   (println)
   (println 
    (bling 
     [:italic.subtle
      ";; Custom error and warning templates with a point-of-interest "]))
   (println 
    (bling 
     [:italic.subtle
      ";; diagram. Checkout Templates section in readme for more details."]))
   

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(myfun foo baz)
                              :type                  :warning
                              :text-decoration-index 2}
     :callout-opts           {:type :warning
                              :theme :gutter
                            ;;  :label "WARNING: Invalid arg value"
                              }})

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(myfun foo baz)
                              :type                  :warning
                              :text-decoration-index 2}
     :callout-opts           {:type :warning
                            ;;  :label "WARNING: Invalid arg value"
                              }})

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(myfun foo baz)
                              :type                  :warning
                              :text-decoration-index 2}
     :callout-opts           {:type :warning
                              :theme :sideline-bold
                            ;;  :label "WARNING: Invalid arg value"
                              }})

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(myfun foo baz)
                              :type                  :warning
                              :text-decoration-index 2}
     :callout-opts           {:type :warning
                              :label-theme :marquee
                            ;;  :label "WARNING: Invalid arg value"
                              }})

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(myfun foo baz)
                              :type                  :warning
                              :text-decoration-index 2}
     :callout-opts           {:type :warning
                              :theme :sideline-bold
                              :label-theme :marquee
                            ;;  :label "WARNING: Invalid arg value"
                              }})

  ;; gutter with marquee, leave off for now
   #_(example-custom-callout
      {:point-of-interest-opts {:file                  "example.ns.core"
                                :line                  11
                                :column                1
                                :form                  '(myfun foo baz)
                                :type                  :warning
                                :text-decoration-index 2}
       :callout-opts           {:type :warning
                                :theme :gutter
                                :label-theme :marquee
                                :label "WARNING: Invalid arg value"}}) 

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(+ foo baz)
                              :text-decoration-index 2
                              :theme                 :gutter}
     :callout-opts           {:type :error
                              :theme :gutter
                            ;;  :label "ERROR: ClassCastException"
                              }})

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(+ foo baz)
                              :text-decoration-index 2
                              :type                  :error}
     :callout-opts           {:type :error
                            ;;  :label "ERROR: ClassCastException"
                              }})
   
   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(+ foo baz)
                              :text-decoration-index 2}
     :callout-opts           {:type  :error
                              :theme :sideline-bold
                            ;;  :label "ERROR: ClassCastException"
                              }})

   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(+ foo baz)
                              :text-decoration-index 2
                              :type                  :error}
     :callout-opts           {:type        :error
                              :label-theme :marquee
                            ;;  :label "ERROR: ClassCastException"
                              }})
   
   (example-custom-callout
    {:point-of-interest-opts {:file                  "example.ns.core"
                              :line                  11
                              :column                1
                              :form                  '(+ foo baz)
                              :text-decoration-index 2}
     :callout-opts           {:type        :error
                              :label-theme :marquee
                              :theme       :sideline-bold
                            ;;  :label "ERROR: ClassCastException"
                              }})

  ;; gutter with marquee, leave off for now
   #_(example-custom-callout
      {:point-of-interest-opts {:file                  "example.ns.core"
                                :line                  11
                                :column                1
                                :form                  '(+ foo baz)
                                :text-decoration-index 2
                                :theme                 :gutter}
       :callout-opts           {:type :error
                                :theme :gutter
                                :label "ERROR: ClassCastException"
                                :label-theme :marquee}})
   ;; Off for readme banner
   #_
     (when extras?
       (callout {:colorway :subtle
                 :theme    :sideline-bold}
                "Example callout, with :colorway of :subtle (or :gray)")

       (callout {:colorway :magenta
                 :theme    :sideline-bold}
                "Example callout, with :colorway of :magenta")

       (callout {:theme :sideline-bold} "Example callout, default"))



   ;; Off for readme banner
   #_#_
       (when extras?
         (callout {:colorway :subtle
                   :theme    :gutter}
                  "Example callout, with :colorway of :subtle (or :gray)")

         (callout {:colorway :magenta
                   :theme    :gutter}
                  "Example callout, with :colorway of :magenta")

         (callout {:theme :gutter} "Example callout, default"))

     (callout
      {:type         :warning
       :theme        :gutter
       :padding-left 4}
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
        callout-opts (merge {:padding-top 1}
                            callout-opts)]
    (callout callout-opts message)))

;; CALLOUTS with poi -----------------------------------------------------------

(defn visual-test-suite []

   (callout {:label "Callout, no body, body is \"\""} "")

   (callout {:theme :gutter :label "Callout, no body, body is \"\""} "")
   
   (callout {:label "Callout, no body"} nil)

   (callout {:theme :gutter :label "Callout, no body, body is nil"} nil)
   
   (callout {:label "Callout, no body, body is not supplied"})

   (callout {:theme :gutter :label "Callout, no body, body is not supplied"})
   
   (callout "Callout, only body")

   (callout (bling [:magenta "Callout, only body"]))

   (callout {:theme :gutter}  "Callout, only body, body is nil")

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

   (callout {:type     :warning
             :label-string " My warning "
             :label        (bling [:bold.yellow-bg.black " My warning "])}
            "Callout, type :warning, custom enriched label")

   (callout {:type       :warning
             :padding-top    1
             :padding-bottom 1
             :padding-left   2} 
            "Callout, type :warning, default label, custom padding")

   (callout {:type     :positive
             :label-string " YES "
             :label        (bling [:positive-bg.white.bold " YES "])}
            "Callout, type :positve, enriched label")

   (callout {:label "YES"
             :colorway  "positive"}
            "Callout, type :positve, user label")

   (callout {:type :warning}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :type   :warning}))


   (callout {:colorway :magenta}
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

   (callout {:colorway          :magenta}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :type   :magenta}))

   ;; poi with :margin-block of 0
   (callout {:colorway          :magenta
             :theme :gutter}
            (point-of-interest {:line         11
                                :column       2
                                :form         '(+ 1 1)
                                :file         "myfile.cljs"
                                :type     :magenta
                                ;; TODO explore this
                                :margin-block 0}))


   (callout {:colorway          :magenta
             :theme :gutter}
            (bling [:blue "One line."]
                   #_"\n"
                   #_[:red "red"]))

   (callout {:colorway          :magenta
             :theme :gutter
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
     :label-string " RAINBOW "
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
     :label-string "RAINBOW"
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
    {:type      :info
     :theme :gutter
     :label-string  "RAINBOW"
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
     :label     {:a :b}}
    "Example callout, with :type of :warning, and custom :label." )


   (println "\n\n\n")
  
  (bling-basics)

  ;;Default callout w/ poi, regex-form
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type :warning}})

  ;;Medium callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type          :warning
                             :theme :sideline-bold}})

  ;;Heavy callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type          :warning
                             :theme :gutter}})


  ;;Default callout w/ poi, regex-form
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type :error}})

  ;;Medium callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error
                            ;;  :text-decoration-index 2
                             }
    :callout-opts           {:type          :error
                             :theme :sideline-bold}})

  ;;Heavy callout w/ poi
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type          :error
                             :theme :gutter}})

  ;;Normal callout w/ poi, label enriched and regex form

  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   #"All of this regex should underlined"
                             :type   :error}
    :callout-opts           {:label (bling [:red-bg.bold.white " ERROR "])
                             :label-string " ERROR "
                             :type  :error}})

  ;;callout w/ enriched header and body on poi
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
          ;; :theme :sideline-bold
          ;; :label         "My label"
              :margin-left   2
              :margin-top    0
              :margin-bottom 0}
             (bling [:bold "Hi"]))

  #_(callout {:theme :sideline-bold
              :label         "My label"
              :margin-left   4}
             "Hi")


  #_(callout {:theme :sideline-bold
              :colorway          :positive
              :margin-top    0
              :data?         true}
             (str "line 1"
                  "\n"
                  
                  "\n"
                  "line 3"))

  #_(callout {:theme :sideline-bold
              :colorway          :blue
              :margin-top    0
              :data?         true}
             (string/join "\n" (range 10)))


  #_(callout {
              :theme :gutter
              :label         "my label"}
             (callout {
                       :theme :sideline-bold
                       :colorway          :positive
                       :margin-top    0
                       :data?         true}
                      (str "line 1"
                           "\n"
                           (callout {
                                     :theme :sideline-bold
                                     :colorway          :blue
                                     :margin-top    0
                                     :data?         true}
                                    (string/join "\n" (range 10)))
                           "\n"
                           "line 3")))

  (callout {:colorway :neutral
            :theme    :rainbow-gutter }
          (bling [:bold.neutral 
                  "The following two callouts would be"]
                  "\n"
                  [:bold.neutral "issued by bling.core/callout, if the"]
                                  "\n"
                  [:bold.neutral "user were to supply malformed args."]))

  ;; This should issue a warning callout with point-of-interest
  (callout nil)

  ;; This should issue a warning callout with point-of-interest
  (callout [1 2 3]))


;;------------------------------------------------------------------------------
;;------------------------------------------------------------------------------
;;------------------------------------------------------------------------------
;;------------------------------------------------------------------------------

(println)
(println)
#_(bling.sample/sample)
#_(visual-test-suite)
#_(bling-basics)
#_(bling.sample/sample)
(println)
(println)
(println)
(println)


#_(example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(+ foo baz)
                             :text-decoration-index 2
                             :theme                 :gutter}
    :callout-opts           {:type        :error
                             :theme       :gutter
                             :label       "ERROR: ClassCastException"
                             :label-theme :marquee}})

#_(callout {:colorway :positive
          :label    "SUCCESS!"
          ;; :margin-left 3
          ;; :theme    :gutter
          }
         "Example callout, with :colorway of :positive, and custom :label")

#_(callout {
          ;; :colorway :positive
          :type        :error
          ;; :padding-top 1
          :theme       :gutter}
         "Body text")

#_(callout {
          ;; :colorway :positive
          :label       "gone"
          ;; :padding-top 1
          :theme       :gutter}
         "Body text")

  #_(callout {:colorway :neutral
            :theme    :rainbow-gutter }
          (bling [:bold.neutral 
                  "The following two callouts are issued by"]
                  "\n"
                  [:bold.neutral "bling.core/callout, because of malformed args..."]))

;; random samples
#_
#_
#_
#_
#_
#_
#_
(callout {
          :colorway      :magenta
          :margin-bottom 0
          :label-string  "foocore, wtf"
          :label         (bling [:italic.neutral "foo"]
                                [:orange.italic "core"]
                                ", "
                                [:blue.italic "wtf"]
                                "\n ")
          })

(callout {
          :colorway      :magenta
          :margin-bottom 0
          :label-string  "foocore, wtf "
          :label         (bling [:italic.neutral "foo"]
                                [:orange.italic "core"]
                                ", "
                                [:blue.italic "wtf"]
                                "\n ")
          }
         "body")

(callout {
          :theme         :gutter
          :colorway      :magenta
          :margin-bottom 0
          :label         (bling [:italic.neutral "foo"]
                                [:orange.italic "core"]
                                ", "
                                [:blue.italic "wtf"]
                                "\n ")})

(callout {
          :theme         :gutter
          :colorway      :magenta
          :margin-bottom 0
          :label         (bling [:italic.neutral "foo"]
                                [:orange.italic "core"]
                                ", "
                                [:blue.italic "wtf"]
                                "\n ")}
         "body")


(callout {
          :theme :gutter
          :colorway      :magenta
          :margin-bottom 0
          :label         "hi" #_(bling [:italic.neutral "foo"]
                                [:orange.italic "core"]
                                ", "
                                [:blue.italic "wtf"]
                                "\n ")})

(callout {
          :theme :gutter
          :colorway      :magenta
          :margin-bottom 0
          :label         "hi" #_(bling [:italic.neutral "foo"]
                                [:orange.italic "core"]
                                ", "
                                [:blue.italic "wtf"]
                                "\n ")}
         "body")

(callout
 {:type :info :label ""}
 "Hi and something\nSomething else")

;; (callout
;;  {:type :info}
;;  "Hi and something\nSomething else")

;; (callout
;;  {:type :info :label "" :theme :gutter}
;;  "Hi and something\nSomething else")
;; (callout
;;  {:type :info :label "" :theme :gutter :margin-top 0}
;;  "Hi and something\nSomething else")

;; Callouts

   #_(callout
    {:type  :info
     :label-string "RAINBOW"
     :label (bling 
             [:magenta.bold "R"]
             [:red.bold "A"]
             [:yellow.bold "I"]
             [:green.bold "N"]
             [:blue.bold "B"]
             [:magenta.bold "O"]
             [:red.bold "W"])}
    (bling "Example callout, :type of :info, rainbow-bg label" ))

   #_(callout
    {:type        :info
     :theme       :gutter
     :margin-left 5
     :label-theme :marquee
     :label "hi"}
    "Hi and something\nSomething else")

  ;;  (callout
  ;;   {:type  :info
  ;;    :theme :sideline
  ;;    :label "HELLO"}
  ;;   "hi")

  ;;  (callout
  ;;   {:type          :info
  ;;    :theme         :sideline
  ;;    :label-string  "RAINBOW"
  ;;    :label         (bling 
  ;;                    [:magenta.bold "R"]
  ;;                    [:red.bold "A"]
  ;;                    [:yellow.bold "I"]
  ;;                    [:green.bold "N"]
  ;;                    [:blue.bold "B"]
  ;;                    [:magenta.bold "O"]
  ;;                    [:red.bold "W"])}
  ;;   (bling "Example callout, :type of :info, rainbow-bg label" ))


#_(example-custom-callout
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
    {:label       (bling [:magenta-bg.white.bold " HEY "])
     :label-string " HEY "
     :padding-top 1}
    (bling "Example callout, styled-label, :padding-top of 1"))

   #_(callout {:colorway          :magenta}
            (point-of-interest {:line   11
                                :column 2
                                :form   '(+ 1 1)
                                :file   "myfile.cljs"
                                :colorway   :magenta}))

#_(callout
    {:type  :error
    ;;  :label     {:a :b}
     }
    "Example callout, with :colorway of :warning, and custom :label." )

#_(println 
 (into []
       [[1 2 3 4]
        [:a :b :c :d]
        [:x 'y 'z]]))
