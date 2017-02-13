(ns auth-example.middleware
  (:require
    [macchiato.auth.backends.session :refer [session-backend]]
    [macchiato.auth.middleware :refer [wrap-authentication]]
    [macchiato.middleware.defaults :as defaults]))

(defn wrap-method-override [handler]
  (fn [req res raise]
    (let [orig-method (:request-method req)
          new-method  (some-> req
                              :params
                              :_method
                              (keyword))]
      (if (and (= :post orig-method)
               (#{:put :patch :delete} new-method))
        (handler (assoc req :request-method new-method) res raise)
        (handler req res raise)))))

(defn wrap-defaults [handler]
  (-> handler
      (wrap-authentication (session-backend))
      (wrap-method-override)
      (defaults/wrap-defaults defaults/site-defaults)))
