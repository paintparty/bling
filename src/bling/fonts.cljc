(ns bling.fonts)



;;  ▖  ▖▘  ▘   ▘
;;  ▛▖▞▌▌▛▌▌▌▌▌▌
;;  ▌▝ ▌▌▌▌▌▚▚▘▌
;;  
;;     ▘  ▘   ▘
;;  ▛▛▌▌▛▌▌▌▌▌▌
;;  ▌▌▌▌▌▌▌▚▚▘▌

(def miniwi
'{:font-name "Miniwi",
 :example
 ["▖  ▖▘  ▘   ▘"
  "▛▖▞▌▌▛▌▌▌▌▌▌"
  "▌▝ ▌▌▌▌▌▚▚▘▌"
  ""
  "   ▘  ▘   ▘"
  "▛▛▌▌▛▌▌▌▌▌▌"
  "▌▌▌▌▌▌▌▚▚▘▌"],
 :desc "miniwi font\n        after the miniwi bitmap font",
 :url "http://github.com/sshbio/miniwi",
 :example-text "Miniwi",
 :font-sym miniwi,
 :widest-char "M",
 :char-height 4,
 :max-char-width 4,
 :missing-chars ["Ä" "Ö" "Ü" "ä" "ö" "ü" "ß"],
 :chars-array-map
 {" "
  {:bands ["  " "  " "  " "  "],
   :i 0,
   :character " ",
   :width 2,
   :height 4},
  "!"
  {:bands ["▌" "▌" "▖" " "], :i 1, :character "!", :width 1, :height 4},
  "\""
  {:bands ["▌▌" "  " "  " "  "],
   :i 2,
   :character "\"",
   :width 2,
   :height 4},
  "#"
  {:bands ["▗▗ " "▜▜▘" "▜▜▘" "   "],
   :i 3,
   :character "#",
   :width 3,
   :height 4},
  "$"
  {:bands ["▗ " "█▘" "▟▌" "▝ "],
   :i 4,
   :character "$",
   :width 2,
   :height 4},
  "%"
  {:bands ["▖▖" "▗▘" "▌▖" "  "],
   :i 5,
   :character "%",
   :width 2,
   :height 4},
  "&"
  {:bands ["▗ " "▚▘" "▚▌" "  "],
   :i 6,
   :character "&",
   :width 2,
   :height 4},
  "'"
  {:bands ["▌" " " " " " "], :i 7, :character "'", :width 1, :height 4},
  "("
  {:bands ["▗▘" "▐ " "▐ " "▝▖"],
   :i 8,
   :character "(",
   :width 2,
   :height 4},
  ")"
  {:bands ["▝▖" " ▌" " ▌" "▗▘"],
   :i 9,
   :character ")",
   :width 2,
   :height 4},
  "*"
  {:bands ["▖▖" "▟▖" "▞▖" "  "],
   :i 10,
   :character "*",
   :width 2,
   :height 4},
  "+"
  {:bands ["  " "▟▖" "▝ " "  "],
   :i 11,
   :character "+",
   :width 2,
   :height 4},
  ","
  {:bands ["  " "  " "▗ " "▘ "],
   :i 12,
   :character ",",
   :width 2,
   :height 4},
  "-"
  {:bands ["  " "▄▖" "  " "  "],
   :i 13,
   :character "-",
   :width 2,
   :height 4},
  "."
  {:bands ["  " "  " "▗ " "  "],
   :i 14,
   :character ".",
   :width 2,
   :height 4},
  "/"
  {:bands [" ▌" "▐ " "▞ " "▘ "],
   :i 15,
   :character "/",
   :width 2,
   :height 4},
  "0"
  {:bands ["▄▖" "▛▌" "█▌" "  "],
   :i 16,
   :character "0",
   :width 2,
   :height 4},
  "1"
  {:bands ["▗ " "▜ " "▟▖" "  "],
   :i 17,
   :character "1",
   :width 2,
   :height 4},
  "2"
  {:bands ["▄▖" "▄▌" "▙▖" "  "],
   :i 18,
   :character "2",
   :width 2,
   :height 4},
  "3"
  {:bands ["▄▖" "▄▌" "▄▌" "  "],
   :i 19,
   :character "3",
   :width 2,
   :height 4},
  "4"
  {:bands ["▖▖" "▙▌" " ▌" "  "],
   :i 20,
   :character "4",
   :width 2,
   :height 4},
  "5"
  {:bands ["▄▖" "▙▖" "▄▌" "  "],
   :i 21,
   :character "5",
   :width 2,
   :height 4},
  "6"
  {:bands ["▄▖" "▙▖" "▙▌" "  "],
   :i 22,
   :character "6",
   :width 2,
   :height 4},
  "7"
  {:bands ["▄▖" " ▌" " ▌" "  "],
   :i 23,
   :character "7",
   :width 2,
   :height 4},
  "8"
  {:bands ["▄▖" "▙▌" "▙▌" "  "],
   :i 24,
   :character "8",
   :width 2,
   :height 4},
  "9"
  {:bands ["▄▖" "▙▌" "▄▌" "  "],
   :i 25,
   :character "9",
   :width 2,
   :height 4},
  ":"
  {:bands [" " "▖" "▖" " "],
   :i 26,
   :character ":",
   :width 1,
   :height 4},
  ";"
  {:bands [" " "▖" "▖" "▘"],
   :i 27,
   :character ";",
   :width 1,
   :height 4},
  "<"
  {:bands [" ▖" "▞ " "▝▖" "  "],
   :i 28,
   :character "<",
   :width 2,
   :height 4},
  "="
  {:bands ["  " "▀▘" "▀▘" "  "],
   :i 29,
   :character "=",
   :width 2,
   :height 4},
  ">"
  {:bands ["▖ " "▝▖" "▞ " "  "],
   :i 30,
   :character ">",
   :width 2,
   :height 4},
  "?"
  {:bands ["▄▖" "▗▘" "▗ " "  "],
   :i 31,
   :character "?",
   :width 2,
   :height 4},
  "@"
  {:bands ["▗▄▖" "▌▄▐" "▌▀▀" " ▀ "],
   :i 32,
   :character "@",
   :width 3,
   :height 4},
  "A"
  {:bands ["▄▖" "▌▌" "▛▌" "  "],
   :i 33,
   :character "A",
   :width 2,
   :height 4},
  "B"
  {:bands ["▄ " "▙▘" "▙▘" "  "],
   :i 34,
   :character "B",
   :width 2,
   :height 4},
  "C"
  {:bands ["▄▖" "▌ " "▙▖" "  "],
   :i 35,
   :character "C",
   :width 2,
   :height 4},
  "D"
  {:bands ["▄ " "▌▌" "▙▘" "  "],
   :i 36,
   :character "D",
   :width 2,
   :height 4},
  "E"
  {:bands ["▄▖" "▙▖" "▙▖" "  "],
   :i 37,
   :character "E",
   :width 2,
   :height 4},
  "F"
  {:bands ["▄▖" "▙▖" "▌ " "  "],
   :i 38,
   :character "F",
   :width 2,
   :height 4},
  "G"
  {:bands ["▄▖" "▌ " "▙▌" "  "],
   :i 39,
   :character "G",
   :width 2,
   :height 4},
  "H"
  {:bands ["▖▖" "▙▌" "▌▌" "  "],
   :i 40,
   :character "H",
   :width 2,
   :height 4},
  "I"
  {:bands ["▄▖" "▐ " "▟▖" "  "],
   :i 41,
   :character "I",
   :width 2,
   :height 4},
  "J"
  {:bands [" ▖" " ▌" "▙▌" "  "],
   :i 42,
   :character "J",
   :width 2,
   :height 4},
  "K"
  {:bands ["▖▖" "▙▘" "▌▌" "  "],
   :i 43,
   :character "K",
   :width 2,
   :height 4},
  "L"
  {:bands ["▖ " "▌ " "▙▖" "  "],
   :i 44,
   :character "L",
   :width 2,
   :height 4},
  "M"
  {:bands ["▖  ▖" "▛▖▞▌" "▌▝ ▌" "    "],
   :i 45,
   :character "M",
   :width 4,
   :height 4},
  "N"
  {:bands ["▖ ▖" "▛▖▌" "▌▝▌" "   "],
   :i 46,
   :character "N",
   :width 3,
   :height 4},
  "O"
  {:bands ["▄▖" "▌▌" "▙▌" "  "],
   :i 47,
   :character "O",
   :width 2,
   :height 4},
  "P"
  {:bands ["▄▖" "▙▌" "▌ " "  "],
   :i 48,
   :character "P",
   :width 2,
   :height 4},
  "Q"
  {:bands ["▄▖" "▌▌" "█▌" " ▘"],
   :i 49,
   :character "Q",
   :width 2,
   :height 4},
  "R"
  {:bands ["▄▖" "▙▘" "▌▌" "  "],
   :i 50,
   :character "R",
   :width 2,
   :height 4},
  "S"
  {:bands ["▄▖" "▚ " "▄▌" "  "],
   :i 51,
   :character "S",
   :width 2,
   :height 4},
  "T"
  {:bands ["▄▖" "▐ " "▐ " "  "],
   :i 52,
   :character "T",
   :width 2,
   :height 4},
  "U"
  {:bands ["▖▖" "▌▌" "▙▌" "  "],
   :i 53,
   :character "U",
   :width 2,
   :height 4},
  "V"
  {:bands ["▖▖" "▌▌" "▚▘" "  "],
   :i 54,
   :character "V",
   :width 2,
   :height 4},
  "W"
  {:bands ["▖  ▖" "▌▞▖▌" "▛ ▝▌" "    "],
   :i 55,
   :character "W",
   :width 4,
   :height 4},
  "X"
  {:bands ["▖▖" "▚▘" "▌▌" "  "],
   :i 56,
   :character "X",
   :width 2,
   :height 4},
  "Y"
  {:bands ["▖▖" "▌▌" "▐ " "  "],
   :i 57,
   :character "Y",
   :width 2,
   :height 4},
  "Z"
  {:bands ["▄▖" "▗▘" "▙▖" "  "],
   :i 58,
   :character "Z",
   :width 2,
   :height 4},
  "["
  {:bands ["▐▘" "▐ " "▐ " "▝▘"],
   :i 59,
   :character "[",
   :width 2,
   :height 4},
  "\\"
  {:bands ["▌ " "▐ " "▝▖" " ▘"],
   :i 60,
   :character "\\",
   :width 2,
   :height 4},
  "]"
  {:bands ["▜ " "▐ " "▐ " "▀ "],
   :i 61,
   :character "]",
   :width 2,
   :height 4},
  "^"
  {:bands ["▗ " "▘▘" "  " "  "],
   :i 62,
   :character "^",
   :width 2,
   :height 4},
  "_"
  {:bands ["  " "  " "▄▖" "  "],
   :i 63,
   :character "_",
   :width 2,
   :height 4},
  "`"
  {:bands ["▚ " " ▘" "  " "  "],
   :i 64,
   :character "`",
   :width 2,
   :height 4},
  "a"
  {:bands ["  " "▀▌" "█▌" "  "],
   :i 65,
   :character "a",
   :width 2,
   :height 4},
  "b"
  {:bands ["▌ " "▛▌" "▙▌" "  "],
   :i 66,
   :character "b",
   :width 2,
   :height 4},
  "c"
  {:bands ["  " "▛▘" "▙▖" "  "],
   :i 67,
   :character "c",
   :width 2,
   :height 4},
  "d"
  {:bands [" ▌" "▛▌" "▙▌" "  "],
   :i 68,
   :character "d",
   :width 2,
   :height 4},
  "e"
  {:bands ["  " "█▌" "▙▖" "  "],
   :i 69,
   :character "e",
   :width 2,
   :height 4},
  "f"
  {:bands ["▐▘" "▜▘" "▐ " "  "],
   :i 70,
   :character "f",
   :width 2,
   :height 4},
  "g"
  {:bands ["  " "▛▌" "▙▌" "▄▌"],
   :i 71,
   :character "g",
   :width 2,
   :height 4},
  "h"
  {:bands ["▌ " "▛▌" "▌▌" "  "],
   :i 72,
   :character "h",
   :width 2,
   :height 4},
  "i"
  {:bands ["▘" "▌" "▌" " "],
   :i 73,
   :character "i",
   :width 1,
   :height 4},
  "j"
  {:bands [" ▘" " ▌" " ▌" "▙▌"],
   :i 74,
   :character "j",
   :width 2,
   :height 4},
  "k"
  {:bands ["▌ " "▙▘" "▛▖" "  "],
   :i 75,
   :character "k",
   :width 2,
   :height 4},
  "l"
  {:bands ["▜ " "▐ " "▐▖" "  "],
   :i 76,
   :character "l",
   :width 2,
   :height 4},
  "m"
  {:bands ["   " "▛▛▌" "▌▌▌" "   "],
   :i 77,
   :character "m",
   :width 3,
   :height 4},
  "n"
  {:bands ["  " "▛▌" "▌▌" "  "],
   :i 78,
   :character "n",
   :width 2,
   :height 4},
  "o"
  {:bands ["  " "▛▌" "▙▌" "  "],
   :i 79,
   :character "o",
   :width 2,
   :height 4},
  "p"
  {:bands ["  " "▛▌" "▙▌" "▌ "],
   :i 80,
   :character "p",
   :width 2,
   :height 4},
  "q"
  {:bands ["  " "▛▌" "▙▌" " ▌"],
   :i 81,
   :character "q",
   :width 2,
   :height 4},
  "r"
  {:bands ["  " "▛▘" "▌ " "  "],
   :i 82,
   :character "r",
   :width 2,
   :height 4},
  "s"
  {:bands ["  " "▛▘" "▄▌" "  "],
   :i 83,
   :character "s",
   :width 2,
   :height 4},
  "t"
  {:bands ["▗ " "▜▘" "▐▖" "  "],
   :i 84,
   :character "t",
   :width 2,
   :height 4},
  "u"
  {:bands ["  " "▌▌" "▙▌" "  "],
   :i 85,
   :character "u",
   :width 2,
   :height 4},
  "v"
  {:bands ["  " "▌▌" "▚▘" "  "],
   :i 86,
   :character "v",
   :width 2,
   :height 4},
  "w"
  {:bands ["   " "▌▌▌" "▚▚▘" "   "],
   :i 87,
   :character "w",
   :width 3,
   :height 4},
  "x"
  {:bands ["  " "▚▘" "▞▖" "  "],
   :i 88,
   :character "x",
   :width 2,
   :height 4},
  "y"
  {:bands ["  " "▌▌" "▙▌" "▄▌"],
   :i 89,
   :character "y",
   :width 2,
   :height 4},
  "z"
  {:bands ["  " "▀▌" "▙▖" "  "],
   :i 90,
   :character "z",
   :width 2,
   :height 4},
  "{"
  {:bands ["▐▘" "▐ " "▐ " "▐▖"],
   :i 91,
   :character "{",
   :width 2,
   :height 4},
  "|"
  {:bands ["▌" "▌" "▌" "▘"],
   :i 92,
   :character "|",
   :width 1,
   :height 4},
  "}"
  {:bands ["▜ " "▐▖" "▐ " "▀ "],
   :i 93,
   :character "}",
   :width 2,
   :height 4},
  "~"
  {:bands ["   " "▖▄ " "▝▘▘" "   "],
   :i 94,
   :character "~",
   :width 3,
   :height 4}}})


;;   █████╗ ███╗   ██╗███████╗██╗    ███████╗██╗  ██╗ █████╗ ██████╗  ██████╗ ██╗    ██╗
;;  ██╔══██╗████╗  ██║██╔════╝██║    ██╔════╝██║  ██║██╔══██╗██╔══██╗██╔═══██╗██║    ██║
;;  ███████║██╔██╗ ██║███████╗██║    ███████╗███████║███████║██║  ██║██║   ██║██║ █╗ ██║
;;  ██╔══██║██║╚██╗██║╚════██║██║    ╚════██║██╔══██║██╔══██║██║  ██║██║   ██║██║███╗██║
;;  ██║  ██║██║ ╚████║███████║██║    ███████║██║  ██║██║  ██║██████╔╝╚██████╔╝╚███╔███╔╝
;;  ╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝    ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝  ╚══╝╚══╝ 

(def ansi-shadow
'{:font-name "ANSI Shadow",
 :example
 [" █████╗ ███╗   ██╗███████╗██╗    ███████╗██╗  ██╗ █████╗ ██████╗  ██████╗ ██╗    ██╗"
  "██╔══██╗████╗  ██║██╔════╝██║    ██╔════╝██║  ██║██╔══██╗██╔══██╗██╔═══██╗██║    ██║"
  "███████║██╔██╗ ██║███████╗██║    ███████╗███████║███████║██║  ██║██║   ██║██║ █╗ ██║"
  "██╔══██║██║╚██╗██║╚════██║██║    ╚════██║██╔══██║██╔══██║██║  ██║██║   ██║██║███╗██║"
  "██║  ██║██║ ╚████║███████║██║    ███████║██║  ██║██║  ██║██████╔╝╚██████╔╝╚███╔███╔╝"
  "╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝    ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝  ╚══╝╚══╝ "],
 :desc "",
 :more-info
 "https://web.archive.org/web/20120819044459/http://www.roysac.com/thedrawfonts-tdf.asp",
 :url "",
 :example-text "ANSI",
 :font-sym ansi-shadow,
 :widest-char "M",
 :char-height 7,
 :max-char-width 11,
 :missing-chars ["{" "|" "}" "~" "Ä" "Ö" "Ü" "ä" "ö" "ü" "ß"],
 :chars-array-map
 {" "
  {:bands ["    " "    " "    " "    " "    " "    " "    "],
   :i 0,
   :character " ",
   :width 4,
   :height 7},
  "!"
  {:bands ["██╗" "██║" "██║" "╚═╝" "██╗" "╚═╝" "   "],
   :i 1,
   :character "!",
   :width 3,
   :height 7},
  "\""
  {:bands
   ["██╗██╗" "╚═╝╚═╝" "      " "      " "      " "      " "      "],
   :i 2,
   :character "\"",
   :width 6,
   :height 7},
  "#"
  {:bands
   [" ██╗ ██╗ "
    "████████╗"
    "╚██╔═██╔╝"
    "████████╗"
    "╚██╔═██╔╝"
    " ╚═╝ ╚═╝ "
    "         "],
   :i 3,
   :character "#",
   :width 9,
   :height 7},
  "$"
  {:bands
   ["▄▄███▄▄·"
    "██╔════╝"
    "███████╗"
    "╚════██║"
    "███████║"
    "╚═▀▀▀══╝"
    "        "],
   :i 4,
   :character "$",
   :width 8,
   :height 7},
  "%"
  {:bands
   ["██╗ ██╗"
    "╚═╝██╔╝"
    "  ██╔╝ "
    " ██╔╝  "
    "██╔╝██╗"
    "╚═╝ ╚═╝"
    "       "],
   :i 5,
   :character "%",
   :width 7,
   :height 7},
  "&"
  {:bands
   ["   ██╗   "
    "   ██║   "
    "████████╗"
    "██╔═██╔═╝"
    "██████║  "
    "╚═════╝  "
    "         "],
   :i 6,
   :character "&",
   :width 9,
   :height 7},
  "'"
  {:bands ["██╗" "╚═╝" "   " "   " "   " "   " "   "],
   :i 7,
   :character "'",
   :width 3,
   :height 7},
  "("
  {:bands [" ██╗" "██╔╝" "██║ " "██║ " "╚██╗" " ╚═╝" "    "],
   :i 8,
   :character "(",
   :width 4,
   :height 7},
  ")"
  {:bands ["██╗ " "╚██╗" " ██║" " ██║" "██╔╝" "╚═╝ " "    "],
   :i 9,
   :character ")",
   :width 4,
   :height 7},
  "*"
  {:bands
   ["      " "▄ ██╗▄" " ████╗" "▀╚██╔▀" "  ╚═╝ " "      " "       "],
   :i 10,
   :character "*",
   :width 6,
   :height 7},
  "+"
  {:bands
   ["       "
    "  ██╗  "
    "██████╗"
    "╚═██╔═╝"
    "  ╚═╝  "
    "       "
    "       "],
   :i 11,
   :character "+",
   :width 7,
   :height 7},
  ","
  {:bands ["   " "   " "   " "   " "▄█╗" "╚═╝" "   "],
   :i 12,
   :character ",",
   :width 3,
   :height 7},
  "-"
  {:bands
   ["      " "      " "█████╗" "╚════╝" "      " "      " "      "],
   :i 13,
   :character "-",
   :width 6,
   :height 7},
  "."
  {:bands ["   " "   " "   " "   " "██╗" "╚═╝" "   "],
   :i 14,
   :character ".",
   :width 3,
   :height 7},
  "/"
  {:bands
   ["    ██╗"
    "   ██╔╝"
    "  ██╔╝ "
    " ██╔╝  "
    "██╔╝   "
    "╚═╝    "
    "       "],
   :i 15,
   :character "/",
   :width 7,
   :height 7},
  "0"
  {:bands
   [" ██████╗ "
    "██╔═████╗"
    "██║██╔██║"
    "████╔╝██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 16,
   :character "0",
   :width 9,
   :height 7},
  "1"
  {:bands [" ██╗" "███║" "╚██║" " ██║" " ██║" " ╚═╝" "    "],
   :i 17,
   :character "1",
   :width 4,
   :height 7},
  "2"
  {:bands
   ["██████╗ "
    "╚════██╗"
    " █████╔╝"
    "██╔═══╝ "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 18,
   :character "2",
   :width 8,
   :height 7},
  "3"
  {:bands
   ["██████╗ "
    "╚════██╗"
    " █████╔╝"
    " ╚═══██╗"
    "██████╔╝"
    "╚═════╝ "
    "        "],
   :i 19,
   :character "3",
   :width 8,
   :height 7},
  "4"
  {:bands
   ["██╗  ██╗"
    "██║  ██║"
    "███████║"
    "╚════██║"
    "     ██║"
    "     ╚═╝"
    "        "],
   :i 20,
   :character "4",
   :width 8,
   :height 7},
  "5"
  {:bands
   ["███████╗"
    "██╔════╝"
    "███████╗"
    "╚════██║"
    "███████║"
    "╚══════╝"
    "        "],
   :i 21,
   :character "5",
   :width 8,
   :height 7},
  "6"
  {:bands
   [" ██████╗ "
    "██╔════╝ "
    "███████╗ "
    "██╔═══██╗"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 22,
   :character "6",
   :width 9,
   :height 7},
  "7"
  {:bands
   ["███████╗"
    "╚════██║"
    "    ██╔╝"
    "   ██╔╝ "
    "   ██║  "
    "   ╚═╝  "
    "        "],
   :i 23,
   :character "7",
   :width 8,
   :height 7},
  "8"
  {:bands
   [" █████╗ "
    "██╔══██╗"
    "╚█████╔╝"
    "██╔══██╗"
    "╚█████╔╝"
    " ╚════╝ "
    "        "],
   :i 24,
   :character "8",
   :width 8,
   :height 7},
  "9"
  {:bands
   [" █████╗ "
    "██╔══██╗"
    "╚██████║"
    " ╚═══██║"
    " █████╔╝"
    " ╚════╝ "
    "        "],
   :i 25,
   :character "9",
   :width 8,
   :height 7},
  ":"
  {:bands ["   " "██╗" "╚═╝" "██╗" "╚═╝" "   " "   "],
   :i 26,
   :character ":",
   :width 3,
   :height 7},
  ";"
  {:bands ["   " "██╗" "╚═╝" "▄█╗" "▀═╝" "   " "   "],
   :i 27,
   :character ";",
   :width 3,
   :height 7},
  "<"
  {:bands ["  ██╗" " ██╔╝" "██╔╝ " "╚██╗ " " ╚██╗" "  ╚═╝" "     "],
   :i 28,
   :character "<",
   :width 5,
   :height 7},
  "="
  {:bands
   ["       "
    "██████╗"
    "╚═════╝"
    "██████╗"
    "╚═════╝"
    "       "
    "       "],
   :i 29,
   :character "=",
   :width 7,
   :height 7},
  ">"
  {:bands ["██╗  " "╚██╗ " " ╚██╗" " ██╔╝" "██╔╝ " "╚═╝  " "     "],
   :i 30,
   :character ">",
   :width 5,
   :height 7},
  "?"
  {:bands
   ["██████╗ "
    "╚════██╗"
    "  ▄███╔╝"
    "  ▀▀══╝ "
    "  ██╗   "
    "  ╚═╝   "
    "        "],
   :i 31,
   :character "?",
   :width 8,
   :height 7},
  "@"
  {:bands
   [" ██████╗ "
    "██╔═══██╗"
    "██║██╗██║"
    "██║██║██║"
    "╚█║████╔╝"
    " ╚╝╚═══╝ "
    "         "],
   :i 32,
   :character "@",
   :width 9,
   :height 7},
  "A"
  {:bands
   [" █████╗ "
    "██╔══██╗"
    "███████║"
    "██╔══██║"
    "██║  ██║"
    "╚═╝  ╚═╝"
    "        "],
   :i 33,
   :character "A",
   :width 8,
   :height 7},
  "B"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██████╔╝"
    "██╔══██╗"
    "██████╔╝"
    "╚═════╝ "
    "        "],
   :i 34,
   :character "B",
   :width 8,
   :height 7},
  "C"
  {:bands
   [" ██████╗"
    "██╔════╝"
    "██║     "
    "██║     "
    "╚██████╗"
    " ╚═════╝"
    "        "],
   :i 35,
   :character "C",
   :width 8,
   :height 7},
  "D"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██║  ██║"
    "██║  ██║"
    "██████╔╝"
    "╚═════╝ "
    "        "],
   :i 36,
   :character "D",
   :width 8,
   :height 7},
  "E"
  {:bands
   ["███████╗"
    "██╔════╝"
    "█████╗  "
    "██╔══╝  "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 37,
   :character "E",
   :width 8,
   :height 7},
  "F"
  {:bands
   ["███████╗"
    "██╔════╝"
    "█████╗  "
    "██╔══╝  "
    "██║     "
    "╚═╝     "
    "        "],
   :i 38,
   :character "F",
   :width 8,
   :height 7},
  "G"
  {:bands
   [" ██████╗ "
    "██╔════╝ "
    "██║  ███╗"
    "██║   ██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 39,
   :character "G",
   :width 9,
   :height 7},
  "H"
  {:bands
   ["██╗  ██╗"
    "██║  ██║"
    "███████║"
    "██╔══██║"
    "██║  ██║"
    "╚═╝  ╚═╝"
    "        "],
   :i 40,
   :character "H",
   :width 8,
   :height 7},
  "I"
  {:bands ["██╗" "██║" "██║" "██║" "██║" "╚═╝" "   "],
   :i 41,
   :character "I",
   :width 3,
   :height 7},
  "J"
  {:bands
   ["     ██╗"
    "     ██║"
    "     ██║"
    "██   ██║"
    "╚█████╔╝"
    " ╚════╝ "
    "        "],
   :i 42,
   :character "J",
   :width 8,
   :height 7},
  "K"
  {:bands
   ["██╗  ██╗"
    "██║ ██╔╝"
    "█████╔╝ "
    "██╔═██╗ "
    "██║  ██╗"
    "╚═╝  ╚═╝"
    "        "],
   :i 43,
   :character "K",
   :width 8,
   :height 7},
  "L"
  {:bands
   ["██╗     "
    "██║     "
    "██║     "
    "██║     "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 44,
   :character "L",
   :width 8,
   :height 7},
  "M"
  {:bands
   ["███╗   ███╗"
    "████╗ ████║"
    "██╔████╔██║"
    "██║╚██╔╝██║"
    "██║ ╚═╝ ██║"
    "╚═╝     ╚═╝"
    "           "],
   :i 45,
   :character "M",
   :width 11,
   :height 7},
  "N"
  {:bands
   ["███╗   ██╗"
    "████╗  ██║"
    "██╔██╗ ██║"
    "██║╚██╗██║"
    "██║ ╚████║"
    "╚═╝  ╚═══╝"
    "          "],
   :i 46,
   :character "N",
   :width 10,
   :height 7},
  "O"
  {:bands
   [" ██████╗ "
    "██╔═══██╗"
    "██║   ██║"
    "██║   ██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 47,
   :character "O",
   :width 9,
   :height 7},
  "P"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██████╔╝"
    "██╔═══╝ "
    "██║     "
    "╚═╝     "
    "        "],
   :i 48,
   :character "P",
   :width 8,
   :height 7},
  "Q"
  {:bands
   [" ██████╗ "
    "██╔═══██╗"
    "██║   ██║"
    "██║▄▄ ██║"
    "╚██████╔╝"
    " ╚══▀▀═╝ "
    "         "],
   :i 49,
   :character "Q",
   :width 9,
   :height 7},
  "R"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██████╔╝"
    "██╔══██╗"
    "██║  ██║"
    "╚═╝  ╚═╝"
    "        "],
   :i 50,
   :character "R",
   :width 8,
   :height 7},
  "S"
  {:bands
   ["███████╗"
    "██╔════╝"
    "███████╗"
    "╚════██║"
    "███████║"
    "╚══════╝"
    "        "],
   :i 51,
   :character "S",
   :width 8,
   :height 7},
  "T"
  {:bands
   ["████████╗"
    "╚══██╔══╝"
    "   ██║   "
    "   ██║   "
    "   ██║   "
    "   ╚═╝   "
    "         "],
   :i 52,
   :character "T",
   :width 9,
   :height 7},
  "U"
  {:bands
   ["██╗   ██╗"
    "██║   ██║"
    "██║   ██║"
    "██║   ██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 53,
   :character "U",
   :width 9,
   :height 7},
  "V"
  {:bands
   ["██╗   ██╗"
    "██║   ██║"
    "██║   ██║"
    "╚██╗ ██╔╝"
    " ╚████╔╝ "
    "  ╚═══╝  "
    "         "],
   :i 54,
   :character "V",
   :width 9,
   :height 7},
  "W"
  {:bands
   ["██╗    ██╗"
    "██║    ██║"
    "██║ █╗ ██║"
    "██║███╗██║"
    "╚███╔███╔╝"
    " ╚══╝╚══╝ "
    "          "],
   :i 55,
   :character "W",
   :width 10,
   :height 7},
  "X"
  {:bands
   ["██╗  ██╗"
    "╚██╗██╔╝"
    " ╚███╔╝ "
    " ██╔██╗ "
    "██╔╝ ██╗"
    "╚═╝  ╚═╝"
    "        "],
   :i 56,
   :character "X",
   :width 8,
   :height 7},
  "Y"
  {:bands
   ["██╗   ██╗"
    "╚██╗ ██╔╝"
    " ╚████╔╝ "
    "  ╚██╔╝  "
    "   ██║   "
    "   ╚═╝   "
    "         "],
   :i 57,
   :character "Y",
   :width 9,
   :height 7},
  "Z"
  {:bands
   ["███████╗"
    "╚══███╔╝"
    "  ███╔╝ "
    " ███╔╝  "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 58,
   :character "Z",
   :width 8,
   :height 7},
  "["
  {:bands ["███╗" "██╔╝" "██║ " "██║ " "███╗" "╚══╝" "    "],
   :i 59,
   :character "[",
   :width 4,
   :height 7},
  "\\"
  {:bands
   ["██╗    "
    "╚██╗   "
    " ╚██╗  "
    "  ╚██╗ "
    "   ╚██╗"
    "    ╚═╝"
    "       "],
   :i 60,
   :character "\\",
   :width 7,
   :height 7},
  "]"
  {:bands ["███╗" "╚██║" " ██║" " ██║" "███║" "╚══╝" "    "],
   :i 61,
   :character "]",
   :width 4,
   :height 7},
  "^"
  {:bands
   [" ███╗ " "██╔██╗" "╚═╝╚═╝" "      " "      " "      " "      "],
   :i 62,
   :character "^",
   :width 6,
   :height 7},
  "_"
  {:bands
   ["        "
    "        "
    "        "
    "        "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 63,
   :character "_",
   :width 8,
   :height 7},
  "`"
  {:bands
   ["██╗     "
    "╚██╗    "
    " ╚═╝    "
    "        "
    "        "
    "        "
    "       "],
   :i 64,
   :character "`",
   :width 8,
   :height 7},
  "a"
  {:bands
   [" █████╗ "
    "██╔══██╗"
    "███████║"
    "██╔══██║"
    "██║  ██║"
    "╚═╝  ╚═╝"
    "        "],
   :i 65,
   :character "a",
   :width 8,
   :height 7},
  "b"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██████╔╝"
    "██╔══██╗"
    "██████╔╝"
    "╚═════╝ "
    "        "],
   :i 66,
   :character "b",
   :width 8,
   :height 7},
  "c"
  {:bands
   [" ██████╗"
    "██╔════╝"
    "██║     "
    "██║     "
    "╚██████╗"
    " ╚═════╝"
    "        "],
   :i 67,
   :character "c",
   :width 8,
   :height 7},
  "d"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██║  ██║"
    "██║  ██║"
    "██████╔╝"
    "╚═════╝ "
    "        "],
   :i 68,
   :character "d",
   :width 8,
   :height 7},
  "e"
  {:bands
   ["███████╗"
    "██╔════╝"
    "█████╗  "
    "██╔══╝  "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 69,
   :character "e",
   :width 8,
   :height 7},
  "f"
  {:bands
   ["███████╗"
    "██╔════╝"
    "█████╗  "
    "██╔══╝  "
    "██║     "
    "╚═╝     "
    "        "],
   :i 70,
   :character "f",
   :width 8,
   :height 7},
  "g"
  {:bands
   [" ██████╗ "
    "██╔════╝ "
    "██║  ███╗"
    "██║   ██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 71,
   :character "g",
   :width 9,
   :height 7},
  "h"
  {:bands
   ["██╗  ██╗"
    "██║  ██║"
    "███████║"
    "██╔══██║"
    "██║  ██║"
    "╚═╝  ╚═╝"
    "        "],
   :i 72,
   :character "h",
   :width 8,
   :height 7},
  "i"
  {:bands ["██╗" "██║" "██║" "██║" "██║" "╚═╝" "   "],
   :i 73,
   :character "i",
   :width 3,
   :height 7},
  "j"
  {:bands
   ["     ██╗"
    "     ██║"
    "     ██║"
    "██   ██║"
    "╚█████╔╝"
    " ╚════╝ "
    "        "],
   :i 74,
   :character "j",
   :width 8,
   :height 7},
  "k"
  {:bands
   ["██╗  ██╗"
    "██║ ██╔╝"
    "█████╔╝ "
    "██╔═██╗ "
    "██║  ██╗"
    "╚═╝  ╚═╝"
    "        "],
   :i 75,
   :character "k",
   :width 8,
   :height 7},
  "l"
  {:bands
   ["██╗     "
    "██║     "
    "██║     "
    "██║     "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 76,
   :character "l",
   :width 8,
   :height 7},
  "m"
  {:bands
   ["███╗   ███╗"
    "████╗ ████║"
    "██╔████╔██║"
    "██║╚██╔╝██║"
    "██║ ╚═╝ ██║"
    "╚═╝     ╚═╝"
    "           "],
   :i 77,
   :character "m",
   :width 11,
   :height 7},
  "n"
  {:bands
   ["███╗   ██╗"
    "████╗  ██║"
    "██╔██╗ ██║"
    "██║╚██╗██║"
    "██║ ╚████║"
    "╚═╝  ╚═══╝"
    "          "],
   :i 78,
   :character "n",
   :width 10,
   :height 7},
  "o"
  {:bands
   [" ██████╗ "
    "██╔═══██╗"
    "██║   ██║"
    "██║   ██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 79,
   :character "o",
   :width 9,
   :height 7},
  "p"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██████╔╝"
    "██╔═══╝ "
    "██║     "
    "╚═╝     "
    "        "],
   :i 80,
   :character "p",
   :width 8,
   :height 7},
  "q"
  {:bands
   [" ██████╗ "
    "██╔═══██╗"
    "██║   ██║"
    "██║▄▄ ██║"
    "╚██████╔╝"
    " ╚══▀▀═╝ "
    "         "],
   :i 81,
   :character "q",
   :width 9,
   :height 7},
  "r"
  {:bands
   ["██████╗ "
    "██╔══██╗"
    "██████╔╝"
    "██╔══██╗"
    "██║  ██║"
    "╚═╝  ╚═╝"
    "        "],
   :i 82,
   :character "r",
   :width 8,
   :height 7},
  "s"
  {:bands
   ["███████╗"
    "██╔════╝"
    "███████╗"
    "╚════██║"
    "███████║"
    "╚══════╝"
    "        "],
   :i 83,
   :character "s",
   :width 8,
   :height 7},
  "t"
  {:bands
   ["████████╗"
    "╚══██╔══╝"
    "   ██║   "
    "   ██║   "
    "   ██║   "
    "   ╚═╝   "
    "         "],
   :i 84,
   :character "t",
   :width 9,
   :height 7},
  "u"
  {:bands
   ["██╗   ██╗"
    "██║   ██║"
    "██║   ██║"
    "██║   ██║"
    "╚██████╔╝"
    " ╚═════╝ "
    "         "],
   :i 85,
   :character "u",
   :width 9,
   :height 7},
  "v"
  {:bands
   ["██╗   ██╗"
    "██║   ██║"
    "██║   ██║"
    "╚██╗ ██╔╝"
    " ╚████╔╝ "
    "  ╚═══╝  "
    "         "],
   :i 86,
   :character "v",
   :width 9,
   :height 7},
  "w"
  {:bands
   ["██╗    ██╗"
    "██║    ██║"
    "██║ █╗ ██║"
    "██║███╗██║"
    "╚███╔███╔╝"
    " ╚══╝╚══╝ "
    "          "],
   :i 87,
   :character "w",
   :width 10,
   :height 7},
  "x"
  {:bands
   ["██╗  ██╗"
    "╚██╗██╔╝"
    " ╚███╔╝ "
    " ██╔██╗ "
    "██╔╝ ██╗"
    "╚═╝  ╚═╝"
    "        "],
   :i 88,
   :character "x",
   :width 8,
   :height 7},
  "y"
  {:bands
   ["██╗   ██╗"
    "╚██╗ ██╔╝"
    " ╚████╔╝ "
    "  ╚██╔╝  "
    "   ██║   "
    "   ╚═╝   "
    "         "],
   :i 89,
   :character "y",
   :width 9,
   :height 7},
  "z"
  {:bands
   ["███████╗"
    "╚══███╔╝"
    "  ███╔╝ "
    " ███╔╝  "
    "███████╗"
    "╚══════╝"
    "        "],
   :i 90,
   :character "z",
   :width 8,
   :height 7}}})


;;  ▓█████▄  ██▀███   ██▓ ██▓███   ██▓███  ▓██   ██▓
;;  ▒██▀ ██▌▓██ ▒ ██▒▓██▒▓██░  ██▒▓██░  ██▒ ▒██  ██▒
;;  ░██   █▌▓██ ░▄█ ▒▒██▒▓██░ ██▓▒▓██░ ██▓▒  ▒██ ██░
;;  ░▓█▄   ▌▒██▀▀█▄  ░██░▒██▄█▓▒ ▒▒██▄█▓▒ ▒  ░ ▐██▓░
;;  ░▒████▓ ░██▓ ▒██▒░██░▒██▒ ░  ░▒██▒ ░  ░  ░ ██▒▓░
;;   ▒▒▓  ▒ ░ ▒▓ ░▒▓░░▓  ▒▓▒░ ░  ░▒▓▒░ ░  ░   ██▒▒▒
;;   ░ ▒  ▒   ░▒ ░ ▒░ ▒ ░░▒ ░     ░▒ ░      ▓██ ░▒░
;;   ░ ░  ░   ░░   ░  ▒ ░░░       ░░        ▒ ▒ ░░
;;     ░       ░      ░                     ░ ░
;;   ░                                      ░ ░

(def drippy
'{:font-name "Drippy",
 :example
 ["▓█████▄  ██▀███   ██▓ ██▓███   ██▓███  ▓██   ██▓"
  "▒██▀ ██▌▓██ ▒ ██▒▓██▒▓██░  ██▒▓██░  ██▒ ▒██  ██▒"
  "░██   █▌▓██ ░▄█ ▒▒██▒▓██░ ██▓▒▓██░ ██▓▒  ▒██ ██░"
  "░▓█▄   ▌▒██▀▀█▄  ░██░▒██▄█▓▒ ▒▒██▄█▓▒ ▒  ░ ▐██▓░"
  "░▒████▓ ░██▓ ▒██▒░██░▒██▒ ░  ░▒██▒ ░  ░  ░ ██▒▓░"
  " ▒▒▓  ▒ ░ ▒▓ ░▒▓░░▓  ▒▓▒░ ░  ░▒▓▒░ ░  ░   ██▒▒▒"
  " ░ ▒  ▒   ░▒ ░ ▒░ ▒ ░░▒ ░     ░▒ ░      ▓██ ░▒░"
  " ░ ░  ░   ░░   ░  ▒ ░░░       ░░        ▒ ▒ ░░"
  "   ░       ░      ░                     ░ ░"
  " ░                                      ░ ░"],
 :desc "Original font name: \"Bloody\"",
 :example-text "Drippy",
 :font-sym drippy,
 :widest-char "M",
 :char-height 10,
 :max-char-width 11,
 :missing-chars
 ["\""
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
  "["
  "\\"
  "]"
  "^"
  "_"
  "`"
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
  "ß"],
 :chars-array-map
 {" "
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 0,
   :character " ",
   :width 5,
   :height 10},
  "!"
  {:bands
   ["▐██▌"
    "▐██▌"
    "▐██▌"
    "▓██▒"
    "▒▄▄ "
    "░▀▀▒"
    "░  ░"
    "   ░"
    "░   "
    "    "],
   :i 1,
   :character "!",
   :width 4,
   :height 10},
  "\""
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  \"  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 2,
   :character "\"",
   :width 0,
   :height 10,
   :missing? true},
  "#"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  #  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 3,
   :character "#",
   :width 0,
   :height 10,
   :missing? true},
  "$"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  $  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 4,
   :character "$",
   :width 0,
   :height 10,
   :missing? true},
  "%"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  %  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 5,
   :character "%",
   :width 0,
   :height 10,
   :missing? true},
  "&"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  &  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 6,
   :character "&",
   :width 0,
   :height 10,
   :missing? true},
  "'"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  '  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 7,
   :character "'",
   :width 0,
   :height 10,
   :missing? true},
  "("
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  (  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 8,
   :character "(",
   :width 0,
   :height 10,
   :missing? true},
  ")"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  )  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 9,
   :character ")",
   :width 0,
   :height 10,
   :missing? true},
  "*"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  *  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 10,
   :character "*",
   :width 0,
   :height 10,
   :missing? true},
  "+"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  +  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 11,
   :character "+",
   :width 0,
   :height 10,
   :missing? true},
  ","
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ,  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 12,
   :character ",",
   :width 0,
   :height 10,
   :missing? true},
  "-"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  -  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 13,
   :character "-",
   :width 0,
   :height 10,
   :missing? true},
  "."
  {:bands
   ["$   $"
    "$   $"
    "$   $"
    "$   $"
    "$██▓$"
    "$▒▓▒$"
    "$░▒ $"
    "$░  $"
    "$ ░ $"
    "$ ░ $"],
   :i 14,
   :character ".",
   :width 5,
   :height 10},
  "/"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  /  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 15,
   :character "/",
   :width 0,
   :height 10,
   :missing? true},
  "0"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  0  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 16,
   :character "0",
   :width 0,
   :height 10,
   :missing? true},
  "1"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  1  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 17,
   :character "1",
   :width 0,
   :height 10,
   :missing? true},
  "2"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  2  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 18,
   :character "2",
   :width 0,
   :height 10,
   :missing? true},
  "3"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  3  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 19,
   :character "3",
   :width 0,
   :height 10,
   :missing? true},
  "4"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  4  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 20,
   :character "4",
   :width 0,
   :height 10,
   :missing? true},
  "5"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  5  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 21,
   :character "5",
   :width 0,
   :height 10,
   :missing? true},
  "6"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  6  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 22,
   :character "6",
   :width 0,
   :height 10,
   :missing? true},
  "7"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  7  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 23,
   :character "7",
   :width 0,
   :height 10,
   :missing? true},
  "8"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  8  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 24,
   :character "8",
   :width 0,
   :height 10,
   :missing? true},
  "9"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  9  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 25,
   :character "9",
   :width 0,
   :height 10,
   :missing? true},
  ":"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  :  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 26,
   :character ":",
   :width 0,
   :height 10,
   :missing? true},
  ";"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ;  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 27,
   :character ";",
   :width 0,
   :height 10,
   :missing? true},
  "<"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  <  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 28,
   :character "<",
   :width 0,
   :height 10,
   :missing? true},
  "="
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  =  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 29,
   :character "=",
   :width 0,
   :height 10,
   :missing? true},
  ">"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  >  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 30,
   :character ">",
   :width 0,
   :height 10,
   :missing? true},
  "?"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ?  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 31,
   :character "?",
   :width 0,
   :height 10,
   :missing? true},
  "@"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  @  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 32,
   :character "@",
   :width 0,
   :height 10,
   :missing? true},
  "A"
  {:bands
   [" ▄▄▄      "
    "▒████▄    "
    "▒██  ▀█▄  "
    "░██▄▄▄▄██ "
    " ▓█   ▓██▒"
    " ▒▒   ▓▒█░"
    "  ▒   ▒▒ ░"
    "  ░   ▒   "
    "      ░  ░"
    "          "],
   :i 33,
   :character "A",
   :width 10,
   :height 10},
  "B"
  {:bands
   [" ▄▄▄▄   "
    "▓█████▄ "
    "▒██▒ ▄██"
    "▒██░█▀  "
    "░▓█  ▀█▓"
    "░▒▓███▀▒"
    "▒░▒   ░ "
    " ░    ░ "
    " ░      "
    "      ░ "],
   :i 34,
   :character "B",
   :width 8,
   :height 10},
  "C"
  {:bands
   [" ▄████▄  "
    "▒██▀ ▀█  "
    "▒▓█    ▄ "
    "▒▓▓▄ ▄██▒"
    "▒ ▓███▀ ░"
    "░ ░▒ ▒  ░"
    "  ░  ▒   "
    "░        "
    "░ ░      "
    "░        "],
   :i 35,
   :character "C",
   :width 9,
   :height 10},
  "D"
  {:bands
   ["▓█████▄ "
    "▒██▀ ██▌"
    "░██   █▌"
    "░▓█▄   ▌"
    "░▒████▓ "
    " ▒▒▓  ▒ "
    " ░ ▒  ▒ "
    " ░ ░  ░ "
    "   ░    "
    " ░      "],
   :i 36,
   :character "D",
   :width 8,
   :height 10},
  "E"
  {:bands
   ["▓█████ "
    "▓█   ▀ "
    "▒███   "
    "▒▓█  ▄ "
    "░▒████▒"
    "░░ ▒░ ░"
    " ░ ░  ░"
    "   ░   "
    "   ░  ░"
    "       "],
   :i 37,
   :character "E",
   :width 7,
   :height 10},
  "F"
  {:bands
   ["  █████▒"
    "▓██   ▒ "
    "▒████ ░ "
    "░▓█▒  ░ "
    "░▒█░    "
    " ▒ ░    "
    " ░      "
    " ░ ░    "
    "        "
    "        "],
   :i 38,
   :character "F",
   :width 8,
   :height 10},
  "G"
  {:bands
   ["  ▄████ "
    " ██▒ ▀█▒"
    "▒██░▄▄▄░"
    "░▓█  ██▓"
    "░▒▓███▀▒"
    " ░▒   ▒ "
    "  ░   ░ "
    "░ ░   ░ "
    "      ░ "
    "        "],
   :i 39,
   :character "G",
   :width 8,
   :height 10},
  "H"
  {:bands
   [" ██░ ██ "
    "▓██░ ██▒"
    "▒██▀▀██░"
    "░▓█ ░██ "
    "░▓█▒░██▓"
    " ▒ ░░▒░▒"
    " ▒ ░▒░ ░"
    " ░  ░░ ░"
    " ░  ░  ░"
    "        "],
   :i 40,
   :character "H",
   :width 8,
   :height 10},
  "I"
  {:bands
   [" ██▓"
    "▓██▒"
    "▒██▒"
    "░██░"
    "░██░"
    "░▓  "
    " ▒ ░"
    " ▒ ░"
    " ░  "
    "    "],
   :i 41,
   :character "I",
   :width 4,
   :height 10},
  "J"
  {:bands
   [" ▄▄▄██▀▀▀"
    "   ▒██   "
    "   ░██   "
    "▓██▄██▓  "
    " ▓███▒   "
    " ▒▓▒▒░   "
    " ▒ ░▒░   "
    " ░ ░ ░   "
    " ░   ░   "
    "         "],
   :i 42,
   :character "J",
   :width 9,
   :height 10},
  "K"
  {:bands
   [" ██ ▄█▀"
    " ██▄█▒ "
    "▓███▄░ "
    "▓██ █▄ "
    "▒██▒ █▄"
    "▒ ▒▒ ▓▒"
    "░ ░▒ ▒░"
    "░ ░░ ░ "
    "░  ░   "
    "       "],
   :i 43,
   :character "K",
   :width 7,
   :height 10},
  "L"
  {:bands
   [" ██▓    "
    "▓██▒    "
    "▒██░    "
    "▒██░    "
    "░██████▒"
    "░ ▒░▓  ░"
    "░ ░ ▒  ░"
    "  ░ ░   "
    "    ░  ░"
    "        "],
   :i 44,
   :character "L",
   :width 8,
   :height 10},
  "M"
  {:bands
   [" ███▄ ▄███▓"
    "▓██▒▀█▀ ██▒"
    "▓██    ▓██░"
    "▒██    ▒██ "
    "▒██▒   ░██▒"
    "░ ▒░   ░  ░"
    "░  ░      ░"
    "░      ░   "
    "       ░   "
    "           "],
   :i 45,
   :character "M",
   :width 11,
   :height 10},
  "N"
  {:bands
   [" ███▄    █ "
    " ██ ▀█   █ "
    "▓██  ▀█ ██▒"
    "▓██▒  ▐▌██▒"
    "▒██░   ▓██░"
    "░ ▒░   ▒ ▒ "
    "░ ░░   ░ ▒░"
    "   ░   ░ ░ "
    "         ░ "
    "           "],
   :i 46,
   :character "N",
   :width 11,
   :height 10},
  "O"
  {:bands
   [" ▒█████  "
    "▒██▒  ██▒"
    "▒██░  ██▒"
    "▒██   ██░"
    "░ ████▓▒░"
    "░ ▒░▒░▒░ "
    "  ░ ▒ ▒░ "
    "░ ░ ░ ▒  "
    "    ░ ░  "
    "         "],
   :i 47,
   :character "O",
   :width 9,
   :height 10},
  "P"
  {:bands
   [" ██▓███  "
    "▓██░  ██▒"
    "▓██░ ██▓▒"
    "▒██▄█▓▒ ▒"
    "▒██▒ ░  ░"
    "▒▓▒░ ░  ░"
    "░▒ ░     "
    "░░       "
    "         "
    "         "],
   :i 48,
   :character "P",
   :width 9,
   :height 10},
  "Q"
  {:bands
   ["  █████  "
    "▒██▓  ██▒"
    "▒██▒  ██░"
    "░██  █▀ ░"
    "░▒███▒█▄ "
    "░░ ▒▒░ ▒ "
    " ░ ▒░  ░ "
    "   ░   ░ "
    "    ░    "
    "         "],
   :i 49,
   :character "Q",
   :width 9,
   :height 10},
  "R"
  {:bands
   [" ██▀███  "
    "▓██ ▒ ██▒"
    "▓██ ░▄█ ▒"
    "▒██▀▀█▄  "
    "░██▓ ▒██▒"
    "░ ▒▓ ░▒▓░"
    "  ░▒ ░ ▒░"
    "  ░░   ░ "
    "   ░     "
    "         "],
   :i 50,
   :character "R",
   :width 9,
   :height 10},
  "S"
  {:bands
   ["  ██████ "
    "▒██    ▒ "
    "░ ▓██▄   "
    "  ▒   ██▒"
    "▒██████▒▒"
    "▒ ▒▓▒ ▒ ░"
    "░ ░▒  ░ ░"
    "░  ░  ░  "
    "      ░  "
    "         "],
   :i 51,
   :character "S",
   :width 9,
   :height 10},
  "T"
  {:bands
   ["▄▄▄█████▓"
    "▓  ██▒ ▓▒"
    "▒ ▓██░ ▒░"
    "░ ▓██▓ ░ "
    "  ▒██▒ ░ "
    "  ▒ ░░   "
    "    ░    "
    "  ░      "
    "         "
    "         "],
   :i 52,
   :character "T",
   :width 9,
   :height 10},
  "U"
  {:bands
   [" █    ██ "
    " ██  ▓██▒"
    "▓██  ▒██░"
    "▓▓█  ░██░"
    "▒▒█████▓ "
    "░▒▓▒ ▒ ▒ "
    "░░▒░ ░ ░ "
    " ░░░ ░ ░ "
    "   ░     "
    "         "],
   :i 53,
   :character "U",
   :width 9,
   :height 10},
  "V"
  {:bands
   [" ██▒   █▓"
    "▓██░   █▒"
    " ▓██  █▒░"
    "  ▒██ █░░"
    "   ▒▀█░  "
    "   ░ ▐░  "
    "   ░ ░░  "
    "     ░░  "
    "      ░  "
    "     ░   "],
   :i 54,
   :character "V",
   :width 9,
   :height 10},
  "W"
  {:bands
   [" █     █░"
    "▓█░ █ ░█░"
    "▒█░ █ ░█ "
    "░█░ █ ░█ "
    "░░██▒██▓ "
    "░ ▓░▒ ▒  "
    "  ▒ ░ ░  "
    "  ░   ░  "
    "    ░    "
    "         "],
   :i 55,
   :character "W",
   :width 9,
   :height 10},
  "X"
  {:bands
   ["▒██   ██▒"
    "▒▒ █ █ ▒░"
    "░░  █   ░"
    " ░ █ █ ▒ "
    "▒██▒ ▒██▒"
    "▒▒ ░ ░▓ ░"
    "░░   ░▒ ░"
    " ░    ░  "
    " ░    ░  "
    "         "],
   :i 56,
   :character "X",
   :width 9,
   :height 10},
  "Y"
  {:bands
   ["▓██   ██▓"
    " ▒██  ██▒"
    "  ▒██ ██░"
    "  ░ ▐██▓░"
    "  ░ ██▒▓░"
    "   ██▒▒▒ "
    " ▓██ ░▒░ "
    " ▒ ▒ ░░  "
    " ░ ░     "
    " ░ ░     "],
   :i 57,
   :character "Y",
   :width 9,
   :height 10},
  "Z"
  {:bands
   ["▒███████▒"
    "▒ ▒ ▒ ▄▀░"
    "░ ▒ ▄▀▒░ "
    "  ▄▀▒   ░"
    "▒███████▒"
    "░▒▒ ▓░▒░▒"
    "░░▒ ▒ ░ ▒"
    "░ ░ ░ ░ ░"
    "  ░ ░    "
    "░        "],
   :i 58,
   :character "Z",
   :width 9,
   :height 10},
  "["
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  [  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 59,
   :character "[",
   :width 0,
   :height 10,
   :missing? true},
  "\\"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  \\  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 60,
   :character "\\",
   :width 0,
   :height 10,
   :missing? true},
  "]"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ]  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 61,
   :character "]",
   :width 0,
   :height 10,
   :missing? true},
  "^"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ^  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 62,
   :character "^",
   :width 0,
   :height 10,
   :missing? true},
  "_"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  _  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 63,
   :character "_",
   :width 0,
   :height 10,
   :missing? true},
  "`"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  `  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 64,
   :character "`",
   :width 0,
   :height 10,
   :missing? true},
  "a"
  {:bands
   [" ▄▄▄      "
    "▒████▄    "
    "▒██  ▀█▄  "
    "░██▄▄▄▄██ "
    " ▓█   ▓██▒"
    " ▒▒   ▓▒█░"
    "  ▒   ▒▒ ░"
    "  ░   ▒   "
    "      ░  ░"
    "          "],
   :i 65,
   :character "a",
   :width 10,
   :height 10},
  "b"
  {:bands
   [" ▄▄▄▄   "
    "▓█████▄ "
    "▒██▒ ▄██"
    "▒██░█▀  "
    "░▓█  ▀█▓"
    "░▒▓███▀▒"
    "▒░▒   ░ "
    " ░    ░ "
    " ░      "
    "      ░ "],
   :i 66,
   :character "b",
   :width 8,
   :height 10},
  "c"
  {:bands
   [" ▄████▄  "
    "▒██▀ ▀█  "
    "▒▓█    ▄ "
    "▒▓▓▄ ▄██▒"
    "▒ ▓███▀ ░"
    "░ ░▒ ▒  ░"
    "  ░  ▒   "
    "░        "
    "░ ░      "
    "░        "],
   :i 67,
   :character "c",
   :width 9,
   :height 10},
  "d"
  {:bands
   ["▓█████▄ "
    "▒██▀ ██▌"
    "░██   █▌"
    "░▓█▄   ▌"
    "░▒████▓ "
    " ▒▒▓  ▒ "
    " ░ ▒  ▒ "
    " ░ ░  ░ "
    "   ░    "
    " ░      "],
   :i 68,
   :character "d",
   :width 8,
   :height 10},
  "e"
  {:bands
   ["▓█████ "
    "▓█   ▀ "
    "▒███   "
    "▒▓█  ▄ "
    "░▒████▒"
    "░░ ▒░ ░"
    " ░ ░  ░"
    "   ░   "
    "   ░  ░"
    "       "],
   :i 69,
   :character "e",
   :width 7,
   :height 10},
  "f"
  {:bands
   ["  █████▒"
    "▓██   ▒ "
    "▒████ ░ "
    "░▓█▒  ░ "
    "░▒█░    "
    " ▒ ░    "
    " ░      "
    " ░ ░    "
    "        "
    "        "],
   :i 70,
   :character "f",
   :width 8,
   :height 10},
  "g"
  {:bands
   ["  ▄████ "
    " ██▒ ▀█▒"
    "▒██░▄▄▄░"
    "░▓█  ██▓"
    "░▒▓███▀▒"
    " ░▒   ▒ "
    "  ░   ░ "
    "░ ░   ░ "
    "      ░ "
    "        "],
   :i 71,
   :character "g",
   :width 8,
   :height 10},
  "h"
  {:bands
   [" ██░ ██ "
    "▓██░ ██▒"
    "▒██▀▀██░"
    "░▓█ ░██ "
    "░▓█▒░██▓"
    " ▒ ░░▒░▒"
    " ▒ ░▒░ ░"
    " ░  ░░ ░"
    " ░  ░  ░"
    "        "],
   :i 72,
   :character "h",
   :width 8,
   :height 10},
  "i"
  {:bands
   [" ██▓"
    "▓██▒"
    "▒██▒"
    "░██░"
    "░██░"
    "░▓  "
    " ▒ ░"
    " ▒ ░"
    " ░  "
    "    "],
   :i 73,
   :character "i",
   :width 4,
   :height 10},
  "j"
  {:bands
   [" ▄▄▄██▀▀▀"
    "   ▒██   "
    "   ░██   "
    "▓██▄██▓  "
    " ▓███▒   "
    " ▒▓▒▒░   "
    " ▒ ░▒░   "
    " ░ ░ ░   "
    " ░   ░   "
    "         "],
   :i 74,
   :character "j",
   :width 9,
   :height 10},
  "k"
  {:bands
   [" ██ ▄█▀"
    " ██▄█▒ "
    "▓███▄░ "
    "▓██ █▄ "
    "▒██▒ █▄"
    "▒ ▒▒ ▓▒"
    "░ ░▒ ▒░"
    "░ ░░ ░ "
    "░  ░   "
    "       "],
   :i 75,
   :character "k",
   :width 7,
   :height 10},
  "l"
  {:bands
   [" ██▓    "
    "▓██▒    "
    "▒██░    "
    "▒██░    "
    "░██████▒"
    "░ ▒░▓  ░"
    "░ ░ ▒  ░"
    "  ░ ░   "
    "    ░  ░"
    "        "],
   :i 76,
   :character "l",
   :width 8,
   :height 10},
  "m"
  {:bands
   [" ███▄ ▄███▓"
    "▓██▒▀█▀ ██▒"
    "▓██    ▓██░"
    "▒██    ▒██ "
    "▒██▒   ░██▒"
    "░ ▒░   ░  ░"
    "░  ░      ░"
    "░      ░   "
    "       ░   "
    "           "],
   :i 77,
   :character "m",
   :width 11,
   :height 10},
  "n"
  {:bands
   [" ███▄    █ "
    " ██ ▀█   █ "
    "▓██  ▀█ ██▒"
    "▓██▒  ▐▌██▒"
    "▒██░   ▓██░"
    "░ ▒░   ▒ ▒ "
    "░ ░░   ░ ▒░"
    "   ░   ░ ░ "
    "         ░ "
    "           "],
   :i 78,
   :character "n",
   :width 11,
   :height 10},
  "o"
  {:bands
   [" ▒█████  "
    "▒██▒  ██▒"
    "▒██░  ██▒"
    "▒██   ██░"
    "░ ████▓▒░"
    "░ ▒░▒░▒░ "
    "  ░ ▒ ▒░ "
    "░ ░ ░ ▒  "
    "    ░ ░  "
    "         "],
   :i 79,
   :character "o",
   :width 9,
   :height 10},
  "p"
  {:bands
   [" ██▓███  "
    "▓██░  ██▒"
    "▓██░ ██▓▒"
    "▒██▄█▓▒ ▒"
    "▒██▒ ░  ░"
    "▒▓▒░ ░  ░"
    "░▒ ░     "
    "░░       "
    "         "
    "         "],
   :i 80,
   :character "p",
   :width 9,
   :height 10},
  "q"
  {:bands
   ["  █████  "
    "▒██▓  ██▒"
    "▒██▒  ██░"
    "░██  █▀ ░"
    "░▒███▒█▄ "
    "░░ ▒▒░ ▒ "
    " ░ ▒░  ░ "
    "   ░   ░ "
    "    ░    "
    "         "],
   :i 81,
   :character "q",
   :width 9,
   :height 10},
  "r"
  {:bands
   [" ██▀███  "
    "▓██ ▒ ██▒"
    "▓██ ░▄█ ▒"
    "▒██▀▀█▄  "
    "░██▓ ▒██▒"
    "░ ▒▓ ░▒▓░"
    "  ░▒ ░ ▒░"
    "  ░░   ░ "
    "   ░     "
    "         "],
   :i 82,
   :character "r",
   :width 9,
   :height 10},
  "s"
  {:bands
   ["  ██████ "
    "▒██    ▒ "
    "░ ▓██▄   "
    "  ▒   ██▒"
    "▒██████▒▒"
    "▒ ▒▓▒ ▒ ░"
    "░ ░▒  ░ ░"
    "░  ░  ░  "
    "      ░  "
    "         "],
   :i 83,
   :character "s",
   :width 9,
   :height 10},
  "t"
  {:bands
   ["▄▄▄█████▓"
    "▓  ██▒ ▓▒"
    "▒ ▓██░ ▒░"
    "░ ▓██▓ ░ "
    "  ▒██▒ ░ "
    "  ▒ ░░   "
    "    ░    "
    "  ░      "
    "         "
    "         "],
   :i 84,
   :character "t",
   :width 9,
   :height 10},
  "u"
  {:bands
   [" █    ██ "
    " ██  ▓██▒"
    "▓██  ▒██░"
    "▓▓█  ░██░"
    "▒▒█████▓ "
    "░▒▓▒ ▒ ▒ "
    "░░▒░ ░ ░ "
    " ░░░ ░ ░ "
    "   ░     "
    "         "],
   :i 85,
   :character "u",
   :width 9,
   :height 10},
  "v"
  {:bands
   [" ██▒   █▓"
    "▓██░   █▒"
    " ▓██  █▒░"
    "  ▒██ █░░"
    "   ▒▀█░  "
    "   ░ ▐░  "
    "   ░ ░░  "
    "     ░░  "
    "      ░  "
    "     ░   "],
   :i 86,
   :character "v",
   :width 9,
   :height 10},
  "w"
  {:bands
   [" █     █░"
    "▓█░ █ ░█░"
    "▒█░ █ ░█ "
    "░█░ █ ░█ "
    "░░██▒██▓ "
    "░ ▓░▒ ▒  "
    "  ▒ ░ ░  "
    "  ░   ░  "
    "    ░    "
    "         "],
   :i 87,
   :character "w",
   :width 9,
   :height 10},
  "x"
  {:bands
   ["▒██   ██▒"
    "▒▒ █ █ ▒░"
    "░░  █   ░"
    " ░ █ █ ▒ "
    "▒██▒ ▒██▒"
    "▒▒ ░ ░▓ ░"
    "░░   ░▒ ░"
    " ░    ░  "
    " ░    ░  "
    "         "],
   :i 88,
   :character "x",
   :width 9,
   :height 10},
  "y"
  {:bands
   ["▓██   ██▓"
    " ▒██  ██▒"
    "  ▒██ ██░"
    "  ░ ▐██▓░"
    "  ░ ██▒▓░"
    "   ██▒▒▒ "
    " ▓██ ░▒░ "
    " ▒ ▒ ░░  "
    " ░ ░     "
    " ░ ░     "],
   :i 89,
   :character "y",
   :width 9,
   :height 10},
  "z"
  {:bands
   ["▒███████▒"
    "▒ ▒ ▒ ▄▀░"
    "░ ▒ ▄▀▒░ "
    "  ▄▀▒   ░"
    "▒███████▒"
    "░▒▒ ▓░▒░▒"
    "░░▒ ▒ ░ ▒"
    "░ ░ ░ ░ ░"
    "  ░ ░    "
    "░        "],
   :i 90,
   :character "z",
   :width 9,
   :height 10},
  "{"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  {  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 91,
   :character "{",
   :width 0,
   :height 10,
   :missing? true},
  "|"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  |  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 92,
   :character "|",
   :width 0,
   :height 10,
   :missing? true},
  "}"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  }  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 93,
   :character "}",
   :width 0,
   :height 10,
   :missing? true},
  "~"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ~  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 94,
   :character "~",
   :width 0,
   :height 10,
   :missing? true},
  "Ä"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  Ä  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 95,
   :character "Ä",
   :width 0,
   :height 10,
   :missing? true},
  "Ö"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  Ö  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 96,
   :character "Ö",
   :width 0,
   :height 10,
   :missing? true},
  "Ü"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  Ü  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 97,
   :character "Ü",
   :width 0,
   :height 10,
   :missing? true},
  "ä"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ä  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 98,
   :character "ä",
   :width 0,
   :height 10,
   :missing? true},
  "ö"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ö  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 99,
   :character "ö",
   :width 0,
   :height 10,
   :missing? true},
  "ü"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ü  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 100,
   :character "ü",
   :width 0,
   :height 10,
   :missing? true},
  "ß"
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "  ß  "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 101,
   :character "ß",
   :width 0,
   :height 10,
   :missing? true}}})


;;   ____   _
;;  |  _ \ (_)
;;  | |_) | _   __ _
;;  |  _ < | | / _` |
;;  | |_) || || (_| |
;;  |____/ |_| \__, |
;;              __/ |
;;             |___/

(def big
'{:font-name "Big",
 :example
 [" ____   _"
  "|  _ \\ (_)"
  "| |_) | _   __ _"
  "|  _ < | | / _` |"
  "| |_) || || (_| |"
  "|____/ |_| \\__, |"
  "            __/ |"
  "           |___/"],
 :desc
 "Big by Glenn Chappell 4/93 -- based on Standard\nIncludes ISO Latin-1\nGreek characters by Bruce Jakeway <pbjakeway@neumann.uwaterloo.ca>\nfiglet release 2.2 -- November 1996\nPermission is hereby given to modify this font, as long as the\nmodifier's name is placed on a comment line.\n\nModified by Paul Burton <solution@earthlink.net> 12/96 to include new parameter\nsupported by FIGlet and FIGWin.  May also be slightly modified for better use\nof new full-width/kern/smush alternatives, but default output is NOT changed.",
 :example-text "Big",
 :font-sym big,
 :widest-char "W",
 :char-height 8,
 :max-char-width 14,
 :missing-chars [],
 :chars-array-map
 {" "
  {:bands ["    " "    " "    " "    " "    " "    " "    " "    "],
   :i 0,
   :character " ",
   :width 4,
   :height 8},
  "!"
  {:bands [" _ " "| |" "| |" "| |" "|_|" "(_)" "   " "   "],
   :i 1,
   :character "!",
   :width 3,
   :height 8},
  "\""
  {:bands
   [" _ _ " "( | )" " V V " "     " "     " "     " "     " "     "],
   :i 2,
   :character "\"",
   :width 5,
   :height 8},
  "#"
  {:bands
   ["   _  _   "
    " _| || |_ "
    "|_  __  _|"
    " _| || |_ "
    "|_  __  _|"
    "  |_||_|  "
    "          "
    "          "],
   :i 3,
   :character "#",
   :width 10,
   :height 8},
  "$"
  {:bands
   ["  _  " " | | " "/ __)" "\\__ \\" "(   /" " |_| " "     " "     "],
   :i 4,
   :character "$",
   :width 5,
   :height 8},
  "%"
  {:bands
   [" _   __"
    "(_) / /"
    "   / / "
    "  / /  "
    " / / _ "
    "/_/ (_)"
    "       "
    "       "],
   :i 5,
   :character "%",
   :width 7,
   :height 8},
  "&"
  {:bands
   ["        "
    "  ___   "
    " ( _ )  "
    " / _ \\/\\"
    "| (_>  <"
    " \\___/\\/"
    "        "
    "        "],
   :i 6,
   :character "&",
   :width 8,
   :height 8},
  "'"
  {:bands [" _ " "( )" "|/ " "   " "   " "   " "   " "   "],
   :i 7,
   :character "'",
   :width 3,
   :height 8},
  "("
  {:bands ["  __" " / /" "| | " "| | " "| | " "| | " " \\_\\" "    "],
   :i 8,
   :character "(",
   :width 4,
   :height 8},
  ")"
  {:bands ["__  " "\\ \\ " " | |" " | |" " | |" " | |" "/_/ " "    "],
   :i 9,
   :character ")",
   :width 4,
   :height 8},
  "*"
  {:bands
   ["    _    "
    " /\\| |/\\ "
    " \\ ` ' / "
    "|_     _|"
    " / , . \\ "
    " \\/|_|\\/ "
    "         "
    "         "],
   :i 10,
   :character "*",
   :width 9,
   :height 8},
  "+"
  {:bands
   ["       "
    "   _   "
    " _| |_ "
    "|_   _|"
    "  |_|  "
    "       "
    "       "
    "       "],
   :i 11,
   :character "+",
   :width 7,
   :height 8},
  ","
  {:bands ["   " "   " "   " "   " " _ " "( )" "|/ " "   "],
   :i 12,
   :character ",",
   :width 3,
   :height 8},
  "-"
  {:bands
   ["        "
    "        "
    " ______ "
    "|______|"
    "        "
    "        "
    "        "
    "        "],
   :i 13,
   :character "-",
   :width 8,
   :height 8},
  "."
  {:bands ["   " "   " "   " "   " " _ " "(_)" "   " "   "],
   :i 14,
   :character ".",
   :width 3,
   :height 8},
  "/"
  {:bands
   ["     __"
    "    / /"
    "   / / "
    "  / /  "
    " / /   "
    "/_/    "
    "       "
    "       "],
   :i 15,
   :character "/",
   :width 7,
   :height 8},
  "0"
  {:bands
   ["  ___  "
    " / _ \\ "
    "| | | |"
    "| | | |"
    "| |_| |"
    " \\___/ "
    "       "
    "       "],
   :i 16,
   :character "0",
   :width 7,
   :height 8},
  "1"
  {:bands [" __ " "/_ |" " | |" " | |" " | |" " |_|" "    " "    "],
   :i 17,
   :character "1",
   :width 4,
   :height 8},
  "2"
  {:bands
   [" ___  "
    "|__ \\ "
    "   ) |"
    "  / / "
    " / /_ "
    "|____|"
    "      "
    "      "],
   :i 18,
   :character "2",
   :width 6,
   :height 8},
  "3"
  {:bands
   [" ____  "
    "|___ \\ "
    "  __) |"
    " |__ < "
    " ___) |"
    "|____/ "
    "       "
    "       "],
   :i 19,
   :character "3",
   :width 7,
   :height 8},
  "4"
  {:bands
   [" _  _   "
    "| || |  "
    "| || |_ "
    "|__   _|"
    "   | |  "
    "   |_|  "
    "        "
    "        "],
   :i 20,
   :character "4",
   :width 8,
   :height 8},
  "5"
  {:bands
   ["_____ "
    "| ____|"
    "| |__  "
    "|___ \\ "
    " ___) |"
    "|____/ "
    "       "
    "       "],
   :i 21,
   :character "5",
   :width 6,
   :height 8},
  "6"
  {:bands
   ["   __  "
    "  / /  "
    " / /_  "
    "| '_ \\ "
    "| (_) |"
    " \\___/ "
    "       "
    "       "],
   :i 22,
   :character "6",
   :width 7,
   :height 8},
  "7"
  {:bands
   [" ______ "
    "|____  |"
    "    / / "
    "   / /  "
    "  / /   "
    " /_/    "
    "        "
    "        "],
   :i 23,
   :character "7",
   :width 8,
   :height 8},
  "8"
  {:bands
   ["  ___  "
    " / _ \\ "
    "| (_) |"
    " > _ < "
    "| (_) |"
    " \\___/ "
    "       "
    "       "],
   :i 24,
   :character "8",
   :width 7,
   :height 8},
  "9"
  {:bands
   ["  ___  "
    " / _ \\ "
    "| (_) |"
    " \\__, |"
    "   / / "
    "  /_/  "
    "       "
    "       "],
   :i 25,
   :character "9",
   :width 7,
   :height 8},
  ":"
  {:bands ["   " " _ " "(_)" "   " " _ " "(_)" "   " "   "],
   :i 26,
   :character ":",
   :width 3,
   :height 8},
  ";"
  {:bands ["   " " _ " "(_)" "   " " _ " "( )" "|/ " "   "],
   :i 27,
   :character ";",
   :width 3,
   :height 8},
  "<"
  {:bands
   ["   __"
    "  / /"
    " / / "
    "< <  "
    " \\ \\ "
    "  \\_\\"
    "     "
    "     "],
   :i 28,
   :character "<",
   :width 5,
   :height 8},
  "="
  {:bands
   ["        "
    " ______ "
    "|______|"
    " ______ "
    "|______|"
    "        "
    "        "
    "        "],
   :i 29,
   :character "=",
   :width 8,
   :height 8},
  ">"
  {:bands
   ["__   "
    "\\ \\  "
    " \\ \\ "
    "  > >"
    " / / "
    "/_/  "
    "     "
    "     "],
   :i 30,
   :character ">",
   :width 5,
   :height 8},
  "?"
  {:bands
   [" ___  "
    "|__ \\ "
    "   ) |"
    "  / / "
    " |_|  "
    " (_)  "
    "      "
    "      "],
   :i 31,
   :character "?",
   :width 6,
   :height 8},
  "@"
  {:bands
   ["         "
    "   ____  "
    "  / __ \\ "
    " / / _` |"
    "| | (_| |"
    " \\ \\__,_|"
    "  \\____/ "
    "         "],
   :i 32,
   :character "@",
   :width 9,
   :height 8},
  "A"
  {:bands
   ["          "
    "    /\\    "
    "   /  \\   "
    "  / /\\ \\  "
    " / ____ \\ "
    "/_/    \\_\\"
    "          "
    "          "],
   :i 33,
   :character "A",
   :width 10,
   :height 8},
  "B"
  {:bands
   [" ____  "
    "|  _ \\ "
    "| |_) |"
    "|  _ < "
    "| |_) |"
    "|____/ "
    "       "
    "       "],
   :i 34,
   :character "B",
   :width 7,
   :height 8},
  "C"
  {:bands
   ["  _____ "
    " / ____|"
    "| |     "
    "| |     "
    "| |____ "
    " \\_____|"
    "        "
    "        "],
   :i 35,
   :character "C",
   :width 8,
   :height 8},
  "D"
  {:bands
   [" _____  "
    "|  __ \\ "
    "| |  | |"
    "| |  | |"
    "| |__| |"
    "|_____/ "
    "        "
    "        "],
   :i 36,
   :character "D",
   :width 8,
   :height 8},
  "E"
  {:bands
   [" ______ "
    "|  ____|"
    "| |__   "
    "|  __|  "
    "| |____ "
    "|______|"
    "        "
    "        "],
   :i 37,
   :character "E",
   :width 8,
   :height 8},
  "F"
  {:bands
   [" ______ "
    "|  ____|"
    "| |__   "
    "|  __|  "
    "| |     "
    "|_|     "
    "        "
    "        "],
   :i 38,
   :character "F",
   :width 8,
   :height 8},
  "G"
  {:bands
   ["  _____ "
    " / ____|"
    "| |  __ "
    "| | |_ |"
    "| |__| |"
    " \\_____|"
    "        "
    "        "],
   :i 39,
   :character "G",
   :width 8,
   :height 8},
  "H"
  {:bands
   [" _    _ "
    "| |  | |"
    "| |__| |"
    "|  __  |"
    "| |  | |"
    "|_|  |_|"
    "        "
    "        "],
   :i 40,
   :character "H",
   :width 8,
   :height 8},
  "I"
  {:bands
   [" _____ "
    "|_   _|"
    "  | |  "
    "  | |  "
    " _| |_ "
    "|_____|"
    "       "
    "       "],
   :i 41,
   :character "I",
   :width 7,
   :height 8},
  "J"
  {:bands
   ["      _ "
    "     | |"
    "     | |"
    " _   | |"
    "| |__| |"
    " \\____/ "
    "        "
    "        "],
   :i 42,
   :character "J",
   :width 8,
   :height 8},
  "K"
  {:bands
   [" _  __"
    "| |/ /"
    "| ' / "
    "|  <  "
    "| . \\ "
    "|_|\\_\\"
    "      "
    "      "],
   :i 43,
   :character "K",
   :width 6,
   :height 8},
  "L"
  {:bands
   [" _      "
    "| |     "
    "| |     "
    "| |     "
    "| |____ "
    "|______|"
    "        "
    "        "],
   :i 44,
   :character "L",
   :width 8,
   :height 8},
  "M"
  {:bands
   [" __  __ "
    "|  \\/  |"
    "| \\  / |"
    "| |\\/| |"
    "| |  | |"
    "|_|  |_|"
    "        "
    "        "],
   :i 45,
   :character "M",
   :width 8,
   :height 8},
  "N"
  {:bands
   [" _   _ "
    "| \\ | |"
    "|  \\| |"
    "| . ` |"
    "| |\\  |"
    "|_| \\_|"
    "       "
    "       "],
   :i 46,
   :character "N",
   :width 7,
   :height 8},
  "O"
  {:bands
   ["  ____  "
    " / __ \\ "
    "| |  | |"
    "| |  | |"
    "| |__| |"
    " \\____/ "
    "        "
    "        "],
   :i 47,
   :character "O",
   :width 8,
   :height 8},
  "P"
  {:bands
   [" _____  "
    "|  __ \\ "
    "| |__) |"
    "|  ___/ "
    "| |     "
    "|_|     "
    "        "
    "        "],
   :i 48,
   :character "P",
   :width 8,
   :height 8},
  "Q"
  {:bands
   ["  ____  "
    " / __ \\ "
    "| |  | |"
    "| |  | |"
    "| |__| |"
    " \\___\\_\\"
    "        "
    "        "],
   :i 49,
   :character "Q",
   :width 8,
   :height 8},
  "R"
  {:bands
   [" _____  "
    "|  __ \\ "
    "| |__) |"
    "|  _  / "
    "| | \\ \\ "
    "|_|  \\_\\"
    "        "
    "        "],
   :i 50,
   :character "R",
   :width 8,
   :height 8},
  "S"
  {:bands
   ["  _____ "
    " / ____|"
    "| (___  "
    " \\___ \\ "
    " ____) |"
    "|_____/ "
    "        "
    "        "],
   :i 51,
   :character "S",
   :width 8,
   :height 8},
  "T"
  {:bands
   [" _______ "
    "|__   __|"
    "   | |   "
    "   | |   "
    "   | |   "
    "   |_|   "
    "         "
    "         "],
   :i 52,
   :character "T",
   :width 9,
   :height 8},
  "U"
  {:bands
   [" _    _ "
    "| |  | |"
    "| |  | |"
    "| |  | |"
    "| |__| |"
    " \\____/ "
    "        "
    "        "],
   :i 53,
   :character "U",
   :width 8,
   :height 8},
  "V"
  {:bands
   ["__      __"
    "\\ \\    / /"
    " \\ \\  / / "
    "  \\ \\/ /  "
    "   \\  /   "
    "    \\/    "
    "          "
    "          "],
   :i 54,
   :character "V",
   :width 10,
   :height 8},
  "W"
  {:bands
   ["__          __"
    "\\ \\        / /"
    " \\ \\  /\\  / / "
    "  \\ \\/  \\/ /  "
    "   \\  /\\  /   "
    "    \\/  \\/    "
    "              "
    "              "],
   :i 55,
   :character "W",
   :width 14,
   :height 8},
  "X"
  {:bands
   ["__   __"
    "\\ \\ / /"
    " \\ V / "
    "  > <  "
    " / . \\ "
    "/_/ \\_\\"
    "       "
    "       "],
   :i 56,
   :character "X",
   :width 7,
   :height 8},
  "Y"
  {:bands
   ["__     __"
    "\\ \\   / /"
    " \\ \\_/ / "
    "  \\   /  "
    "   | |   "
    "   |_|   "
    "         "
    "         "],
   :i 57,
   :character "Y",
   :width 9,
   :height 8},
  "Z"
  {:bands
   [" ______"
    "|___  /"
    "   / / "
    "  / /  "
    " / /__ "
    "/_____|"
    "       "
    "       "],
   :i 58,
   :character "Z",
   :width 7,
   :height 8},
  "["
  {:bands
   [" ___ " "|  _|" "| |  " "| |  " "| |  " "| |_ " "|___|" "     "],
   :i 59,
   :character "[",
   :width 5,
   :height 8},
  "\\"
  {:bands
   ["__     "
    "\\ \\    "
    " \\ \\   "
    "  \\ \\  "
    "   \\ \\ "
    "    \\_\\"
    "       "
    "       "],
   :i 60,
   :character "\\",
   :width 7,
   :height 8},
  "]"
  {:bands
   [" ___ " "|_  |" "  | |" "  | |" "  | |" " _| |" "|___|" "     "],
   :i 61,
   :character "]",
   :width 5,
   :height 8},
  "^"
  {:bands [" /\\ " "|/\\|" "    " "    " "    " "    " "    " "    "],
   :i 62,
   :character "^",
   :width 4,
   :height 8},
  "_"
  {:bands
   ["        "
    "        "
    "        "
    "        "
    "        "
    "        "
    " ______ "
    "|______|"],
   :i 63,
   :character "_",
   :width 8,
   :height 8},
  "`"
  {:bands [" _ " "( )" " \\|" "   " "   " "   " "   " "   "],
   :i 64,
   :character "`",
   :width 3,
   :height 8},
  "a"
  {:bands
   ["       "
    "       "
    "  __ _ "
    " / _` |"
    "| (_| |"
    " \\__,_|"
    "       "
    "       "],
   :i 65,
   :character "a",
   :width 7,
   :height 8},
  "b"
  {:bands
   [" _     "
    "| |    "
    "| |__  "
    "| '_ \\ "
    "| |_) |"
    "|_.__/ "
    "       "
    "       "],
   :i 66,
   :character "b",
   :width 7,
   :height 8},
  "c"
  {:bands
   ["      "
    "      "
    "  ___ "
    " / __|"
    "| (__ "
    " \\___|"
    "      "
    "      "],
   :i 67,
   :character "c",
   :width 6,
   :height 8},
  "d"
  {:bands
   ["     _ "
    "    | |"
    "  __| |"
    " / _` |"
    "| (_| |"
    " \\__,_|"
    "       "
    "       "],
   :i 68,
   :character "d",
   :width 7,
   :height 8},
  "e"
  {:bands
   ["      "
    "      "
    "  ___ "
    " / _ \\"
    "|  __/"
    " \\___|"
    "      "
    "      "],
   :i 69,
   :character "e",
   :width 6,
   :height 8},
  "f"
  {:bands
   ["  __ " " / _|" "| |_ " "|  _|" "| |  " "|_|  " "     " "     "],
   :i 70,
   :character "f",
   :width 5,
   :height 8},
  "g"
  {:bands
   ["       "
    "       "
    "  __ _ "
    " / _` |"
    "| (_| |"
    " \\__, |"
    "  __/ |"
    " |___/ "],
   :i 71,
   :character "g",
   :width 7,
   :height 8},
  "h"
  {:bands
   [" _     "
    "| |    "
    "| |__  "
    "| '_ \\ "
    "| | | |"
    "|_| |_|"
    "       "
    "       "],
   :i 72,
   :character "h",
   :width 7,
   :height 8},
  "i"
  {:bands [" _ " "(_)" " _ " "| |" "| |" "|_|" "   " "   "],
   :i 73,
   :character "i",
   :width 3,
   :height 8},
  "j"
  {:bands
   ["   _ " "  (_)" "   _ " "  | |" "  | |" "  | |" " _/ |" "|__/ "],
   :i 74,
   :character "j",
   :width 5,
   :height 8},
  "k"
  {:bands
   [" _    "
    "| |   "
    "| | __"
    "| |/ /"
    "|   < "
    "|_|\\_\\"
    "      "
    "      "],
   :i 75,
   :character "k",
   :width 6,
   :height 8},
  "l"
  {:bands [" _ " "| |" "| |" "| |" "| |" "|_|" "   " "   "],
   :i 76,
   :character "l",
   :width 3,
   :height 8},
  "m"
  {:bands
   ["           "
    "           "
    " _ __ ___  "
    "| '_ ` _ \\ "
    "| | | | | |"
    "|_| |_| |_|"
    "           "
    "           "],
   :i 77,
   :character "m",
   :width 11,
   :height 8},
  "n"
  {:bands
   ["       "
    "       "
    " _ __  "
    "| '_ \\ "
    "| | | |"
    "|_| |_|"
    "       "
    "       "],
   :i 78,
   :character "n",
   :width 7,
   :height 8},
  "o"
  {:bands
   ["       "
    "       "
    "  ___  "
    " / _ \\ "
    "| (_) |"
    " \\___/ "
    "       "
    "       "],
   :i 79,
   :character "o",
   :width 7,
   :height 8},
  "p"
  {:bands
   ["       "
    "       "
    " _ __  "
    "| '_ \\ "
    "| |_) |"
    "| .__/ "
    "| |    "
    "|_|    "],
   :i 80,
   :character "p",
   :width 7,
   :height 8},
  "q"
  {:bands
   ["       "
    "       "
    "  __ _ "
    " / _` |"
    "| (_| |"
    " \\__, |"
    "    | |"
    "    |_|"],
   :i 81,
   :character "q",
   :width 7,
   :height 8},
  "r"
  {:bands
   ["      "
    "      "
    " _ __ "
    "| '__|"
    "| |   "
    "|_|   "
    "      "
    "      "],
   :i 82,
   :character "r",
   :width 6,
   :height 8},
  "s"
  {:bands
   ["     " "     " " ___ " "/ __|" "\\__ \\" "|___/" "     " "     "],
   :i 83,
   :character "s",
   :width 5,
   :height 8},
  "t"
  {:bands
   [" _   " "| |  " "| |_ " "| __|" "| |_ " " \\__|" "     " "     "],
   :i 84,
   :character "t",
   :width 5,
   :height 8},
  "u"
  {:bands
   ["       "
    "       "
    " _   _ "
    "| | | |"
    "| |_| |"
    " \\__,_|"
    "       "
    "       "],
   :i 85,
   :character "u",
   :width 7,
   :height 8},
  "v"
  {:bands
   ["       "
    "       "
    "__   __"
    "\\ \\ / /"
    " \\ V / "
    "  \\_/  "
    "       "
    "       "],
   :i 86,
   :character "v",
   :width 7,
   :height 8},
  "w"
  {:bands
   ["          "
    "          "
    "__      __"
    "\\ \\ /\\ / /"
    " \\ V  V / "
    "  \\_/\\_/  "
    "          "
    "          "],
   :i 87,
   :character "w",
   :width 10,
   :height 8},
  "x"
  {:bands
   ["      "
    "      "
    "__  __"
    "\\ \\/ /"
    " >  < "
    "/_/\\_\\"
    "      "
    "      "],
   :i 88,
   :character "x",
   :width 6,
   :height 8},
  "y"
  {:bands
   ["       "
    "       "
    " _   _ "
    "| | | |"
    "| |_| |"
    " \\__, |"
    "  __/ |"
    " |___/ "],
   :i 89,
   :character "y",
   :width 7,
   :height 8},
  "z"
  {:bands
   ["     " "     " " ____" "|_  /" " / / " "/___|" "     " "     "],
   :i 90,
   :character "z",
   :width 5,
   :height 8},
  "{"
  {:bands
   ["   __"
    "  / /"
    " | | "
    "/ /  "
    "\\ \\  "
    " | | "
    "  \\_\\"
    "     "],
   :i 91,
   :character "{",
   :width 5,
   :height 8},
  "|"
  {:bands [" _ " "| |" "| |" "| |" "| |" "| |" "| |" "|_|"],
   :i 92,
   :character "|",
   :width 3,
   :height 8},
  "}"
  {:bands
   ["__   "
    "\\ \\  "
    " | | "
    "  \\ \\"
    "  / /"
    " | | "
    "/_/  "
    "     "],
   :i 93,
   :character "}",
   :width 5,
   :height 8},
  "~"
  {:bands
   [" /\\/|" "|/\\/ " "     " "     " "     " "     " "     " "     "],
   :i 94,
   :character "~",
   :width 5,
   :height 8},
  "Ä"
  {:bands
   ["  _   _  "
    " (_)_(_) "
    "   / \\   "
    "  / _ \\  "
    " / ___ \\ "
    "/_/   \\_\\"
    "         "
    "         "],
   :i 95,
   :character "Ä",
   :width 9,
   :height 8},
  "Ö"
  {:bands
   [" _   _ "
    "(_)_(_)"
    " / _ \\ "
    "| | | |"
    "| |_| |"
    " \\___/ "
    "       "
    "       "],
   :i 96,
   :character "Ö",
   :width 7,
   :height 8},
  "Ü"
  {:bands
   [" _   _ "
    "(_) (_)"
    "| | | |"
    "| | | |"
    "| |_| |"
    " \\___/ "
    "       "
    "       "],
   :i 97,
   :character "Ü",
   :width 7,
   :height 8},
  "ä"
  {:bands
   [" _   _ "
    "(_) (_)"
    "  __ _ "
    " / _` |"
    "| (_| |"
    " \\__,_|"
    "       "
    "       "],
   :i 98,
   :character "ä",
   :width 7,
   :height 8},
  "ö"
  {:bands
   [" _   _ "
    "(_) (_)"
    "  ___  "
    " / _ \\ "
    "| (_) |"
    " \\___/ "
    "       "
    "       "],
   :i 99,
   :character "ö",
   :width 7,
   :height 8},
  "ü"
  {:bands
   [" _   _ "
    "(_) (_)"
    " _   _ "
    "| | | |"
    "| |_| |"
    " \\__,_|"
    "       "
    "       "],
   :i 100,
   :character "ü",
   :width 7,
   :height 8},
  "ß"
  {:bands
   ["  ___  "
    " / _ \\ "
    "| | ) |"
    "| |< < "
    "| | ) |"
    "| ||_/ "
    "|_|    "
    "       "],
   :i 101,
   :character "ß",
   :width 7,
   :height 8}}})


;;  $$$$$$$\  $$\                $$\      $$\
;;  $$  __$$\ \__|               $$$\    $$$ |
;;  $$ |  $$ |$$\  $$$$$$\       $$$$\  $$$$ | $$$$$$\  $$$$$$$\   $$$$$$\  $$\   $$\
;;  $$$$$$$\ |$$ |$$  __$$\      $$\$$\$$ $$ |$$  __$$\ $$  __$$\ $$  __$$\ $$ |  $$ |
;;  $$  __$$\ $$ |$$ /  $$ |     $$ \$$$  $$ |$$ /  $$ |$$ |  $$ |$$$$$$$$ |$$ |  $$ |
;;  $$ |  $$ |$$ |$$ |  $$ |     $$ |\$  /$$ |$$ |  $$ |$$ |  $$ |$$   ____|$$ |  $$ |
;;  $$$$$$$  |$$ |\$$$$$$$ |     $$ | \_/ $$ |\$$$$$$  |$$ |  $$ |\$$$$$$$\ \$$$$$$$ |
;;  \_______/ \__| \____$$ |     \__|     \__| \______/ \__|  \__| \_______| \____$$ |
;;                $$\   $$ |                                                $$\   $$ |
;;                \$$$$$$  |                                                \$$$$$$  |
;;                 \______/                                                  \______/

(def big-money
'{:font-name "Big Money",
 :example
 ["$$$$$$$\\  $$\\                $$\\      $$\\"
  "$$  __$$\\ \\__|               $$$\\    $$$ |"
  "$$ |  $$ |$$\\  $$$$$$\\       $$$$\\  $$$$ | $$$$$$\\  $$$$$$$\\   $$$$$$\\  $$\\   $$\\"
  "$$$$$$$\\ |$$ |$$  __$$\\      $$\\$$\\$$ $$ |$$  __$$\\ $$  __$$\\ $$  __$$\\ $$ |  $$ |"
  "$$  __$$\\ $$ |$$ /  $$ |     $$ \\$$$  $$ |$$ /  $$ |$$ |  $$ |$$$$$$$$ |$$ |  $$ |"
  "$$ |  $$ |$$ |$$ |  $$ |     $$ |\\$  /$$ |$$ |  $$ |$$ |  $$ |$$   ____|$$ |  $$ |"
  "$$$$$$$  |$$ |\\$$$$$$$ |     $$ | \\_/ $$ |\\$$$$$$  |$$ |  $$ |\\$$$$$$$\\ \\$$$$$$$ |"
  "\\_______/ \\__| \\____$$ |     \\__|     \\__| \\______/ \\__|  \\__| \\_______| \\____$$ |"
  "              $$\\   $$ |                                                $$\\   $$ |"
  "              \\$$$$$$  |                                                \\$$$$$$  |"
  "               \\______/                                                  \\______/"],
 :desc
 "bigmoney-nw : by nathan bloomfield (xzovik@gmail.com)\n  based on art from the legendary MAKEMONEYFAST chain letter\n\n  History:\n  5-30-2007 : first version (required characters only)",
 :example-text "Money",
 :font-sym big-money,
 :widest-char "@",
 :char-height 11,
 :max-char-width 16,
 :missing-chars [],
 :chars-array-map
 {" "
  {:bands
   ["     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "
    "     "],
   :i 0,
   :character " ",
   :width 5,
   :height 11},
  "!"
  {:bands
   ["$$\\ "
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
   :i 1,
   :character "!",
   :width 4,
   :height 11},
  "\""
  {:bands
   ["$$\\ $$\\ "
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
   :i 2,
   :character "\"",
   :width 8,
   :height 11},
  "#"
  {:bands
   ["  $$\\ $$\\   "
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
   :i 3,
   :character "#",
   :width 12,
   :height 11},
  "$"
  {:bands
   ["   $$\\    "
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
   :i 4,
   :character "$",
   :width 10,
   :height 11},
  "%"
  {:bands
   ["$$\\   $$\\ "
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
   :i 5,
   :character "%",
   :width 10,
   :height 11},
  "&"
  {:bands
   [" $$$\\     "
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
   :i 6,
   :character "&",
   :width 10,
   :height 11},
  "'"
  {:bands
   ["$$\\ "
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
   :i 7,
   :character "'",
   :width 4,
   :height 11},
  "("
  {:bands
   ["  $$$\\ "
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
   :i 8,
   :character "(",
   :width 7,
   :height 11},
  ")"
  {:bands
   ["$$$\\   "
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
   :i 9,
   :character ")",
   :width 7,
   :height 11},
  "*"
  {:bands
   ["         "
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
   :i 10,
   :character "*",
   :width 9,
   :height 11},
  "+"
  {:bands
   ["          "
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
   :i 11,
   :character "+",
   :width 10,
   :height 11},
  ","
  {:bands
   ["    "
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
   :i 12,
   :character ",",
   :width 4,
   :height 11},
  "-"
  {:bands
   ["        "
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
   :i 13,
   :character "-",
   :width 8,
   :height 11},
  "."
  {:bands
   ["    "
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
   :i 14,
   :character ".",
   :width 4,
   :height 11},
  "/"
  {:bands
   ["      $$\\ "
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
   :i 15,
   :character "/",
   :width 10,
   :height 11},
  "0"
  {:bands
   [" $$$$$$\\  "
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
   :i 16,
   :character "0",
   :width 10,
   :height 11},
  "1"
  {:bands
   ["  $$\\   "
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
   :i 17,
   :character "1",
   :width 8,
   :height 11},
  "2"
  {:bands
   [" $$$$$$\\  "
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
   :i 18,
   :character "2",
   :width 10,
   :height 11},
  "3"
  {:bands
   [" $$$$$$\\  "
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
   :i 19,
   :character "3",
   :width 10,
   :height 11},
  "4"
  {:bands
   ["$$\\   $$\\ "
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
   :i 20,
   :character "4",
   :width 10,
   :height 11},
  "5"
  {:bands
   ["$$$$$$$\\  "
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
   :i 21,
   :character "5",
   :width 10,
   :height 11},
  "6"
  {:bands
   [" $$$$$$\\  "
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
   :i 22,
   :character "6",
   :width 10,
   :height 11},
  "7"
  {:bands
   ["$$$$$$$$\\ "
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
   :i 23,
   :character "7",
   :width 10,
   :height 11},
  "8"
  {:bands
   [" $$$$$$\\  "
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
   :i 24,
   :character "8",
   :width 10,
   :height 11},
  "9"
  {:bands
   [" $$$$$$\\  "
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
   :i 25,
   :character "9",
   :width 10,
   :height 11},
  ":"
  {:bands
   ["    "
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
   :i 26,
   :character ":",
   :width 4,
   :height 11},
  ";"
  {:bands
   ["    "
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
   :i 27,
   :character ";",
   :width 4,
   :height 11},
  "<"
  {:bands
   ["   $$\\ "
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
   :i 28,
   :character "<",
   :width 7,
   :height 11},
  "="
  {:bands
   ["      "
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
   :i 29,
   :character "=",
   :width 6,
   :height 11},
  ">"
  {:bands
   ["$$\\    "
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
   :i 30,
   :character ">",
   :width 7,
   :height 11},
  "?"
  {:bands
   [" $$$$\\  "
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
   :i 31,
   :character "?",
   :width 8,
   :height 11},
  "@"
  {:bands
   ["    $$$$$$\\     "
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
   :i 32,
   :character "@",
   :width 16,
   :height 11},
  "A"
  {:bands
   [" $$$$$$\\  "
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
   :i 33,
   :character "A",
   :width 10,
   :height 11},
  "B"
  {:bands
   ["$$$$$$$\\  "
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
   :i 34,
   :character "B",
   :width 10,
   :height 11},
  "C"
  {:bands
   [" $$$$$$\\  "
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
   :i 35,
   :character "C",
   :width 10,
   :height 11},
  "D"
  {:bands
   ["$$$$$$$\\  "
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
   :i 36,
   :character "D",
   :width 10,
   :height 11},
  "E"
  {:bands
   ["$$$$$$$$\\ "
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
   :i 37,
   :character "E",
   :width 10,
   :height 11},
  "F"
  {:bands
   ["$$$$$$$$\\ "
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
   :i 38,
   :character "F",
   :width 10,
   :height 11},
  "G"
  {:bands
   [" $$$$$$\\  "
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
   :i 39,
   :character "G",
   :width 10,
   :height 11},
  "H"
  {:bands
   ["$$\\   $$\\ "
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
   :i 40,
   :character "H",
   :width 10,
   :height 11},
  "I"
  {:bands
   ["$$$$$$\\ "
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
   :i 41,
   :character "I",
   :width 8,
   :height 11},
  "J"
  {:bands
   ["   $$$$$\\ "
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
   :i 42,
   :character "J",
   :width 10,
   :height 11},
  "K"
  {:bands
   ["$$\\   $$\\ "
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
   :i 43,
   :character "K",
   :width 10,
   :height 11},
  "L"
  {:bands
   ["$$\\       "
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
   :i 44,
   :character "L",
   :width 10,
   :height 11},
  "M"
  {:bands
   ["$$\\      $$\\ "
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
   :i 45,
   :character "M",
   :width 13,
   :height 11},
  "N"
  {:bands
   ["$$\\   $$\\ "
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
   :i 46,
   :character "N",
   :width 10,
   :height 11},
  "O"
  {:bands
   [" $$$$$$\\  "
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
   :i 47,
   :character "O",
   :width 10,
   :height 11},
  "P"
  {:bands
   ["$$$$$$$\\  "
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
   :i 48,
   :character "P",
   :width 10,
   :height 11},
  "Q"
  {:bands
   [" $$$$$$\\  "
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
   :i 49,
   :character "Q",
   :width 10,
   :height 11},
  "R"
  {:bands
   ["$$$$$$$\\  "
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
   :i 50,
   :character "R",
   :width 10,
   :height 11},
  "S"
  {:bands
   [" $$$$$$\\  "
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
   :i 51,
   :character "S",
   :width 10,
   :height 11},
  "T"
  {:bands
   ["$$$$$$$$\\ "
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
   :i 52,
   :character "T",
   :width 10,
   :height 11},
  "U"
  {:bands
   ["$$\\   $$\\ "
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
   :i 53,
   :character "U",
   :width 10,
   :height 11},
  "V"
  {:bands
   ["$$\\    $$\\ "
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
   :i 54,
   :character "V",
   :width 11,
   :height 11},
  "W"
  {:bands
   ["$$\\      $$\\ "
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
   :i 55,
   :character "W",
   :width 13,
   :height 11},
  "X"
  {:bands
   ["$$\\   $$\\ "
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
   :i 56,
   :character "X",
   :width 10,
   :height 11},
  "Y"
  {:bands
   ["$$\\     $$\\ "
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
   :i 57,
   :character "Y",
   :width 12,
   :height 11},
  "Z"
  {:bands
   ["$$$$$$$$\\ "
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
   :i 58,
   :character "Z",
   :width 10,
   :height 11},
  "["
  {:bands
   ["$$$$\\ "
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
   :i 59,
   :character "[",
   :width 6,
   :height 11},
  "\\"
  {:bands
   ["$$\\       "
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
   :i 60,
   :character "\\",
   :width 10,
   :height 11},
  "]"
  {:bands
   ["$$$$\\ "
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
   :i 61,
   :character "]",
   :width 6,
   :height 11},
  "^"
  {:bands
   ["   $\\    "
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
   :i 62,
   :character "^",
   :width 9,
   :height 11},
  "_"
  {:bands
   ["        "
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
   :i 63,
   :character "_",
   :width 8,
   :height 11},
  "`"
  {:bands
   ["$$\\ "
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
   :i 64,
   :character "`",
   :width 4,
   :height 11},
  "a"
  {:bands
   ["          "
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
   :i 65,
   :character "a",
   :width 10,
   :height 11},
  "b"
  {:bands
   ["$$\\       "
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
   :i 66,
   :character "b",
   :width 10,
   :height 11},
  "c"
  {:bands
   ["          "
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
   :i 67,
   :character "c",
   :width 10,
   :height 11},
  "d"
  {:bands
   ["      $$\\ "
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
   :i 68,
   :character "d",
   :width 10,
   :height 11},
  "e"
  {:bands
   ["          "
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
   :i 69,
   :character "e",
   :width 10,
   :height 11},
  "f"
  {:bands
   [" $$$$$$\\  "
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
   :i 70,
   :character "f",
   :width 10,
   :height 11},
  "g"
  {:bands
   ["          "
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
   :i 71,
   :character "g",
   :width 10,
   :height 11},
  "h"
  {:bands
   ["$$\\       "
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
   :i 72,
   :character "h",
   :width 10,
   :height 11},
  "i"
  {:bands
   ["$$\\ "
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
   :i 73,
   :character "i",
   :width 4,
   :height 11},
  "j"
  {:bands
   ["          "
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
   :i 74,
   :character "j",
   :width 10,
   :height 11},
  "k"
  {:bands
   ["$$\\       "
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
   :i 75,
   :character "k",
   :width 10,
   :height 11},
  "l"
  {:bands
   ["$$\\ "
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
   :i 76,
   :character "l",
   :width 4,
   :height 11},
  "m"
  {:bands
   ["              "
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
   :i 77,
   :character "m",
   :width 14,
   :height 11},
  "n"
  {:bands
   ["          "
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
   :i 78,
   :character "n",
   :width 10,
   :height 11},
  "o"
  {:bands
   ["          "
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
   :i 79,
   :character "o",
   :width 10,
   :height 11},
  "p"
  {:bands
   ["          "
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
   :i 80,
   :character "p",
   :width 10,
   :height 11},
  "q"
  {:bands
   ["          "
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
   :i 81,
   :character "q",
   :width 10,
   :height 11},
  "r"
  {:bands
   ["          "
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
   :i 82,
   :character "r",
   :width 10,
   :height 11},
  "s"
  {:bands
   ["          "
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
   :i 83,
   :character "s",
   :width 10,
   :height 11},
  "t"
  {:bands
   ["  $$\\     "
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
   :i 84,
   :character "t",
   :width 10,
   :height 11},
  "u"
  {:bands
   ["          "
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
   :i 85,
   :character "u",
   :width 10,
   :height 11},
  "v"
  {:bands
   ["           "
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
   :i 86,
   :character "v",
   :width 11,
   :height 11},
  "w"
  {:bands
   ["              "
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
   :i 87,
   :character "w",
   :width 14,
   :height 11},
  "x"
  {:bands
   ["          "
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
   :i 88,
   :character "x",
   :width 10,
   :height 11},
  "y"
  {:bands
   ["          "
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
   :i 89,
   :character "y",
   :width 10,
   :height 11},
  "z"
  {:bands
   ["          "
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
   :i 90,
   :character "z",
   :width 10,
   :height 11},
  "{"
  {:bands
   ["  $$$\\ "
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
   :i 91,
   :character "{",
   :width 7,
   :height 11},
  "|"
  {:bands
   ["$$\\ "
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
   :i 92,
   :character "|",
   :width 4,
   :height 11},
  "}"
  {:bands
   ["$$$\\   "
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
   :i 93,
   :character "}",
   :width 7,
   :height 11},
  "~"
  {:bands
   [" $$$\\ $$\\ "
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
   :i 94,
   :character "~",
   :width 10,
   :height 11},
  "Ä"
  {:bands
   ["  $\\ $\\   "
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
   :i 95,
   :character "Ä",
   :width 10,
   :height 11},
  "Ö"
  {:bands
   ["  $\\ $\\   "
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
   :i 96,
   :character "Ö",
   :width 10,
   :height 11},
  "Ü"
  {:bands
   ["  $\\ $\\   "
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
   :i 97,
   :character "Ü",
   :width 10,
   :height 11},
  "ä"
  {:bands
   ["  $\\ $\\   "
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
   :i 98,
   :character "ä",
   :width 10,
   :height 11},
  "ö"
  {:bands
   ["  $\\ $\\   "
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
   :i 99,
   :character "ö",
   :width 10,
   :height 11},
  "ü"
  {:bands
   ["  $\\ $\\   "
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
   :i 100,
   :character "ü",
   :width 10,
   :height 11},
  "ß"
  {:bands
   [" $$$$$$\\  "
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
   :i 101,
   :character "ß",
   :width 10,
   :height 11}}})


;;   ______                            _             _
;;  (_____ \                          | |           | |
;;   _____) )  ___   _   _  ____    __| | _____   __| |
;;  |  __  /  / _ \ | | | ||  _ \  / _  || ___ | / _  |
;;  | |  \ \ | |_| || |_| || | | |( (_| || ____|( (_| |
;;  |_|   |_| \___/ |____/ |_| |_| \____||_____) \____|
;;  
;;   ______   _______  _     _  _______  ______   _______  ______
;;  (_____ \ (_______)(_)   (_)(_______)(______) (_______)(______)
;;   _____) ) _     _  _     _  _     _  _     _  _____    _     _
;;  |  __  / | |   | || |   | || |   | || |   | ||  ___)  | |   | |
;;  | |  \ \ | |___| || |___| || |   | || |__/ / | |_____ | |__/ /
;;  |_|   |_| \_____/  \_____/ |_|   |_||_____/  |_______)|_____/

(def rounded
'{:font-name "Rounded",
 :example
 [" ______                            _             _"
  "(_____ \\                          | |           | |"
  " _____) )  ___   _   _  ____    __| | _____   __| |"
  "|  __  /  / _ \\ | | | ||  _ \\  / _  || ___ | / _  |"
  "| |  \\ \\ | |_| || |_| || | | |( (_| || ____|( (_| |"
  "|_|   |_| \\___/ |____/ |_| |_| \\____||_____) \\____|"
  ""
  " ______   _______  _     _  _______  ______   _______  ______"
  "(_____ \\ (_______)(_)   (_)(_______)(______) (_______)(______)"
  " _____) ) _     _  _     _  _     _  _     _  _____    _     _"
  "|  __  / | |   | || |   | || |   | || |   | ||  ___)  | |   | |"
  "| |  \\ \\ | |___| || |___| || |   | || |__/ / | |_____ | |__/ /"
  "|_|   |_| \\_____/  \\_____/ |_|   |_||_____/  |_______)|_____/"],
 :desc
 "Rounded by Nick Miners N.M.Miners@durham.ac.uk\n        May 1994",
 :example-text "Rounded",
 :font-sym rounded,
 :widest-char "#",
 :char-height 7,
 :max-char-width 9,
 :missing-chars [],
 :chars-array-map
 {" "
  {:bands ["    " "    " "    " "    " "    " "    " "    "],
   :i 0,
   :character " ",
   :width 4,
   :height 7},
  "!"
  {:bands [" _ " "| |" "| |" "|_|" " _ " "|_|" "   "],
   :i 1,
   :character "!",
   :width 3,
   :height 7},
  "\""
  {:bands
   [" _  _ " "( )( )" "|/  \\|" "      " "      " "      " "      "],
   :i 2,
   :character "\"",
   :width 6,
   :height 7},
  "#"
  {:bands
   ["   _ _   "
    " _| U |_ "
    "(_     _)"
    " _| O |_ "
    "(_     _)"
    "  |_n_|  "
    "         "],
   :i 3,
   :character "#",
   :width 9,
   :height 7},
  "$"
  {:bands
   ["   _   "
    " _| |_ "
    "|  ___)"
    "|___  |"
    "(_   _|"
    "  |_|  "
    "       "],
   :i 4,
   :character "$",
   :width 7,
   :height 7},
  "%"
  {:bands
   [" _   _ "
    "(_) | |"
    "   / / "
    "  / /  "
    " / / _ "
    "|_| (_)"
    "       "],
   :i 5,
   :character "%",
   :width 7,
   :height 7},
  "&"
  {:bands
   ["  ___   "
    " / _ \\  "
    "( (_) ) "
    " ) _ (  "
    "( (/  \\ "
    " \\__/\\_)"
    "        "],
   :i 6,
   :character "&",
   :width 8,
   :height 7},
  "'"
  {:bands [" _ " "( )" "|/ " "   " "   " "   " "   "],
   :i 7,
   :character "'",
   :width 3,
   :height 7},
  "("
  {:bands ["  _ " " / )" "| | " "| | " "| | " " \\_)" "    "],
   :i 8,
   :character "(",
   :width 4,
   :height 7},
  ")"
  {:bands [" _  " "( \\ " " | |" " | |" " | |" "(_/ " "    "],
   :i 9,
   :character ")",
   :width 4,
   :height 7},
  "*"
  {:bands
   ["    _    "
    " _ | | _ "
    "( \\| |/ )"
    " )     ( "
    "(_/| |\\_)"
    "   |_|   "
    "         "],
   :i 10,
   :character "*",
   :width 9,
   :height 7},
  "+"
  {:bands
   ["       "
    "   _   "
    " _| |_ "
    "(_   _)"
    "  |_|  "
    "       "
    "       "],
   :i 11,
   :character "+",
   :width 7,
   :height 7},
  ","
  {:bands ["   " "   " "   " "   " " _ " "( )" "|/ "],
   :i 12,
   :character ",",
   :width 3,
   :height 7},
  "-"
  {:bands
   ["       "
    "       "
    " _____ "
    "(_____)"
    "       "
    "       "
    "       "],
   :i 13,
   :character "-",
   :width 7,
   :height 7},
  "."
  {:bands ["   " "   " "   " "   " " _ " "(_)" "   "],
   :i 14,
   :character ".",
   :width 3,
   :height 7},
  "/"
  {:bands
   ["     _ "
    "    | |"
    "   / / "
    "  / /  "
    " / /   "
    "|_|    "
    "       "],
   :i 15,
   :character "/",
   :width 7,
   :height 7},
  "0"
  {:bands
   ["  _____  "
    " (_____) "
    " _  __ _ "
    "| |/ /| |"
    "|   /_| |"
    " \\_____/ "
    "         "],
   :i 16,
   :character "0",
   :width 9,
   :height 7},
  "1"
  {:bands
   ["  ___   "
    " (___)  "
    "    _   "
    "   | |  "
    "  _| |_ "
    " (_____)"
    "        "],
   :i 17,
   :character "1",
   :width 8,
   :height 7},
  "2"
  {:bands
   [" ______  "
    "(_____ \\ "
    "  ____) )"
    " / ____/ "
    "| (_____ "
    "|_______)"
    "         "],
   :i 18,
   :character "2",
   :width 9,
   :height 7},
  "3"
  {:bands
   [" ______  "
    "(_____ \\ "
    " _____) )"
    "(_____ ( "
    " _____) )"
    "(______/ "
    "         "],
   :i 19,
   :character "3",
   :width 9,
   :height 7},
  "4"
  {:bands
   [" _     _ "
    "| |   (_)"
    "| |_____ "
    "|_____  |"
    "      | |"
    "      |_|"
    "         "],
   :i 20,
   :character "4",
   :width 9,
   :height 7},
  "5"
  {:bands
   [" _______ "
    "(_______)"
    " ______  "
    "(_____ \\ "
    " _____) )"
    "(______/ "
    "         "],
   :i 21,
   :character "5",
   :width 9,
   :height 7},
  "6"
  {:bands
   [" _______ "
    "(_______)"
    " ______  "
    "|  ___ \\ "
    "| |___) )"
    "|______/ "
    "         "],
   :i 22,
   :character "6",
   :width 9,
   :height 7},
  "7"
  {:bands
   [" _______ "
    "(_______)"
    "      _  "
    "     / ) "
    "    / /  "
    "   (_/   "
    "         "],
   :i 23,
   :character "7",
   :width 9,
   :height 7},
  "8"
  {:bands
   ["  _____  "
    " (_____) "
    "  _____  "
    " / ___ \\ "
    "( (___) )"
    " \\_____/ "
    "         "],
   :i 24,
   :character "8",
   :width 9,
   :height 7},
  "9"
  {:bands
   [" _______ "
    "(_______)"
    " _______ "
    "(_____  |"
    "      | |"
    "      |_|"
    "         "],
   :i 25,
   :character "9",
   :width 9,
   :height 7},
  ":"
  {:bands ["   " "   " " _ " "(_)" " _ " "(_)" "   "],
   :i 26,
   :character ":",
   :width 3,
   :height 7},
  ";"
  {:bands ["   " "   " " _ " "(_)" " _ " "( )" "|/ "],
   :i 27,
   :character ";",
   :width 3,
   :height 7},
  "<"
  {:bands ["    " "  _ " " / )" "( ( " " \\_)" "    " "    "],
   :i 28,
   :character "<",
   :width 4,
   :height 7},
  "="
  {:bands
   ["       "
    " _____ "
    "(_____)"
    " _____ "
    "(_____)"
    "       "
    "       "],
   :i 29,
   :character "=",
   :width 7,
   :height 7},
  ">"
  {:bands ["    " " _  " "( \\ " " ) )" "(_/ " "    " "    "],
   :i 30,
   :character ">",
   :width 4,
   :height 7},
  "?"
  {:bands
   ["  ___  "
    " / _ \\ "
    "(_( ) )"
    "   (_/ "
    "   _   "
    "  (_)  "
    "       "],
   :i 31,
   :character "?",
   :width 7,
   :height 7},
  "@"
  {:bands
   ["  _____  "
    " / __  \\ "
    "| | /   )"
    "| | \\__/ "
    "| |____  "
    " \\_____) "
    "         "],
   :i 32,
   :character "@",
   :width 9,
   :height 7},
  "A"
  {:bands
   [" _______ "
    "(_______)"
    " _______ "
    "|  ___  |"
    "| |   | |"
    "|_|   |_|"
    "         "],
   :i 33,
   :character "A",
   :width 9,
   :height 7},
  "B"
  {:bands
   [" ______  "
    "(____  \\ "
    " ____)  )"
    "|  __  ( "
    "| |__)  )"
    "|______/ "
    "         "],
   :i 34,
   :character "B",
   :width 9,
   :height 7},
  "C"
  {:bands
   [" _______ "
    "(_______)"
    " _       "
    "| |      "
    "| |_____ "
    " \\______)"
    "         "],
   :i 35,
   :character "C",
   :width 9,
   :height 7},
  "D"
  {:bands
   [" ______  "
    "(______) "
    " _     _ "
    "| |   | |"
    "| |__/ / "
    "|_____/  "
    "         "],
   :i 36,
   :character "D",
   :width 9,
   :height 7},
  "E"
  {:bands
   [" _______ "
    "(_______)"
    " _____   "
    "|  ___)  "
    "| |_____ "
    "|_______)"
    "         "],
   :i 37,
   :character "E",
   :width 9,
   :height 7},
  "F"
  {:bands
   [" _______ "
    "(_______)"
    " _____   "
    "|  ___)  "
    "| |      "
    "|_|      "
    "         "],
   :i 38,
   :character "F",
   :width 9,
   :height 7},
  "G"
  {:bands
   [" _______ "
    "(_______)"
    " _   ___ "
    "| | (_  |"
    "| |___) |"
    " \\_____/ "
    "         "],
   :i 39,
   :character "G",
   :width 9,
   :height 7},
  "H"
  {:bands
   [" _     _ "
    "(_)   (_)"
    " _______ "
    "|  ___  |"
    "| |   | |"
    "|_|   |_|"
    "         "],
   :i 40,
   :character "H",
   :width 9,
   :height 7},
  "I"
  {:bands [" _ " "| |" "| |" "| |" "| |" "|_|" "   "],
   :i 41,
   :character "I",
   :width 3,
   :height 7},
  "J"
  {:bands
   [" _______ "
    "(_______)"
    "     _   "
    " _  | |  "
    "| |_| |  "
    " \\___/   "
    "         "],
   :i 42,
   :character "J",
   :width 9,
   :height 7},
  "K"
  {:bands
   [" _     _ "
    "(_)   | |"
    " _____| |"
    "|  _   _)"
    "| |  \\ \\ "
    "|_|   \\_)"
    "         "],
   :i 43,
   :character "K",
   :width 9,
   :height 7},
  "L"
  {:bands
   [" _       "
    "(_)      "
    " _       "
    "| |      "
    "| |_____ "
    "|_______)"
    "         "],
   :i 44,
   :character "L",
   :width 9,
   :height 7},
  "M"
  {:bands
   [" _______ "
    "(_______)"
    " _  _  _ "
    "| ||_|| |"
    "| |   | |"
    "|_|   |_|"
    "         "],
   :i 45,
   :character "M",
   :width 9,
   :height 7},
  "N"
  {:bands
   [" _______ "
    "(_______)"
    " _     _ "
    "| |   | |"
    "| |   | |"
    "|_|   |_|"
    "         "],
   :i 46,
   :character "N",
   :width 9,
   :height 7},
  "O"
  {:bands
   [" _______ "
    "(_______)"
    " _     _ "
    "| |   | |"
    "| |___| |"
    " \\_____/ "
    "         "],
   :i 47,
   :character "O",
   :width 9,
   :height 7},
  "P"
  {:bands
   [" ______  "
    "(_____ \\ "
    " _____) )"
    "|  ____/ "
    "| |      "
    "|_|      "
    "         "],
   :i 48,
   :character "P",
   :width 9,
   :height 7},
  "Q"
  {:bands
   [" _______ "
    "(_______)"
    " _    _  "
    "| |  | | "
    "| |__| | "
    " \\______)"
    "         "],
   :i 49,
   :character "Q",
   :width 9,
   :height 7},
  "R"
  {:bands
   [" ______  "
    "(_____ \\ "
    " _____) )"
    "|  __  / "
    "| |  \\ \\ "
    "|_|   |_|"
    "         "],
   :i 50,
   :character "R",
   :width 9,
   :height 7},
  "S"
  {:bands
   ["  ______ "
    " / _____)"
    "( (____  "
    " \\____ \\ "
    " _____) )"
    "(______/ "
    "         "],
   :i 51,
   :character "S",
   :width 9,
   :height 7},
  "T"
  {:bands
   [" _______ "
    "(_______)"
    "    _    "
    "   | |   "
    "   | |   "
    "   |_|   "
    "         "],
   :i 52,
   :character "T",
   :width 9,
   :height 7},
  "U"
  {:bands
   [" _     _ "
    "(_)   (_)"
    " _     _ "
    "| |   | |"
    "| |___| |"
    " \\_____/ "
    "         "],
   :i 53,
   :character "U",
   :width 9,
   :height 7},
  "V"
  {:bands
   [" _     _ "
    "(_)   (_)"
    " _     _ "
    "| |   | |"
    " \\ \\ / / "
    "  \\___/  "
    "         "],
   :i 54,
   :character "V",
   :width 9,
   :height 7},
  "W"
  {:bands
   [" _  _  _ "
    "(_)(_)(_)"
    " _  _  _ "
    "| || || |"
    "| || || |"
    " \\_____/ "
    "         "],
   :i 55,
   :character "W",
   :width 9,
   :height 7},
  "X"
  {:bands
   [" _     _ "
    "(_)   (_)"
    "   ___   "
    "  |   |  "
    " / / \\ \\ "
    "|_|   |_|"
    "         "],
   :i 56,
   :character "X",
   :width 9,
   :height 7},
  "Y"
  {:bands
   [" _     _ "
    "| |   | |"
    "| |___| |"
    "|_____  |"
    " _____| |"
    "(_______|"
    "         "],
   :i 57,
   :character "Y",
   :width 9,
   :height 7},
  "Z"
  {:bands
   [" _______ "
    "(_______)"
    "   __    "
    "  / /    "
    " / /____ "
    "(_______)"
    "         "],
   :i 58,
   :character "Z",
   :width 9,
   :height 7},
  "["
  {:bands [" ___ " "|  _)" "| |  " "| |  " "| |_ " "|___)" "     "],
   :i 59,
   :character "[",
   :width 5,
   :height 7},
  "\\"
  {:bands
   [" _     "
    "| |    "
    " \\ \\   "
    "  \\ \\  "
    "   \\ \\ "
    "    |_|"
    "       "],
   :i 60,
   :character "\\",
   :width 7,
   :height 7},
  "]"
  {:bands [" ___ " "(_  |" "  | |" "  | |" " _| |" "(___|" "     "],
   :i 61,
   :character "]",
   :width 5,
   :height 7},
  "^"
  {:bands
   ["   __  "
    "  /  \\ "
    " (_/\\_)"
    "       "
    "       "
    "       "
    "       "],
   :i 62,
   :character "^",
   :width 7,
   :height 7},
  "_"
  {:bands
   ["         "
    "         "
    "         "
    "         "
    " _______ "
    "(_______)"
    "         "],
   :i 63,
   :character "_",
   :width 9,
   :height 7},
  "`"
  {:bands [" _ " "( )" " \\|" "   " "   " "   " "   "],
   :i 64,
   :character "`",
   :width 3,
   :height 7},
  "a"
  {:bands
   ["       "
    "       "
    " _____ "
    "(____ |"
    "/ ___ |"
    "\\_____|"
    "       "],
   :i 65,
   :character "a",
   :width 7,
   :height 7},
  "b"
  {:bands
   [" _     "
    "| |    "
    "| |__  "
    "|  _ \\ "
    "| |_) )"
    "|____/ "
    "       "],
   :i 66,
   :character "b",
   :width 7,
   :height 7},
  "c"
  {:bands
   ["       "
    "       "
    "  ____ "
    " / ___)"
    "( (___ "
    " \\____)"
    "       "],
   :i 67,
   :character "c",
   :width 7,
   :height 7},
  "d"
  {:bands
   ["     _ "
    "    | |"
    "  __| |"
    " / _  |"
    "( (_| |"
    " \\____|"
    "       "],
   :i 68,
   :character "d",
   :width 7,
   :height 7},
  "e"
  {:bands
   ["       "
    "       "
    " _____ "
    "| ___ |"
    "| ____|"
    "|_____)"
    "       "],
   :i 69,
   :character "e",
   :width 7,
   :height 7},
  "f"
  {:bands
   ["    ___ "
    "   / __)"
    " _| |__ "
    "(_   __)"
    "  | |   "
    "  |_|   "
    "        "],
   :i 70,
   :character "f",
   :width 8,
   :height 7},
  "g"
  {:bands
   ["       "
    "       "
    "  ____ "
    " / _  |"
    "( (_| |"
    " \\___ |"
    "(_____|"],
   :i 71,
   :character "g",
   :width 7,
   :height 7},
  "h"
  {:bands
   [" _     "
    "| |    "
    "| |__  "
    "|  _ \\ "
    "| | | |"
    "|_| |_|"
    "       "],
   :i 72,
   :character "h",
   :width 7,
   :height 7},
  "i"
  {:bands [" _ " "(_)" " _ " "| |" "| |" "|_|" "   "],
   :i 73,
   :character "i",
   :width 3,
   :height 7},
  "j"
  {:bands ["   _ " "  (_)" "   _ " "  | |" "  | |" " _| |" "(__/ "],
   :i 74,
   :character "j",
   :width 5,
   :height 7},
  "k"
  {:bands
   [" _     "
    "| |    "
    "| |  _ "
    "| |_/ )"
    "|  _ ( "
    "|_| \\_)"
    "       "],
   :i 75,
   :character "k",
   :width 7,
   :height 7},
  "l"
  {:bands [" _  " "| | " "| | " "| | " "| | " " \\_)" "    "],
   :i 76,
   :character "l",
   :width 4,
   :height 7},
  "m"
  {:bands
   ["       "
    "       "
    " ____  "
    "|    \\ "
    "| | | |"
    "|_|_|_|"
    "       "],
   :i 77,
   :character "m",
   :width 7,
   :height 7},
  "n"
  {:bands
   ["       "
    "       "
    " ____  "
    "|  _ \\ "
    "| | | |"
    "|_| |_|"
    "       "],
   :i 78,
   :character "n",
   :width 7,
   :height 7},
  "o"
  {:bands
   ["       "
    "       "
    "  ___  "
    " / _ \\ "
    "| |_| |"
    " \\___/ "
    "       "],
   :i 79,
   :character "o",
   :width 7,
   :height 7},
  "p"
  {:bands
   ["       "
    "       "
    " ____  "
    "|  _ \\ "
    "| |_| |"
    "|  __/ "
    "|_|    "],
   :i 80,
   :character "p",
   :width 7,
   :height 7},
  "q"
  {:bands
   ["       "
    "       "
    "  ____ "
    " / _  |"
    "| |_| |"
    " \\__  |"
    "    |_|"],
   :i 81,
   :character "q",
   :width 7,
   :height 7},
  "r"
  {:bands
   ["       "
    "       "
    "  ____ "
    " / ___)"
    "| |    "
    "|_|    "
    "       "],
   :i 82,
   :character "r",
   :width 7,
   :height 7},
  "s"
  {:bands
   ["      " "      " "  ___ " " /___)" "|___ |" "(___/ " "      "],
   :i 83,
   :character "s",
   :width 6,
   :height 7},
  "t"
  {:bands
   ["       "
    "   _   "
    " _| |_ "
    "(_   _)"
    "  | |_ "
    "   \\__)"
    "       "],
   :i 84,
   :character "t",
   :width 7,
   :height 7},
  "u"
  {:bands
   ["       "
    "       "
    " _   _ "
    "| | | |"
    "| |_| |"
    "|____/ "
    "       "],
   :i 85,
   :character "u",
   :width 7,
   :height 7},
  "v"
  {:bands
   ["       "
    "       "
    " _   _ "
    "| | | |"
    " \\ V / "
    "  \\_/  "
    "       "],
   :i 86,
   :character "v",
   :width 7,
   :height 7},
  "w"
  {:bands
   ["       "
    "       "
    " _ _ _ "
    "| | | |"
    "| | | |"
    " \\___/ "
    "       "],
   :i 87,
   :character "w",
   :width 7,
   :height 7},
  "x"
  {:bands
   ["       "
    "       "
    " _   _ "
    "( \\ / )"
    " ) X ( "
    "(_/ \\_)"
    "       "],
   :i 88,
   :character "x",
   :width 7,
   :height 7},
  "y"
  {:bands
   ["       "
    "       "
    " _   _ "
    "| | | |"
    "| |_| |"
    " \\__  |"
    "(____/ "],
   :i 89,
   :character "y",
   :width 7,
   :height 7},
  "z"
  {:bands
   ["       "
    "       "
    " _____ "
    "(___  )"
    " / __/ "
    "(_____)"
    "       "],
   :i 90,
   :character "z",
   :width 7,
   :height 7},
  "{"
  {:bands
   ["   __ " "  / _)" " | |  " "( (   " " | |_ " "  \\__)" "      "],
   :i 91,
   :character "{",
   :width 6,
   :height 7},
  "|"
  {:bands [" _ " "| |" "|_|" " _ " "| |" "|_|" "   "],
   :i 92,
   :character "|",
   :width 3,
   :height 7},
  "}"
  {:bands
   [" __   " "(_ \\  " "  | | " "   ) )" " _| | " "(__/  " "      "],
   :i 93,
   :character "}",
   :width 6,
   :height 7},
  "~"
  {:bands
   ["  __  _ "
    " /  \\/ )"
    "(_/\\__/ "
    "        "
    "        "
    "        "
    "        "],
   :i 94,
   :character "~",
   :width 8,
   :height 7},
  "Ä"
  {:bands
   [" _______ "
    "(_)___(_)"
    " _______ "
    "|  ___  |"
    "| |   | |"
    "|_|   |_|"
    "         "],
   :i 95,
   :character "Ä",
   :width 9,
   :height 7},
  "Ö"
  {:bands
   [" _______ "
    "(_)___(_)"
    " _     _ "
    "| |   | |"
    "| |___| |"
    " \\_____/ "
    "         "],
   :i 96,
   :character "Ö",
   :width 9,
   :height 7},
  "Ü"
  {:bands
   [" _     _ "
    "(_)   (_)"
    " _     _ "
    "| |   | |"
    "| |___| |"
    " \\_____/ "
    "         "],
   :i 97,
   :character "Ü",
   :width 9,
   :height 7},
  "ä"
  {:bands
   [" _   _ "
    "(_) (_)"
    " _____ "
    "(____ |"
    "/ ___ |"
    "\\_____|"
    "       "],
   :i 98,
   :character "ä",
   :width 7,
   :height 7},
  "ö"
  {:bands
   [" _   _ "
    "(_) (_)"
    "  ___  "
    " / _ \\ "
    "| |_| |"
    " \\___/ "
    "       "],
   :i 99,
   :character "ö",
   :width 7,
   :height 7},
  "ü"
  {:bands
   [" _   _ "
    "(_) (_)"
    " _   _ "
    "| | | |"
    "| |_| |"
    "|____/ "
    "       "],
   :i 100,
   :character "ü",
   :width 7,
   :height 7},
  "ß"
  {:bands
   ["  ___  "
    " / _ \\ "
    "| ( ) )"
    "| |( ( "
    "| | ) )"
    "|_|(_/ "
    "       "],
   :i 101,
   :character "ß",
   :width 7,
   :height 7}}})


;;                   ___           ___           ___           ___           ___           ___                       ___
;;       ___        /\  \         /\  \         /\__\         /\  \         /\  \         /\  \          ___        /\  \
;;      /\  \      /::\  \       /::\  \       /::|  |       /::\  \        \:\  \       /::\  \        /\  \      /::\  \
;;      \:\  \    /:/\ \  \     /:/\:\  \     /:|:|  |      /:/\:\  \        \:\  \     /:/\:\  \       \:\  \    /:/\:\  \
;;      /::\__\  _\:\~\ \  \   /:/  \:\  \   /:/|:|__|__   /::\~\:\  \       /::\  \   /::\~\:\  \      /::\__\  /:/  \:\  \
;;   __/:/\/__/ /\ \:\ \ \__\ /:/__/ \:\__\ /:/ |::::\__\ /:/\:\ \:\__\     /:/\:\__\ /:/\:\ \:\__\  __/:/\/__/ /:/__/ \:\__\
;;  /\/:/  /    \:\ \:\ \/__/ \:\  \ /:/  / \/__/~~/:/  / \:\~\:\ \/__/    /:/  \/__/ \/_|::\/:/  / /\/:/  /    \:\  \  \/__/
;;  \::/__/      \:\ \:\__\    \:\  /:/  /        /:/  /   \:\ \:\__\     /:/  /         |:|::/  /  \::/__/      \:\  \
;;   \:\__\       \:\/:/  /     \:\/:/  /        /:/  /     \:\ \/__/     \/__/          |:|\/__/    \:\__\       \:\  \
;;    \/__/        \::/  /       \::/  /        /:/  /       \:\__\                      |:|  |       \/__/        \:\__\
;;                  \/__/         \/__/         \/__/         \/__/                       \|__|                     \/__/

(def isometric-1
'{:font-name "Isometric 1",
 :example
 ["                 ___           ___           ___           ___           ___           ___                       ___"
  "     ___        /\\  \\         /\\  \\         /\\__\\         /\\  \\         /\\  \\         /\\  \\          ___        /\\  \\"
  "    /\\  \\      /::\\  \\       /::\\  \\       /::|  |       /::\\  \\        \\:\\  \\       /::\\  \\        /\\  \\      /::\\  \\"
  "    \\:\\  \\    /:/\\ \\  \\     /:/\\:\\  \\     /:|:|  |      /:/\\:\\  \\        \\:\\  \\     /:/\\:\\  \\       \\:\\  \\    /:/\\:\\  \\"
  "    /::\\__\\  _\\:\\~\\ \\  \\   /:/  \\:\\  \\   /:/|:|__|__   /::\\~\\:\\  \\       /::\\  \\   /::\\~\\:\\  \\      /::\\__\\  /:/  \\:\\  \\"
  " __/:/\\/__/ /\\ \\:\\ \\ \\__\\ /:/__/ \\:\\__\\ /:/ |::::\\__\\ /:/\\:\\ \\:\\__\\     /:/\\:\\__\\ /:/\\:\\ \\:\\__\\  __/:/\\/__/ /:/__/ \\:\\__\\"
  "/\\/:/  /    \\:\\ \\:\\ \\/__/ \\:\\  \\ /:/  / \\/__/~~/:/  / \\:\\~\\:\\ \\/__/    /:/  \\/__/ \\/_|::\\/:/  / /\\/:/  /    \\:\\  \\  \\/__/"
  "\\::/__/      \\:\\ \\:\\__\\    \\:\\  /:/  /        /:/  /   \\:\\ \\:\\__\\     /:/  /         |:|::/  /  \\::/__/      \\:\\  \\"
  " \\:\\__\\       \\:\\/:/  /     \\:\\/:/  /        /:/  /     \\:\\ \\/__/     \\/__/          |:|\\/__/    \\:\\__\\       \\:\\  \\"
  "  \\/__/        \\::/  /       \\::/  /        /:/  /       \\:\\__\\                      |:|  |       \\/__/        \\:\\__\\"
  "                \\/__/         \\/__/         \\/__/         \\/__/                       \\|__|                     \\/__/"],
 :desc
 "Figlet conversion by Kent Nassen (kentn@cyberspace.org), 8-10-94, based\non the fonts posted by Lennert Stock:\n\nFrom: stock@fwi.uva.nl (Lennert Stock)\nDate: 15 Jul 1994 00:04:25 GMT\n\nHere are some fonts. Non-figlet I'm afraid, if you wanna convert them, be\nmy guest. I posted the isometric fonts before.\n\n------------------------------------------------------------------------------\n\n     .x%%%%%%x.                                             .x%%%%%%x.\n    ,%%%%%%%%%%.                                           .%%%%%%%%%%.\n   ,%%%'  )'  \\)                                           :(  `(  `%%%.\n  ,%x%)________) --------- L e n n e r t   S t o c k       ( _   __ (%x%.\n  (%%%~^88P~88P|                                           |~=> .=-~ %%%)\n  (%%::. .:,\\ .'                                           `. /,:. .::%%)\n  `;%:`\\. `-' |                                             | `-' ./':%:'\n   ``x`. -===.'                   stock@fwi.uva.nl -------- `.===- .'x''\n    / `:`.__.;                                               :.__.':' \\\n .d8b.     ..`.                                             .'..     .d8b.",
 :example-text "ABCDE",
 :font-sym isometric-1,
 :widest-char "A",
 :char-height 11,
 :max-char-width 14,
 :missing-chars
 ["!"
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
  "\\"
  "]"
  "^"
  "_"
  "`"
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
  "ß"],
 :chars-array-map
 {" "
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 0,
   :character " ",
   :width 7,
   :height 11},
  "!"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   !   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 1,
   :character "!",
   :width 0,
   :height 11,
   :missing? true},
  "\""
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   \"   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 2,
   :character "\"",
   :width 0,
   :height 11,
   :missing? true},
  "#"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   #   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 3,
   :character "#",
   :width 0,
   :height 11,
   :missing? true},
  "$"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   $   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 4,
   :character "$",
   :width 0,
   :height 11,
   :missing? true},
  "%"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   %   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 5,
   :character "%",
   :width 0,
   :height 11,
   :missing? true},
  "&"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   &   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 6,
   :character "&",
   :width 0,
   :height 11,
   :missing? true},
  "'"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   '   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 7,
   :character "'",
   :width 0,
   :height 11,
   :missing? true},
  "("
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   (   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 8,
   :character "(",
   :width 0,
   :height 11,
   :missing? true},
  ")"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   )   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 9,
   :character ")",
   :width 0,
   :height 11,
   :missing? true},
  "*"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   *   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 10,
   :character "*",
   :width 0,
   :height 11,
   :missing? true},
  "+"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   +   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 11,
   :character "+",
   :width 0,
   :height 11,
   :missing? true},
  ","
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ,   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 12,
   :character ",",
   :width 0,
   :height 11,
   :missing? true},
  "-"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   -   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 13,
   :character "-",
   :width 0,
   :height 11,
   :missing? true},
  "."
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   .   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 14,
   :character ".",
   :width 0,
   :height 11,
   :missing? true},
  "/"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   /   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 15,
   :character "/",
   :width 0,
   :height 11,
   :missing? true},
  "0"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   0   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 16,
   :character "0",
   :width 0,
   :height 11,
   :missing? true},
  "1"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   1   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 17,
   :character "1",
   :width 0,
   :height 11,
   :missing? true},
  "2"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   2   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 18,
   :character "2",
   :width 0,
   :height 11,
   :missing? true},
  "3"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   3   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 19,
   :character "3",
   :width 0,
   :height 11,
   :missing? true},
  "4"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   4   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 20,
   :character "4",
   :width 0,
   :height 11,
   :missing? true},
  "5"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   5   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 21,
   :character "5",
   :width 0,
   :height 11,
   :missing? true},
  "6"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   6   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 22,
   :character "6",
   :width 0,
   :height 11,
   :missing? true},
  "7"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   7   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 23,
   :character "7",
   :width 0,
   :height 11,
   :missing? true},
  "8"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   8   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 24,
   :character "8",
   :width 0,
   :height 11,
   :missing? true},
  "9"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   9   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 25,
   :character "9",
   :width 0,
   :height 11,
   :missing? true},
  ":"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   :   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 26,
   :character ":",
   :width 0,
   :height 11,
   :missing? true},
  ";"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ;   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 27,
   :character ";",
   :width 0,
   :height 11,
   :missing? true},
  "<"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   <   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 28,
   :character "<",
   :width 0,
   :height 11,
   :missing? true},
  "="
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   =   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 29,
   :character "=",
   :width 0,
   :height 11,
   :missing? true},
  ">"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   >   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 30,
   :character ">",
   :width 0,
   :height 11,
   :missing? true},
  "?"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ?   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 31,
   :character "?",
   :width 0,
   :height 11,
   :missing? true},
  "@"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   @   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 32,
   :character "@",
   :width 0,
   :height 11,
   :missing? true},
  "A"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/__\\:\\/:/  /"
    "      \\::/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 33,
   :character "A",
   :width 14,
   :height 11},
  "B"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\__\\ "
    " /:/\\:\\ \\:|__|"
    " \\:\\~\\:\\/:/  /"
    "  \\:\\ \\::/  / "
    "   \\:\\/:/  /  "
    "    \\::/__/   "
    "     ‾‾       "],
   :i 34,
   :character "B",
   :width 14,
   :height 11},
  "C"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\  \\ "
    " /:/__/ \\:\\__\\"
    " \\:\\  \\  \\/__/"
    "  \\:\\  \\      "
    "   \\:\\  \\     "
    "    \\:\\__\\    "
    "     \\/__/    "],
   :i 35,
   :character "C",
   :width 14,
   :height 11},
  "D"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\__\\ "
    " /:/__/ \\:|__|"
    " \\:\\  \\ /:/  /"
    "  \\:\\  /:/  / "
    "   \\:\\/:/  /  "
    "    \\::/__/   "
    "     ‾‾       "],
   :i 36,
   :character "D",
   :width 14,
   :height 11},
  "E"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\:\\~\\:\\ \\/__/"
    "  \\:\\ \\:\\__\\  "
    "   \\:\\ \\/__/  "
    "    \\:\\__\\    "
    "     \\/__/    "],
   :i 37,
   :character "E",
   :width 14,
   :height 11},
  "F"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/__\\:\\ \\/__/"
    "      \\:\\__\\  "
    "       \\/__/  "
    "              "
    "              "],
   :i 38,
   :character "F",
   :width 14,
   :height 11},
  "G"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\  \\ "
    " /:/__/_\\:\\__\\"
    " \\:\\  /\\ \\/__/"
    "  \\:\\ \\:\\__\\  "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 39,
   :character "G",
   :width 14,
   :height 11},
  "H"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/__/     "
    "  /::\\  \\ ___ "
    " /:/\\:\\  /\\__\\"
    " \\/__\\:\\/:/  /"
    "      \\::/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 40,
   :character "H",
   :width 14,
   :height 11},
  "I"
  {:bands
   ["            "
    "      ___   "
    "     /\\  \\  "
    "     \\:\\  \\ "
    "     /::\\__\\"
    "  __/:/\\/__/"
    " /\\/:/  /   "
    " \\::/__/    "
    "  \\:\\__\\    "
    "   \\/__/    "
    "            "],
   :i 41,
   :character "I",
   :width 12,
   :height 11},
  "J"
  {:bands
   ["       ___   "
    "      /\\  \\  "
    "      \\:\\  \\ "
    "  ___ /::\\__\\"
    " /\\  /:/\\/__/"
    " \\:\\/:/  /   "
    "  \\::/  /    "
    "   \\/__/     "
    "             "
    "             "
    "             "],
   :i 42,
   :character "J",
   :width 13,
   :height 11},
  "K"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/__/     "
    "  /::\\__\\____ "
    " /:/\\:::::\\__\\"
    " \\/_|:|~~|~   "
    "    |:|  |    "
    "    |:|  |    "
    "    |:|  |    "
    "     \\|__|    "],
   :i 43,
   :character "K",
   :width 14,
   :height 11},
  "L"
  {:bands
   ["      ___ "
    "     /\\__\\"
    "    /:/  /"
    "   /:/  / "
    "  /:/  /  "
    " /:/__/   "
    " \\:\\  \\   "
    "  \\:\\  \\  "
    "   \\:\\  \\ "
    "    \\:\\__\\"
    "     \\/__/"],
   :i 44,
   :character "L",
   :width 10,
   :height 11},
  "M"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /::|  |   "
    "   /:|:|  |   "
    "  /:/|:|__|__ "
    " /:/ |::::\\__\\"
    " \\/__/~~/:/  /"
    "       /:/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 45,
   :character "M",
   :width 14,
   :height 11},
  "N"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /::|  |   "
    "   /:|:|  |   "
    "  /:/|:|  |__ "
    " /:/ |:| /\\__\\"
    " \\/__|:|/:/  /"
    "     |:/:/  / "
    "     |::/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 46,
   :character "N",
   :width 14,
   :height 11},
  "O"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\  \\ "
    " /:/__/ \\:\\__\\"
    " \\:\\  \\ /:/  /"
    "  \\:\\  /:/  / "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 47,
   :character "O",
   :width 14,
   :height 11},
  "P"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/__\\:\\/:/  /"
    "      \\::/  / "
    "       \\/__/  "
    "              "
    "              "],
   :i 48,
   :character "P",
   :width 14,
   :height 11},
  "Q"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "   \\:\\~\\:\\  \\ "
    "    \\:\\ \\:\\__\\"
    "     \\:\\/:/  /"
    "      \\::/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 49,
   :character "Q",
   :width 14,
   :height 11},
  "R"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/_|::\\/:/  /"
    "    |:|::/  / "
    "    |:|\\/__/  "
    "    |:|  |    "
    "     \\|__|    "],
   :i 50,
   :character "R",
   :width 14,
   :height 11},
  "S"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\ \\  \\  "
    "  _\\:\\~\\ \\  \\ "
    " /\\ \\:\\ \\ \\__\\"
    " \\:\\ \\:\\ \\/__/"
    "  \\:\\ \\:\\__\\  "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 51,
   :character "S",
   :width 14,
   :height 11},
  "T"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "     \\:\\  \\   "
    "      \\:\\  \\  "
    "      /::\\  \\ "
    "     /:/\\:\\__\\"
    "    /:/  \\/__/"
    "   /:/  /     "
    "   \\/__/      "
    "              "
    "              "],
   :i 52,
   :character "T",
   :width 14,
   :height 11},
  "U"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/  /     "
    "  /:/  /  ___ "
    " /:/__/  /\\__\\"
    " \\:\\  \\ /:/  /"
    "  \\:\\  /:/  / "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 53,
   :character "U",
   :width 14,
   :height 11},
  "V"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/  /     "
    "  /:/__/  ___ "
    "  |:|  | /\\__\\"
    "  |:|  |/:/  /"
    "  |:|__/:/  / "
    "   \\::::/__/  "
    "    ~~~~      "
    "              "],
   :i 54,
   :character "V",
   :width 14,
   :height 11},
  "W"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/ _/_   "
    "   /:/ /\\__\\  "
    "  /:/ /:/ _/_ "
    " /:/_/:/ /\\__\\"
    " \\:\\/:/ /:/  /"
    "  \\::/_/:/  / "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 55,
   :character "W",
   :width 14,
   :height 11},
  "X"
  {:bands
   ["      ___     "
    "     |\\__\\    "
    "     |:|  |   "
    "     |:|  |   "
    "     |:|__|__ "
    " ____/::::\\__\\"
    " \\::::/~~/~   "
    "  ~~|:|~~|    "
    "    |:|  |    "
    "    |:|  |    "
    "     \\|__|    "],
   :i 56,
   :character "X",
   :width 14,
   :height 11},
  "Y"
  {:bands
   ["      ___     "
    "     |\\__\\    "
    "     |:|  |   "
    "     |:|  |   "
    "     |:|__|__ "
    "     /::::\\__\\"
    "    /:/~~/~   "
    "   /:/  /     "
    "   \\/__/      "
    "              "
    "              "],
   :i 57,
   :character "Y",
   :width 14,
   :height 11},
  "Z"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "     \\:\\  \\   "
    "      \\:\\  \\  "
    "       \\:\\  \\ "
    " _______\\:\\__\\"
    " \\::::::::/__/"
    "  \\:\\~~\\~~    "
    "   \\:\\  \\     "
    "    \\:\\__\\    "
    "     \\/__/    "],
   :i 58,
   :character "Z",
   :width 14,
   :height 11},
  "["
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /::::\\  \\  "
    "  /::::::\\  \\ "
    " /:::LS:::\\__\\"
    " \\::1994::/  /"
    "  \\::::::/  / "
    "   \\::::/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 59,
   :character "[",
   :width 14,
   :height 11},
  "\\"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   \\   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 60,
   :character "\\",
   :width 0,
   :height 11,
   :missing? true},
  "]"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ]   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 61,
   :character "]",
   :width 0,
   :height 11,
   :missing? true},
  "^"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ^   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 62,
   :character "^",
   :width 0,
   :height 11,
   :missing? true},
  "_"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   _   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 63,
   :character "_",
   :width 0,
   :height 11,
   :missing? true},
  "`"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   `   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 64,
   :character "`",
   :width 0,
   :height 11,
   :missing? true},
  "a"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/__\\:\\/:/  /"
    "      \\::/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 65,
   :character "a",
   :width 14,
   :height 11},
  "b"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\__\\ "
    " /:/\\:\\ \\:|__|"
    " \\:\\~\\:\\/:/  /"
    "  \\:\\ \\::/  / "
    "   \\:\\/:/  /  "
    "    \\::/__/   "
    "     ‾‾       "],
   :i 66,
   :character "b",
   :width 14,
   :height 11},
  "c"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\  \\ "
    " /:/__/ \\:\\__\\"
    " \\:\\  \\  \\/__/"
    "  \\:\\  \\      "
    "   \\:\\  \\     "
    "    \\:\\__\\    "
    "     \\/__/    "],
   :i 67,
   :character "c",
   :width 14,
   :height 11},
  "d"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\__\\ "
    " /:/__/ \\:|__|"
    " \\:\\  \\ /:/  /"
    "  \\:\\  /:/  / "
    "   \\:\\/:/  /  "
    "    \\::/__/   "
    "     ‾‾       "],
   :i 68,
   :character "d",
   :width 14,
   :height 11},
  "e"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\:\\~\\:\\ \\/__/"
    "  \\:\\ \\:\\__\\  "
    "   \\:\\ \\/__/  "
    "    \\:\\__\\    "
    "     \\/__/    "],
   :i 69,
   :character "e",
   :width 14,
   :height 11},
  "f"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/__\\:\\ \\/__/"
    "      \\:\\__\\  "
    "       \\/__/  "
    "              "
    "              "],
   :i 70,
   :character "f",
   :width 14,
   :height 11},
  "g"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\  \\ "
    " /:/__/_\\:\\__\\"
    " \\:\\  /\\ \\/__/"
    "  \\:\\ \\:\\__\\  "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 71,
   :character "g",
   :width 14,
   :height 11},
  "h"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/__/     "
    "  /::\\  \\ ___ "
    " /:/\\:\\  /\\__\\"
    " \\/__\\:\\/:/  /"
    "      \\::/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 72,
   :character "h",
   :width 14,
   :height 11},
  "i"
  {:bands
   ["            "
    "      ___   "
    "     /\\  \\  "
    "     \\:\\  \\ "
    "     /::\\__\\"
    "  __/:/\\/__/"
    " /\\/:/  /   "
    " \\::/__/    "
    "  \\:\\__\\    "
    "   \\/__/    "
    "            "],
   :i 73,
   :character "i",
   :width 12,
   :height 11},
  "j"
  {:bands
   ["       ___   "
    "      /\\  \\  "
    "      \\:\\  \\ "
    "  ___ /::\\__\\"
    " /\\  /:/\\/__/"
    " \\:\\/:/  /   "
    "  \\::/  /    "
    "   \\/__/     "
    "             "
    "             "
    "             "],
   :i 74,
   :character "j",
   :width 13,
   :height 11},
  "k"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/__/     "
    "  /::\\__\\____ "
    " /:/\\:::::\\__\\"
    " \\/_|:|~~|~   "
    "    |:|  |    "
    "    |:|  |    "
    "    |:|  |    "
    "     \\|__|    "],
   :i 75,
   :character "k",
   :width 14,
   :height 11},
  "l"
  {:bands
   ["      ___ "
    "     /\\__\\"
    "    /:/  /"
    "   /:/  / "
    "  /:/  /  "
    " /:/__/   "
    " \\:\\  \\   "
    "  \\:\\  \\  "
    "   \\:\\  \\ "
    "    \\:\\__\\"
    "     \\/__/"],
   :i 76,
   :character "l",
   :width 10,
   :height 11},
  "m"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /::|  |   "
    "   /:|:|  |   "
    "  /:/|:|__|__ "
    " /:/ |::::\\__\\"
    " \\/__/~~/:/  /"
    "       /:/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 77,
   :character "m",
   :width 14,
   :height 11},
  "n"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /::|  |   "
    "   /:|:|  |   "
    "  /:/|:|  |__ "
    " /:/ |:| /\\__\\"
    " \\/__|:|/:/  /"
    "     |:/:/  / "
    "     |::/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 78,
   :character "n",
   :width 14,
   :height 11},
  "o"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /:/  \\:\\  \\ "
    " /:/__/ \\:\\__\\"
    " \\:\\  \\ /:/  /"
    "  \\:\\  /:/  / "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 79,
   :character "o",
   :width 14,
   :height 11},
  "p"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/__\\:\\/:/  /"
    "      \\::/  / "
    "       \\/__/  "
    "              "
    "              "],
   :i 80,
   :character "p",
   :width 14,
   :height 11},
  "q"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "   \\:\\~\\:\\  \\ "
    "    \\:\\ \\:\\__\\"
    "     \\:\\/:/  /"
    "      \\::/  / "
    "      /:/  /  "
    "     /:/  /   "
    "     \\/__/    "],
   :i 81,
   :character "q",
   :width 14,
   :height 11},
  "r"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\:\\  \\  "
    "  /::\\~\\:\\  \\ "
    " /:/\\:\\ \\:\\__\\"
    " \\/_|::\\/:/  /"
    "    |:|::/  / "
    "    |:|\\/__/  "
    "    |:|  |    "
    "     \\|__|    "],
   :i 82,
   :character "r",
   :width 14,
   :height 11},
  "s"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "    /::\\  \\   "
    "   /:/\\ \\  \\  "
    "  _\\:\\~\\ \\  \\ "
    " /\\ \\:\\ \\ \\__\\"
    " \\:\\ \\:\\ \\/__/"
    "  \\:\\ \\:\\__\\  "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 83,
   :character "s",
   :width 14,
   :height 11},
  "t"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "     \\:\\  \\   "
    "      \\:\\  \\  "
    "      /::\\  \\ "
    "     /:/\\:\\__\\"
    "    /:/  \\/__/"
    "   /:/  /     "
    "   \\/__/      "
    "              "
    "              "],
   :i 84,
   :character "t",
   :width 14,
   :height 11},
  "u"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/  /     "
    "  /:/  /  ___ "
    " /:/__/  /\\__\\"
    " \\:\\  \\ /:/  /"
    "  \\:\\  /:/  / "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 85,
   :character "u",
   :width 14,
   :height 11},
  "v"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/  /    "
    "   /:/  /     "
    "  /:/__/  ___ "
    "  |:|  | /\\__\\"
    "  |:|  |/:/  /"
    "  |:|__/:/  / "
    "   \\::::/__/  "
    "    ‾‾‾‾      "
    "              "],
   :i 86,
   :character "v",
   :width 14,
   :height 11},
  "w"
  {:bands
   ["      ___     "
    "     /\\__\\    "
    "    /:/ _/_   "
    "   /:/ /\\__\\  "
    "  /:/ /:/ _/_ "
    " /:/_/:/ /\\__\\"
    " \\:\\/:/ /:/  /"
    "  \\::/_/:/  / "
    "   \\:\\/:/  /  "
    "    \\::/  /   "
    "     \\/__/    "],
   :i 87,
   :character "w",
   :width 14,
   :height 11},
  "x"
  {:bands
   ["      ___     "
    "     |\\__\\    "
    "     |:|  |   "
    "     |:|  |   "
    "     |:|__|__ "
    " ____/::::\\__\\"
    " \\::::/~~/~   "
    "  ~~|:|~~|    "
    "    |:|  |    "
    "    |:|  |    "
    "     \\|__|    "],
   :i 88,
   :character "x",
   :width 14,
   :height 11},
  "y"
  {:bands
   ["      ___     "
    "     |\\__\\    "
    "     |:|  |   "
    "     |:|  |   "
    "     |:|__|__ "
    "     /::::\\__\\"
    "    /:/~~/~   "
    "   /:/  /     "
    "   \\/__/      "
    "              "
    "              "],
   :i 89,
   :character "y",
   :width 14,
   :height 11},
  "z"
  {:bands
   ["      ___     "
    "     /\\  \\    "
    "     \\:\\  \\   "
    "      \\:\\  \\  "
    "       \\:\\  \\ "
    " _______\\:\\__\\"
    " \\::::::::/__/"
    "  \\:\\~~\\~~    "
    "   \\:\\  \\     "
    "    \\:\\__\\    "
    "     \\/__/    "],
   :i 90,
   :character "z",
   :width 14,
   :height 11},
  "{"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   {   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 91,
   :character "{",
   :width 0,
   :height 11,
   :missing? true},
  "|"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   |   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 92,
   :character "|",
   :width 0,
   :height 11,
   :missing? true},
  "}"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   }   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 93,
   :character "}",
   :width 0,
   :height 11,
   :missing? true},
  "~"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ~   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 94,
   :character "~",
   :width 0,
   :height 11,
   :missing? true},
  "Ä"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   Ä   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 95,
   :character "Ä",
   :width 0,
   :height 11,
   :missing? true},
  "Ö"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   Ö   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 96,
   :character "Ö",
   :width 0,
   :height 11,
   :missing? true},
  "Ü"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   Ü   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 97,
   :character "Ü",
   :width 0,
   :height 11,
   :missing? true},
  "ä"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ä   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 98,
   :character "ä",
   :width 0,
   :height 11,
   :missing? true},
  "ö"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ö   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 99,
   :character "ö",
   :width 0,
   :height 11,
   :missing? true},
  "ü"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ü   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 100,
   :character "ü",
   :width 0,
   :height 11,
   :missing? true},
  "ß"
  {:bands
   ["       "
    "       "
    "       "
    "       "
    "   ß   "
    "       "
    "       "
    "       "
    "       "
    "       "
    "       "],
   :i 101,
   :character "ß",
   :width 0,
   :height 11,
   :missing? true}}})

(def
 fonts-by-kw
 {:miniwi miniwi,
  :ansi-shadow ansi-shadow,
  :drippy drippy,
  :big big,
  :big-money big-money,
  :rounded rounded,
  :isometric-1 isometric-1})
