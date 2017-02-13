(ns auth-example.middleware
  (:require
    [macchiato.auth.backends.session :refer [session-backend]]
    [macchiato.auth.middleware :refer [wrap-authentication]]
    [macchiato.middleware.defaults :as defaults]))

(defn wrap-defaults [handler]
  (-> handler
      (wrap-authentication (session-backend))
      (defaults/wrap-defaults defaults/site-defaults)))


