(ns guestbook.routes
  (:require
    [bidi.bidi :as bidi]
    [hiccups.runtime]
    [guestbook.db :as db]
    [macchiato.middleware.anti-forgery :as af]
    [macchiato.util.response :as r])
  (:require-macros
    [hiccups.core :refer [html]]))

(defn home [req res raise]
  (let [af-token af/*anti-forgery-token*]
    (db/messages
      (fn [messages]
        (-> (html
              [:html
               [:body
                [:h2 "Messages"]
                [:ul
                 (for [{:keys [name message time]} messages]
                   [:li name " says " message " at " time])]
                [:hr]
                [:h2 "leave a message"]
                [:form {:method "POST" :action "/message"}
                 [:input
                  {:type        :text
                   :name        "name"
                   :placeholder "name"}]
                 [:input
                  {:type  "hidden"
                   :name  "__anti-forgery-token"
                   :value af-token}]
                 [:input
                  {:type        :text
                   :name        "message"
                   :placeholder "message"}]
                 [:input
                  {:type  :submit
                   :value "add message"}]]]])
            (r/ok)
            (r/content-type "text/html")
            (res))))))

(defn message [req res raise]
  (db/add-message (select-keys (:params req) [:name :message]))
  (res (r/found "/")))

(defn not-found [req res raise]
  (-> (html
        [:html
         [:body
          [:h2 (:uri req) " was not found"]]])
      (r/not-found)
      (r/content-type "text/html")
      (res)))

(def routes
  ["/"
   [["" home]
    ["message" message]
    [true not-found]]])

(defn router [req res raise]
  (let [route (->> req :uri (bidi/match-route routes) :handler)]
    (route req res raise)))
