(ns clojure-university-project.core-test
  (:require [midje.sweet :refer :all]
            [clojure-university-project.core :as sut]))

(facts "random-ratings"
  (let [v (sut/random-ratings 5 1 10)]
    (count v) => 5
    v => vector?
    (every? number? v) => true
    (every? #(<= 1 % 10) v) => true
    (every? #(= % (/ (Math/round (* 100 %)) 100.0)) v) => true))

;; The result will change depending on the expert system logic.
;; Currently, only one test case is used.
;; Tip - click the down-arrow (or whatever) in the IDE to collapse/expand the code block.
(facts "recommended-it-job-positions"
  (sut/recommended-it-job-positions
    {:data           9
     :statistics     8
     :math           7
     :engineering    0
     :algorithms     7
     :optimization   8
     :hardware       0
     :physics        0
     :geometry       10
     :ui             0
     :people         0
     :ux             0
     :testing        0
     :edge-cases     0
     :debugging      0
     :empathy        0
     :simplification 0
     :patience       0
     :monotony       0
     :money          0})
  => (contains ["data analyst, data science, ML, AI" 
                "game development, simulations, graphics"]
                :in-any-order))
