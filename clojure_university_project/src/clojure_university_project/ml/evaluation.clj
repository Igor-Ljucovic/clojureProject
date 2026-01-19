(ns clojure-university-project.ml.evaluation
  (:require
    [tech.v3.dataset :as ds]
    
    [clojure-university-project.ml.model :as model]
    [clojure-university-project.ml.config :as config]))

(defn predict! 
  [user-skills]
  (let [{:keys [feature-columns roles test pipe fit-context accuracy]} (model/init-model!)
        ;; "(first roles)" is the dummy target value; it won't be used in prediction.
        new-person (ds/->dataset [(assoc (zipmap feature-columns user-skills)
                                         config/TARGET-COLUMN
                                         (first roles))])
        {:keys [it-job-position-predictions]} (model/predict-probabilities test new-person pipe fit-context roles)]
    {:it-job-position-predictions it-job-position-predictions
     :accuracy                    accuracy}))

(defn print-report!
  [{:keys [accuracy it-job-position-predictions]}]
  (println (format "Model accuracy: %.2f%%" (* 100 (double accuracy))))
  (println "All job probabilities:")
  (doseq [[it-job-position probability] it-job-position-predictions]
    (println (format "%5.2f%% %s" (* 100 (double probability)) it-job-position))))
