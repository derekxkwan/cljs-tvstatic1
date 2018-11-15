(ns cljs-tvstatic1.text
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def bg-color 75)
(def text-color 0)
(def font-sz 150)
(def text-ld 10)
(def scaling nil)
(def cur-dim {:w nil :h nil})

(def texts ["MODEL\nMINORITY" "PERPETUAL\nFOREIGNER" "GOOD\nAT\nMATH"
            "ASIAN\nMASCULINITY" "FREQUENCY\nOF\nREPRESENTATION" "INDUSTRIOUS,\nRULE-ABIDING"])



(defn setup-text [w h cur-scaling]
  (let* [cur-w (quot w cur-scaling)
         cur-h (quot h cur-scaling)
        ;; want-font (get (q/available-fonts) 0)
         ;;cur-font (q/load-font want-font)
         cur-font (q/load-font "../data/LiberationSans-Bold.ttf")
         new-gr (q/create-graphics cur-w cur-h :p2d)]
    (set! cur-dim (merge cur-dim {:w cur-w :h cur-h}))
    (set! scaling cur-scaling)

    ;(set! text-ld (/ text-ld scaling))
    {:font cur-font :text-gfx new-gr :num-texts (count texts)}
    )
  )
    
(defn draw-text [text-idx]
  (q/background bg-color)
  (q/text-leading text-ld)
  (q/text-align :center :center)
  (q/fill text-color)
  (q/text-size (/ font-sz scaling))
  (q/rect-mode :corner)
  (let [w (get cur-dim :w)
        h (get cur-dim :h)
        my-str (get texts text-idx)
        ;str-chunks (vec (re-seq #"[^\s\n]+" my-str))
        ;str-counts (map-indexed (fn [i n] [i (count n)]) str-chunks)
        ;longest-str-idx (first (apply max-key second str-counts))
        ;text-asc (+ (q/text-ascent) (q/text-descent))
        ;nstr (count str-chunks)
        ;cur-width (q/text-width (get str-chunks longest-str-idx))
        ;x1 (- (/ w 2.0) (/ cur-width 2.0))
        ;x2 (+ (/ w 2.0) (/ cur-width 2.0))
        ;actual-height (* (+ text-asc text-ld) nstr)
        ;y1 (- (/ h 2.0) (/ actual-height 2.0))
        ;y2 (+ (/ h 2.0) (/ actual-height 2.0))
        ]
    (q/text my-str (/ w 2) (/ h 2))
    )
)
