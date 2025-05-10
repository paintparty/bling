
<!-- TODO - Update images with new colors -->
# Bling

**Rich text in the console.**  


<p align="left">
  <a href="https://clojars.org/io.github.paintparty/bling">
    <img src="https://img.shields.io/clojars/v/io.github.paintparty/bling.svg?color=0969da&style=flat&cacheSeconds=3" alt="bling on Clojars"></img>
  </a>
</p>

<div align="left">

**[Features]**
&nbsp;•&nbsp;
**[Setup]**
&nbsp;•&nbsp;
**[Basic Usage]**
&nbsp;•&nbsp;
**[Callout Blocks]**
&nbsp;•&nbsp; 
**[Error Templates]**
&nbsp;•&nbsp; 
**[Figlet Banners]**
</div>

[Features]: #features
[Setup]: #setup
[Basic Usage]: #basic-usage
[Callout Blocks]: #callout-blocks

[Error Templates]: #templates-for-errors-and-warnings
[Figlet Banners]: #figlet-banners
[Interop]: #printing-conventions
[Contributing]: #contributing

<br>
<br>

<p align="left">
  <img src="resources/docs/chromed/bling-banner-light-0.6.0.png" width="700px" />
</p>

The same example as above, in a terminal emulator with a dark background.
<p align="left">
  <img src="resources/docs/chromed/bling-banner-dark-0.6.0.png" width="700px" />
</p>

## Features

- Works great for Clojure, ClojureScript, and Babashka.

- Supports both terminal emulators and browser consoles.

- Simple, accessibility-focused, 11-color pallette.

- All colors provide reasonable contrast on both light and dark backgrounds.

- Simple and intuitive hiccup-like markup syntax.

- Sensible templates for warning and error callouts.


<br>

## Setup


Add as a dependency to your project:


```Clojure
;; lein
[io.github.paintparty/bling "0.7.0-SNAPSHOT"]

;; deps
io.github.paintparty/bling {:mvn/version "0.7.0-SNAPSHOT"}
```
<br>

Require:

```Clojure
(require '[bling.core :refer [bling print-bling callout point-of-interest]])
```

<br>

Or, import into your namespace:

```Clojure
(ns myns.core
  (:require
    [bling.core :refer [bling print-bling callout point-of-interest]]))
```

<br>

You can view an exhaustive sampling of Bling output by pasting this snippet into your terminal:

```clojure
clj -Sdeps '{:deps {io.github.paintparty/bling {:mvn/version "0.7.0-SNAPSHOT"}}}' -e "(require '[bling.sample]) (println (bling.sample/sample))"
```
<br>
<br>

To view the above sample with Babashka, paste this snippet into your terminal:

```clojure
bb -Sdeps '{:deps {io.github.paintparty/bling {:mvn/version "0.7.0-SNAPSHOT"}}}' -e "(require '[bling.sample]) (println (bling.sample/sample))"
```

<br>
<br>


## Basic Usage 

> [!NOTE]
> If you are reading this on github in a light-mode theme, the dark-mode samples in the sections below will appear to have lower contrast than they actually do if you were viewing them in dark-mode. 

<br>

**`bling.core/print-bling`** takes any number of arguments and prints to the
console with colorization, italics, boldness, and text-decoration:

```Clojure
(print-bling [:bold "bold"]
             ", "
             [:italic "italic"]
             ", or "
             [:blue "colored"])
```


<p align="center">
  <img src="./resources/docs/chromed/basics-light.png" width="700px" align="center"/>
</p>

</p>

<p align="center"><img src="resources/docs/chromed/basics-dark.png" width="700px"/></p>

**`bling.core/print-bling`** returns `nil`.

<br>

If you just want a string (no printing), **`bling.core/bling`** works exactly
like **`bling.core/print-bling`** but does not print. Instead it returns a
string of text decorated with the appropriate ANSI SGR tags:
```Clojure
(bling [:bold.red "hello"]) ;; => "\033[38;5;203;1mhello\033[0;m"
```

In ClojureScript (browser context), **`bling`** returns a js object that
`print-bling` uses to construct a call to `js/console.log` which results in the
text decorated as expected in a browser dev console.<br>


<br>

### Combo styles

You can add multiple decorations with hiccup-style tags (a keyword with dot separators). The order of the things separated by dots doesn't matter.
```Clojure
(print-bling [:bold.italic "bold & italic"]
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
             "bold & italic & colored & strikethrough"])
```
<p align="center"><img src="resources/docs/chromed/combos-light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/combos-dark.png" width="700px" /></p>

<br>

#### Using a map instead of a hiccup-style keyword
You can also pass a map (instead of a hiccup-style keyword tag) to style the text:

```Clojure
(print-bling [{:color            :green
               :background-color :black
               :font-style       :italic
               :font-weight      :bold}
               "bold italic green text on black background"])
```
Using a map is preferrable if you are doing something like this:
```Clojure
(println (string/join (for [c ["red"
                               "orange"
                               "yellow"
                               "olive"
                               "green"
                               "blue"
                               "purple"
                               "magenta"]]
                        (bling [{:background-color c
                                 :color            :white
                                 :font-weight      :bold}
                                (str " " c " ")]))))
```

<br>


### Underline styles

```Clojure
(print-bling [:underline "underline"]
              "\n"
              [:solid-underline "solid-underline"]
              "\n"
              [:double-underline "double-underline"]
              "\n"
              [:wavy-underline "wavy-underline"]
              "\n"
              [:dotted-underline "dotted-underline"]
              "\n"
              [:dashed-underline "dashed-underline"])
```
<p align="center"><img src="resources/docs/chromed/underline-styles_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/underline-styles_dark.png" width="700px" /></p>

<br>


### Hyperlinks

Bling supports clickable hyperlinks in terminal environments. You must use `cmd` + click to navigate to the link.

```Clojure
(print-bling [{:href "http://example.com"}
              "cmd + click to follow this hyperlink"])
```
<p align="center"><img src="resources/docs/chromed/hyperlink_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/hyperlink_dark.png" width="700px" /></p>


#### Hyperlink support in browser dev consoles
Hyperlink support in browser dev consoles is actually more limited.
This is not a limitation of Bling but rather the browser.

```Clojure
(print-bling [{:href "http://example.com"}
              "My site"])
```
In a browser dev console, Bling would format the above example above like this:<br>
<code>My site <ins>http://example.com</ins></code>

If you just want a clickable link in a browser dev environment, you don't
necessarily need to use Bling's `:href` feature, as a valid url will be automatically
clickable and styled by the browser dev console.


<br>


#### Valid arguments to bling
Note that all the arguments to **`bling.core/print-bling`** and
**`bling.core/bling`** must satisfy this predicate:

```Clojure
(every? (fn [x]
          (or (and (vector? x)
                   (= 2 (count x))
                   (-> x
                       (nth 0)
                       (maybe #(or (keyword? %)
                                   (map? %)))))
              (not (coll? x))))
        args)
```

In other words, every one of the arguments to **`bling.core/print-bling`**
and **`bling.core/bling`** must be either:<br>

- A two-element vector, with the first element being a keyword or map.<br>
- A value which is not a collection.

If, for example, you wanted to print `[1 2 3]` in red, you will need to stringify the vector:

```Clojure
(print-bling [:red (str [1 2 3])])
```


<br>
<br>

### The Bling pallette 

Eleven carefully selected colors, from the [8-bit(256 colors)](https://en.m.wikipedia.org/wiki/Xterm#/media/File%3AXterm_256color_chart.svg), range(16-255) are available for use. All of these colors should display consistantly across most consoles on the end-user side. Don't expect all of the colors to pass the [strictest APCA contrast criterion](https://www.myndex.com/APCA/), but you can be sure of reasonable visibility on both light and dark backgrounds:

```Clojure
(print-bling [:bold.red "Red"]
             ", "
             [:bold.orange "Orange"]
             ", "
             [:bold.yellow "Yellow"]
             ", "
             [:bold.green "Olive"]
             ", "
             [:bold.green "Green"]
             ", "
             [:bold.blue "Blue"]
             ", "
             [:bold.blue "Purple"]
             ", "
             [:bold.magenta "Magenta"]
             ", "
             [:bold.gray "Gray"]
             ", "
             [:bold.black "Black"]
             ", "
             [:bold.white "White"])
```
<p align="center"><img src="resources/docs/chromed/colors-light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/colors-dark.png" width="700px" /></p>

<br>

### Color aliases

You can use the following semantic aliases for some colors:
```Clojure
(print-bling [:bold.negative "Negative"]
             ", "
             [:bold.error "Error"]
             ", "
             [:bold.warning "Warning"]
             ", "
             [:bold.positive "Positive"]
             ", "
             [:bold.info "Info"]
             ", "
             [:bold.subtle "Subtle"]
             ", "
             [:bold.neutral "Neutral"])
```
<br>

<p align="center"><img src="resources/docs/chromed/semantic-colors-light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/semantic-colors-dark.png" width="700px" /></p>

<br>

### Using system colors

Bling also supports named color aliases for system colors (16 colors).

Most likely, you do **not** want to use these. They will not display consistently across user spaces, as the actual color is dictated by the theme the user has selected in their particular terminal emulator.

If, however, you are using Bling to provide errors, warnings, and messages for that only you will ever see (on your own computer), and your terminal emulator theme is totally dialed-in to your liking, then system colors might be an option worth exploring.


```Clojure
(print-bling [:system-black "black (SYSTEM)"])
(print-bling [:system-maroon "maroon (SYSTEM)"])
(print-bling [:system-green "green (SYSTEM)"])
(print-bling [:system-olive "olive (SYSTEM)"])
(print-bling [:system-navy "navy (SYSTEM)"])
(print-bling [:system-purple "purple (SYSTEM)"])
(print-bling [:system-teal "teal (SYSTEM)"])
(print-bling [:system-silver "silver (SYSTEM)"])
(print-bling [:system-grey "grey (SYSTEM)"])
(print-bling [:system-red "red (SYSTEM)"])
(print-bling [:system-lime "lime (SYSTEM)"])
(print-bling [:system-yellow "yellow (SYSTEM)"])
(print-bling [:system-blue "blue (SYSTEM)"])
(print-bling [:system-fuchsia "fuchsia (SYSTEM)"])
(print-bling [:system-aqua "aqua (SYSTEM)"])
(print-bling [:system-white "white (SYSTEM)"])
```
<br>

### Using arbitrary colors

Bling also supports arbitrary colors in the 8-bit(256 colors range). If you are using
arbitrary colors to construct messages to stdout that other people might have to
read, you may want to test the appearance with both light and dark terminal
themes. They must be provided as integers, so you will need to use an options
map instead of a hiccup-style keyword:

```Clojure
(print-bling [{:color 180} "8-bit color 180, aka Tan"])
```

<br>

<br>

## Callout blocks
**`bling.core/callout`** will print a message "block" to the console with a colored bounding border in the inline-start position.

**`callout`** takes one or two arguments. If two arguments are supplied, the first should be a map with 0 or more of following entries:

| Key               | Pred                    | Description                                                  |
| :---------------  | -----------------       | ------------------------------------------------------------ |
| `:type`           | #{`keyword?` `string?`} | Should be one of: `:error`,  `:warning` , or `:info`. <br>Will set the label text (unless provided via `:label`). Will also set the `:colorway`, and override any provided `:colorway` value. |
| `:colorway`       | #{`keyword?` `string?`} | The color of the sideline border, or gutter, depending on the value of `:theme`.<br />Should be one of: `:error`,  `:warning` , `:info` , `:positive`, or `:subtle`. <br>Can also be any one of the pallete colors such as  `:magenta`, `:green`,  `:negative`, `:neutral`, etc. |
| `:theme`          | #{`keyword?` `string?`} | Theme of callout. Can be one of `:sideline`, `:sideline-bold`, or `:gutter`. Defaults to `:sideline`. |
| `:label`          | `any?`                  | Labels the callout. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string). <br>In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively. |
| `:label-theme`    | #{`keyword?` `string?`} | Theme of label. Can be one of `:marquee` or `:minimal`. Defaults to `:minimal`. |
| `:padding-top`    | `int?`                  | Amount of padding (in newlines) at top, inside callout.<br/>Defaults to `0`. |
| `:padding-bottom` | `int?`                  | Amount of padding (in newlines) at bottom, inside callout.<br>Defaults to `0`. In browser console, defaults to `1` in the case of callouts of type `:warning` or `:error`.|
| `:padding-left`   | `int?`                  | Amount of padding (in blank character spaces) at left, inside callout.<br>In console emulator, defaults to `2`. In browser console, defaults to `0`.|
| `:margin-top`     | `int?`                  | Amount of margin (in newlines) at top, outside callout.<br>Defaults to `1`. Only applies to terminal emulator printing. |
| `:margin-bottom`  | `int?`                  | Amount of margin (in newlines) at bottom, outside callout.<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-left`    | `int?`                  | Amount of margin (in blank character spaces) at left, outside callout.<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:data?`          | `boolean?`              | Returns a data representation of result instead of printing it. |


<br>
<br>

Examples of `callout` with different `:type` / `:colorway` options:

```Clojure
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

(callout {:type :subtle}
         "Example callout, with :type of :subtle (or :gray)")

(callout {:type :magenta}
         "Example callout, with :type of :magenta")

(callout "Example callout, default")
```

<br>

The above calls would render the following in your terminal emulator: 
<p align="center"><img src="resources/docs/chromed/callouts_sideline_minimal_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callouts_sideline_minimal_dark.png" width="700px" /></p>
<br>

With `{:theme :sideline-bold}`: 
<p align="center"><img src="resources/docs/chromed/callouts_sideline-bold_minimal_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callouts_sideline-bold_minimal_dark.png" width="700px" /></p>
<br>

Callout accepts a `:label-theme` option. Supplying a value of `:marquee`, will render
the label inside a box: 

<p align="center"><img src="resources/docs/chromed/callouts_sideline_marquee_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callouts_sideline_marquee_dark.png" width="700px" /></p>
<br>

With `{:theme :sideline-bold :label-theme :marquee}`: 
<p align="center"><img src="resources/docs/chromed/callouts_sideline-bold_marquee_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callouts_sideline-bold_marquee_dark.png" width="700px" /></p>
<br>


<br>

## Templates for errors and warnings

**`bling.core/callout`**, paired with **`bling.core/point-of-interest`** 
is perfect for creating your own custom error or warning messages. 



Here is an example of creating a custom callout for an error message.
You must provide the relevant `:file`, `:line`, `:column`, and `:form` values.

```Clojure
(defn example-custom-callout
  [{:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header "Your header message goes here."
                             :body   (str "The body of your message goes here."
                                          "\n"
                                          "Another line of copy."
                                          "\n"
                                          "Another line."
                                          )}
                            point-of-interest-opts)
        message      (point-of-interest poi-opts)
        callout-opts (merge callout-opts
                            {:padding-top 1})]
    (callout callout-opts message)))

(example-custom-callout
 {:point-of-interest-opts {:type                  :error
                           :file                  "example.ns.core"
                           :line                  11
                           :column                1
                           :form                  '(+ foo baz)
                           :text-decoration-index 2}
  :callout-opts           {:type :error}})
```


<br>

The above callout would render like this your terminal emulator: 

<p align="center"><img src="resources/docs/chromed/callout-with-poi_sideline_minimal_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callout-with-poi_sideline_minimal_dark.png" width="700px" /></p>

You can also render such warning and error callouts using a `:label-theme` value of `:marquee`. 

<p align="center"><img src="resources/docs/chromed/callout-with-poi_sideline_marquee_dark.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callout-with-poi_sideline_marquee_dark.png" width="700px" /></p>

The diagram inside the callout that shows the namespace, line, column, and form
with underlined is created by **`bling.core/point-of-interest`**, which takes a
single map with the following options:

<br>


| Key                | Pred                   | Description                                                  |
| :--------          | -----------------      | ------------------------------------------------------------ |
| `:file`            | `string?`              | File or namespace                                            |
| `:line`            | `integer?`             | Line number                                                  |
| `:column`          | `integer?`             | Column number                                                |
| `:form`            | `any?`                 | The form to draw attention to. Will be cast to string and truncated at 33 chars |
| `:header`          | `any?`                 | Typically, a string. If multi-line, string should be composed with newlines as desired. In a browser context, can be an instance of `bling.core/Enriched` (produced by using `bling.core/enriched`)|
| `:body`            | `any?`                 | Typically, a string. If multi-line, string should be composed with newlines as desired. In a browser context, can be an instance of `bling.core/Enriched` (produced by using `bling.core/enriched`)|
| `:margin-block`    | `int?`                 | Controls the number of blank lines above and below the diagram.<br/>Defaults to `1`.|
| `:type`            | #{`:error` `:warning`} | Automatically sets the `:text-decoration-color`. |
| `:text-decoration-color` | #{`keyword?` `string?`} | Controls the color of the underline. Should be one of: `:error` `:warning`, or `:neutral`.<br>Can also be any one of the pallete colors such as  `:magenta`, `:green`,  `:negative`, `:neutral`, etc. Defaults to `:neutral` |
| `:text-decoration-style` | #{`:wavy` `:solid` `:dashed` `:dotted` `:double`} | Controls the color of the underline. |
| `:text-decoration-index` | `pos-int?` | If the value of `:form` is a collection, this is the index of the item to apply text-decoration (underline). |
<br>
<br>

## Go heavy

If you want to place more emphasis on your callouts you can pass
**`bling.core/callout`** a `:theme` option with a value of `:gutter`. With the
`:gutter` theme, the thickness of the colored border is controlled by the value of
`:margin-left`. Here is an example using the `example-custom-callout` function we
defined above:

```Clojure
(example-custom-callout
 {:file          "example.ns.core"
  :line          11
  :column        1
  :form          '(+ 1 true)
  :type          :error
  :theme         :gutter})
```

<p align="center"><img src="resources/docs/chromed/callout-with-poi_gutter_with-colored-labels_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callout-with-poi_gutter_with-colored-labels_dark.png" width="700px" /></p>

Example value of `2` for `:margin-left`, to increase the weight:

```Clojure
(example-custom-callout
 {:file          "example.ns.core"
  :line          11
  :column        1
  :form          '(+ 1 true)
  :type          :error
  :margin-left   2
  :theme         :gutter})
```
<p align="center"><img src="resources/docs/chromed/callout-with-poi_gutter2_with-colored-labels_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callout-with-poi_gutter2_with-colored-labels_dark.png" width="700px" /></p>

More callout examples of the `:gutter` theme:

<p align="center"><img src="resources/docs/chromed/callouts_gutter_with-colored-labels_light.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callouts_gutter_with-colored-labels_dark.png"  width="700px" /></p>


More example values of `2` for `:margin-left`, to increase the weight:

<p align="center"><img src="resources/docs/chromed/callouts_gutter2_with-colored-labels_dark.png" width="700px" /></p>
<p align="center"><img src="resources/docs/chromed/callouts_gutter2_with-colored-labels_dark.png"  width="700px" /></p>


<br>

## Figlet banners

Bling features basic support for composing <a href="https://en.wikipedia.org/wiki/FIGlet" target="_blank">Figlet</a> ascii-art
banners with <a href="https://github.com/busyloop/lolcat" target="_blank">lolcat-like gradient overlays</a>. Bling ships with a
small handful of ported Figlet fonts. We will probably add a few more in the future but offering an exhaustive library of Figlet
fonts is currently a non-goal. The glyph layout implementation is bare-bones and there is currently no support for standard figlet
"smushing". Figlet banners only work in terminal context (JVM Clojure or Node.js ClojureScript).

For a quick sample in your terminal:
```clojure
clj -Sdeps '{:deps {io.github.paintparty/bling {:mvn/version "0.7.0-SNAPSHOT"}}}' -e "(require '[bling.sample]) (println (bling.sample/sample))"
```
### Banner usage

Require:
```Clojure
(require '[bling.banner :refer [banner]])
```

Or add to your namespace `:requires`:
```Clojure
(ns myns.core
 (:require [bling.banner :refer [banner]])
```

Available fonts:
```Clojure
bling.fonts/miniwi
bling.fonts/ansi-shadow
bling.fonts/drippy
bling.fonts/big
bling.fonts/big-money
bling.fonts/rounded
bling.fonts/isometric-1
```

Below are some example calls and a screenshot of the results.
```Clojure
(banner 
 {:font     miniwi
  :text     "Miniwi"
  :gradient "to right, purple, orange"})

(banner
 {:font     ansi-shadow
  :text     "Ansi"
  :gradient "to top, warm, cool"}

(banner
 {:font     drippy
  :text     "Drippy"
  :gradient "to bottom, red, magenta"}

(banner
 {:font     big
  :text     "Big"
  :gradient "to top, yellow, purple"}

(banner
 {:font     big-money
  :text     "Money"
  :gradient "to top, green, blue"}

(banner
 {:font        rounded
  :font-weight :bold
  :text        "Rounded" 
  :gradient    "to left, cool, warm"}

(banner
 {:font        isometric-1
  :font-weight :bold
  :text        "ABCDE"
  :gradient    "to right, red, magenta"}
```

<p align="center">
  <img src="resources/docs/chromed/bling-banner-fonts_dark-0.7.0.png"
       width="700px" />
</p>

### All the options for `bling.banner/banner` 

| Key               | Pred       | Description   |
| :---------------  | -----------| ------------- |
| `:font`           | `map?`     | Must be one of the fonts that ships with Bling:<br><br> `bling.fonts/ansi-shadow`<br> `bling.fonts/big-money`<br> `bling.fonts/big`<br> `bling.fonts/miniwi`<br> `bling.fonts/drippy`<br>`bling.fonts/isometric-1`<br><br>Defaults to `bling.fonts/ansi-shadow`.<br>|
| `:text`           | `string?`  | The text to set in the banner.
| `:font-weight`    | `keyword?` | If set to bold, each subchar in figlet characters will be bolded. Only applies when a gradient is set.
| `:gradient`       | `string?`  | Expects a string as first argument representing a linear-gradient in standard css syntax: `"to bottom, yellow, purple"`.<br><br>Only the following color pairs are valid (order can be reversed):<br>`green, blue`<br>`red, magenta`<br>`yellow, purple`<br>`orange, purple`<br>`cool, warm`.<br><br>Valid directions are:<br>`to top`<br>`to bottom`<br>`to right`<br>`to left`.<br><br>Only applies to terminal emulator printing|
| `:gradient-shift` | `int?`     | If gradient is `warm` / `cool` pair, this will shift the hue. `0-5`. Defaults to `0`.|
| `:contrast`       | `keyword?` | If gradient is set, this will force an overall lighter or darker tone. Defaults to `medium`. If the user has a `BLING_THEME` env var set, it will default to `high` in order to optimize contrast for the users terminal theme (light or dark) |
| `:margin-top`     | `int?`     | Amount of margin (in newlines) at top, outside banner.<br>Defaults to `1`. Only applies to terminal emulator printing. |
| `:margin-bottom`  | `int?`     | Amount of margin (in newlines) at bottom, outside banner.<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-left`    | `int?`     | Amount of margin (in blank character spaces) at left, outside banner.<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-right`   | `int?`     | Amount of margin (in blank character spaces) at right, outside banner.<br>Defaults to `0`. Only applies to terminal emulator printing. |

<br>

> [!NOTE]
Figlet banners only work in terminal context (JVM Clojure or Node.js ClojureScript).
If you want a simple banner in a browser dev console, you can do the following:
```Clojure
(banner {:text "Hello" :browser-style "font-color:red;font-size:24px"})
```


## Testing
There is a set of visual test suites in `bling.core-test`.

For visual testing of output in node / deno context first do:

`shadow-cljs compile node-script`

Then do 

`node out/bling-in-node-demo-script.js`

<br>

## Status / Roadmap
Alpha, subject to change. Issues welcome, see [contributing](#contributing).

<br>

## Contributing
Issues for bugs, improvements, or features are very welcome. Please file an
issue for discussion before starting or issuing a PR.


<br>

<!-- ## Alternatives / Prior Art -->

<br>          
