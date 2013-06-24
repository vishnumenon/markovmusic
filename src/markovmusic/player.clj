(ns markovmusic.player
  (:require [markovmusic.midi :as midi])
  (:require [markovmusic.chain :as chain])
  (:use overtone.live))

(definst saw-wave [freq 440 sustain 0.4 vol 0.4] 
  (* (env-gen (lin-env 0 sustain 0) 1 1 0 1 FREE)
     (saw freq)
     vol))

(defn play-note [midinote sustain vol]
  (saw-wave (midi->hz midinote) sustain vol))

(defn play [note-matrix volume-matrix duration-matrix]
  (loop [note-count 1
         sound (first (keys note-matrix))
         volume (first (keys volume-matrix))
        duration (first (keys duration-matrix))]
           (do
             (doseq [note sound]
               (play-note note (/ duration 1000.0) (/ volume 128.0)))
             (Thread/sleep duration)
             (println (str "Playing Note #" note-count ", pitch(es): " (clojure.string/join " & " sound) ", velocity: " volume ", duration: " duration))
             (recur
               (inc note-count)
               (chain/make-choice (get note-matrix sound {(rand-nth (keys note-matrix)) 1}))
               (chain/make-choice (get volume-matrix volume {(rand-nth (keys volume-matrix)) 1}))
               (chain/make-choice (get duration-matrix duration {(rand-nth (keys duration-matrix)) 1}))))))

(defn random-play []
  (while true
    (let [sound (repeatedly (rand-int 3) #(+ (rand-int 20) 60))
          volume (+ (rand-int 60) 40)
          duration (rand-nth [128 256 512 1024 42 384])]
      (do
             (doseq [note sound]
               (play-note note (/ duration 1000.0) (/ volume 128.0)))
             (Thread/sleep duration)
             (println (str "Playing Pitch(es): " (clojure.string/join " & " sound) ", velocity: " volume ", duration: " duration))))))

(defn note-freq [songs] (chain/frequency-to-probability (reduce #(chain/generate-frequency-matrix %2 chain/get-notes %1) {} songs)))
(defn volume-freq [songs] (chain/frequency-to-probability (reduce #(chain/generate-frequency-matrix %2 chain/get-volume %1) {} songs)))
(defn duration-freq [songs] (chain/frequency-to-probability (reduce #(chain/generate-frequency-matrix %2 chain/get-duration %1) {} songs)))
