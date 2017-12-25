(ns guestbook.middleware
  (:require
   [macchiato.middleware.defaults :as defaults]
   [macchiato.middleware.restful-format :as restful-format]))

(defn wrap-defaults [handler]
  (-> handler
      (defaults/wrap-defaults defaults/site-defaults)
      (restful-format/wrap-restful-format {:keywordize? true})))
