(ns auth-example.routes
  (:require
    [bidi.bidi :as bidi]
    [hiccups.runtime]
    [macchiato.middleware.anti-forgery :as af]
    [macchiato.util.response :as r])
  (:require-macros
    [hiccups.core :refer [html]]))

(defn logged-in? [{:keys [identity]}]
  (boolean identity))

(defn login-form []
  [:div
   [:h3 "Please login"]
   [:form
    {:method "POST"
     :action "/login"}
    [:input
     {:type  :hidden
      :name  "__anti-forgery-token"
      :value af/*anti-forgery-token*}]
    [:input
     {:type        :text
      :name        "name"
      :placeholder "name"}]
    [:input
     {:type        :text
      :name        "pass"
      :placeholder "password"}]
    [:input
     {:type  :submit
      :value "login"}]]])

(defn logout-form [{:keys [identity]}]
  [:div
   [:h3 "Welcome " (:name identity)]
   [:a {:href "/logout"} "logout"]])

(defn home [req res raise]
  (-> (html
        [:html
         [:body
          (if (logged-in? req)
            (logout-form req)
            (login-form))]])
      (r/ok)
      (r/content-type "text/html")
      (res)))

(defn not-found [req res raise]
  (-> (html
        [:html
         [:body
          [:h2 (:uri req) " was not found"]]])
      (r/not-found)
      (r/content-type "text/html")
      (res)))

(defn login [req res raise]
  (println (:params req))
  (-> (r/found "/")
      (assoc-in [:session :identity :name] (-> req :params :name))
      (res)))

(defn logout [req res raise]
  (-> (r/found "/")
      (update :session dissoc :identity)
      (res)))

(def routes
  ["/" {""       {:get home}
        "login"  {:post login}
        "logout" {:get logout}}])

(defn router [req res raise]
  (if-let [{:keys [handler route-params]} (bidi/match-route* routes (:uri req) req)]
    (handler (assoc req :route-params route-params) res raise)
    (not-found req res raise)))
