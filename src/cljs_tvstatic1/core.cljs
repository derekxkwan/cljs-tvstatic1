(ns cljs-tvstatic1.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [cljs-tvstatic1.snow :as sn]
            [cljs-tvstatic1.text :as tx]))

(def base-dim {:w 1024 :h 768})
(def want-width (.-innerWidth js/window))
(def want-height (.-innerHeight js/window))
;(def cur-dim (let [w (float (get base-dim :w)) h (float (get base-dim :h))]
;               {:w (int want-width) :h (int (* want-width (/ h w)))}
;               ))
(def cur-dim {:w want-width :h want-height})
;(def cur-dim {:w 1024 :h 768})
(def scaling 8)
(def frate 24)
;(def max-fade-depth 150)
(def max-fade-depth 250)
(def fade-chance 0.01)
;(def fade-chance 0.5)
(def text-chance 0.5)
(def min-fade-ms 5000)
;(def min-fade-ms 1000)
;(def range-fade-ms 1000)
(def range-fade-ms 10000)
(def paths ["res/breakfast-at-tiffanys-mickey-rooney-2.jpg", "res/breakfast-at-tiffanys-mickey-rooney-3.jpg",
      "res/Long_Duk_Dong.jpg", "res/Starring_Mickey_Rooney.jpg", "res/usp_entertainment__88th_academy_awards_80081100.jpg"
            "res/kungfu.jpeg" "res/charliechan.jpg" "fumanchu.jpeg" "ming-merciless.jpg"])
(def num-photos (count paths))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate frate)
  ; Set color mode to HSB (HSV) instead of default RGB.
  ; setup function returns initial state. It contains
  ; circle color and position.
  (let [cur-state (atom {:debug false})
        w (get cur-dim :w)
        h (get cur-dim :h)]
    (swap! cur-state merge {:pics (mapv #(q/load-image %) paths)})
    (swap! cur-state merge {:snow-gfx (sn/setup-snow w h scaling)})
    (swap! cur-state merge (tx/setup-text w h 1))
    (swap! cur-state merge {:cur-ms (q/millis) :cur-image 0 :fade-start -1 :fade-ms 0 :fade-depth 0 :cur-alpha 255 :text-idx 0})

    )
  )

(defn update-state [state]

  ;fade-start-time is time fade started in ms, if not fading, set to -1
  (let [cur-state (atom state)
        cur-sec (get state :cur-sec)
        num-texts (get state :num-texts)
        cur-image (get state :cur-image)
        fade-start (get state :fade-start)
        fade-depth (get state :fade-depth)
        fading? (> fade-start -1)
        new-fade? (get state :new-fade?)
        fade-ms (get state :fade-ms)]
    (if fading?
      (let [cur-ms (q/millis)
            cur-fade-ms (- cur-ms fade-start)
            alpha-low (max 0 (- 255 fade-depth))
            fade-pos (/ cur-fade-ms fade-ms) ; want to map 0 - 1 to 1 - 0 - 1
            cur-alpha (+ alpha-low (* fade-depth (Math/abs (* (- (- 1 fade-pos) 0.5) 2.0))))
            fade-done? (> cur-fade-ms fade-ms)]
        (if (not fade-done?)
          (swap! cur-state merge {:cur-alpha cur-alpha})
          (swap! cur-state merge {:cur-alpha 255 :fade-start -1})
        ))
      (let [new-fade? (< (rand) fade-chance)]
        (when new-fade?
          (swap! cur-state merge {:fade-start (q/millis) :fade-ms (+ (rand-int range-fade-ms) min-fade-ms)
                                  :fade-depth (rand-int max-fade-depth) :cur-image (rand-int num-photos)
                                  :display-text? (< (rand) text-chance) :text-idx (rand-int num-texts)}
        ))
      )
    
    
    )
        
  @cur-state
  )
  )

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color
  ;(q/background 0)
  (let [snow-gfx (get state :snow-gfx)
        text-gfx (get state :text-gfx)
        text-idx (get state :text-idx)
        display-text? (get state :display-text?)
        pics (get state :pics)
        cur-pic (get pics (get state :cur-image))
        cur-w (get cur-dim :w)
        cur-h (get cur-dim :h)
        cur-alpha (get state :cur-alpha)]
    (q/with-graphics snow-gfx
      (sn/draw-snow cur-alpha))
    (if display-text?
      (do (tx/draw-text text-idx)
          (q/image text-gfx 0 0 cur-w cur-h))
      (q/image cur-pic 0 0 cur-w cur-h)
      )
    (q/image snow-gfx 0 0 cur-w cur-h)
    ;(.log js/console cur-w cur-h)
      ;(q/background 0)
    )
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
(run-sketch)
