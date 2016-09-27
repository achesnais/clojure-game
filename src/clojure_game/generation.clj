(ns clojure-game.generation)

(def content
  (vec (shuffle (cons :chest (repeat 29 :empty)))))

(defn make [kw]
  (case kw
    :wall  {:type :wall}
    :chest {:type     :chest
            :contents [{:type :gold :quantity (rand-int 100)}]}
    :empty {}))

(defn random-content []
  (make (rand-nth content)))

(defn room-row [w]
  (conj (into [(make :wall)]
              (repeatedly (- w 2) #(random-content)))
        (make :wall)))

(defn hor-wall [w]
  (vec (repeat w (make :wall))))

(defn get-room []
  (let [[w h] (repeatedly #(+ 4 (rand-int 8)))]
    {:width w :height h
     :layout
     (conj (into [(hor-wall w)]
                 (repeatedly (- h 2) #(room-row w)))
           (hor-wall w))}))

(comment

  (take 100 (repeatedly #(random-content)))

  (room-row 10)

  (get-room)

  :empty

  )
