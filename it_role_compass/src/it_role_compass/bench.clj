(ns it-role-compass.bench
  (:require [criterium.core :as c]
            [it-role-compass.expert-system :as es]))

(def- input
  {:data 9 :statistics 8 :math 7
   :engineering 0 :algorithms 7 :optimization 8
   :hardware 0 :physics 0
   :geometry 10
   :ui 0 :people 0 :ux 0
   :testing 0 :edge-cases 0 :debugging 0
   :empathy 0 :simplification 0 :patience 0
   :monotony 0 :money 0})

(defn- bench-recommended 
  []
  (c/quick-bench
    (es/recommended-it-job-position-weights input)))