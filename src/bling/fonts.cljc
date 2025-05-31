(ns
 bling.fonts
 (:require
  [bling.fonts.miniwi :refer [miniwi]]
  [bling.fonts.ansi-shadow :refer [ansi-shadow]]
  [bling.fonts.drippy :refer [drippy]]
  [bling.fonts.big :refer [big]]
  [bling.fonts.big-money :refer [big-money]]
  [bling.fonts.rounded :refer [rounded]]
  [bling.fonts.isometric-1 :refer [isometric-1]]))



(def
 fonts-by-kw
 {:miniwi miniwi,
  :ansi-shadow ansi-shadow,
  :drippy drippy,
  :big big,
  :big-money big-money,
  :rounded rounded,
  :isometric-1 isometric-1})
