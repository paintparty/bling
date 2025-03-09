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

## 0.5.0
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

