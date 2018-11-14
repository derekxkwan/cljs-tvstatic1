(ns cljs-tvstatic1.snow
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            ))

(def cur-dim {:w nil :h nil})
(def ctr (atom 0))

(defn setup-snow [w h scaling]
  (let [real-w (int (/ w scaling))
        real-h (int (/ h scaling))]
    (set! cur-dim (merge cur-dim {:w real-w :h real-h}))
    (.log js/console real-w real-h)
    (q/create-graphics real-w real-h :p2d)
    )
  )

(defn draw-snow [cur-alpha]
  (let [pix (q/pixels)
        cur-w (get cur-dim :w)
        cur-h (get cur-dim :h)]
;      (swap! ctr #(mod (inc %) 128))
 ;   (when (= @ctr 0) (.log js/console (.-length pix)))
    ;(.log js/console cur-w cur-h)
    (doseq [j (range cur-h)
            i (range cur-w)]
      (let [cur-color (q/color (rand-int 256) cur-alpha)
            cur-coord (+ i (* cur-w j))]
        ;(.log js/console i j)
        ;(.log js/console (rand-int 256))
        ;(.log js/console cur-color)
        ;(q/set-pixel i j cur-color)
        (aset pix cur-coord cur-color)
        ))
    (q/update-pixels)
  
    )

  
  )
      
    
