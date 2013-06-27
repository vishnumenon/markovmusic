(ns markovmusic.core
  (:require [markovmusic.midi :as midi])
  (:require [markovmusic.chain :as chain])
  (:require [markovmusic.player :as player])
  (:require clojure.pprint)
  (:use [clojure.tools.namespace.repl :only (refresh)] clojure.stacktrace))

(def ek (midi/parse-midi-file "/home/vishnu/Downloads/mozart_eine_kleine_easy.mid" 1))
(def gs (midi/parse-midi-file "/home/vishnu/Downloads/greensleeves.mid" 1))
(def hb (midi/parse-midi-file "/home/vishnu/Downloads/happy_birthday_easy.mid" 1))
(def oj (midi/parse-midi-file "/home/vishnu/Downloads/beethoven_ode_to_joy.mid" 1))
(def sf (midi/parse-midi-file "/home/vishnu/Downloads/scarborough_fair.mid" 1))



(defn play [& songs]
  (player/play (player/note-freq songs) (player/duration-freq songs)))

