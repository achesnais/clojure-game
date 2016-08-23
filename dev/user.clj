(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]))

(defn reset []
  (refresh))
