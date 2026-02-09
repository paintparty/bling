(ns bling.friends)

(defn foos
  "Foos for \"you\" and me ¯\\_(ツ)_/¯"
  {:desc     ["Foos for \"you\" and me ¯\\_(ツ)_/¯"]

   ;;  :examples [{:desc   "Basic example"
   ;;              :form   '(friends)
   ;;              :result "guh"}]

   ;;  :options  [:map
   ;;             {:desc "All the options"}
   ;;             [:prefix
   ;;              {:desc     "Text to prefix the moji"
   ;;               :optional true}
   ;;              :string]
   ;;             [:moji
   ;;              {:desc     "The name of the friend. \n[See more](https://pets.com)"
   ;;               :optional true
   ;;               :default  :shruggie}
   ;;              [:enum
   ;;               :crying
   ;;               :flipping
   ;;               :happy
   ;;               :excited
   ;;               :lenny
   ;;               :shruggie]]]
   }
  ([]
   (foos nil))
  ([{:keys [prefix moji]}]
   "Foos return value"))

(defn friends
  "One-line ASCII emoji featuring Shruggie and \"friends\".
   This function is a contrived example to use as a test function for the docstring lib.
   Use a newline \"\\n\"
   
   Basic example
   ```clojure
   (friends)
   ;; =>
   \"¯\\_(ツ)_/¯\"
   ```
   
   Example with prefix and `:flipping` moji
   ```clojure
   (friends {:prefix \"Flip a table: \", :moji :flipping})
   ;; =>
   \"Flip a table: (╯°□°）╯︵ ┻━┻\"
   ```
   
   Illustrated example
   With inline comments.
   ```clojure
   (friends {:prefix \"Flip out\"
             :moji   :flipping    ; (╯°□°）╯︵ ┻━┻     
             ;; :moji   :crying      ; ಥ_ಥ    
             ;; :moji   :flipping    ; (╯°□°）╯︵ ┻━┻     
             ;; :moji   :happy       ; ヽ(・∀・)ﾉ
             ;; :moji   :excited     ; ヾ(≧▽≦*)o
             ;; :moji   :lenny       ; ( ͡° ͜ʖ ͡°)
             ;; :moji   :shruggie    ; ¯\\_(ツ)_/¯
             })```
   
   All the options:
   
   * **`:prefix`**
       - `string?`
       - Optional.
       - Text to prefix the moji
   
   * **`:moji`**
       - `#{:flipping :happy :shruggie :lenny :excited :crying}`
       - Optional.
       - Defaults to `:shruggie`.
       - The name of the friend. 
         [See more](https://pets.com)"
  {:desc     ["One-line ASCII emoji featuring Shruggie and \"friends\".\n"
              "This function is a contrived example to use as a test function for the docstring lib.\n"
              "Use a newline \"\\n\""]

   :examples [{:desc   "Basic example"
               :form   '(friends)
               :result "¯\\_(ツ)_/¯"}

              {:desc   "Example with prefix and `:flipping` moji"
               :form   '(friends {:prefix "Flip a table: "
                                  :moji   :flipping})
               :result "Flip a table: (╯°□°）╯︵ ┻━┻"}

              {:desc ["Illustrated example\n"
                      "With inline comments."]
               :form "(friends {:prefix \"Flip out\"
                     |          :moji   :flipping    ; (╯°□°）╯︵ ┻━┻     
                     |          ;; :moji   :crying      ; ಥ_ಥ    
                     |          ;; :moji   :flipping    ; (╯°□°）╯︵ ┻━┻     
                     |          ;; :moji   :happy       ; ヽ(・∀・)ﾉ
                     |          ;; :moji   :excited     ; ヾ(≧▽≦*)o
                     |          ;; :moji   :lenny       ; ( ͡° ͜ʖ ͡°)
                     |          ;; :moji   :shruggie    ; ¯\\_(ツ)_/¯
                     |          })"}]

   :options  [:map
              {:desc "All the options"}
              [:prefix
               {:desc     "Text to prefix the moji"
                :optional true}
               :string]
              [:moji
               {:desc     "The name of the friend. \n[See more](https://pets.com)"
                :optional true
                :default  :shruggie}
               [:enum
                :crying
                :flipping
                :happy
                :excited
                :lenny
                :shruggie]]]}
  ([]
   (friends nil))
  ([{:keys [prefix moji]}]
   (let [mojis {:crying   "ಥ_ಥ"
                :flipping "(╯°□°）╯︵ ┻━┻"
                :happy    "ヽ(・∀・)ﾉ"
                :excited  "ヾ(≧▽≦*)o"
                :lenny    "( ͡° ͜ʖ ͡°)"
                :shruggie "¯\\_(ツ)_/¯"}
         moji  (if (contains? mojis moji) moji :shruggie)]
     (str prefix (get mojis moji)))))
