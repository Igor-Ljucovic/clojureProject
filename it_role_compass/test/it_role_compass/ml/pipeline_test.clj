(ns it-role-compass.ml.pipeline-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.ml.pipeline :as sut]
            [tech.v3.dataset :as ds]))

(fact "load-dataset"
  (let [path         "file_name_sample.csv"
        target       :job-role
        mock-dataset (ds/->dataset [{:skill-a "Not interested" :skill-b "Average"        :job-role "Web Developer"}
                                    {:skill-a "Not interested" :skill-b "Not interested" :job-role "IT Support"}
                                    {:skill-a "Intermediate"   :skill-b "Average"        :job-role "IT Support"}])]

    (sut/load-dataset path target) => (contains {:feature-columns [:skill-a :skill-b]
                                                 :labels          ["IT Support" "Web Developer"]
                                                 :dataset         mock-dataset})
    (provided
      (ds/->dataset path {:key-fn keyword}) => mock-dataset)))
