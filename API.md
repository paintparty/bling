# Table of contents
-  [`bling.banner`](#bling.banner) 
    -  [`banner`](#bling.banner/banner) - Returns a multi-line Figlet (ascii art) string with the provided <code>:text</code> option.
-  [`bling.core`](#bling.core) 
    -  [`!?sgr`](#bling.core/!?sgr) - Temporarily silences debugging of sgr code printing.
    -  [`?sgr`](#bling.core/?sgr) - For debugging of ANSI SGR tagged output.
    -  [`bling`](#bling.core/bling) - Returns a styled string tagged with ANSI SGR codes.
    -  [`bling-colors`](#bling.core/bling-colors) - Array map of the blink color pallette with light, dark, and medium entries for each color.
    -  [`bling-colors*`](#bling.core/bling-colors*) - Array map of the blink color pallette.
    -  [`callout`](#bling.core/callout) - Uses a predesigned template to format and print a message block.
    -  [`file-info-str`](#bling.core/file-info-str) - Creates a file-info string consisting of the file name, line number and column number.
    -  [`highlighted-location`](#bling.core/highlighted-location) - Gets position of last occurence of highlighting in a potentially multi-line string.
    -  [`point-of-interest`](#bling.core/point-of-interest) - Formatted and decorated diagram of a form with line, column, and file info.
    -  [`print-bling`](#bling.core/print-bling) - Sugar for <code>(println (bling ...))</code>.
    -  [`stringified`](#bling.core/stringified) - Stringifies form to a specified with and height, with optionally supplied printing-fn such as <code>pprint</code>.
    -  [`with-ascii-underline`](#bling.core/with-ascii-underline) - Reformats a potentially multi-line string to include an ascii underline at a specificed location.
    -  [`with-floating-label`](#bling.core/with-floating-label) - Annotates a line of text at supplied index with floating label.
-  [`bling.explain`](#bling.explain) 
    -  [`explain-malli`](#bling.explain/explain-malli) - Prints a Malli validation error "callout" block via <code>bling.core/callout</code>.

-----
# <a name="bling.banner">bling.banner</a>






## <a name="bling.banner/banner">`banner`</a>
``` clojure

(banner m)
```
Function.

Returns a multi-line Figlet (ascii art) string with the provided
   `:text` option. Intended to render a "single line" banner,
   consisting of multiple lines of constituent characters.
   
   Can be optionally colorized with the `:gradient` option,
   using a small set of pre-defined gradients:
   ```clojure
   (banner {:text "Hello", :gradient-colors [:green :blue]})
   
   (banner {:text "Hello", :gradient-colors [:red :magenta]})
   
   (banner {:text "Hello", :gradient-colors [:yellow :purple]})
   
   (banner {:text "Hello", :gradient-colors [:orange :purple]})
   
   (banner {:text "Hello", :gradient-colors [:cool :warm]})
   ```
   
   Can be colorized solid with [`bling.core/bling`](#bling.core/bling)
   ```clojure
   (bling [:red (banner {:text "Hello"})])
   ```
   
   Options:
   
   * **`:text`**
       - `string?`
       - Required.
       - The text to set in the banner.
   
   * **`:font-weight`**
       - `#{:bold "bold"}`
       - Optional.
       - If set to `:bold`, each subchar in figlet characters
         will be bolded. Only applies when a gradient is set.
   
   * **`:gradient-colors`**
       - `#{[:yellow :purple] ["orange" "purple"] ["yellow" "purple"] ["red" "magenta"] [:red :magenta] ["green" "blue"] ["cool" "warm"] [:green :blue] [:cool :warm] [:orange :purple]}`
       - Optional.
       - Sets the beginning and end colors of the gradient.
         Expects a vector of 2 keywords.
   
   * **`:gradient-direction`**
       - `#{:to-right "to-left" :to-top "to-top" :to-left "to-bottom" "to-right" :to-bottom}`
       - Optional.
       - Direction of the gradient.
   
   * **`:gradient-shift`**
       - `[:int {:min 0, :max 5}]`
       - Optional.
       - Defaults to `0`.
       - If gradient is `[:warm :cool]` pair, this will shift the hue.
   
   * **`:contrast`**
       - `keyword?`
       - Optional.
       - Defaults to `:medium`.
       - If gradient is set, this will force an overall lighter or
         darker tone. If the user has a `BLING_MOOD` env var set,
         it will default to `:high` in order to optimize contrast
         for the users terminal theme (light or dark)
   
   * **`:margin-top`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of margin (in newlines) at top, outside banner.
   
   * **`:margin-bottom`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in newlines) at bottom, outside banner.
   
   * **`:margin-left`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in blank character spaces) at left,
         outside banner.
   
   * **`:margin-right`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in blank character spaces) at right,
         outside banner.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/banner.cljc#L985-L1208">Source</a></sub></p>

-----
# <a name="bling.core">bling.core</a>






## <a name="bling.core/!?sgr">`!?sgr`</a>
``` clojure

(!?sgr s)
```
Function.

Temporarily silences debugging of sgr code printing.
   Returns the value.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L266-L270">Source</a></sub></p>

## <a name="bling.core/?sgr">`?sgr`</a>
``` clojure

(?sgr s)
```
Function.

For debugging of ANSI SGR tagged output.

   Prints the value with escaped ANSI SGR codes so you can read them in terminal
   emulators (otherwise text would just get colored). Preserves coloring.

   Returns the value.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L248-L264">Source</a></sub></p>

## <a name="bling.core/bling">`bling`</a>
``` clojure

(bling & coll)
```
Function.

Returns a styled string tagged with ANSI SGR codes.
   
   Takes an arbitrary number strings or hiccup-like vectors.
   Hiccup vectors can be nested.
   
   Bold text
   ```clojure
   (bling [:bold "Bold text"])
   ```
   
   Red text
   ```clojure
   (bling [:red "Red text"])
   ```
   
   Italic text
   ```clojure
   (bling [:italic "Italic text"])
   ```
   
   Bold red italic text
   ```clojure
   (bling [:bold.red.italic "Bold red italic text"])
   ```
   
   Bold red italic text, order of tags doesn't matter
   ```clojure
   (bling [:italic.bold.red "Bold red italic text"])
   ```
   
   Bold red italic text, and blue text
   ```clojure
   (bling
    [:italic.bold.red "Bold red italic text"]
    " and "
    [:blue "blue text"])
   ```
   
   Italic black on yellow text
   ```clojure
   (bling [:italic.black.yellow-bg "Black on yellow text"])
   ```
   
   Italic black on yellow text, with hiccup map syntax
   ```clojure
   (bling
    [{:font-style :italic, :font-color :black, :background-color :yellow}
     "Black on yellow text"])
   ```
   
   Bling color pallette
   ```clojure
   (bling [:red "Red text"])
   
   (bling [:orange "Orange text"])
   
   (bling [:yellow "Yellow text"])
   
   (bling [:olive "Olive text"])
   
   (bling [:green "Green text"])
   
   (bling [:blue "Blue text"])
   
   (bling [:purple "Purple text"])
   
   (bling [:magenta "Magenta text"])
   
   (bling [:gray "Gray text"])
   
   (bling [:black "Black text"])
   
   (bling [:white "White text"])
   ```
   
   Bling color aliases
   ```clojure
   (bling [:error "Error"])
   
   (bling [:negative "Negative"])
   
   (bling [:warning "Warning"])
   
   (bling [:success "Success"])
   
   (bling [:accent "Success"])
   
   (bling [:neutral "Neutral"])
   
   (bling [:subtle "Subtle"])
   ```
   
   Nested hiccup example
   ```clojure
   (print-bling
    [:p "First paragraph"]
    [:p
     [:bold
      "Bold, "
      [:italic "bold italic, " [:red "bold italic red, "]]
      "bold."]]
    "Last line")
   ```
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L3676-L3834">Source</a></sub></p>

## <a name="bling.core/bling-colors">`bling-colors`</a>




Array map of the blink color pallette with light, dark, and medium entries
   for each color.
   
   ```Clojure
   {...
    "purple"        {:sgr 141
                       :css "#af87ff"
                       :sgr-dark 129
                       :sgr-light 147
                       :css-dark "#af00ff"
                       :css-light "#afafff"}
    "medium-purple" {:sgr 141 :css "#af87ff"}
    "dark-purple"   {:sgr 129 :css "#af00ff"}
    "light-purple"  {:sgr 147 :css "#afafff"}
    ...}
   ```
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L171-L216">Source</a></sub></p>

## <a name="bling.core/bling-colors*">`bling-colors*`</a>




Array map of the blink color pallette.
   
   ```Clojure
   {...
    "purple" {:sgr 141 :css "#af87ff"}
    ...}
   ```
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L139-L169">Source</a></sub></p>

## <a name="bling.core/callout">`callout`</a>
``` clojure

(callout x & args)
```
Function.

Uses a predesigned template to format and print a message block.
   
   Prints a message to the console with a block-based coloring motif.
   
   Returns nil.
   
   If the `:data?` option is set to `true`, it does not print anything, and
   returns a data representation of the formatting and styling.
   
   Callout uses any one of a number of predesigned templates to format a block
   of information that is delineated by an optionally colored border motif.
   The color of the border is determined by the value of the `:type` option,
   or the `:colorway` option. The characteristics of this border are controlled
   by the `:theme` option. By default, a label is printed in the block
   start position.
   
   For callouts of the type `:error`, `:warning`, or `:info`, the label
   string will default to an uppercased version of that string, e.g.
   `{:type :INFO} => "INFO"`. If a `:label` option is supplied, that value is
   used instead. When you want to omit label for callouts of the type `:error`,
   `:warning`, or `:info`, you must explicitly set the :label option to an
   empty string.
   
   If two arguments are provided, the first should be a map of valid options.
   
   Example call with all of the options
   ```clojure
   (callout {:type                   :error            ; :warning, :info
             ;; :colorway            :purple           ; <- any bling palette color, overrides :type
             ;; :label               "My label"      ; overrides label assigned by :type
             :side-label             "My side label" ; must have a :label if you want a :side-label        
             :theme                  :sideline         ; :sideline :sandwich :gutter
             :label-theme            :simple           ; :simple :marquee
             ;; :padding-block       0                 
             ;; :padding-top         0                 
             ;; :padding-bottom      0                 
             ;; :padding-inline      2                 
             ;; :padding-left        2                 
             ;; :padding-right       0                 
             ;; :margin-top          1                 
             ;; :margin-botom        0                 
             ;; :margin-left         0                 
             ;; :data?               true              ; <- just returns string, no printing
             
             :border-shape           :sharp            ; :round
             :border-style           :solid            ; :double
             :border-weight          :normal           ; :bold
             :width                  40
             :min-width              40
             
             ;; --- The options below exclusive to :theme of :boxed ---
             ;; :border-char            "*"
             ;; :vertical-border-char   "**"
             ;; :max-width              100
             ;; :padding-block          1
             ;; :padding-inline         2
             }
    (bling [:bold (str "Line 1" "\n" "Line 2")]))
   ```
   
   Options:
   
   * **`:colorway`**
       - `#{"neutral" "magenta" "warning" "positive" :neutral "info" :green :positive "negative" :negative "error" "subtle" :warning "green" :info :error :magenta :subtle}`
       - Optional.
       - The color of the border, or gutter, depending on the value of `:theme`.
   
   * **`:theme`**
       - `#{"boxed" "sideline" :boxed :sideline "gutter" :sandwich "simple" :gutter}`
       - Optional.
       - Defaults to `:sideline`.
       - Name of callout layout template.
   
   * **`:label`**
       - `any?`
       - Optional.
       - Labels the callout.
         In the case of a callout `:type` of `:warning`, `:error`, or `:info`,
         the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively.
   
   * **`:side-label`**
       - `any?`
       - Optional.
       - Side label to the the callout label. In the case of a callout
         `:type` of `:warning`, `:error`, or `:info`, the value of the label
         will default to `WARNING`, `ERROR`, or `INFO`, respectively.
   
   * **`:label-theme`**
       - `#{:simple "marquee" :marquee "simple"}`
       - Optional.
       - Defaults to `:simple`.
       - Name of label flavor.
   
   * **`:padding-block`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of padding (in newlines) at top and bottom, inside callout.
   
   * **`:padding-top`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of padding (in newlines) at top, inside callout.
   
   * **`:padding-bottom`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of padding (in newlines) at bottom, inside callout.
   
   * **`:padding-inline`**
       - `int?`
       - Optional.
       - Defaults to `2`.
       - Amount of padding (in blank character spaces) at left and right, inside callout.
   
   * **`:padding-left`**
       - `int?`
       - Optional.
       - Defaults to `2`.
       - Amount of padding (in blank character spaces) at left, inside callout.
   
   * **`:margin-top`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of margin (in newlines) at top, outside callout.
         Only applies to terminal emulator printing.
   
   * **`:margin-bottom`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in newlines) at bottom, outside callout.
         Only applies to terminal emulator printing.
   
   * **`:margin-left`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in blank character spaces) at left, outside callout.
   
   * **`:data?`**
       - `boolean?`
       - Optional.
       - Returns a data representation of result instead of printing it.
   
   * **`:header-padding-left`**
       - `int?`
       - Optional.
       - Defaults to `2`.
       - Amount of left margin (in character spaces) for the label, in the callout header
   
   * **`:header-gap`**
       - `int?`
       - Optional.
       - Defaults to `5`.
       - The gap (in character spaces) the label and the optional side lable, in the callout header.
   
   * **`:border-style`**
       - `#{:double :solid "solid" "double"}`
       - Optional.
       - Defaults to `:solid`.
       - The style of box-drawing character used.
   
   * **`:border-weight`**
       - `#{:bold "normal" :normal "bold"}`
       - Optional.
       - Defaults to `:normal`.
       - The weight of box-drawing character used. Applies only to `:border-style` of `:solid`
   
   * **`:border-shape`**
       - `#{"sharp" :round :sharp "round"}`
       - Optional.
       - Defaults to `:sharp`.
       - The corner shape of the borders, either sharp or round. Only applies when `:border-style` is
         `:solid` AND `:border-weight` is `:normal`
   
   * **`:border-notches?`**
       - `boolean?`
       - Optional.
       - Defaults to `:false`.
       - Only applies to `:sandwich` callout theme. Will use top-left-corner
         and bottom-right-corner box-drawing chars for first character of header and footer borders.
   
   * **`:width`**
       - `pos-int?`
       - Optional.
       - Width of the box in number of chars, aka columns in
         terminal. If not set, will be the width of the terminal.
         If terminal width cannot be detected, will fallback to 80.
   
   * **`:min-width`**
       - `pos-int?`
       - Optional.
       - Min width of box in number of chars, aka columns in
         terminal. Overridden by the `:width` value, if set.
   
   * **`:border-char`**
       - `string?`
       - Optional.
       - A char that will override the default box-drawing
   
   * **`:vertical-border-char`**
       - `string?`
       - Optional.
       - A char that will override the default box-drawing character,
         for the vertical borders.
   
   * **`:horizontal-border-char`**
       - `string?`
       - Optional.
       - A char that will override the default box-drawing character,
         for the horizontal borders.
   
   * **`:box-drawing-style`**
       - `#{:bold :thin-round :double "thin" :thin "thin-round" "double" "bold"}`
       - Optional.
       - The style of box-drawing character used.
   
   * **`:max-width`**
       - `pos-int?`
       - Optional.
       - Max width of box in number of chars, aka columns in
         terminal. Overridden by the `:width` value, if set.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L2850-L3373">Source</a></sub></p>

## <a name="bling.core/file-info-str">`file-info-str`</a>
``` clojure

(file-info-str {:keys [:file :line :column :line-style :column-style :file-style :style], :or {style {}}})
```
Function.

Creates a file-info string consisting of the file name, line number
   and column number. Optionally styled with Bling.
   
   Basic example
   ```clojure
   (file-info-str {:file "foo.cljc", :line 42, :column 44})
   ;; =>
   "foo.cljc:42:44"
   ```
   
   Options:
   
   * **`:line`**
       - `int?`
       - Optional.
       - Line number
   
   * **`:column`**
       - `int?`
       - Optional.
       - Column number
   
   * **`:file-style`**
       - `map?`
       - Optional.
       - CSS style map for the file name
   
   * **`:line-style`**
       - `map?`
       - Optional.
       - CSS style map for the line number
   
   * **`:column-style`**
       - `map?`
       - Optional.
       - CSS style map for the column number
   
   * **`:style`**
       - `map?`
       - Optional.
       - CSS style map for file-info string
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L705-L820">Source</a></sub></p>

## <a name="bling.core/highlighted-location">`highlighted-location`</a>
``` clojure

(highlighted-location s)
(highlighted-location s target-highlight-style)
```
Function.

Gets position of last occurence of highlighting in a potentially
   multi-line string. Designed to pinpoint highlighting that was
   applied to a form using bling.hifi/hifi (with `:find` option).
   
   If an `:underline-char` option is supplied, the map returned will
   include a string that can be used as a distinct line with an ascii
   underline for the supplied stringified form. This underline line is
   optionally decorated with a supplied `:text-decoration-style` map.
   
   If a `:floating-annotation-text` option is supplied, the string
   will be annotated. This string is optionally decorated with a
   supplied `:floating-annotation-style` map.
   
   Options:
   
   * **`:class`**
       - `#{:highlight-error-universal :highlight-error-dark :highlight-error-light}`
       - Optional.
       - Highlight style class 
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L966-L1031">Source</a></sub></p>

## <a name="bling.core/point-of-interest">`point-of-interest`</a>
``` clojure

(point-of-interest
 {:keys
  [form
   line
   column
   header-file-info-style
   header-file-name-style
   header-line-number-style
   header-column-number-style
   margin-block
   margin-top
   margin-bottom
   gutter-line-number-style],
  :as opts})
```
Function.

Formatted and decorated diagram of a form with line, column, and file info.
   
   Provides the namespace, column, and line number and a representation of the
   specific form of interest.
   
   The `:line`, `:column`, and `:form` options must all be present in
   order for the info diagram to be rendered. If the `:form` option is supplied,
   but any of the others are omitted, only the form will be rendered.
   
   If the form is provided is a collection, it will be stringified and truncated
   at 33 chars.
   
   To print a multi-line form, pre-format the `:form` value with
   [`bling.core/stringified`](#bling.core/stringified), or `bling.hifi/hifi`.
   
   If you would like to print a multi-line form with individual subforms 
   highlighted, you can pre-format the `:form` value with some combo of
   bling.hifi/hifi (with `:find` option supplied),
   [`bling.core/with-ascii-underline`](#bling.core/with-ascii-underline), and `bling.core/with-floating-label.`
   
   By default, the diagram is created with a leading and trailing newlines,
   via a default value of `1` for `:margin-block`. This can be set to zero,
   or increased, with the `:margin-block` option.
   
   Basic Example
   ```clojure
   (point-of-interest
    {:form (+ 1 true), :line 42, :column 11, :file "myfile.core"})
   ```
   
   With styled file-info
   ```clojure
   (point-of-interest
    {:form (+ 1 true),
     :header-file-name-style {:color :subtle, :font-style :italic},
     :line 42,
     :header-line-number-style {:color :red},
     :gutter-line-number-style {:color :red, :font-style :italic},
     :column 11,
     :file "myfile.core"})
   ```
   
   With collection supplied as `:form`
   ```clojure
   (point-of-interest
    {:form
     {:a 1,
      :b [333 444 555],
      :c "aadfasdfasdfads",
      :d "asdfasdfasdfasdfasdfasdf"},
     :header-file-info-style {:color :subtle, :font-style :italic},
     :line 42,
     :column 11,
     :file "myfile.core"})
   ```
   
   Options:
   
   * **`:file`**
       - `string?`
       - Optional.
       - File or namespace
   
   * **`:header-file-info-style`**
       - `map?`
       - Optional.
       - File info style, in header of point-of-interest diagram.
         This will apply default styles to `:header-file-name-style`,
         `:header-line-number-style`, `:header-column-number-style`,  
         and `:gutter-line-number-style`.
   
   * **`:header-file-name-style`**
       - `map?`
       - Optional.
       - File name style, in header of point-of-interest diagram
   
   * **`:line`**
       - `int?`
       - Optional.
       - Line number
   
   * **`:header-line-number-style`**
       - `map?`
       - Optional.
       - Line number style, in header of point-of-interest diagram
   
   * **`:gutter-line-number-style`**
       - `map?`
       - Optional.
       - Gutter line number style, in header of point-of-interest diagram
   
   * **`:column`**
       - `int?`
       - Optional.
       - Column number
   
   * **`:header-column-number-style`**
       - `map?`
       - Optional.
       - Column number style, in header of point-of-interest diagram
   
   * **`:margin-block`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Controls the number of blank lines above and below the diagram.
   
   * **`:margin-top`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Controls the number of blank lines above the diagram.
   
   * **`:margin-bottom`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Controls the number of blank lines below the diagram.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L1292-L1614">Source</a></sub></p>

## <a name="bling.core/print-bling">`print-bling`</a>
``` clojure

(print-bling & args)
```
Function.

Sugar for `(println (bling ...))`.
   
   In JVM Clojure, cljs(Node), and bb, [`print-bling`](#bling.core/print-bling) is sugar for:
   `(println (bling [:bold.blue "my blue text"]))`.
   
   In cljs(browser), [`print-bling`](#bling.core/print-bling) is sugar for the the following:
   `(print-to-browser-dev-console (bling [:bold.blue "my blue text"]))`.
   
   print bold text
   ```clojure
   (print-bling [:bold "Bold text"])
   ```
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L3836-L3865">Source</a></sub></p>

## <a name="bling.core/stringified">`stringified`</a>
``` clojure

(stringified form)
(stringified form {:keys [height width printing-fn]})
```
Function.

Stringifies form to a specified with and height, with optionally
   supplied printing-fn such as `pprint`
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L1260-L1284">Source</a></sub></p>

## <a name="bling.core/with-ascii-underline">`with-ascii-underline`</a>
``` clojure

(with-ascii-underline s {:keys [line-index underline-char text-decoration-style text-decoration-color text-decoration-weight] :or {text-decoration-style :wavy line-index ::unsupplied} :as opts}])
```
Function.

Reformats a potentially multi-line string to include an ascii underline
   at a specificed location.
   
   If supplied value for `:form` is a multi-line string, and supplied
   value for `:line-index` is an integer less than the number of lines
   present, inserts an ascii underline below the specified row.
   
   Options:
   
   * **`:offset`**
       - `pos-int?`
       - Required.
       - Controls offset, in columns, of the underline.
         If not provided, defaults to index of first non-blank character in line first.
   
   * **`:width`**
       - `pos-int?`
       - Required.
       - Defaults to `3`.
       - Controls the width, in columns, of the underline.
         If not provided, defaults to the length of the line, minus leading blank spaces
   
   * **`:underline-char`**
       - `keyword?`
       - Optional.
       - Char used to build the ascii underline.
         Overrides `:text-decoration-style`
   
   * **`:text-decoration-color`**
       - `keyword?`
       - Optional.
       - Controls the color of the underline.
   
   * **`:text-decoration-weight`**
       - `#{:bold "normal" :normal "bold"}`
       - Optional.
       - Controls the font-weight of the underline.
   
   * **`:text-decoration-style`**
       - `#{:double :wavy :solid :dashed :dotted}`
       - Optional.
       - Defaults to `:wavy`.
       - Controls the ascii char used to construct the underline.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L1108-L1233">Source</a></sub></p>

## <a name="bling.core/with-floating-label">`with-floating-label`</a>
``` clojure

(with-floating-label s {:keys [line-index label-text label-style label-offset]})
```
Function.

Annotates a line of text at supplied index with floating label.
   
   This label is optionally decorated with a supplied `:floating-annotation-style` map.
   
   Options:
   
   * **`:label-text`**
       - `string?`
       - Required.
       - The text of the floating annotation.
   
   * **`:label-style`**
       - `map?`
       - Optional.
       - Controls the style of the floating annotation
   
   * **`:label-offset`**
       - `pos-int?`
       - Optional.
       - Defaults to `3`.
       - Controls offset of the floating annotation
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/core.cljc#L1033-L1088">Source</a></sub></p>

-----
# <a name="bling.explain">bling.explain</a>






## <a name="bling.explain/explain-malli">`explain-malli`</a>
``` clojure

(explain-malli schema v)
(explain-malli schema v opts)
```
Macro.

Prints a Malli validation error "callout" block via [`bling.core/callout`](#bling.core/callout).
   
   Within the block, the value is pretty-printed, potentially with syntax
   coloring. The problem value is highlighted with the `:highlight-error`
   class of the active fireworks theme, or the `:highlight-error-underlined`
   class, if the value is not a collection.
   
   If three arguments are provided, the third should be a map
                        with the following optional keys:
   
   * **`:function-name`**
       - `string?`
       - Optional.
       - The name of the function that can be used to construct
         the source location.
   
   * **`:spacing`**
       - `#{"compact" :compact}`
       - Optional.
       - If the value of `:spacing` is set to `:compact`, the
         callout is compacted vertically.
   
   * **`:display-schema?`**
       - `boolean?`
       - Optional.
       - Displays the schema passed to the underlying call to
         `malli.core/explain`.
   
   * **`:display-explain-data?`**
       - `boolean?`
       - Optional.
       - Displays the output of `malli.core/explain` within the
         callout block.
   
   * **`:callout-opts`**
       - `map?`
       - Optional.
       - A map of options for the underlying call to
         bling.core/callout.
   
   * **`:hifi-opts`**
       - `map?`
       - Optional.
       - The options map for bling.hifi/hifi
   
   * **`:file`**
       - `string?`
       - Optional.
       - The file name of the call site.
         This value be automatially supplied by the macro,
         so only use if you want to manually override
   
   * **`:line`**
       - `int?`
       - Optional.
       - The line number of the call site.
         This value be automatially supplied by the macro,
         so only use if you want to manually override.
   
   * **`:column`**
       - `int?`
       - Optional.
       - The column number of the call site.
         This value be automatially supplied by the macro,
         so only use if you want to manually override.
   
   * **`:highlighted-problem-section-label`**
       - `string?`
       - Optional.
       - Label for the highlighted problem diagram
   
   * **`:section-body-indentation`**
       - `pos-int?`
       - Optional.
       - Number of spaces to indent the body of each section
   
   * **`:preamble-section-label`**
       - `string?`
       - Optional.
       - The label of the preamble section
   
   * **`:preamble-section-body`**
       - `string?`
       - Optional.
       - The body of the preamble section
   
   * **`:success-message`**
       - `string?`
       - Optional.
       - The message to display if value passes schema validation
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/explain.cljc#L943-L1140">Source</a></sub></p>
