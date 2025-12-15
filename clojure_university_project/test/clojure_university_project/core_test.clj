(ns clojure-university-project.core-test
  (:require [midje.sweet :refer :all]
            [clojure-university-project.core :as sut]
  )
)

(facts "random-vector basic properties"
  (let [v (sut/random-vector 5 1 10)]
    (count v) => 5
    v => vector?
    (every? number? v) => true
    (every? #(<= 1 % 10) v) => true
    (every? #(= % (/ (Math/round (* 100 %)) 100.0)) v) => true
  )
)

(facts "recommending IT job positions"
  (sut/recommended-it-job-jositions
    {:data 9 :statistics 8 :math 7
     :engineering 0 :algorithms 7 :optimization 8
     :hardware 0 :physics 0
     :geometry 10
     :ui 0 :people 0 :ux 0
     :testing 0 :edge-cases 0 :debugging 0
     :empathy 0 :simplification 0 :patience 0
     :monotony 0 :money 0}
  )
  => (contains ["data analyst, data science, ML, AI" 
                "game development, simulations, graphics"])
)

