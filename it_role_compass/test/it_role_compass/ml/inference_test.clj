(ns it-role-compass.ml.inference-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.ml.inference :as sut]
            [it-role-compass.ml.pipeline :as pipe]
            [tech.v3.dataset :as ds]))

(fact "rank-predicted-probabilities"
  ;; The dataset values don't affect the test
  (let [test-ds     (ds/->dataset [{:skill-a "Not interested" :skill-b "Average"        :skill-c "Beginner"}
                                   {:skill-a "Not interested" :skill-b "Not interested" :skill-c "Average"}
                                   {:skill-a "Intermediate"   :skill-b "Average"        :skill-c "Intermediate"}])
        
        new-p       (ds/->dataset [{:skill-a "Intermediate"   :skill-b "Average"        :skill-c "Average"}])
        
        mock-pipe   :pipeline-object
        mock-ctx    :context-map
        roles       ["IT Support" "Web Developer" "Cyber Security"]
        
        mock-output (ds/->dataset [{"Web Developer" 0.33 "IT Support" 0.33 "Cyber Security" 0.34}
                                   ;; Row 1 probabilities (the new-person row, row 0 is the test-ds row)
                                   {"Web Developer" 0.06 "IT Support" 0.73 "Cyber Security" 0.21}])]

    (sut/rank-predicted-probabilities test-ds new-p mock-pipe mock-ctx roles)
    => {:it-job-position-predictions [["IT Support"     0.73] 
                                      ["Cyber Security" 0.21] 
                                      ["Web Developer"  0.06]]}

    (provided
      (pipe/run-pipeline anything mock-pipe mock-ctx) => mock-output)))
