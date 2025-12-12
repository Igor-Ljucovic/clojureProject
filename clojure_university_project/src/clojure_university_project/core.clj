(ns clojure-university-project.core (:gen-class))

(def questions
  ["How interesting do you find working with large amounts of data and trying to find useful conclusions in it? (0-10)"
   "How good are you at advanced math (integrals, derivatives, functions with multiple variables)? (0-10)"
   "How interesting do you find data structures and solving algorithmic problems? (0-10)"
   "How good are you at mechanical and electrical physics? (0-10)"
   "How interesting does working with 2D and 3D geometry in programming sound to you (0-10)?"
   "How fun does abstracting to more general rules sound to you? (0-10)"
   "How interesting does optimizing memory and making things run as fast and efficiently as possible sound to you? (0-10)"
   "How interesting do you find designing/engineering new things and how they work? (0-10)"
   "How interesting do you find working with hardware? (0-10)"
   "How fun does making things look nice and be simple and intuitive sound? (0-10)"
   "How good are you at identifying weak spots in things or people? (0-10)"
   "How good are you at finding the only wrong way to do something? (0-10)"
   "How interesting does testing, thinking about edge cases and predicting bugs in code sound? (0-10)"
   "How interesting do you find working with people, understanding their problems and solving them? (0-10)"
   "How much do you care about how technology feels in everyday life and how people interact with it moment-to-moment? (0-10)"
   "How exciting is the idea of building something interactive that reacts differently based on the user's actions? (0-10)"
   "How fun is it for you to imagine how something could go wrong, and how you'd prevent it? (0-10)"
   "How much does it interest you to make things that can grow from small to huge without breaking? (0-10)"
   "How interesting does statistics and probability sound to you? (0-10)"
   "How naturally does it come to you to predict what someone will find confusing or difficult before they even try it? (0-10)"
   "How satisfying is it for you to take messy information and structure it in a clean, efficient way? (0-10)"
   "How patient are you when someone doesn't understand something, and you need to explain it calmly? (0-10)"
   "How willing are you to spend a long time figuring out why something doesn't work, even if the reason is tiny or obscure? (0-10)"
   "Would you mind solving the same problems the majority of the time at work? (0-10)"
   "How much do you care about how much money you'll make? (0-10)"
  ]
)

;had to seperate parsing to avoid "Cannot recur across try" error"
(defn parse-double [s]
  (try
    (Double/parseDouble s)
    (catch NumberFormatException _ nil)
  )
)

(defn ask-for-it-topics-ratings [question]
  (loop []
    (println question)
    (flush)
    (let 
      [input (read-line)
      value (parse-double input)]
      (if (and value (<= 0 value 10))
        value
        (do
          (println "Please enter a number from 0 to 10 (decimals allowed).")
          (recur)
        )
      )
    )
  )
)

(defn build-profile
  [[data math algorithms physics geometry abstraction optimization engineering hardware ui edge-cases testing people ux statistics
    empathy simplification patience debugging monotony money]]
  {:data              data
   :math              math
   :algorithms        algorithms
   :physics           physics
   :geometry          geometry
   :abstraction       abstraction
   :optimization      optimization
   :engineering       engineering
   :hardware          hardware
   :ui                ui
   :edge-cases        edge-cases
   :testing           testing
   :people            people
   :ux                ux
   :statistics        statistics
   :empathy           empathy
   :simplification    simplification
   :patience          patience
   :debugging         debugging
   :monotony          monotony
   :money             money
  }
)

(defn strong-areas
  [profile threshold]
  (let [filtered (filter (fn [[_ score]] (>= score threshold)) profile)
    names (map (fn [[k _]] (name k)) filtered)]
    (vec (sort names))
  )
)

(defn weak-areas
  [profile threshold]
  (let [filtered (filter (fn [[_ score]] (<= score threshold)) profile)
    names (map (fn [[k _]] (name k)) filtered)]
    (vec (sort names))
  )
)

(defn recommended-it-job-jositions
  [{:keys [data math algorithms physics geometry abstraction optimization engineering hardware ui edge-cases testing people ux statistics
    empathy simplification patience debugging monotony money]}]
  (let 
    [results 
      (cond-> []
          (and (>= data 8) (>= statistics 7) (>= math 6))
          (conj "data analyst, data science, ML/AI, AI")

          (and (>= engineering 7) (>= algorithms 7) (>= optimization 7))
          (conj "backend development, systems engineering")

          (and (>= engineering 6) (>= debugging 7) (>= monotony 6))
          (conj "DevOps")

          (and (>= hardware 7) (>= engineering 6) (>= physics 6))
          (conj "embedded systems, IoT, firmware, robotics, hardware-related")

          (and (>= geometry 7) (>= algorithms 7) (>= optimization 7) (>= math 7))
          (conj "game development, simulations, graphics")

          (and (>= ui 7) (>= people 6) (>= simplification 7))
          (conj "frontend development, mobile development")

          (and (>= ux 7) (>= empathy 7) (>= people 6))
          (conj "UX/UI design, product design")

          (and (>= testing 8) (>= edge-cases 8) (>= debugging 7) (>= monotony 6))
          (conj "QA engineering, test automation")

          (and (>= edge-cases 8) (>= debugging 8))
          (conj "security")
      )
    ]
    (if (empty? results)
      [
        (str "Your interests are mixed; explore different fields "
        "(frontend, backend, data, QA etc.) and see what fits you.")
      ]
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
    "The IT field might be okay for you, but consider it as one of several options."
    :else
    "You probably shouldn't work in the IT sector."
  )
)

(defn run-app []
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

(defn -main
  [& args]
  (run-app)
)
