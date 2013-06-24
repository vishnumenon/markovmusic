(ns markovmusic.midi
  (:import (java.io File)
           (javax.sound.midi MidiSystem Sequence MidiMessage MidiEvent ShortMessage Track)))

(declare add-note)

(defn parse-midi-file
  ([file-name] (parse-midi-file file-name 0))
  ([file-name track] (let [note-on 0x90
                           note-off 0x80
                           sequence (MidiSystem/getSequence (File. file-name))
                           track  (-> sequence .getTracks (aget track))]
                       (loop [current-notes {}
                              parsed []
                              last-time 0
                              event-index 0]
                         (let [event (.get track event-index)
                               message (.getMessage event)]
                           (cond
                             (= (inc event-index) (.size track)) parsed
                             (not (instance? ShortMessage message)) (recur current-notes parsed last-time (inc event-index))
                             (= (.getCommand message) note-on) (if (= (.getTick event) last-time)
                                                                 (recur 
                                                                   (add-note message current-notes)
                                                                   parsed
                                                                   last-time
                                                                   (inc event-index))
                                                                 (recur
                                                                   (add-note message current-notes)
                                                                   (conj parsed {:sound current-notes :duration (- (.getTick event) last-time)})
                                                                   (.getTick event)
                                                                   (inc event-index)))
                             (= (.getCommand message) note-off) (if (= (.getTick event) last-time)
                                                                  (recur
                                                                    (dissoc current-notes (.getData1 message))
                                                                    parsed
                                                                    last-time
                                                                    (inc event-index))
                                                                  (recur
                                                                    (dissoc current-notes (.getData1 message))
                                                                    (conj parsed {:sound current-notes :duration (- (.getTick event) last-time)})
                                                                    (.getTick event)
                                                                    (inc event-index)))
                             :else (recur current-notes parsed last-time (inc event-index))))))))

(defn add-note [msg notes] (let [k (.getData1 msg) v (.getData2 msg)] (if (> v 0) (assoc notes k (+ (.getData2 msg) (get notes k 0))) (dissoc notes k))))
