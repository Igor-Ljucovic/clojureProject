(ns it-role-compass.bench
  (:require [criterium.core :as c]
            [it-role-compass.expert-system :as es]))

(def ratings
  {:data           8.5
   :math           7.5
   :algorithms     8.0
   :physics        5.5
   :geometry       6.5
   :abstraction    8.0
   :optimization   8.5
   :engineering    7.5
   :hardware       4.0
   :ui             4.5
   :analysis       7.0
   :wrong-way      6.5
   :testing        6.0
   :people         4.5
   :ux             4.0
   :interactivity  5.0
   :edge-cases     7.5
   :scalability    7.0
   :statistics     7.5
   :empathy        5.0
   :simplification 6.5
   :patience       6.0
   :debugging      8.5
   :monotony       3.5
   :money          4.0})

(defn bench-expert-system 
  []
  (c/quick-bench
    (es/it-job-position-expert-system-recommendations ratings)))