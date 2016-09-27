(ns clojure-game.generation
  (:require [clojure.walk :as walk]))

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



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;

(defn- rand-ratio []
  (float (/ (+ 2 (rand-int 8)) 10)))

(defn split [rects]
  (walk/postwalk
   #(if (map? %)
      (let [dir (rand-nth [:w :h])
            per (rand-ratio)]
        [(update % dir * per)
         (update % dir * (- 1 per))])
      %)
   rects))

(defn pad [room]
  (reduce (fn [r dim] (assoc r (keyword (str (name dim) "-actual"))
                             (* (dim r) (rand-ratio)))) room [:w :h]))

#_(->> (iterate split [{:w 100 :h 100}])
       (take 10)
       (last))
