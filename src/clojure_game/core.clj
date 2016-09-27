(ns clojure-game.core
  (:require [clojure.set :as set]
            [lanterna.screen :as s]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Initial state

(defn initial-state []
  {:position {:x 0 :y 0}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Updating the state

(def key->action
  {:up    :move-up
   :down  :move-down
   :right :move-right
   :left  :move-left
   \a     :attack})

(defmulti update-game (fn [state key] (key->action key)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Moving around

(defmethod update-game :move-up
  [state _]
  (update-in state [:position :y] dec))

(defmethod update-game :move-down
  [state _]
  (update-in state [:position :y] inc))

(defmethod update-game :move-right
  [state _]
  (update-in state [:position :x] inc))

(defmethod update-game :move-left
  [state _]
  (update-in state [:position :x] dec))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Render

(defn clear-screen [screen]
  (let [blank (apply str (repeat 80 \space))]
    (doseq [row (range 24)]
      (s/put-string screen 0 row blank))))

(defn render [screen {{:keys [x y]} :position :as state}]
  (clear-screen screen)
  (s/put-string screen x y "@")
  (s/redraw screen)
  state)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Loop

(defn run-game []
  (let [screen (s/get-screen :auto {:cols 80 :rows 24})]
    (s/in-screen
     screen
     (loop [state (initial-state)]
       (let [k (s/get-key-blocking screen)]
         (cond
           (#{\q} k) state
           :else     (let [new-state (update-game state k)]
                       (render screen new-state)
                       (recur new-state))))))))


(defn -main []
  (run-game))

(comment

  (-main)

  )
