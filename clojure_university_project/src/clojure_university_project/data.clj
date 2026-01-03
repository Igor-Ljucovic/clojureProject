(ns clojure-university-project.data)

(def questions
  [{:id :data           :q "How interesting do you find working with large amounts of data? (0-10)"}
   {:id :math           :q "How good are you at advanced math (integrals, derivatives)? (0-10)"}
   {:id :algorithms     :q "How interesting do you find data structures and algorithmic problems? (0-10)"}
   {:id :physics        :q "How good are you at mechanical and electrical physics? (0-10)"}
   {:id :geometry       :q "How interesting does working with 2D and 3D geometry sound? (0-10)"}
   {:id :abstraction    :q "How fun does abstracting to more general rules sound? (0-10)"}
   {:id :optimization   :q "How interesting does optimizing memory/speed sound? (0-10)"}
   {:id :engineering    :q "How interesting do you find designing/engineering new things? (0-10)"}
   {:id :hardware       :q "How interesting do you find working with hardware? (0-10)"}
   {:id :ui             :q "How fun does making things look nice and simple sound? (0-10)"}
   {:id :analysis       :q "How good are you at identifying weak spots in things or people? (0-10)"}
   {:id :wrong-way      :q "How good are you at finding the only wrong way to do something? (0-10)"}
   {:id :testing        :q "How interesting does testing and predicting bugs sound? (0-10)"}
   {:id :people         :q "How interesting do you find working with and understanding people? (0-10)"}
   {:id :ux             :q "How much do you care about how technology feels and interacts? (0-10)"}
   {:id :interactivity  :q "How exciting is the idea of building interactive UI? (0-10)"}
   {:id :edge-cases     :q "How fun is it to imagine how something could go wrong? (0-10)"}
   {:id :scalability    :q "How much does it interest you to make things that grow huge? (0-10)"}
   {:id :statistics     :q "How interesting does statistics and probability sound? (0-10)"}
   {:id :empathy        :q "How naturally do you predict what someone will find confusing? (0-10)"}
   {:id :simplification :q "How satisfying is it to take messy info and structure it simply? (0-10)"}
   {:id :patience       :q "How patient are you when you need to explain things calmly? (0-10)"}
   {:id :debugging      :q "How willing are you to spend a long time finding tiny bugs? (0-10)"}
   {:id :monotony       :q "Would you mind solving the same problems the majority of the time? (0-10)"}
   {:id :money          :q "How much do you care about how much money you'll make? (0-10)"}])

(def job-weight-sets
  [{:label "Data analytics, data science, ML, AI"
    :weights {:data 160 :statistics 140 :math 120}}
   {:label "Backend development, systems engineering"
    :weights {:engineering 16 :algorithms 14 :optimization 12}}
   {:label "DevOps"
    :weights {:engineering 14 :debugging 16 :monotony 12}}
   {:label "Embedded systems, IoT, firmware, robotics, hardware-related"
    :weights {:hardware 16 :engineering 12 :physics 12}}
   {:label "Game development, simulations, graphics"
    :weights {:geometry 14 :algorithms 14 :optimization 14 :math 12}}
   {:label "Frontend development, mobile development"
    :weights {:ui 16 :simplification 14 :people 10}}
   {:label "UX/UI design, product design"
    :weights {:ux 16 :empathy 16 :people 10}}
   {:label "QA engineering, test automation"
    :weights {:testing 16 :edge-cases 16 :debugging 14 :monotony 10}}
   {:label "Cyber security"
    :weights {:edge-cases 16 :debugging 16}}])

(def rating-label-separator "|")
(def rating-label-separator-regex
  (re-pattern (java.util.regex.Pattern/quote rating-label-separator)))