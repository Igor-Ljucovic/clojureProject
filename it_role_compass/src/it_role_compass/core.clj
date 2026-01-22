(ns it-role-compass.core
  (:require 
    [it-role-compass.utils :as utils]
    [it-role-compass.data-transformations :as dt]
    [it-role-compass.expert-system :as es]
    [it-role-compass.ui :as ui]
    [it-role-compass.data :as data]
    [it-role-compass.ml.config :as config]
    [it-role-compass.ml.inference :as inference]
    [it-role-compass.ml.ui :as ml-ui])
  (:gen-class))

(defn- run-app
  []
  (println (ui/app-intro->string))
  (let [user-ratings               (ui/ask-all-ratings! data/questions)
        ml-quant-ratings           (es/expert-system->ml-ratings-data-refactor user-ratings)
        ml-qual-ratings-unordered  (dt/quant->qual ml-quant-ratings config/ML-LABELS 0 10)
        ml-ratings                 (utils/ordered-values ml-qual-ratings-unordered config/ML-FEATURE-ORDER)]
    (println (ui/general-summary->string user-ratings))
    (println (ui/it-job-position-predictions->string (es/it-job-position-expert-system-recommendations user-ratings)))
    (println (ml-ui/machine-learning-report->string (inference/predict! ml-ratings)))))

(defn- -main
  [& args]
  (run-app))