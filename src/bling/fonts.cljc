(ns bling.fonts
  (:require [clojure.string :as string]
            [clojure.pprint :refer [pprint]]
            [bling.fontlib]
            [bling.util :refer [sjr]]
            [bling.macros :refer [?]]
            [clojure.set]))

(declare ascii-chars)

(def ^{:default "ANSI Shadow"} fonts
  {"ANSI Shadow"  {:chars-array-map {" " {:bands     ["   " "   " "   " "   " "   " "   " "   "],
                                          :i         0,
                                          :character " ",
                                          :width     3,
                                          :height    7},
                                     "!" {:bands     ["██╗" "██║" "██║" "╚═╝" "██╗" "╚═╝" "   "],
                                          :i         1,
                                          :character "!",
                                          :width     3,
                                          :height    7},
                                     "\"" {:bands     ["" "" "" "" "" ""],
                                           :i         2,
                                           :character "\"",
                                           :width     0,
                                           :height    6},
                                     "#" {:bands     [" ██╗ ██╗ "
                                                      "████████╗"
                                                      "╚██╔═██╔╝"
                                                      "████████╗"
                                                      "╚██╔═██╔╝"
                                                      " ╚═╝ ╚═╝ "
                                                      "         "],
                                          :i         3,
                                          :character "#",
                                          :width     9,
                                          :height    7},
                                     "$" {:bands     ["▄▄███▄▄·"
                                                      "██╔════╝"
                                                      "███████╗"
                                                      "╚════██║"
                                                      "███████║"
                                                      "╚═▀▀▀══╝"
                                                      "        "],
                                          :i         4,
                                          :character "$",
                                          :width     8,
                                          :height    7},
                                     "%" {:bands     ["██╗ ██╗"
                                                      "╚═╝██╔╝"
                                                      "  ██╔╝ "
                                                      " ██╔╝  "
                                                      "██╔╝██╗"
                                                      "╚═╝ ╚═╝"
                                                      "       "],
                                          :i         5,
                                          :character "%",
                                          :width     7,
                                          :height    7},
                                     "&" {:bands     ["   ██╗   "
                                                      "   ██║   "
                                                      "████████╗"
                                                      "██╔═██╔═╝"
                                                      "██████║  "
                                                      "╚═════╝  "
                                                      "         "],
                                          :i         6,
                                          :character "&",
                                          :width     9,
                                          :height    7},
                                     "'" {:bands     ["" "" "" "" "" ""],
                                          :i         7,
                                          :character "'",
                                          :width     0,
                                          :height    6},
                                     "(" {:bands     [" ██╗" "██╔╝" "██║ " "██║ " "╚██╗" " ╚═╝" "    "],
                                          :i         8,
                                          :character "(",
                                          :width     4,
                                          :height    7},
                                     ")" {:bands     ["██╗ " "╚██╗" " ██║" " ██║" "██╔╝" "╚═╝ " "    "],
                                          :i         9,
                                          :character ")",
                                          :width     4,
                                          :height    7},
                                     "*" {:bands     ["      " "▄ ██╗▄" " ████╗" "▀╚██╔▀" "  ╚═╝ " "      " "      "],
                                          :i         10,
                                          :character "*",
                                          :width     6,
                                          :height    7},
                                     "+" {:bands     ["" "" "" "" "" ""],
                                          :i         11,
                                          :character "+",
                                          :width     0,
                                          :height    6},
                                     "," {:bands     ["   " "   " "   " "   " "▄█╗" "╚═╝" "   "],
                                          :i         12,
                                          :character ",",
                                          :width     3,
                                          :height    7},
                                     "-" {:bands     ["      " "      " "█████╗" "╚════╝" "      " "      " "      "],
                                          :i         13,
                                          :character "-",
                                          :width     6,
                                          :height    7},
                                     "." {:bands     ["   " "   " "   " "   " "██╗" "╚═╝" "   "],
                                          :i         14,
                                          :character ".",
                                          :width     3,
                                          :height    7},
                                     "/" {:bands     ["    ██╗"
                                                      "   ██╔╝"
                                                      "  ██╔╝ "
                                                      " ██╔╝  "
                                                      "██╔╝   "
                                                      "╚═╝    "
                                                      "       "],
                                          :i         15,
                                          :character "/",
                                          :width     7,
                                          :height    7},
                                     "0" {:bands     [" ██████╗ "
                                                      "██╔═████╗"
                                                      "██║██╔██║"
                                                      "████╔╝██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         16,
                                          :character "0",
                                          :width     9,
                                          :height    7},
                                     "1" {:bands     [" ██╗" "███║" "╚██║" " ██║" " ██║" " ╚═╝" "    "],
                                          :i         17,
                                          :character "1",
                                          :width     4,
                                          :height    7},
                                     "2" {:bands     ["██████╗ "
                                                      "╚════██╗"
                                                      " █████╔╝"
                                                      "██╔═══╝ "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         18,
                                          :character "2",
                                          :width     8,
                                          :height    7},
                                     "3" {:bands     ["██████╗ "
                                                      "╚════██╗"
                                                      " █████╔╝"
                                                      " ╚═══██╗"
                                                      "██████╔╝"
                                                      "╚═════╝ "
                                                      "        "],
                                          :i         19,
                                          :character "3",
                                          :width     8,
                                          :height    7},
                                     "4" {:bands     ["██╗  ██╗"
                                                      "██║  ██║"
                                                      "███████║"
                                                      "╚════██║"
                                                      "     ██║"
                                                      "     ╚═╝"
                                                      "        "],
                                          :i         20,
                                          :character "4",
                                          :width     8,
                                          :height    7},
                                     "5" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "███████╗"
                                                      "╚════██║"
                                                      "███████║"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         21,
                                          :character "5",
                                          :width     8,
                                          :height    7},
                                     "6" {:bands     [" ██████╗ "
                                                      "██╔════╝ "
                                                      "███████╗ "
                                                      "██╔═══██╗"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         22,
                                          :character "6",
                                          :width     9,
                                          :height    7},
                                     "7" {:bands     ["███████╗"
                                                      "╚════██║"
                                                      "    ██╔╝"
                                                      "   ██╔╝ "
                                                      "   ██║  "
                                                      "   ╚═╝  "
                                                      "        "],
                                          :i         23,
                                          :character "7",
                                          :width     8,
                                          :height    7},
                                     "8" {:bands     [" █████╗ "
                                                      "██╔══██╗"
                                                      "╚█████╔╝"
                                                      "██╔══██╗"
                                                      "╚█████╔╝"
                                                      " ╚════╝ "
                                                      "        "],
                                          :i         24,
                                          :character "8",
                                          :width     8,
                                          :height    7},
                                     "9" {:bands     [" █████╗ "
                                                      "██╔══██╗"
                                                      "╚██████║"
                                                      " ╚═══██║"
                                                      " █████╔╝"
                                                      " ╚════╝ "
                                                      "        "],
                                          :i         25,
                                          :character "9",
                                          :width     8,
                                          :height    7},
                                     ":" {:bands     ["   " "██╗" "╚═╝" "██╗" "╚═╝" "   " "   "],
                                          :i         26,
                                          :character ":",
                                          :width     3,
                                          :height    7},
                                     ";" {:bands     ["   " "██╗" "╚═╝" "▄█╗" "▀═╝" "   " "   "],
                                          :i         27,
                                          :character ";",
                                          :width     3,
                                          :height    7},
                                     "<" {:bands     ["  ██╗" " ██╔╝" "██╔╝ " "╚██╗ " " ╚██╗" "  ╚═╝" "     "],
                                          :i         28,
                                          :character "<",
                                          :width     5,
                                          :height    7},
                                     "=" {:bands     ["" "" "" "" "" ""],
                                          :i         29,
                                          :character "=",
                                          :width     0,
                                          :height    6},
                                     ">" {:bands     ["██╗  " "╚██╗ " " ╚██╗" " ██╔╝" "██╔╝ " "╚═╝  " "     "],
                                          :i         30,
                                          :character ">",
                                          :width     5,
                                          :height    7},
                                     "?" {:bands     ["██████╗ "
                                                      "╚════██╗"
                                                      "  ▄███╔╝"
                                                      "  ▀▀══╝ "
                                                      "  ██╗   "
                                                      "  ╚═╝   "
                                                      "        "],
                                          :i         31,
                                          :character "?",
                                          :width     8,
                                          :height    7},
                                     "@" {:bands     [" ██████╗ "
                                                      "██╔═══██╗"
                                                      "██║██╗██║"
                                                      "██║██║██║"
                                                      "╚█║████╔╝"
                                                      " ╚╝╚═══╝ "
                                                      "         "],
                                          :i         32,
                                          :character "@",
                                          :width     9,
                                          :height    7},
                                     "A" {:bands     [" █████╗ "
                                                      "██╔══██╗"
                                                      "███████║"
                                                      "██╔══██║"
                                                      "██║  ██║"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         33,
                                          :character "A",
                                          :width     8,
                                          :height    7},
                                     "B" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "╚═════╝ "
                                                      "        "],
                                          :i         34,
                                          :character "B",
                                          :width     8,
                                          :height    7},
                                     "C" {:bands     [" ██████╗"
                                                      "██╔════╝"
                                                      "██║     "
                                                      "██║     "
                                                      "╚██████╗"
                                                      " ╚═════╝"
                                                      "        "],
                                          :i         35,
                                          :character "C",
                                          :width     8,
                                          :height    7},
                                     "D" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██║  ██║"
                                                      "██║  ██║"
                                                      "██████╔╝"
                                                      "╚═════╝ "
                                                      "        "],
                                          :i         36,
                                          :character "D",
                                          :width     8,
                                          :height    7},
                                     "E" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "█████╗  "
                                                      "██╔══╝  "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         37,
                                          :character "E",
                                          :width     8,
                                          :height    7},
                                     "F" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "█████╗  "
                                                      "██╔══╝  "
                                                      "██║     "
                                                      "╚═╝     "
                                                      "        "],
                                          :i         38,
                                          :character "F",
                                          :width     8,
                                          :height    7},
                                     "G" {:bands     [" ██████╗ "
                                                      "██╔════╝ "
                                                      "██║  ███╗"
                                                      "██║   ██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         39,
                                          :character "G",
                                          :width     9,
                                          :height    7},
                                     "H" {:bands     ["██╗  ██╗"
                                                      "██║  ██║"
                                                      "███████║"
                                                      "██╔══██║"
                                                      "██║  ██║"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         40,
                                          :character "H",
                                          :width     8,
                                          :height    7},
                                     "I" {:bands     ["██╗" "██║" "██║" "██║" "██║" "╚═╝" "   "],
                                          :i         41,
                                          :character "I",
                                          :width     3,
                                          :height    7},
                                     "J" {:bands     ["     ██╗"
                                                      "     ██║"
                                                      "     ██║"
                                                      "██   ██║"
                                                      "╚█████╔╝"
                                                      " ╚════╝ "
                                                      "        "],
                                          :i         42,
                                          :character "J",
                                          :width     8,
                                          :height    7},
                                     "K" {:bands     ["██╗  ██╗"
                                                      "██║ ██╔╝"
                                                      "█████╔╝ "
                                                      "██╔═██╗ "
                                                      "██║  ██╗"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         43,
                                          :character "K",
                                          :width     8,
                                          :height    7},
                                     "L" {:bands     ["██╗     "
                                                      "██║     "
                                                      "██║     "
                                                      "██║     "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         44,
                                          :character "L",
                                          :width     8,
                                          :height    7},
                                     "M" {:bands     ["███╗   ███╗"
                                                      "████╗ ████║"
                                                      "██╔████╔██║"
                                                      "██║╚██╔╝██║"
                                                      "██║ ╚═╝ ██║"
                                                      "╚═╝     ╚═╝"
                                                      "           "],
                                          :i         45,
                                          :character "M",
                                          :width     11,
                                          :height    7},
                                     "N" {:bands     ["███╗   ██╗"
                                                      "████╗  ██║"
                                                      "██╔██╗ ██║"
                                                      "██║╚██╗██║"
                                                      "██║ ╚████║"
                                                      "╚═╝  ╚═══╝"
                                                      "          "],
                                          :i         46,
                                          :character "N",
                                          :width     10,
                                          :height    7},
                                     "O" {:bands     [" ██████╗ "
                                                      "██╔═══██╗"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         47,
                                          :character "O",
                                          :width     9,
                                          :height    7},
                                     "P" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "██╔═══╝ "
                                                      "██║     "
                                                      "╚═╝     "
                                                      "        "],
                                          :i         48,
                                          :character "P",
                                          :width     8,
                                          :height    7},
                                     "Q" {:bands     [" ██████╗ "
                                                      "██╔═══██╗"
                                                      "██║   ██║"
                                                      "██║▄▄ ██║"
                                                      "╚██████╔╝"
                                                      " ╚══▀▀═╝ "
                                                      "         "],
                                          :i         49,
                                          :character "Q",
                                          :width     9,
                                          :height    7},
                                     "R" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "██╔══██╗"
                                                      "██║  ██║"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         50,
                                          :character "R",
                                          :width     8,
                                          :height    7},
                                     "S" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "███████╗"
                                                      "╚════██║"
                                                      "███████║"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         51,
                                          :character "S",
                                          :width     8,
                                          :height    7},
                                     "T" {:bands     ["████████╗"
                                                      "╚══██╔══╝"
                                                      "   ██║   "
                                                      "   ██║   "
                                                      "   ██║   "
                                                      "   ╚═╝   "
                                                      "         "],
                                          :i         52,
                                          :character "T",
                                          :width     9,
                                          :height    7},
                                     "U" {:bands     ["██╗   ██╗"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         53,
                                          :character "U",
                                          :width     9,
                                          :height    7},
                                     "V" {:bands     ["██╗   ██╗"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "╚██╗ ██╔╝"
                                                      " ╚████╔╝ "
                                                      "  ╚═══╝  "
                                                      "         "],
                                          :i         54,
                                          :character "V",
                                          :width     9,
                                          :height    7},
                                     "W" {:bands     ["██╗    ██╗"
                                                      "██║    ██║"
                                                      "██║ █╗ ██║"
                                                      "██║███╗██║"
                                                      "╚███╔███╔╝"
                                                      " ╚══╝╚══╝ "
                                                      "          "],
                                          :i         55,
                                          :character "W",
                                          :width     10,
                                          :height    7},
                                     "X" {:bands     ["██╗  ██╗"
                                                      "╚██╗██╔╝"
                                                      " ╚███╔╝ "
                                                      " ██╔██╗ "
                                                      "██╔╝ ██╗"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         56,
                                          :character "X",
                                          :width     8,
                                          :height    7},
                                     "Y" {:bands     ["██╗   ██╗"
                                                      "╚██╗ ██╔╝"
                                                      " ╚████╔╝ "
                                                      "  ╚██╔╝  "
                                                      "   ██║   "
                                                      "   ╚═╝   "
                                                      "         "],
                                          :i         57,
                                          :character "Y",
                                          :width     9,
                                          :height    7},
                                     "Z" {:bands     ["███████╗"
                                                      "╚══███╔╝"
                                                      "  ███╔╝ "
                                                      " ███╔╝  "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         58,
                                          :character "Z",
                                          :width     8,
                                          :height    7},
                                     "[" {:bands     ["███╗" "██╔╝" "██║ " "██║ " "███╗" "╚══╝" "    "],
                                          :i         59,
                                          :character "[",
                                          :width     4,
                                          :height    7},
                                     "\\" {:bands     ["" "" "" "" "" ""],
                                           :i         60,
                                           :character "\\",
                                           :width     0,
                                           :height    6},
                                     "]" {:bands     ["███╗" "╚██║" " ██║" " ██║" "███║" "╚══╝" "    "],
                                          :i         61,
                                          :character "]",
                                          :width     4,
                                          :height    7},
                                     "^" {:bands     [" ███╗ " "██╔██╗" "╚═╝╚═╝" "      " "      " "      " "      "],
                                          :i         62,
                                          :character "^",
                                          :width     6,
                                          :height    7},
                                     "_" {:bands     ["        "
                                                      "        "
                                                      "        "
                                                      "        "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         63,
                                          :character "_",
                                          :width     8,
                                          :height    7},
                                     "`" {:bands     ["" "" "" "" "" ""],
                                          :i         64,
                                          :character "`",
                                          :width     0,
                                          :height    6},
                                     "a" {:bands     [" █████╗ "
                                                      "██╔══██╗"
                                                      "███████║"
                                                      "██╔══██║"
                                                      "██║  ██║"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         65,
                                          :character "a",
                                          :width     8,
                                          :height    7},
                                     "b" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "╚═════╝ "
                                                      "        "],
                                          :i         66,
                                          :character "b",
                                          :width     8,
                                          :height    7},
                                     "c" {:bands     [" ██████╗"
                                                      "██╔════╝"
                                                      "██║     "
                                                      "██║     "
                                                      "╚██████╗"
                                                      " ╚═════╝"
                                                      "        "],
                                          :i         67,
                                          :character "c",
                                          :width     8,
                                          :height    7},
                                     "d" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██║  ██║"
                                                      "██║  ██║"
                                                      "██████╔╝"
                                                      "╚═════╝ "
                                                      "        "],
                                          :i         68,
                                          :character "d",
                                          :width     8,
                                          :height    7},
                                     "e" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "█████╗  "
                                                      "██╔══╝  "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         69,
                                          :character "e",
                                          :width     8,
                                          :height    7},
                                     "f" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "█████╗  "
                                                      "██╔══╝  "
                                                      "██║     "
                                                      "╚═╝     "
                                                      "        "],
                                          :i         70,
                                          :character "f",
                                          :width     8,
                                          :height    7},
                                     "g" {:bands     [" ██████╗ "
                                                      "██╔════╝ "
                                                      "██║  ███╗"
                                                      "██║   ██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         71,
                                          :character "g",
                                          :width     9,
                                          :height    7},
                                     "h" {:bands     ["██╗  ██╗"
                                                      "██║  ██║"
                                                      "███████║"
                                                      "██╔══██║"
                                                      "██║  ██║"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         72,
                                          :character "h",
                                          :width     8,
                                          :height    7},
                                     "i" {:bands     ["██╗" "██║" "██║" "██║" "██║" "╚═╝" "   "],
                                          :i         73,
                                          :character "i",
                                          :width     3,
                                          :height    7},
                                     "j" {:bands     ["     ██╗"
                                                      "     ██║"
                                                      "     ██║"
                                                      "██   ██║"
                                                      "╚█████╔╝"
                                                      " ╚════╝ "
                                                      "        "],
                                          :i         74,
                                          :character "j",
                                          :width     8,
                                          :height    7},
                                     "k" {:bands     ["██╗  ██╗"
                                                      "██║ ██╔╝"
                                                      "█████╔╝ "
                                                      "██╔═██╗ "
                                                      "██║  ██╗"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         75,
                                          :character "k",
                                          :width     8,
                                          :height    7},
                                     "l" {:bands     ["██╗     "
                                                      "██║     "
                                                      "██║     "
                                                      "██║     "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         76,
                                          :character "l",
                                          :width     8,
                                          :height    7},
                                     "m" {:bands     ["███╗   ███╗"
                                                      "████╗ ████║"
                                                      "██╔████╔██║"
                                                      "██║╚██╔╝██║"
                                                      "██║ ╚═╝ ██║"
                                                      "╚═╝     ╚═╝"
                                                      "           "],
                                          :i         77,
                                          :character "m",
                                          :width     11,
                                          :height    7},
                                     "n" {:bands     ["███╗   ██╗"
                                                      "████╗  ██║"
                                                      "██╔██╗ ██║"
                                                      "██║╚██╗██║"
                                                      "██║ ╚████║"
                                                      "╚═╝  ╚═══╝"
                                                      "          "],
                                          :i         78,
                                          :character "n",
                                          :width     10,
                                          :height    7},
                                     "o" {:bands     [" ██████╗ "
                                                      "██╔═══██╗"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         79,
                                          :character "o",
                                          :width     9,
                                          :height    7},
                                     "p" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "██╔═══╝ "
                                                      "██║     "
                                                      "╚═╝     "
                                                      "        "],
                                          :i         80,
                                          :character "p",
                                          :width     8,
                                          :height    7},
                                     "q" {:bands     [" ██████╗ "
                                                      "██╔═══██╗"
                                                      "██║   ██║"
                                                      "██║▄▄ ██║"
                                                      "╚██████╔╝"
                                                      " ╚══▀▀═╝ "
                                                      "         "],
                                          :i         81,
                                          :character "q",
                                          :width     9,
                                          :height    7},
                                     "r" {:bands     ["██████╗ "
                                                      "██╔══██╗"
                                                      "██████╔╝"
                                                      "██╔══██╗"
                                                      "██║  ██║"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         82,
                                          :character "r",
                                          :width     8,
                                          :height    7},
                                     "s" {:bands     ["███████╗"
                                                      "██╔════╝"
                                                      "███████╗"
                                                      "╚════██║"
                                                      "███████║"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         83,
                                          :character "s",
                                          :width     8,
                                          :height    7},
                                     "t" {:bands     ["████████╗"
                                                      "╚══██╔══╝"
                                                      "   ██║   "
                                                      "   ██║   "
                                                      "   ██║   "
                                                      "   ╚═╝   "
                                                      "         "],
                                          :i         84,
                                          :character "t",
                                          :width     9,
                                          :height    7},
                                     "u" {:bands     ["██╗   ██╗"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "╚██████╔╝"
                                                      " ╚═════╝ "
                                                      "         "],
                                          :i         85,
                                          :character "u",
                                          :width     9,
                                          :height    7},
                                     "v" {:bands     ["██╗   ██╗"
                                                      "██║   ██║"
                                                      "██║   ██║"
                                                      "╚██╗ ██╔╝"
                                                      " ╚████╔╝ "
                                                      "  ╚═══╝  "
                                                      "         "],
                                          :i         86,
                                          :character "v",
                                          :width     9,
                                          :height    7},
                                     "w" {:bands     ["██╗    ██╗"
                                                      "██║    ██║"
                                                      "██║ █╗ ██║"
                                                      "██║███╗██║"
                                                      "╚███╔███╔╝"
                                                      " ╚══╝╚══╝ "
                                                      "          "],
                                          :i         87,
                                          :character "w",
                                          :width     10,
                                          :height    7},
                                     "x" {:bands     ["██╗  ██╗"
                                                      "╚██╗██╔╝"
                                                      " ╚███╔╝ "
                                                      " ██╔██╗ "
                                                      "██╔╝ ██╗"
                                                      "╚═╝  ╚═╝"
                                                      "        "],
                                          :i         88,
                                          :character "x",
                                          :width     8,
                                          :height    7},
                                     "y" {:bands     ["██╗   ██╗"
                                                      "╚██╗ ██╔╝"
                                                      " ╚████╔╝ "
                                                      "  ╚██╔╝  "
                                                      "   ██║   "
                                                      "   ╚═╝   "
                                                      "         "],
                                          :i         89,
                                          :character "y",
                                          :width     9,
                                          :height    7},
                                     "z" {:bands     ["███████╗"
                                                      "╚══███╔╝"
                                                      "  ███╔╝ "
                                                      " ███╔╝  "
                                                      "███████╗"
                                                      "╚══════╝"
                                                      "        "],
                                          :i         90,
                                          :character "z",
                                          :width     8,
                                          :height    7}},
                   :font-name       "ANSI Shadow",
                   :widest-char     "M",
                   :max-char-width  11,
                   :char-height     7},
   "Big Money" {:font-name       "Big Money",
                :widest-char     "@",
                :max-char-width  16,
                :char-height     11,
                :chars-array-map {" " {:bands     ["    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         0,
                                       :character " ",
                                       :width     4,
                                       :height    11},
                                  "!" {:bands     ["$$\\ "
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "\\__|"
                                                   "    "
                                                   "$$\\ "
                                                   "\\__|"
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         1,
                                       :character "!",
                                       :width     4,
                                       :height    11},
                                  "\"" {:bands     ["$$\\ $$\\ "
                                                    "$$ |$$ |"
                                                    "$$ |$$ |"
                                                    "\\__|\\__|"
                                                    "        "
                                                    "        "
                                                    "        "
                                                    "        "
                                                    "        "
                                                    "        "
                                                    "        "],
                                        :i         2,
                                        :character "\"",
                                        :width     8,
                                        :height    11},
                                  "#" {:bands     ["  $$\\ $$\\   "
                                                   "  $$ \\$$ \\  "
                                                   "$$$$$$$$$$\\ "
                                                   "\\_$$  $$   |"
                                                   "$$$$$$$$$$\\ "
                                                   "\\_$$  $$  _|"
                                                   "  $$ |$$ |  "
                                                   "  \\__|\\__|  "
                                                   "            "
                                                   "            "
                                                   "            "],
                                       :i         3,
                                       :character "#",
                                       :width     12,
                                       :height    11},
                                  "$" {:bands     ["   $$\\    "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  \\__|"
                                                   "\\$$$$$$\\  "
                                                   " \\___ $$\\ "
                                                   "$$\\  \\$$ |"
                                                   "\\$$$$$$  |"
                                                   " \\_$$  _/ "
                                                   "   \\ _/   "
                                                   "          "],
                                       :i         4,
                                       :character "$",
                                       :width     10,
                                       :height    11},
                                  "%" {:bands     ["$$\\   $$\\ "
                                                   "\\__| $$  |"
                                                   "    $$  / "
                                                   "   $$  /  "
                                                   "  $$  /   "
                                                   " $$  /    "
                                                   "$$  / $$\\ "
                                                   "\\__/  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         5,
                                       :character "%",
                                       :width     10,
                                       :height    11},
                                  "&" {:bands     [" $$$\\     "
                                                   "$$ $$\\    "
                                                   "\\$$$\\ |   "
                                                   "$$\\$$\\$$\\ "
                                                   "$$ \\$$ __|"
                                                   "$$ |\\$$\\  "
                                                   " $$$$ $$\\ "
                                                   " \\____\\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         6,
                                       :character "&",
                                       :width     10,
                                       :height    11},
                                  "'" {:bands     ["$$\\ "
                                                   "$  |"
                                                   "\\_/ "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         7,
                                       :character "'",
                                       :width     4,
                                       :height    11},
                                  "(" {:bands     ["  $$$\\ "
                                                   " $$  _|"
                                                   "$$  /  "
                                                   "$$ |   "
                                                   "$$ |   "
                                                   "\\$$\\   "
                                                   " \\$$$\\ "
                                                   "  \\___|"
                                                   "       "
                                                   "       "
                                                   "       "],
                                       :i         8,
                                       :character "(",
                                       :width     7,
                                       :height    11},
                                  ")" {:bands     ["$$$\\   "
                                                   " \\$$\\  "
                                                   "  \\$$\\ "
                                                   "   $$ |"
                                                   "   $$ |"
                                                   "  $$  |"
                                                   "$$$  / "
                                                   "\\___/  "
                                                   "       "
                                                   "       "
                                                   "       "],
                                       :i         9,
                                       :character ")",
                                       :width     7,
                                       :height    11},
                                  "*" {:bands     ["         "
                                                   " $$\\$$\\  "
                                                   " \\$$$  | "
                                                   "$$$$$$$\\ "
                                                   "\\_$$$ __|"
                                                   " $$ $$\\  "
                                                   " \\__\\__| "
                                                   "         "
                                                   "         "
                                                   "         "
                                                   "         "],
                                       :i         10,
                                       :character "*",
                                       :width     9,
                                       :height    11},
                                  "+" {:bands     ["          "
                                                   "   $$\\    "
                                                   "   $$ |   "
                                                   "$$$$$$$$\\ "
                                                   "\\__$$  __|"
                                                   "   $$ |   "
                                                   "   \\__|   "
                                                   "          "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         11,
                                       :character "+",
                                       :width     10,
                                       :height    11},
                                  "," {:bands     ["    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "$$\\ "
                                                   "$  |"
                                                   "\\_/ "
                                                   "    "
                                                   "    "],
                                       :i         12,
                                       :character ",",
                                       :width     4,
                                       :height    11},
                                  "-" {:bands     ["        "
                                                   "        "
                                                   "        "
                                                   "$$$$$$\\ "
                                                   "\\______|"
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "        "],
                                       :i         13,
                                       :character "-",
                                       :width     8,
                                       :height    11},
                                  "." {:bands     ["    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "$$\\ "
                                                   "\\__|"
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         14,
                                       :character ".",
                                       :width     4,
                                       :height    11},
                                  "/" {:bands     ["      $$\\ "
                                                   "     $$  |"
                                                   "    $$  / "
                                                   "   $$  /  "
                                                   "  $$  /   "
                                                   " $$  /    "
                                                   "$$  /     "
                                                   "\\__/      "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         15,
                                       :character "/",
                                       :width     10,
                                       :height    11},
                                  "0" {:bands     [" $$$$$$\\  "
                                                   "$$$ __$$\\ "
                                                   "$$$$\\ $$ |"
                                                   "$$\\$$\\$$ |"
                                                   "$$ \\$$$$ |"
                                                   "$$ |\\$$$ |"
                                                   "\\$$$$$$  /"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         16,
                                       :character "0",
                                       :width     10,
                                       :height    11},
                                  "1" {:bands     ["  $$\\   "
                                                   "$$$$ |  "
                                                   "\\_$$ |  "
                                                   "  $$ |  "
                                                   "  $$ |  "
                                                   "  $$ |  "
                                                   "$$$$$$\\ "
                                                   "\\______|"
                                                   "        "
                                                   "        "
                                                   "        "],
                                       :i         17,
                                       :character "1",
                                       :width     8,
                                       :height    11},
                                  "2" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "\\__/  $$ |"
                                                   " $$$$$$  |"
                                                   "$$  ____/ "
                                                   "$$ |      "
                                                   "$$$$$$$$\\ "
                                                   "\\________|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         18,
                                       :character "2",
                                       :width     10,
                                       :height    11},
                                  "3" {:bands     [" $$$$$$\\  "
                                                   "$$ ___$$\\ "
                                                   "\\_/   $$ |"
                                                   "  $$$$$ / "
                                                   "  \\___$$\\ "
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         19,
                                       :character "3",
                                       :width     10,
                                       :height    11},
                                  "4" {:bands     ["$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$$$$$$$ |"
                                                   "\\_____$$ |"
                                                   "      $$ |"
                                                   "      $$ |"
                                                   "      \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         20,
                                       :character "4",
                                       :width     10,
                                       :height    11},
                                  "5" {:bands     ["$$$$$$$\\  "
                                                   "$$  ____| "
                                                   "$$ |      "
                                                   "$$$$$$$\\  "
                                                   "\\_____$$\\ "
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         21,
                                       :character "5",
                                       :width     10,
                                       :height    11},
                                  "6" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  \\__|"
                                                   "$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   " $$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         22,
                                       :character "6",
                                       :width     10,
                                       :height    11},
                                  "7" {:bands     ["$$$$$$$$\\ "
                                                   "\\____$$  |"
                                                   "    $$  / "
                                                   "   $$  /  "
                                                   "  $$  /   "
                                                   " $$  /    "
                                                   "$$  /     "
                                                   "\\__/      "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         23,
                                       :character "7",
                                       :width     10,
                                       :height    11},
                                  "8" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   " $$$$$$  |"
                                                   "$$  __$$< "
                                                   "$$ /  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         24,
                                       :character "8",
                                       :width     10,
                                       :height    11},
                                  "9" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\____$$ |"
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         25,
                                       :character "9",
                                       :width     10,
                                       :height    11},
                                  ":" {:bands     ["    "
                                                   "    "
                                                   "$$\\ "
                                                   "\\__|"
                                                   "    "
                                                   "$$\\ "
                                                   "\\__|"
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         26,
                                       :character ":",
                                       :width     4,
                                       :height    11},
                                  ";" {:bands     ["    "
                                                   "    "
                                                   "$$\\ "
                                                   "\\__|"
                                                   "    "
                                                   "$$\\ "
                                                   "$  |"
                                                   "\\_/ "
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         27,
                                       :character ";",
                                       :width     4,
                                       :height    11},
                                  "<" {:bands     ["   $$\\ "
                                                   "  $$  |"
                                                   " $$  / "
                                                   "$$  /  "
                                                   "\\$$<   "
                                                   " \\$$\\  "
                                                   "  \\$$\\ "
                                                   "   \\__|"
                                                   "       "
                                                   "       "
                                                   "       "],
                                       :i         28,
                                       :character "<",
                                       :width     7,
                                       :height    11},
                                  "=" {:bands     ["      "
                                                   "      "
                                                   "$$$$\\ "
                                                   "\\____|"
                                                   "$$$$\\ "
                                                   "\\____|"
                                                   "      "
                                                   "      "
                                                   "      "
                                                   "      "
                                                   "      "],
                                       :i         29,
                                       :character "=",
                                       :width     6,
                                       :height    11},
                                  ">" {:bands     ["$$\\    "
                                                   "\\$$\\   "
                                                   " \\$$\\  "
                                                   "  \\$$\\ "
                                                   "  $$  |"
                                                   " $$  / "
                                                   "$$  /  "
                                                   "\\__/   "
                                                   "       "
                                                   "       "
                                                   "       "],
                                       :i         30,
                                       :character ">",
                                       :width     7,
                                       :height    11},
                                  "?" {:bands     [" $$$$\\  "
                                                   "$$  $$\\ "
                                                   "\\__/$$ |"
                                                   "   $$  |"
                                                   "  $$  / "
                                                   "  \\__/  "
                                                   "  $$\\   "
                                                   "  \\__|  "
                                                   "        "
                                                   "        "
                                                   "        "],
                                       :i         31,
                                       :character "?",
                                       :width     8,
                                       :height    11},
                                  "@" {:bands     ["    $$$$$$\\     "
                                                   "  $$$ ___$$$\\   "
                                                   " $$ _/   \\_$$\\  "
                                                   "$$ / $$$$$\\ $$\\ "
                                                   "$$ |$$  $$ |$$ |"
                                                   "$$ |$$ /$$ |$$ |"
                                                   "$$ |\\$$$$$$$$  |"
                                                   "\\$$\\ \\________/ "
                                                   " \\$$$\\   $$$\\   "
                                                   "  \\_$$$$$$  _|  "
                                                   "    \\______/    "],
                                       :i         32,
                                       :character "@",
                                       :width     16,
                                       :height    11},
                                  "A" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$$$$$$$ |"
                                                   "$$  __$$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         33,
                                       :character "A",
                                       :width     10,
                                       :height    11},
                                  "B" {:bands     ["$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$$$$$$\\ |"
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$$$$$$  |"
                                                   "\\_______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         34,
                                       :character "B",
                                       :width     10,
                                       :height    11},
                                  "C" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  \\__|"
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "$$ |  $$\\ "
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         35,
                                       :character "C",
                                       :width     10,
                                       :height    11},
                                  "D" {:bands     ["$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$$$$$$  |"
                                                   "\\_______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         36,
                                       :character "D",
                                       :width     10,
                                       :height    11},
                                  "E" {:bands     ["$$$$$$$$\\ "
                                                   "$$  _____|"
                                                   "$$ |      "
                                                   "$$$$$\\    "
                                                   "$$  __|   "
                                                   "$$ |      "
                                                   "$$$$$$$$\\ "
                                                   "\\________|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         37,
                                       :character "E",
                                       :width     10,
                                       :height    11},
                                  "F" {:bands     ["$$$$$$$$\\ "
                                                   "$$  _____|"
                                                   "$$ |      "
                                                   "$$$$$\\    "
                                                   "$$  __|   "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "\\__|      "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         38,
                                       :character "F",
                                       :width     10,
                                       :height    11},
                                  "G" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  \\__|"
                                                   "$$ |$$$$\\ "
                                                   "$$ |\\_$$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         39,
                                       :character "G",
                                       :width     10,
                                       :height    11},
                                  "H" {:bands     ["$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$$$$$$$ |"
                                                   "$$  __$$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         40,
                                       :character "H",
                                       :width     10,
                                       :height    11},
                                  "I" {:bands     ["$$$$$$\\ "
                                                   "\\_$$  _|"
                                                   "  $$ |  "
                                                   "  $$ |  "
                                                   "  $$ |  "
                                                   "  $$ |  "
                                                   "$$$$$$\\ "
                                                   "\\______|"
                                                   "        "
                                                   "        "
                                                   "        "],
                                       :i         41,
                                       :character "I",
                                       :width     8,
                                       :height    11},
                                  "J" {:bands     ["   $$$$$\\ "
                                                   "   \\__$$ |"
                                                   "      $$ |"
                                                   "      $$ |"
                                                   "$$\\   $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         42,
                                       :character "J",
                                       :width     10,
                                       :height    11},
                                  "K" {:bands     ["$$\\   $$\\ "
                                                   "$$ | $$  |"
                                                   "$$ |$$  / "
                                                   "$$$$$  /  "
                                                   "$$  $$<   "
                                                   "$$ |\\$$\\  "
                                                   "$$ | \\$$\\ "
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         43,
                                       :character "K",
                                       :width     10,
                                       :height    11},
                                  "L" {:bands     ["$$\\       "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "$$$$$$$$\\ "
                                                   "\\________|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         44,
                                       :character "L",
                                       :width     10,
                                       :height    11},
                                  "M" {:bands     ["$$\\      $$\\ "
                                                   "$$$\\    $$$ |"
                                                   "$$$$\\  $$$$ |"
                                                   "$$\\$$\\$$ $$ |"
                                                   "$$ \\$$$  $$ |"
                                                   "$$ |\\$  /$$ |"
                                                   "$$ | \\_/ $$ |"
                                                   "\\__|     \\__|"
                                                   "             "
                                                   "             "
                                                   "             "],
                                       :i         45,
                                       :character "M",
                                       :width     13,
                                       :height    11},
                                  "N" {:bands     ["$$\\   $$\\ "
                                                   "$$$\\  $$ |"
                                                   "$$$$\\ $$ |"
                                                   "$$ $$\\$$ |"
                                                   "$$ \\$$$$ |"
                                                   "$$ |\\$$$ |"
                                                   "$$ | \\$$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         46,
                                       :character "N",
                                       :width     10,
                                       :height    11},
                                  "O" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   " $$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         47,
                                       :character "O",
                                       :width     10,
                                       :height    11},
                                  "P" {:bands     ["$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$$$$$$  |"
                                                   "$$  ____/ "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "\\__|      "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         48,
                                       :character "P",
                                       :width     10,
                                       :height    11},
                                  "Q" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ $$\\$$ |"
                                                   "\\$$$$$$ / "
                                                   " \\___$$$\\ "
                                                   "     \\___|"
                                                   "          "
                                                   "          "],
                                       :i         49,
                                       :character "Q",
                                       :width     10,
                                       :height    11},
                                  "R" {:bands     ["$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$$$$$$  |"
                                                   "$$  __$$< "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         50,
                                       :character "R",
                                       :width     10,
                                       :height    11},
                                  "S" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  \\__|"
                                                   "\\$$$$$$\\  "
                                                   " \\____$$\\ "
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         51,
                                       :character "S",
                                       :width     10,
                                       :height    11},
                                  "T" {:bands     ["$$$$$$$$\\ "
                                                   "\\__$$  __|"
                                                   "   $$ |   "
                                                   "   $$ |   "
                                                   "   $$ |   "
                                                   "   $$ |   "
                                                   "   $$ |   "
                                                   "   \\__|   "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         52,
                                       :character "T",
                                       :width     10,
                                       :height    11},
                                  "U" {:bands     ["$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         53,
                                       :character "U",
                                       :width     10,
                                       :height    11},
                                  "V" {:bands     ["$$\\    $$\\ "
                                                   "$$ |   $$ |"
                                                   "$$ |   $$ |"
                                                   "\\$$\\  $$  |"
                                                   " \\$$\\$$  / "
                                                   "  \\$$$  /  "
                                                   "   \\$  /   "
                                                   "    \\_/    "
                                                   "           "
                                                   "           "
                                                   "           "],
                                       :i         54,
                                       :character "V",
                                       :width     11,
                                       :height    11},
                                  "W" {:bands     ["$$\\      $$\\ "
                                                   "$$ | $\\  $$ |"
                                                   "$$ |$$$\\ $$ |"
                                                   "$$ $$ $$\\$$ |"
                                                   "$$$$  _$$$$ |"
                                                   "$$$  / \\$$$ |"
                                                   "$$  /   \\$$ |"
                                                   "\\__/     \\__|"
                                                   "             "
                                                   "             "
                                                   "             "],
                                       :i         55,
                                       :character "W",
                                       :width     13,
                                       :height    11},
                                  "X" {:bands     ["$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "\\$$\\ $$  |"
                                                   " \\$$$$  / "
                                                   " $$  $$<  "
                                                   "$$  /\\$$\\ "
                                                   "$$ /  $$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         56,
                                       :character "X",
                                       :width     10,
                                       :height    11},
                                  "Y" {:bands     ["$$\\     $$\\ "
                                                   "\\$$\\   $$  |"
                                                   " \\$$\\ $$  / "
                                                   "  \\$$$$  /  "
                                                   "   \\$$  /   "
                                                   "    $$ |    "
                                                   "    $$ |    "
                                                   "    \\__|    "
                                                   "            "
                                                   "            "
                                                   "            "],
                                       :i         57,
                                       :character "Y",
                                       :width     12,
                                       :height    11},
                                  "Z" {:bands     ["$$$$$$$$\\ "
                                                   "\\____$$  |"
                                                   "    $$  / "
                                                   "   $$  /  "
                                                   "  $$  /   "
                                                   " $$  /    "
                                                   "$$$$$$$$\\ "
                                                   "\\________|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         58,
                                       :character "Z",
                                       :width     10,
                                       :height    11},
                                  "[" {:bands     ["$$$$\\ "
                                                   "$$  _|"
                                                   "$$ |  "
                                                   "$$ |  "
                                                   "$$ |  "
                                                   "$$ |  "
                                                   "$$$$\\ "
                                                   "\\____|"
                                                   "      "
                                                   "      "
                                                   "      "],
                                       :i         59,
                                       :character "[",
                                       :width     6,
                                       :height    11},
                                  "\\" {:bands     ["$$\\       "
                                                    "\\$$\\      "
                                                    " \\$$\\     "
                                                    "  \\$$\\    "
                                                    "   \\$$\\   "
                                                    "    \\$$\\  "
                                                    "     \\$$\\ "
                                                    "      \\__|"
                                                    "          "
                                                    "          "
                                                    "          "],
                                        :i         60,
                                        :character "\\",
                                        :width     10,
                                        :height    11},
                                  "]" {:bands     ["$$$$\\ "
                                                   "\\_$$ |"
                                                   "  $$ |"
                                                   "  $$ |"
                                                   "  $$ |"
                                                   "  $$ |"
                                                   "$$$$ |"
                                                   "\\____|"
                                                   "      "
                                                   "      "
                                                   "      "],
                                       :i         61,
                                       :character "]",
                                       :width     6,
                                       :height    11},
                                  "^" {:bands     ["   $\\    "
                                                   "  $$$\\   "
                                                   " $$ $$\\  "
                                                   "$$  \\$$\\ "
                                                   "\\__/ \\__|"
                                                   "         "
                                                   "         "
                                                   "         "
                                                   "         "
                                                   "         "
                                                   "         "],
                                       :i         62,
                                       :character "^",
                                       :width     9,
                                       :height    11},
                                  "_" {:bands     ["        "
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "        "
                                                   "$$$$$$\\ "
                                                   "\\______|"
                                                   "        "
                                                   "        "],
                                       :i         63,
                                       :character "_",
                                       :width     8,
                                       :height    11},
                                  "`" {:bands     ["$$\\ "
                                                   "\\$ |"
                                                   " \\_|"
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         64,
                                       :character "`",
                                       :width     4,
                                       :height    11},
                                  "a" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   " \\____$$\\ "
                                                   " $$$$$$$ |"
                                                   "$$  __$$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\_______|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         65,
                                       :character "a",
                                       :width     10,
                                       :height    11},
                                  "b" {:bands     ["$$\\       "
                                                   "$$ |      "
                                                   "$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$$$$$$  |"
                                                   "\\_______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         66,
                                       :character "b",
                                       :width     10,
                                       :height    11},
                                  "c" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$$\\ "
                                                   "$$  _____|"
                                                   "$$ /      "
                                                   "$$ |      "
                                                   "\\$$$$$$$\\ "
                                                   " \\_______|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         67,
                                       :character "c",
                                       :width     10,
                                       :height    11},
                                  "d" {:bands     ["      $$\\ "
                                                   "      $$ |"
                                                   " $$$$$$$ |"
                                                   "$$  __$$ |"
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\_______|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         68,
                                       :character "d",
                                       :width     10,
                                       :height    11},
                                  "e" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$$$$$$$ |"
                                                   "$$   ____|"
                                                   "\\$$$$$$$\\ "
                                                   " \\_______|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         69,
                                       :character "e",
                                       :width     10,
                                       :height    11},
                                  "f" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  \\__|"
                                                   "$$$$\\     "
                                                   "$$  _|    "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "\\__|      "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         70,
                                       :character "f",
                                       :width     10,
                                       :height    11},
                                  "g" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\____$$ |"
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "],
                                       :i         71,
                                       :character "g",
                                       :width     10,
                                       :height    11},
                                  "h" {:bands     ["$$\\       "
                                                   "$$ |      "
                                                   "$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         72,
                                       :character "h",
                                       :width     10,
                                       :height    11},
                                  "i" {:bands     ["$$\\ "
                                                   "\\__|"
                                                   "$$\\ "
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "\\__|"
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         73,
                                       :character "i",
                                       :width     4,
                                       :height    11},
                                  "j" {:bands     ["          "
                                                   "          "
                                                   "      $$\\ "
                                                   "      \\__|"
                                                   "      $$\\ "
                                                   "      $$ |"
                                                   "      $$ |"
                                                   "      $$ |"
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "],
                                       :i         74,
                                       :character "j",
                                       :width     10,
                                       :height    11},
                                  "k" {:bands     ["$$\\       "
                                                   "$$ |      "
                                                   "$$ |  $$\\ "
                                                   "$$ | $$  |"
                                                   "$$$$$$  / "
                                                   "$$  _$$<  "
                                                   "$$ | \\$$\\ "
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         75,
                                       :character "k",
                                       :width     10,
                                       :height    11},
                                  "l" {:bands     ["$$\\ "
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "$$ |"
                                                   "\\__|"
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         76,
                                       :character "l",
                                       :width     4,
                                       :height    11},
                                  "m" {:bands     ["              "
                                                   "              "
                                                   "$$$$$$\\$$$$\\  "
                                                   "$$  _$$  _$$\\ "
                                                   "$$ / $$ / $$ |"
                                                   "$$ | $$ | $$ |"
                                                   "$$ | $$ | $$ |"
                                                   "\\__| \\__| \\__|"
                                                   "              "
                                                   "              "
                                                   "              "],
                                       :i         77,
                                       :character "m",
                                       :width     14,
                                       :height    11},
                                  "n" {:bands     ["          "
                                                   "          "
                                                   "$$$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\__|  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         78,
                                       :character "n",
                                       :width     10,
                                       :height    11},
                                  "o" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         79,
                                       :character "o",
                                       :width     10,
                                       :height    11},
                                  "p" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$$$$$$  |"
                                                   "$$  ____/ "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "\\__|      "],
                                       :i         80,
                                       :character "p",
                                       :width     10,
                                       :height    11},
                                  "q" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\____$$ |"
                                                   "      $$ |"
                                                   "      $$ |"
                                                   "      \\__|"],
                                       :i         81,
                                       :character "q",
                                       :width     10,
                                       :height    11},
                                  "r" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  \\__|"
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "\\__|      "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         82,
                                       :character "r",
                                       :width     10,
                                       :height    11},
                                  "s" {:bands     ["          "
                                                   "          "
                                                   " $$$$$$$\\ "
                                                   "$$  _____|"
                                                   "\\$$$$$$\\  "
                                                   " \\____$$\\ "
                                                   "$$$$$$$  |"
                                                   "\\_______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         83,
                                       :character "s",
                                       :width     10,
                                       :height    11},
                                  "t" {:bands     ["  $$\\     "
                                                   "  $$ |    "
                                                   "$$$$$$\\   "
                                                   "\\_$$  _|  "
                                                   "  $$ |    "
                                                   "  $$ |$$\\ "
                                                   "  \\$$$$  |"
                                                   "   \\____/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         84,
                                       :character "t",
                                       :width     10,
                                       :height    11},
                                  "u" {:bands     ["          "
                                                   "          "
                                                   "$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         85,
                                       :character "u",
                                       :width     10,
                                       :height    11},
                                  "v" {:bands     ["           "
                                                   "           "
                                                   "$$\\    $$\\ "
                                                   "\\$$\\  $$  |"
                                                   " \\$$\\$$  / "
                                                   "  \\$$$  /  "
                                                   "   \\$  /   "
                                                   "    \\_/    "
                                                   "           "
                                                   "           "
                                                   "           "],
                                       :i         86,
                                       :character "v",
                                       :width     11,
                                       :height    11},
                                  "w" {:bands     ["              "
                                                   "              "
                                                   "$$\\  $$\\  $$\\ "
                                                   "$$ | $$ | $$ |"
                                                   "$$ | $$ | $$ |"
                                                   "$$ | $$ | $$ |"
                                                   "\\$$$$$\\$$$$  |"
                                                   " \\_____\\____/ "
                                                   "              "
                                                   "              "
                                                   "              "],
                                       :i         87,
                                       :character "w",
                                       :width     14,
                                       :height    11},
                                  "x" {:bands     ["          "
                                                   "          "
                                                   "$$\\   $$\\ "
                                                   "\\$$\\ $$  |"
                                                   " \\$$$$  / "
                                                   " $$  $$<  "
                                                   "$$  /\\$$\\ "
                                                   "\\__/  \\__|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         88,
                                       :character "x",
                                       :width     10,
                                       :height    11},
                                  "y" {:bands     ["          "
                                                   "          "
                                                   "$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\____$$ |"
                                                   "$$\\   $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "],
                                       :i         89,
                                       :character "y",
                                       :width     10,
                                       :height    11},
                                  "z" {:bands     ["          "
                                                   "          "
                                                   "$$$$$$$$\\ "
                                                   "\\____$$  |"
                                                   "  $$$$ _/ "
                                                   " $$  _/   "
                                                   "$$$$$$$$\\ "
                                                   "\\________|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         90,
                                       :character "z",
                                       :width     10,
                                       :height    11},
                                  "{" {:bands     ["  $$$\\ "
                                                   " $$  _|"
                                                   " $$ |  "
                                                   "$$$ |  "
                                                   "\\$$ |  "
                                                   " $$ |  "
                                                   " \\$$$\\ "
                                                   "  \\___|"
                                                   "       "
                                                   "       "
                                                   "       "],
                                       :i         91,
                                       :character "{",
                                       :width     7,
                                       :height    11},
                                  "|" {:bands     ["$$\\ "
                                                   "$$ |"
                                                   "$$ |"
                                                   "\\__|"
                                                   "$$\\ "
                                                   "$$ |"
                                                   "$$ |"
                                                   "\\__|"
                                                   "    "
                                                   "    "
                                                   "    "],
                                       :i         92,
                                       :character "|",
                                       :width     4,
                                       :height    11},
                                  "}" {:bands     ["$$$\\   "
                                                   "\\_$$\\  "
                                                   "  $$ | "
                                                   "  $$$\\ "
                                                   "  $$  |"
                                                   "  $$ / "
                                                   "$$$  | "
                                                   "\\___/  "
                                                   "       "
                                                   "       "
                                                   "       "],
                                       :i         93,
                                       :character "}",
                                       :width     7,
                                       :height    11},
                                  "~" {:bands     [" $$$\\ $$\\ "
                                                   "$$ $$\\$$ |"
                                                   "$$ \\$$$  |"
                                                   "\\__|\\___/ "
                                                   "          "
                                                   "          "
                                                   "          "
                                                   "          "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         94,
                                       :character "~",
                                       :width     10,
                                       :height    11},
                                  "Ä" {:bands     ["  $\\ $\\   "
                                                   "  \\_|\\_|  "
                                                   " $$$$$$\\  "
                                                   " \\____$$\\ "
                                                   " $$$$$$$ |"
                                                   "$$  __$$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\_______|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         95,
                                       :character "Ä",
                                       :width     10,
                                       :height    11},
                                  "Ö" {:bands     ["  $\\ $\\   "
                                                   "  \\_|\\_|  "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         96,
                                       :character "Ö",
                                       :width     10,
                                       :height    11},
                                  "Ü" {:bands     ["  $\\ $\\   "
                                                   "  \\_|\\_|  "
                                                   "$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         97,
                                       :character "Ü",
                                       :width     10,
                                       :height    11},
                                  "ä" {:bands     ["  $\\ $\\   "
                                                   "  \\_|\\_|  "
                                                   " $$$$$$\\  "
                                                   " \\____$$\\ "
                                                   " $$$$$$$ |"
                                                   "$$  __$$ |"
                                                   "\\$$$$$$$ |"
                                                   " \\_______|"
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         98,
                                       :character "ä",
                                       :width     10,
                                       :height    11},
                                  "ö" {:bands     ["  $\\ $\\   "
                                                   "  \\_|\\_|  "
                                                   " $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ /  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         99,
                                       :character "ö",
                                       :width     10,
                                       :height    11},
                                  "ü" {:bands     ["  $\\ $\\   "
                                                   "  \\_|\\_|  "
                                                   "$$\\   $$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "$$ |  $$ |"
                                                   "\\$$$$$$  |"
                                                   " \\______/ "
                                                   "          "
                                                   "          "
                                                   "          "],
                                       :i         100,
                                       :character "ü",
                                       :width     10,
                                       :height    11},
                                  "ß" {:bands     [" $$$$$$\\  "
                                                   "$$  __$$\\ "
                                                   "$$ |  $$ |"
                                                   "$$ $$$$  |"
                                                   "$$ \\__$$< "
                                                   "$$ |  $$ |"
                                                   "$$ $$$$  |"
                                                   "$$ \\____/ "
                                                   "$$ |      "
                                                   "$$ |      "
                                                   "\\__|      "],
                                       :i         101,
                                       :character "ß",
                                       :width     10,
                                       :height    11}}}
   "Miniwi"      :hi
   "Drippy"      :hi
   "Big"         :hi
   "Isometric 1" :hi
   "Rounded"     :hi})

;; 32-126
(def ascii-chars
  [
   " " 
   "!" 
   "\""
   "#" 
   "$" 
   "%" 
   "&" 
   "'" 
   "(" 
   ")" 
   "*" 
   "+" 
   "," 
   "-" 
   "." 
   "/" 
   "0" 
   "1" 
   "2" 
   "3" 
   "4" 
   "5" 
   "6" 
   "7" 
   "8" 
   "9" 
   ":" 
   ";" 
   "<" 
   "=" 
   ">" 
   "?" 
   "@"             
   "A"             
   "B"             
   "C"             
   "D"             
   "E"             
   "F"             
   "G"             
   "H"             
   "I"             
   "J"             
   "K"             
   "L"             
   "M"             
   "N"             
   "O"             
   "P"             
   "Q"             
   "R"             
   "S"             
   "T"             
   "U"             
   "V"             
   "W"             
   "X"             
   "Y"             
   "Z"             
   "["             
   "\\"             
   "]"             
   "^"             
   "_"
   "`"
   "a"
   "b"
   "c"
   "d"
   "e"
   "f"
   "g"
   "h"
   "i"
   "j"
   "k"
   "l"
   "m"
   "n"
   "o"
   "p"
   "q"
   "r"
   "s"
   "t"
   "u"
   "v"
   "w"
   "x"
   "y"
   "z"
   "{"
   "|"
   "}"
   "~"
   "Ä"
   "Ö"
   "Ü"
   "ä"
   "ö"
   "ü"
   "ß"
   ])

(def ascii-chars-by-index-map
  (reduce 
   (fn [acc i]
     (assoc acc i (nth ascii-chars i :not-found)))
   {}
   (-> ascii-chars count range)))

(def ascii-indices-by-chars
  (clojure.set/map-invert ascii-chars-by-index-map))


(defn figlet-font-multiple-character-heights-warning!
  [font-name m]
  (let [m+                    m #_(update-in m ["o" :height] + 2)
        height-frequencies    (some->> m+
                                       (mapv (fn [[_ v]] (:height v)))
                                       frequencies) ]
    (when (< 1 (count height-frequencies))
       (let [least-frequent-height (some->> height-frequencies
                                            (sort-by val <)
                                            ffirst)
             most-frequent-height (some->> height-frequencies
                                           (sort-by val >)
                                           ffirst)
             chars (keep (fn [[_ {:keys [height character]}]]
                           (when (not= height most-frequent-height)
                             character)) 
                         m+)
             ]
         (println 
          (str "WARNING!\n"
               "Figlet font may contain chars of varying heights:\n\n"
               "font-name:\n"
               "\"" font-name  "\""
               "\n\n"
               "height frequencies:"
               "\n"
               (with-out-str (println height-frequencies))
               "\n"
               "chars found with lesser frequent heights:\n"
               "\"" (string/join "\n" chars) "\""
               "\n"))
         (doseq [char chars]
           (pprint (get m char)))))))

(defn space-width [coll widths]
  (let [pos-widths (filter pos? widths)]
    (Math/round 
     (Math/ceil 
      (double 
       (/ (/ (apply + pos-widths)
             (count pos-widths))
          2))))))

(defn space-char [coll widths max-height]
  (let [space-width (space-width coll widths)]
    {:bands     (->> (repeat space-width " ")
                     string/join
                     (repeat max-height)
                     vec)
     :i         0
     :character (bling.fonts/ascii-chars-by-index-map 0)
     :width     space-width
     :height    max-height}))

(defn- char-bands-coll-inner [i s]
  (when (pos? i)
    (let [[_ & bands]       
          (string/split-lines s)

          [!-char :as bands]
          (mapv #(string/replace % #"@$" "") bands)

          width              
          (count !-char)]
      (merge {:bands     bands
              :i         i
              :character (bling.fonts/ascii-chars-by-index-map i)
              :width     width
              :height    (count bands)}
             (when (every? (fn [s] (= "" s)) bands)  
                   {:missing? true})))))

(defn- chars-array-map [coll]
  (apply array-map
         (reduce (fn [acc m]
                   (conj acc (:character m) m))
                 []
                 coll)))

(defn font-metrics [coll]
  (reduce 
   (fn [{:keys [widest-char widest-char-width widths max-height]
         :as   acc}
        {:keys [width height character]}]
     (assoc acc
            :widths 
            (conj widths width)

            :widest-char-width
            (if (> width widest-char-width)
              width
              widest-char-width)

            :widest-char
            (if (> width widest-char-width)
              character
              widest-char)
            
            :max-height
            (if (> height max-height)
              height
              max-height)))
   {:widths            []
    :widest-char-width 0
    :max-height        0
    :widest-char       ""}
   coll))


(defn- missing-char-str [width s]
  (case width
    0 ""
    1 s
    2 (str " " s)
    3 (str " " s " ")
    (if (odd? width)
      (let [pad (sjr (/ (dec width) 2) " ")]
        (str pad s pad))
      (let [pad (sjr (/ width 2) " ")]
        (str pad s (subs pad 1))))))

(defn- replacement-char-vec 
  [{:keys [width height]}
   {s :character}]
  (into []
        (let [s        (missing-char-str width s)
              band-str (sjr width " ")]
          (if (odd? height)
            (concat (repeat (dec (/ (dec height) 2)) band-str)
                    [s]
                    (repeat (inc (/ (dec height) 2)) band-str))
            (concat (repeat (dec (/ height 2)) band-str)
                    [s]
                    (repeat (/ height 2) band-str))))))

(defn replacement-char-fn [space-char %]
  (if (:missing? %)
    (let [vc (replacement-char-vec space-char %)]
      (assoc %
             :bands
             vc
             :height
             (count vc)))
    %))

(defn banner-font-array-map [font-name]
  (when-let [char-strings-coll
             (some-> (get bling.fontlib/raw-figlet-font-strings-by-name
                                       font-name)
                                  (string/split #"@@"))]
   (let [char-bands-coll
         (keep-indexed char-bands-coll-inner char-strings-coll)

         {:keys [widest-char widths max-height]}
         (font-metrics char-bands-coll)

         space-char
         (space-char char-bands-coll widths max-height)

         char-bands-coll
         (mapv (partial replacement-char-fn space-char) char-bands-coll)

         char-bands-coll
         (cons space-char char-bands-coll)
         
         chars-array-map 
         (chars-array-map char-bands-coll)]

     
     (figlet-font-multiple-character-heights-warning! 
      font-name
      chars-array-map)
     {:chars-array-map chars-array-map
      :font-name       font-name
      :widest-char     widest-char
      :max-char-width  (apply max widths)
      :char-height     max-height})))


(def fonts-ordered
  ["Miniwi"
   "ANSI Shadow"
   "Drippy"
   "Big"
   "Big Money"
   "Rounded"
   "Isometric 1"])
