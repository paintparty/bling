(ns bling.fonts.big)



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