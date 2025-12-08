(ns clojure-university-project.core (:gen-class))

(def questions
  ["How interesting do you find working with large amounts of data (0-10)?"
   "How interesting do you find math (0-10)?"
   "How interesting do you find designing/engineering new things and how they work (0-10)?"
   "How interesting do you find working with hardware (0-10)?"
   "How interesting do you find making things look nice and be intuitive to users (0-10)?"
   "How interesting do you find testing, thinking about edge cases and predicting bugs in code (0-10)?"
  ]
)

(defn ask-for-it-topics-ratings [question]
  (println question)
  (flush)
  (let [v (Double/parseDouble (read-line))]
    (when (or (< v 0) (> v 10)) 
      (throw (ex-info "Value must be between 0 and 10." {:value v}))
    ) v
  )
)

(defn build-profile
  [ratings]
  {"data"        (nth ratings 0) 
   "math"        (nth ratings 1)
   "engineering" (nth ratings 2)
   "hardware"    (nth ratings 3)
   "uiux"        (nth ratings 4)
   "testing"     (nth ratings 5)
  }
)

(defn strong-areas
  [profile threshold]
  (->> profile
       (filter (fn [[_ score]] (>= score threshold)))
       (map first)
       (sort)
       (vec)
  )
)

(defn weak-areas
  [profile threshold]
  (->> profile
       (filter (fn [[_ score]] (<= score threshold)))
       (map first)
       (sort)
       (vec)
  )
)

(defn recommended-it-job-jositions
  [profile]
  (let  
    [
      data        (get profile "data")
      math        (get profile "math")
      engineering (get profile "engineering")
      hardware    (get profile "hardware")
      uiux        (get profile "uiux")
      testing     (get profile "testing")

      results (cond-> []
      (and (>= data 8) (>= math 6))
      (conj "data/analytics/ML/AI")
      (and (>= engineering 7) (>= math 5))
      (conj "backend, systems, or DevOps")
      (and (>= uiux 7) (>= engineering 4))
      (conj "frontend development or UI/UX-heavy")
      (and (>= testing 8))
      (conj "QA, testing, or test automation")
      (and (>= hardware 7) (>= engineering 6) (>= math 6))
      (conj "embedded, IoT, or hardware related"))
    ]
    (if (empty? results)
      [(str "Your interests are mixed; explore different fields "
      "(frontend, backend, data, QA etc.) and see what fits you.")]
      results
    )
  )
)

(defn it-job-suitability-messages
  [average]
  (cond
    (>= average 8)
    "You are very likely to enjoy working in IT."
    (>= average 6)
    "You probably should work in the IT sector."
    (>= average 4)
    "IT might be okay for you, but consider it as one of several options."
    :else
    "You probably shouldn't work in the IT sector."
  )
)

(defn -main
  [& args]
  (println "Rate each topic from 1 (hate it) to 10 (love it).")

  (let 
    [ 
      ratings (vec (map ask-for-it-topics-ratings questions))
      profile (build-profile ratings)
      total (reduce + ratings)
      average (/ (double total) (count ratings))
      it-jobs-suitability (it-job-suitability-messages average)
      strengths (strong-areas profile 7)
      weaknesses (weak-areas profile 4)
      recommended-it-jobs-positions (recommended-it-job-jositions profile)
    ]

    (println (format "Average interest: %.2f" average))
    (println it-jobs-suitability)
    (println "Strong areas:" (clojure.string/join ", " strengths))
    (println "Weak areas:" (clojure.string/join ", " weaknesses))
    (println "Recommended IT job positions:" (clojure.string/join ", " recommended-it-jobs-positions))
  )
)
