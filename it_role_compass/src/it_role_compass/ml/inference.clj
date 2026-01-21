(ns it-role-compass.ml.inference
  (:require
    [tech.v3.dataset :as ds]
    [it-role-compass.utils :as utils]
    [it-role-compass.ml.config :as config]
    [it-role-compass.ml.pipeline :as pipe]
    [it-role-compass.ml.model :as model]))

(defn require-smile! []
  (utils/silently (require 'scicloj.ml.smile.classification)))

(defn predict-probabilities 
  [test-dataset new-person model-pipe fit-context roles]
  (let [prediction-dataset (pipe/transform-data (ds/concat test-dataset new-person) model-pipe fit-context)
        row                (ds/row-at prediction-dataset (dec (ds/row-count prediction-dataset)))
        probabilities      (->> roles
                             (map (fn [r] [r (double (get row r 0.0))]))
                             (sort-by second >))]
    {:it-job-position-predictions probabilities}))

(defn predict! 
  [user-skills]
  (let [{:keys [feature-columns roles test pipe fit-context accuracy]} (model/init-model!)
        ;; "(first roles)" is the dummy target value; it won't be used in prediction.
        new-person (ds/->dataset [(assoc (zipmap feature-columns user-skills)
                                         config/TARGET-COLUMN
                                         (first roles))])
        {:keys [it-job-position-predictions]} (predict-probabilities test new-person pipe fit-context roles)]
    {:it-job-position-predictions it-job-position-predictions
     :accuracy                    accuracy}))