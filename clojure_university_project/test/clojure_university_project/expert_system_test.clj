(ns clojure-university-project.expert-system-test
  (:require [midje.sweet :refer :all]
            [clojure-university-project.expert-system :as sut]))

;; must add "contains" in some functions because roughly won't be evaluated otherwise

(facts "balance-weights"
  (let [ratings {:data 4   :statistics 8   :math 7}
        weights {:data 8   :statistics 7   :math 6}]
    (sut/balance-weights ratings weights)
    ;; (4*160 + 8*140 + 7*120) / (160+140+120) = 2600/420 = 6.190476...
    => (roughly 6.19 0.01)))

  (fact "balance-weights - scaling all weights by the same factor doesn't change the result"
    (let [ratings {:data 4   :statistics 8   :math 7}
          w1      {:data 16  :statistics 14  :math 12}
          w2      {:data 160 :statistics 140 :math 120}]
      (sut/balance-weights ratings w1)
      => (roughly (sut/balance-weights ratings w2) 0.01)))

;; The result will change every time the expert system logic changes.
(facts "recommended-it-job-position-weights"
  (sut/recommended-it-job-position-weights
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
     :money          8
     :abstraction    7
     :analysis       5
     :wrong-way      3
     :interactivity  0
     :scalability    8})
     ;; the values seem arbitrary, but they are temporary and are used for skill comparison
  => (contains
       {"UX/UI design"                                                (roughly 2.76 0.01)
        "Embedded systems, IoT, firmware, robotics, hardware-related" (roughly 5.80 0.01)
        "Cyber security"                                              (roughly 7.50 0.01)
        "Backend development, databases"                              (roughly 7.85 0.01)
        "Data analytics, data science, ML, AI"                        (roughly 9.28 0.01)
        "Game development, simulations, graphics-related"             (roughly 9.48 0.01)
        "DevOps"                                                      (roughly 5.90 0.01)
        "Frontend development, mobile app development"                (roughly 3.80 0.01)
        "QA, test automation"                                         (roughly 5.96 0.01)}))

;; The result will change every time the expert system logic changes.
(facts "expert-system->ml-ratings-data-refactor"
  (sut/expert-system->ml-ratings-data-refactor 
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
     :money          8
     :abstraction    7
     :analysis       5
     :wrong-way      3
     :interactivity  0
     :scalability    8})
     ;; (testing + wrong-way + edge-cases)/3 = (5+3+7)/3 = 5.0 etc.
  => (contains {"AI ML"                              (roughly 8.80 0.01)
                "Business analysis and data science" (roughly 6.20 0.01)
                "Communication"                      (roughly 4.50 0.01)
                "Communication skills"               (roughly 4.50 0.01)
                "Core Technical"                     (roughly 8.00 0.01)
                "Databases"                          (roughly 8.60 0.01)
                "Hardware"                           (roughly 7.00 0.01)
                "IT graphics designing"              (roughly 4.25 0.01)
                "Networking"                         (roughly 6.00 0.01)
                "Programming skills"                 (roughly 8.25 0.01)
                "Project management"                 (roughly 6.40 0.01)
                "Security"                           (roughly 5.75 0.01)
                "Software development"               (roughly 7.50 0.01)
                "Software engineering"               (roughly 6.80 0.01)}))
                