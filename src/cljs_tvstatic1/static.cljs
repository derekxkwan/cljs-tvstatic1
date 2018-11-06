(ns cljs-tvstatic1.static
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            ))

(def cur-dim {:w nil :h nil})

(defn setup-static [w h]
  (set! cur-dim (merge cur-dim {:w w :h h}))
  (q/crate-graphics w h :p2d)
  )
