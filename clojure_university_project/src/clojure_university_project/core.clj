(ns clojure-university-project.core
  (:gen-class))

(def questions
  ["How interesting do you find working with large amounts of data (1-10)?"
   "How interesting do you find math (1-10)?"
   "How interesting do you find designing/engineering new things and how they work (1-10)?"
   "How interesting do you find working with hardware (1-10)?"
   "How interesting do you find making things look nice and be intuitive to users (1-10)?"
   "How interesting do you find testing, thinking about edge cases and predicting bugs in code (1-10)?"
   ])

(defn ask-rating [question]
  (println question)
  (flush)
  (Integer/parseInt (read-line)))

(defn -main
  [& args]
  (println "Rate each topic from 1 (hate it) to 10 (love it), don't use decimals (like 7.5).")
  (println)

  (let [ratings (map ask-rating questions)
        total   (reduce + ratings)
        avg     (/ (double total) (count ratings))]

    (println "\nYour ratings:" (vec ratings))
    (println (format "Average interest: %.2f" avg))
    (if (>= avg 6)
      (println "You probably should work in the IT sector.")
      (println "You probably shouldn't work in the IT sector.")
    )
  )
)
