# Table of contents
-  [`bling.banner`](#bling.banner) 
    -  [`banner`](#bling.banner/banner) - Returns a Figlet (ascii art) banner with the provided <code>:text</code> option.
-  [`bling.explain`](#bling.explain) 
    -  [`explain-malli`](#bling.explain/explain-malli) - Prints a malli validation error callout block via bling.core/callout.

-----
# <a name="bling.banner">bling.banner</a>






## <a name="bling.banner/banner">`banner`</a>
``` clojure

(banner m)
```
Function.

Returns a Figlet (ascii art) banner with the provided `:text` option.

   Intended to render a "single line" banner, or multiple lines of constituent
   characters.

   Can be optionally colorized with the `:gradient` option, using a set of
   pre-defined gradients:

   ```clojure
   (banner {:text "Hello", :gradient-colors [:green :blue]})
   (banner {:text "Hello", :gradient-colors [:red :magenta]})
   (banner {:text "Hello", :gradient-colors [:yellow :purple]})
   (banner {:text "Hello", :gradient-colors [:orange :purple]})
   (banner {:text "Hello", :gradient-colors [:cool :warm]})
   ```

   Can be colorized solid with `bling.core/bling`

   ```clojure
   (bling [:red (banner {:text "Hello"})])
   ```

   All the options you need:

   * **`:font`**
       - `map?`
       - Optional.
       - Defaults to `bling.fonts.ansi-shadow/ansi-shadow`.
       - Must be one of the fonts that ships with Bling:
           - bling.fonts.ansi-shadow/ansi-shadow
           - bling.fonts.big-money.big-money/big-money
           - bling.fonts.big/big
           - bling.fonts.miniwi/miniwi
           - bling.fonts.drippy/drippy
           - bling.fonts.isometric-1/isometric-1

   * **`:text`**
       - `string?`
       - Required.
       - The text to set in the banner.

   * **`:font-weight`**
       - `#{:bold "bold"}`
       - Optional.
       - If set to `:bold`, each subchar in figlet characters will be bolded. Only applies when a gradient is set.

   * **`:gradient-colors`**
       - `#{[:yellow :purple] ["orange" "purple"] ["cool" "warm"] ["green" "blue"] [:red :magenta] ["yellow" "purple"] ["red" "magenta"] [:green :blue] [:cool :warm] [:orange :purple]}`
       - Optional.
       - Sets the beginning and end colors of the gradient. Expects a vector of 2 keywords.

   * **`:gradient-direction`**
       - `#{:to-right :to-top "to-bottom" "to-right" "to-top" "to-left" :to-left :to-bottom}`
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
       - If gradient is set, this will force an overall lighter or darker tone. If the user has a `BLING_MOOD` env var set, it will default to `:high` in order to optimize contrast for the users terminal theme (light or dark)

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
       - Amount of margin (in blank character spaces) at left, outside banner.

   * **`:margin-right`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in blank character spaces) at right, outside banner.

   
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/banner.cljc#L1255-L1484">Source</a></sub></p>

-----
# <a name="bling.explain">bling.explain</a>






## <a name="bling.explain/explain-malli">`explain-malli`</a>
``` clojure

(explain-malli schema v)
(explain-malli
 schema
 v
 {:keys
  [spacing
   success-message
   highlighted-problem-section-label
   section-body-indentation
   omit-section-labels
   omit-sections
   select-keys-in-problem-path?
   preamble-section-label
   preamble-section-body
   surround-disjunctor-with-tilde?
   highlight-missing-keys?
   display-schema?
   display-explain-data?
   file-info-str
   callout-opts],
  :or {success-message :bling.explain/explain-malli-success},
  :as explain-malli-opts})
```
Function.

Prints a malli validation error callout block via bling.core/callout.
   Within the block, the value is pretty-printed, potentially with syntax
   coloring. The problem value is highlighted with the `:highlight-error`
   class of the active fireworks theme, or the `:highlight-error-underlined`
   class, if the value is not a collection.

   If two three arguments are provided, the third should be a map with the following optional keys:

   * **`:function-name`**
       - `string?`
       - Optional.
       - The name of the function that can be used to construct the source location.

   * **`:file`**
       - `[:or :pos-int :string]`
       - Optional.
       - The file name that can be used to construct the source location.

   * **`:line`**
       - `[:or :pos-int :string]`
       - Optional.
       - The line number that can be used to construct the source location.

   * **`:column`**
       - `[:or :pos-int :string]`
       - Optional.
       - The column number that can be used to construct the source location.

   * **`:spacing`**
       - `#{:compact "compact"}`
       - Optional.
       - If the value of `:spacing` is set to `:compact`, the callout is compacted vertically.

   * **`:display-schema?`**
       - `boolean?`
       - Optional.
       - Displays the schema passed to the underlying call to `malli.core/explain`.

   * **`:display-explain-data?`**
       - `boolean?`
       - Optional.
       - Displays the output of `malli.core/explain` within the callout block.

   * **`:callout-opts`**
       - `map?`
       - Optional.
       - A map of options for the underlying call to bling.core/callout.
<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/explain.cljc#L390-L733">Source</a></sub></p>
