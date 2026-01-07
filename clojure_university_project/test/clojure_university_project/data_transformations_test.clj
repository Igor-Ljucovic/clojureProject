(ns clojure-university-project.data-transformations-test
  (:require [midje.sweet :refer :all]
            [clojure-university-project.data-transformations :as sut]))

;; must add contains in some functions because roughly won't be evaluated otherwise

(fact "normalize-to-percent-kv"
  (sut/normalize-to-percent-kv
    {"Data analytics, data science, ML, AI" 260
     "Backend development, databases" 160})
    ;; (260+160) = 420, 260/420 = 61.90..., 160/420 = 38.09...
  => (contains {"Data analytics, data science, ML, AI" (roughly 61.90 0.01) 
                "Backend development, databases"       (roughly 38.09 0.01)}))

(fact "format-it-job-position-recommendations"
  (sut/format-it-job-position-recommendations 
    {"Data analytics" 61.904 
      "Backend"       38.095})
  => (contains ["61.9% Data analytics" 
                "38.1% Backend"]))

(fact "quant->qual"
  (let [scores {"Backend" 1.5
                "Frontend" 5
                "AI" 9.0
                "DevOps" 10.0}
        labels ["Not interested" "Beginner" "Poor" "Average" "Intermediate" "Professional"]
        min-val  0
        max-val  10]
    (sut/quant->qual scores labels min-val max-val)
    ;; (10+0)/6 = 1.66 step -> <1.66 is "Not interested" (backend), >1.66 and <3.33 is "Beginner", etc.
    => {"Backend"  "Not interested"
        "Frontend" "Average"
        "AI"       "Professional"
        "DevOps"   "Professional"}))