(ns clojure-game.core
  (:require [clojure.set :as set]
            [lanterna.screen :as s]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Initial state

(defn initial-state []
  {:position     {:x 0 :y 0}
   :key-sequence []
   :old-states   []})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Updating the state

(def key->action
  {:up    :move
   :down  :move
   :right :move
   :left  :move
   \a     :attack})

(defmulti update-game (fn [state key] (key->action key)))

(defmethod update-game :move
  [state key]
  (case key
    :up    (update-in state [:position :y] dec)
    :down  (update-in state [:position :y] inc)
    :right (update-in state [:position :x] inc)
    :left  (update-in state [:position :x] dec)))

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

(defn main [screen-type]
  (let [screen (s/get-screen screen-type)]
    (s/in-screen
     screen
     (loop [state (initial-state)]
       (let [k (s/get-key-blocking screen)]
         (cond
           (#{\q} k)                     state
           (#{:up :down :right :left} k) (let [new-state (update-game state k)]
                                           (render screen new-state)
                                           (recur new-state))
           :else                         (recur (update state :key-sequence conj k))))))))


(defn -main [& args]
  (let [args        (set args)
        screen-type (cond
                      (args ":swing") :swing
                      (args ":text")  :text
                      :else           :auto)]
    (main screen-type)))

(comment

  (-main)

  )
