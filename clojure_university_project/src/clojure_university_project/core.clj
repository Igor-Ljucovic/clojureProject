(ns clojure-university-project.core
  (:require [criterium.core :as c]
            [clojure.string :as str])
  (:gen-class))

(def ^:private questions
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
   "How much do you care about how much money you'll make? (0-10)"])

(def ^:private score-label-separator "|")
(def ^:private score-label-separator-regex
  (re-pattern (java.util.regex.Pattern/quote score-label-separator)))

;; had to separate parsing to avoid "Cannot recur across try" error
(defn- parse-rating-double
  [s]
  (try
    (Double/parseDouble s)
    (catch NumberFormatException _ nil)))

(defn ask-for-it-topics-ratings
  [question]
  (loop []
    (println question)
    (flush)
    (let [input (read-line)
          value (parse-rating-double input)]
      (if (and value (<= 0 value 10))
        value
        (do
          (println "Please enter a number from 0 to 10 (decimals allowed).")
          (recur))))))

;; HACK: code duplication
(defn build-scores
  [[data math algorithms physics geometry abstraction optimization engineering hardware ui edge-cases testing
    people ux statistics empathy simplification patience debugging monotony money]]
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
   :money             money})

(defn it-areas-by-score-threshold
  [scores comparison-operator threshold]
  (->> scores
       (filter (fn [[_ score]] (comparison-operator score threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

(defn score-with-balanced-weights 
  "Calculates a weighted score using balanced (not yet normalized) weights.

  The absolute values of weights don't matter, only their proportions.
  For example, weights {:data 8 :math 6} and {:data 80 :math 60} return the same result.
  The number of weights also doesn't matter (3, 4, 10, etc.).

  ratings:
    Map of raw scores, e.g. {:data 8 :math 9}

  weights:
    Map of weights per key, e.g. {:data 16 :math 12}

  label:
    String label describing the score,
    e.g. \"Backend development, systems engineering\"

  Returns:
    A string in the form \"<score>score-label-separator<label>\"

  Note:
    Normalization (to percentages so that they total to 100%) is done in a later step."
  [ratings weights label]
  (let [num (reduce-kv (fn [acc key value] (+ acc (* value (get ratings key)))) 0 weights)
        den (reduce + (vals weights))]
    (str (double (/ num den)) score-label-separator label)))


;; HACK: code duplication
(defn recommended-it-job-positions
  [m]
  (let [data           (:data m)
        math           (:math m)
        algorithms     (:algorithms m)
        physics        (:physics m)
        geometry       (:geometry m)
        abstraction    (:abstraction m)
        optimization   (:optimization m)
        engineering    (:engineering m)
        hardware       (:hardware m)
        ui             (:ui m)
        edge-cases     (:edge-cases m)
        testing        (:testing m)
        people         (:people m)
        ux             (:ux m)
        statistics     (:statistics m)
        empathy        (:empathy m)
        simplification (:simplification m)
        patience       (:patience m)
        debugging      (:debugging m)
        monotony       (:monotony m)
        money          (:money m)]
    (-> []
        (conj (score-with-balanced-weights m
               {:data (* data 160) :statistics (* statistics 140) :math (* math 120)}
               "Data analytics, data science, ML, AI"))
        (conj (score-with-balanced-weights m
               {:engineering (* engineering 16) :algorithms (* algorithms 14) :optimization (* optimization 12)}
               "Backend development, systems engineering"))
        (conj (score-with-balanced-weights m
               {:engineering (* engineering 14) :debugging (* debugging 16) :monotony (* monotony 12)}
               "DevOps"))
        (conj (score-with-balanced-weights m
               {:hardware (* hardware 16) :engineering (* engineering 12) :physics (* physics 12)}
               "Embedded systems, IoT, firmware, robotics, hardware-related"))
        (conj (score-with-balanced-weights m
               {:geometry (* geometry 14) :algorithms (* algorithms 14) :optimization (* optimization 14) :math (* math 12)}
               "Game development, simulations, graphics"))
        (conj (score-with-balanced-weights m
               {:ui (* ui 16) :simplification (* simplification 14) :people (* people 10)}
               "Frontend development, mobile development"))
        (conj (score-with-balanced-weights m
               {:ux (* ux 16) :empathy (* empathy 16) :people (* people 10)}
               "UX/UI design, product design"))
        (conj (score-with-balanced-weights m
               {:testing (* testing 16) :edge-cases (* edge-cases 16) :debugging (* debugging 14) :monotony (* monotony 10)}
               "QA engineering, test automation"))
        (conj (score-with-balanced-weights m
               {:edge-cases (* edge-cases 16) :debugging (* debugging 16)}
               "Cyber security")))))

(defn it-job-suitability-messages 
  [average]
  (cond
    (>= average 8) "You are very likely to enjoy working in IT."
    (>= average 6) "You probably should work in the IT sector."
    (>= average 4) "The IT field might be okay for you, but consider it as one of several options."
    :else          "You probably shouldn't work in the IT sector."))

; used for testing purposes, as a stub for "ask-for-it-topics-ratings" questions for ratings
(defn random-ratings
  [n min-val max-val]
  (vec
    (repeatedly n
      #(let [rand-num (+ min-val (* (rand) (- max-val min-val)))] 
       (/ (Math/round (* 100 rand-num)) 100.0)))))

(defn power-transform-job-scores [jobs power]
  "Power is used to adjust the distribution of scores so that the users can better know
  which jobs suit them best. Instead of linear scaling, exponential scaling is used.
  power=4 is recommended, since it will differentiate better between close scores. 
  Instead of having almost all the percentages in the 15-25% range, it will spread them out more."
  (map #(let [[score label] (clojure.string/split % score-label-separator-regex)] 
          [(Math/pow (Double/parseDouble score) power) label]) jobs))

(defn normalize-job-scores-to-percent [pairs]
  (let [total (apply + (map first pairs))]
    (map (fn [[score label]] [(* 100 (/ score total)) label]) pairs)))

(defn rank-job-scores-desc
  [score-label-pairs]
  (sort-by first > score-label-pairs))

(defn format-jobs 
  [normalized-data]
  (map (fn [[score label]] (format "%.1f%% %s" score label)) 
       normalized-data))

;; HACK: single responsibility principle violation
(defn run-app 
  []
  (println "Rate each topic from 0 (hate it) to 10 (love it).")
  (let [ratings (mapv ask-for-it-topics-ratings questions)
        scores (build-scores ratings)
        total (reduce + ratings)
        average (/ (double total) (count ratings))
        it-job-suitability (it-job-suitability-messages average)
        strengths (it-areas-by-score-threshold scores >= 7)
        weaknesses (it-areas-by-score-threshold scores <= 4)
        job-positions (recommended-it-job-positions scores)]
    (println (format "Average interest: %.2f" average))
    (println it-job-suitability)
    (println "Strong areas:" (str/join ", " strengths))
    (println "Weak areas:" (str/join ", " weaknesses))
    (println "Recommended IT job positions:")
    (doseq [line (-> job-positions
                 (power-transform-job-scores 4)
                 (normalize-job-scores-to-percent)
                 (rank-job-scores-desc)
                 (format-jobs))]
    (println line))))

(defn -main
  [& args]
  (run-app))
