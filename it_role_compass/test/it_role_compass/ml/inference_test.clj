(ns it-role-compass.ml.inference-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.ml.inference :as sut]
            [it-role-compass.ml.pipeline :as pipe]
            [it-role-compass.ml.model :as model]
            [it-role-compass.ml.config :as config]
            [tech.v3.dataset :as ds]))

(fact "rank-predicted-probabilities"
  ;; The dataset values don't affect the test
  (let [test-ds       (ds/->dataset [{:skill-a "Not interested" :skill-b "Average"        :skill-c "Beginner"}
                                     {:skill-a "Not interested" :skill-b "Not interested" :skill-c "Average"}
                                     {:skill-a "Intermediate"   :skill-b "Average"        :skill-c "Intermediate"}])
        
        new-sample    (ds/->dataset [{:skill-a "Intermediate"   :skill-b "Average"        :skill-c "Average"}])
        
        mock-pipeline :pipeline-object
        mock-context  :context-map
        labels        ["IT Support" "Web Developer" "Cyber Security"]
        
        mock-output   (ds/->dataset [{"Web Developer" 0.33 "IT Support" 0.33 "Cyber Security" 0.34}
                                   ;; Row 1 probabilities (the new-sample row, row 0 is the test-ds row)
                                    {"Web Developer" 0.06 "IT Support" 0.73 "Cyber Security" 0.21}])]

    (sut/rank-predicted-probabilities test-ds new-sample mock-pipeline mock-context labels)
    => {:it-job-position-predictions [["IT Support"     0.73] 
                                      ["Cyber Security" 0.21] 
                                      ["Web Developer"  0.06]]}

    (provided
      (pipe/run-pipeline anything mock-pipeline mock-context) => mock-output)))

(fact "predict!"
  ;; The dataset values don't affect the test
  (let [new-sample       ["Average" "Intermediate" "Beginner"]
        mock-model-data  {:feature-columns    [:skill-a :skill-b :skill-c]
                          :labels             ["IT Support" "Web Developer"]
                          :reference-dataset  :mock-test-dataset
                          :model-pipeline     :mock-pipeline
                          :fit-context        :mock-context
                          :accuracy           0.77}
        
        mock-predictions [["IT Support" 0.88] ["Web Developer" 0.12]]]

    (sut/predict! new-sample) => {:it-job-position-predictions mock-predictions
                                  :accuracy                    0.77}

    (provided
      (model/initialize-model! config/RANDOM-FOREST-MODEL) => mock-model-data
      
      (sut/rank-predicted-probabilities :mock-test-dataset 
                                        anything 
                                        :mock-pipeline 
                                        :mock-context 
                                        ["IT Support" "Web Developer"]) => {:it-job-position-predictions mock-predictions})))