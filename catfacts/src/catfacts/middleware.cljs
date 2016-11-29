(ns catfacts.middleware
  (:require [taoensso.timbre :refer-macros [log trace debug info warn error fatal]]))

(defn wrap-error [handler]
  (fn [req res]
    (try
      (handler req res)
      (catch js/Error e
        (error e)
        (res {:status 500
              :body   (.-message e)})))))
