(ns bling.fonts.drippy)



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