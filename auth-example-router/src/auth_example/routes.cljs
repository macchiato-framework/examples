(ns auth-example.routes
  (:require
   [darkleaf.router :as router]
   [hiccups.runtime]
   [macchiato.middleware.anti-forgery :as af]
   [macchiato.util.response :as r])
  (:require-macros
   [hiccups.core :refer [html]]))

(defn logged-in? [{:keys [identity]}]
  (boolean identity))

(defn login-form [ctx]
  (let [request-for (::router/request-for ctx)
        req (request-for :create [:session] {})]
    [:div
     [:h3 "Please login"]
     [:form
      {:method (:request-method req)
       :action (:uri req)}
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
        :value "login"}]]]))

(defn logout-form [{:keys [identity], :as ctx}]
  (let [request-for (::router/request-for ctx)
        req (request-for :destroy [:session] {})]
    [:div
     [:h3 "Welcome " (:name identity)]
     [:form
      {:method "POST"
       :action (:uri req)}
      [:input
       {:type  :hidden
        :name  "__anti-forgery-token"
        :value af/*anti-forgery-token*}]
      [:input
       {:type  :hidden
        :name  "_method"
        :value (:request-method req)}]
      [:input
       {:type  :submit
        :value "logout"}]]]))

(defn home-view [ctx]
  (html
   [:html
    [:body
     (if (logged-in? ctx)
       (logout-form ctx)
       (login-form ctx))]]))

(def home-controller
  {:show (fn [req res raise]
           (-> req
               (home-view)
               (r/ok)
               (r/content-type "text/html")
               (res)))})

(def session-controller
  {:create (fn [req res raiss]
             (-> (r/found "/")
                 (assoc-in [:session :identity :name] (-> req :params :name))
                 (res)))
   :destroy (fn [req res raise]
              (-> (r/found "/")
                  (update :session dissoc :identity)
                  (res)))})

(def routes
  (router/group
   (router/resource :home home-controller, :segment false)
   (router/resource :session session-controller)))

(def handler (router/make-handler routes))
