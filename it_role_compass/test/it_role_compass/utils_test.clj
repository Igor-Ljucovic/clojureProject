(ns it-role-compass.utils-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.utils :as sut]))

(fact "ordered-values"
(let [m         {:c 3 :a 1 :b 2}
      key-order [:a :b :c]]
    (sut/ordered-values m key-order)
    => [1 2 3]))

(fact "ordered-values supports any key types, as long as key-order matches them"
(let [m         {"a" "Not interested" "b" "Average" "c" "Beginner"}
      key-order ["b" "a" "c"]]
    (sut/ordered-values m key-order)
    => ["Average" "Not interested" "Beginner"]))