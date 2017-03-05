(ns auth-example.middleware
  (:require
    [darkleaf.router.html.method-override :refer [wrap-method-override]]
    [macchiato.auth.backends.session :refer [session-backend]]
    [macchiato.auth.middleware :refer [wrap-authentication]]
    [macchiato.middleware.defaults :as defaults]))

(defn wrap-defaults [handler]
  (-> handler
      (wrap-authentication (session-backend))
      (wrap-method-override)
      (defaults/wrap-defaults defaults/site-defaults)))
