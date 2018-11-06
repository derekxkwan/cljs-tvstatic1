(ns cljs-tvstatic1.core
  (:require [quil.core :as q :include-macros true]
            [cljs-dreamlike1.static :as static]
            [quil.middleware :as m]))

(def base-dim {:w 1024 :h 768})
(def cur-width (.-innerWidth js/window))
(def cur-dim (let [w (float (get base-dim :w)) h (float (get base-dim :h))]
               {:w (int want-width) :h (int (* want width (/ h w)))}
               ))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  ; setup function returns initial state. It contains
  ; circle color and position.
  (let [cur-state (atom {:debug false})
        w (get cur-dim :w)
        h (get cur-dim :h)]
    (swap! cur-state merge {:static (static/setup-static w h)})
    )
  )

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  )

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)

  

  )

; this function is called in index.html
(defn ^:export run-sketch []
  (q/defsketch cljs-tvstatic1
    :host "cljs-tvstatic1"
    :size [(get cur-dim :w) (get cur-dim :h)]
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    ; This sketch uses functional-mode middleware.
    ; Check quil wiki for more info about middlewares and particularly
    ; fun-mode.
    :middleware [m/fun-mode])
  )

; uncomment this line to reset the sketch:
; (run-sketch)
