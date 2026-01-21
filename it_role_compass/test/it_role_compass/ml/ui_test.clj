(ns it-role-compass.ml.ui-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.ml.ui :as sut]
            [clojure.string :as str]))

(fact "machine-learning-report->string"
  (let [data {:accuracy 0.7560
                   :it-job-position-predictions [["Cyber Security" 0.601]
                                                 ["IT support"     0.327]
                                                 ["Backend tester" 0.072]]}
        result (sut/machine-learning-report->string data)
        lines  (str/split-lines result)]
    
    result => (str "Model accuracy: 75.60%\n"
                   "All job probabilities:\n"
                   "60.10% Cyber Security\n"
                   "32.70% IT support\n"
                   " 7.20% Backend tester")))