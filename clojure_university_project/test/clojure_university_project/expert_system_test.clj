(ns clojure-university-project.expert-system-test
  (:require [midje.sweet :refer :all]
            [clojure-university-project.expert-system :as sut]))

;; The result will change every time the expert system logic changes.
(facts "recommended-it-job-positions"
  (sut/recommended-it-job-positions
    {:data           9
     :statistics     9
     :math           10
     :engineering    6
     :algorithms     9
     :optimization   9
     :hardware       4
     :physics        8
     :geometry       10
     :ui             3
     :people         2
     :ux             4
     :testing        5
     :edge-cases     7
     :debugging      8
     :empathy        2   
     :simplification 6
     :patience       8
     :monotony       3
     :money          8})
  => (contains
       {"UX/UI design" (roughly 2.76 0.01)
        "Embedded systems, IoT, firmware, robotics, hardware-related" (roughly 5.80 0.01)
        "Cyber security" (roughly 7.50 0.01)
        "Backend development, databases" (roughly 7.85 0.01)
        "Data analytics, data science, ML, AI" (roughly 9.28 0.01)
        "Game development, simulations, graphics-related" (roughly 9.48 0.01)
        "DevOps" (roughly 5.90 0.01)
        "Frontend development, mobile app development" (roughly 3.8 0.01)
        "QA, test automation" (roughly 5.96 0.01)}))
