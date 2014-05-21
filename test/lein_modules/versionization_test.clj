(ns lein-modules.versionization-test
  (:use clojure.test
        lein-modules.versionization)
  (:require [leiningen.core.project :as prj]))

(deftest versionization
  (let [child (versionize (prj/read "test-resources/grandparent/parent/child/project.clj"))]
    (is (= "3.0" (-> child :parent second)))
    (are [d expected] (= expected (-> child :dependencies (nth d)))
         0 '[x/x "1.1.1"]
         1 '[y/y "1.0.2"]
         2 '[scope/scope "9.9.9" :scope "pom"]
         3 '[z/z "1.2.3"]
         4 '[foo/a "1"]
         5 '[foo/b "2"]
         6 '[foo/c "3"]
         7 '[foo/d "2.0"])))

(deftest partial-composites-should-work
  (let [p (-> (prj/read "test-resources/lambda/project.clj")
            versionize
            (prj/set-profiles [:dev]))]
    (are [d expected] (= expected (-> p :dependencies (nth d)))
         0 '[cheshire/cheshire "5.2.0"]
         1 '[org.immutant/immutant "1.1.1"]
         2 '[midje/midje "1.6.3"])))
