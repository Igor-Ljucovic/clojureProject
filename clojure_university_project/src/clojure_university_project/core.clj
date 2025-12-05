(ns clojure-university-project.core (:gen-class))

(def questions
  ["How interesting do you find working with large amounts of data (1-10)?"
   "How interesting do you find math (1-10)?"
   "How interesting do you find designing/engineering new things and how they work (1-10)?"
   "How interesting do you find working with hardware (1-10)?"
   "How interesting do you find making things look nice and be intuitive to users (1-10)?"
   "How interesting do you find testing, thinking about edge cases and predicting bugs in code (1-10)?"
  ]
)

(defn ask-for-it-topics-ratings [question]
  (println question)
  (flush)
  (Integer/parseInt (read-line))
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

(defn recommend-it-job-jositions
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
      (conj "data/analytics/ML/AI,")
      (and (>= engineering 7) (>= math 5))
      (conj "backend, systems, or DevOps,")
      (and (>= uiux 7) (>= engineering 4))
      (conj "frontend development or UI/UX-heavy,")
      (and (>= testing 8))
      (conj "QA, testing, or test automation,")
      (and (>= hardware 7) (>= engineering 6) (>= math 6))
      (conj "embedded, IoT, or hardware related,"))
    ]
    (if (empty? results)
    ["Your interests are mixed; explore different fields 
    (frontend, backend, data, QA) and see what fits you."]
    results
    )
  )
)

(defn trim-last-vector-element-comma-and-whitespace [vector]
  (let [last-index (dec (count vector))
        last-item  (nth vector last-index)
        trimmed    (clojure.string/replace last-item #"[,\s]+$" "")]
    (assoc vector last-index trimmed)
  )
)

(defn it-job-position-suitability-messages
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
  (println "Rate each topic from 1 (hate it) to 10 (love it), don't use decimals (like 7.5).\n")

  (let [ratings    (vec (map ask-for-it-topics-ratings questions))
        profile    (build-profile ratings)
        total      (reduce + ratings)
        average    (/ (double total) (count ratings))
        strengths  (strong-areas profile 7)
        weaknesses (weak-areas profile 4)
        path-msg   (recommend-it-job-jositions profile)
        suit-msg   (it-job-position-suitability-messages average)]

    (println "\nYour ratings:" (vec ratings))
    (println (format "Average interest: %.2f" average))
    (println suit-msg)
    (println "Strong areas:" strengths)
    (println "Weak areas:" weaknesses)
    (println "Recommended IT job positions:" (trim-last-vector-element-comma-and-whitespace path-msg))
  )
)
