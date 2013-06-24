(ns markovmusic.core
  (:require [markovmusic.midi :as midi])
  (:require [markovmusic.chain :as chain])
  (:require [markovmusic.player :as player])
  (:use [clojure.tools.namespace.repl :only (refresh)] clojure.stacktrace)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "Hi"))


(def ek (midi/parse-midi-file "/home/vishnu/Downloads/mozart_eine_kleine_easy.mid" 1))
;(def b850 (midi/parse-midi-file "/home/vishnu/Downloads/bach_850_format0.mid" 0))
;(def am (midi/parse-midi-file "/home/vishnu/Downloads/gounod_ave_maria_easy.mid" 1))
(def gs (midi/parse-midi-file "/home/vishnu/Downloads/greensleeves.mid" 1))
(def hb (midi/parse-midi-file "/home/vishnu/Downloads/happy_birthday_easy.mid" 1))
;(def fe (midi/parse-midi-file "/home/vishnu/Downloads/beethoven_fur_elise.mid" 1))

(defn play [& songs]
  (player/play (player/note-freq songs) (player/volume-freq songs) (player/duration-freq songs)))

