(ns markovmusic.chain)

(defn make-choice [choices]
  (let [items (keys choices)
        probabilities (vals choices)
        r (rand)]
    (loop [i 0
           cummulative-probability (nth probabilities 0)]
      (if (< r cummulative-probability)
        (nth items i)
        (recur (inc i) (+ cummulative-probability (nth probabilities (inc i))))))))

(defn val-inc [t r c]
  (assoc-in t [r c] (inc (get-in t [r c] 0))))

(defn get-notes [song position]
  (keys
    ((get song position)
     :sound)))

(defn get-duration [song position]
  ((get song position) :duration))

(defn generate-frequency-matrix
  ([song func] (generate-frequency-matrix song func {}))
  ([song func matrix] (if (< (count song) 2)
                       matrix
                       (recur (vec (rest song)) func (val-inc matrix (func song 0) (func song 1))))))

(defn mapmap [f m]
  (reduce #(assoc %1 %2 (f (m %2))) {} (keys m)))

(defn frequency-to-probability [freqmatrix]
  (mapmap
    (fn [notedata]
      (let [total (* 1.0 (reduce + (vals notedata)))]
        (mapmap #(/ % total) notedata)))
    freqmatrix ))