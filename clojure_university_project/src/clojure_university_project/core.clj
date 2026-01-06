(ns clojure-university-project.core
  (:require 
    [clojure-university-project.utils :as utils]
    [clojure-university-project.data-transformations :as data-transformations]
    [clojure-university-project.expert-system :as expert-system]
    [clojure-university-project.ui :as ui]
    [clojure-university-project.data :as data]
    
    [clojure.string :as str])
  (:gen-class))

;; HACK: single responsibility principle violation
(defn run-app 
  []
  (println "Rate each topic from 0 to 10 (decimals allowed) based on how interested you are in it
            and how good you think you are at it.")
   (let [
        ratings (into {} (for [{:keys [id q]} data/questions]
                          [id (ui/ask-for-it-topics-ratings q)]))
                          
        average (data-transformations/average (vals ratings))
        it-job-suitability (ui/it-job-suitability-messages average)
        strengths (utils/it-skills-by-rating-threshold ratings >= 7)
        weaknesses (utils/it-skills-by-rating-threshold ratings <= 4)
        job-positions (expert-system/recommended-it-job-positions ratings)]
        ;;(println ratings)
        ;;(println job-positions)
    (println (format "Average interest: %.2f" average))
    (println it-job-suitability)
    (println "Strong skills:" (str/join ", " strengths))
    (println "Weak skills:" (str/join ", " weaknesses))
    (println "Recommended IT job positions:")
    (doseq [line (-> job-positions
                 (data-transformations/power-kv 4)
                 (data-transformations/normalize-to-percent-kv)
                 (data-transformations/sort-desc-kv)
                 (data-transformations/format-it-job-position-recommendations)
                 )]
    (println line))))

(defn -main
  [& args]
  (run-app))
