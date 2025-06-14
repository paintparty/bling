# Changelog
[Bling](https://github.com/paintparty/bling): Helps you quickly get rich text into your console printing. 


For a list of breaking changes, check [here](#breaking-changes)


## Unreleased
#### Added 

#### Changed

#### Fixed

#### Removed

<br>
<br>

## 0.8.4

<p><sub>Many thanks to <a href="https://www.clojuriststogether.org/">Clojurists Together</a> for generously supporting this release!</sub></p>

2025-06-08

#### Changed
- Updated `fireworks` dep `0.12.2` 

<br>
<br>


## 0.8.3

<p><sub>Many thanks to <a href="https://www.clojuriststogether.org/">Clojurists Together</a> for generously supporting this release!</sub></p>

2025-06-08

#### Fixed 
- `bling.sample/explain-malli-examples`

#### Changed
- Updated `fireworks` dep `0.12.1` 

<br>
<br>


## 0.8.2

<p><sub>Many thanks to <a href="https://www.clojuriststogether.org/">Clojurists Together</a> for generously supporting this release!</sub></p>

2025-06-08

#### Added 
- [Minimal theme template](https://github.com/paintparty/bling/issues/35)

#### Changed
- `bling.core/callout` now takes any number of arguments.

<br>
<br>

## 0.8.0

<p><sub>Many thanks to <a href="https://www.clojuriststogether.org/">Clojurists Together</a> for generously supporting this release!</sub></p>

2025-06-01

#### Fixed
- [Fix hifi printing for cljs browser context](https://github.com/paintparty/bling/issues/32)

#### Added 
- [Support for NO_COLOR env var](https://github.com/paintparty/bling/issues/27)

<br>
<br>


## 0.7.0

<p><sub>Many thanks to <a href="https://www.clojuriststogether.org/">Clojurists Together</a> for generously supporting this release!</sub></p>

2025-05-30

#### Added 
- [Support for color figlet banner art](https://github.com/paintparty/bling?tab=readme-ov-file#figlet-banners)
  - Docs for `bling.banner` [here](https://github.com/paintparty/bling/tree/main?tab=readme-ov-file#figlet-banners)
- [Add Hi-Fidelity printing of edn data via](https://github.com/paintparty/bling/issues/29)
  - Docs for `bling.hifi` [here](https://github.com/paintparty/bling/tree/main?tab=readme-ov-file#high-fidelity-printing)
- [Create custom explain printer for Malli](https://github.com/paintparty/bling/issues/28)
  - Docs for `bling.explain` [here](https://github.com/paintparty/bling/tree/main?tab=readme-ov-file#usage-with-malli)
- [Introduced `BLING_MOOD` env var](https://github.com/paintparty/bling/issues/30)
  - Docs [here](https://github.com/paintparty/bling/tree/main?tab=readme-ov-file#enhanced-contrast)
- [Light and dark versions of each pallette color for optimal contrast via `BLING_MOOD` env var](https://github.com/paintparty/bling/issues/26)
  - Docs [here](https://github.com/paintparty/bling/tree/main?tab=readme-ov-file#enhanced-contrast)

<br>
<br>


## 0.6.0

<p><sub>Many thanks to <a href="https://www.clojuriststogether.org/">Clojurists Together</a> for generously supporting this release!</sub></p>

2025-04-25

Added support for underline styles and also hyperlinks.

#### Fixed
- [:text-decoration value bug](https://github.com/paintparty/bling/issues/21)

#### Added 
- [Underline/text-decoration styles](https://github.com/paintparty/bling/issues/24)
- [Support for hyperlinks](https://github.com/paintparty/bling/issues/10)

<br>
<br>


## 0.5.2
2025-03-09

Various internal refactoring and code cleanup, in addtion to new features:

#### Fixed
- [Dynamic underline length in point-of-interest diagram](https://github.com/paintparty/bling/issues/4)

#### Added 
- [Theming system](https://github.com/paintparty/bling/issues/13)
- New "marquee" style labels for callouts
- New `:gutter` theme allows for arbitrarily large styled gutters, controlled by `:margin-left` value
- `:text-decoration-*` options added to `point-of-interest` options. 

#### Changed
- [Redesigned `point-of-interest` interface](https://github.com/paintparty/bling/issues/14)

#### Removed
- `:border-weight` options (now irrelevent with new theming system).

<br>
<br>

## 0.4.2
2024-11-19
#### Fixed
- Cljs error when printing only body with enriched text

<br>
<br>

## 0.4.1
2024-11-19
#### Fixed
- Support for terminal printing in node context
- Prining of callout with label and no body

<br>
<br>


## 0.4.0
#### Added 
- Added bling.sample namespace

#### Fixed
- `:text-decoration` like underline and strikethrough props not working in browser console.

<br>
<br>

## 0.3.0
#### Added 
- Add underline and strikethrough styling

<br>
<br>

## 0.2.0
2024-08-26
#### Added 
- Orange, Olive, and Purple to the standard Bling pallette
- [Named color aliases for system colors](https://github.com/paintparty/bling#using-system-colors)
- [Ability to use arbitrary xTerm (0-256) colors](https://github.com/paintparty/bling#using-arbitrary-colors)

<br>
<br>

## 0.1.1
2024-08-26
#### Fixed
- Uncolored bold italic not showing italics - `(bling [:bold.italic "hi"])`

<br>
<br>

## 0.1.0
2024-08-24

### Initial Release

<br>
<br>

## Breaking changes

## 0.5.0
- The `:type` option for `callout` now only accepts for `:error`, `:warning`, and `:info`. Previously, it accepted the other semantic color aliases such as `:positive`, `:subtle`, `:accent`, etc., as well any of the other named colors from the [Bling pallette](https://github.com/paintparty/bling?tab=readme-ov-file#the-bling-pallette). These alias and color names can be now provided by the new `:colorway` option. If both `:colorway` and `:type` options are supplied, the `:type` option will take precedence. 

- The `:border-weight` option has been removed, replaced by the functionality provided by the new `:theme` option.

- By default, `callout` labels are printed in `:neutral`. This change was made for accesibility (contrast) reasons. Previously, these labels inherited the color set by the `:type` value. 

- Labels with the default value of `:minimal` for the `:label-theme` option are bolded by default. Labels with the value of `:marquee` for the `:label-value` option are not bolded.

- The `:warning` alias is now mapped to `:orange.` Previously it was mapped to `:yellow`.

