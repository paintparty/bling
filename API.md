# Table of contents
-  [`bling.banner`](#bling.banner) 
    -  [`banner`](#bling.banner/banner) - Returns a multi-line Figlet (ascii art) string with the provided <code>:text</code> option.

-----
# <a name="bling.banner">bling.banner</a>






## <a name="bling.banner/banner">`banner`</a>
``` clojure

(banner m)
```
Function.

Returns a multi-line Figlet (ascii art) string with the provided `:text` option.

Intended to render a single line banner.

Can be optionally colorized with the `:gradient` option, using a set of pre-defined gradients:

```clojure
(banner {:text "Hello", :gradient-colors [:green :blue]})
(banner {:text "Hello", :gradient-colors [:red :magenta]})
(banner {:text "Hello", :gradient-colors [:yellow :purple]})
(banner {:text "Hello", :gradient-colors [:orange :purple]})
(banner {:text "Hello", :gradient-colors [:cool :warm]})
```

All the options:

* **`:font`**
    - `map?`
    - Optional.
    - Defaults to bling.fonts.ansi-shadow/ansi-shadow.
    - Must be one of the fonts that ships with Bling:
        - `bling.fonts.ansi-shadow/ansi-shadow`,
        - `bling.fonts.big-money.big-money/big-money`,
        - `bling.fonts.big/big`, `bling.fonts.miniwi/miniwi`,
        - `bling.fonts.drippy/drippy,` or
        - `bling.fonts.isometric-1/isometric-1`.
* **`:text`**
    - `string?`
    - Required.
    - The text to set in the banner.
* **`:font-weight`**
    - `#{:bold "bold"}`
    - Optional.
    - If set to `:bold`, each subchar in figlet characters will be bolded. Only applies when a gradient is set.
* `:gradient-colors`
    - `#{[:yellow :purple] ["orange" "purple"] ["cool" "warm"] ["green" "blue"] [:red :magenta] ["yellow" "purple"] ["red" "magenta"] [:green :blue] [:cool :warm] [:orange :purple]}`
    - Optional.
    - Sets the beginning and end colors of the gradient. Expects a vector of 2 keywords.
* `:gradient-direction`
    - `#{:to-right :to-top "to-bottom" "to-right" "to-top" "to-left" :to-left :to-bottom}`
    - Optional.
    - Direction of the gradient.
* `:gradient-shift`
    - `[:int {:min 0, :max 5}]`
    - Optional.
    - Defaults to 0.
    - If gradient is `[:warm :cool]` pair, this will shift the hue.
* `:contrast`
    - `keyword?`
    - Optional.
    - Defaults to :medium.
    - If gradient is set, this will force an overall lighter or darker tone. If the user has a `BLING_MOOD` env var set, it will default to `:high` in order to optimize contrast for the users terminal theme (light or dark)
* `:margin-top`
    - `int?`
    - Optional.
    - Defaults to 1.
    - Amount of margin (in newlines) at top, outside banner.
* `:margin-bottom`
    - `int?`
    - Optional.
    - Defaults to 0.
    - Amount of margin (in newlines) at bottom, outside banner.
* `:margin-left`
    - `int?`
    - Optional.
    - Defaults to 0.
    - Amount of margin (in blank character spaces) at left, outside banner.
* `:margin-right`
    - `int?`
    - Optional.
    - Defaults to 0.
    - Amount of margin (in blank character spaces) at right, outside banner.

<p><sub><a href="https://github.com/paintparty/bling/blob/0.10.0v2/src/bling/banner.cljc#L1252-L1461">Source</a></sub></p>
