(ns guestbook.routes
  (:require
    [bidi.bidi :as bidi]
    [hiccups.runtime]
    [guestbook.db :as db]
    [macchiato.middleware.anti-forgery :as af]
    [macchiato.util.response :as r]
    [cljs.nodejs :as node])
  (:require-macros
    [hiccups.core :refer [html]]))

(def sync (node/require "synchronize"))

(defn home [req res raise]
  (->
    [:html
     [:body
      [:div {:id "app"}]
      [:script {:src "js/guestbook.js"}]]]
    (html)
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

(def routes
  ["/" {:get home}])

(defn router [req res raise]
  ((:handler (bidi/match-route* routes (:uri req) req) not-found)
    req res raise))

(defn add-message [io message]
  (let [message (-> message
                    (js->clj :keywordize-keys true)
                    (assoc :time (js/Date.)))]
    (db/add-message message)
    (.emit io "message" (clj->js message))))

(defn ws-handler [server]
  (let [io ((node/require "socket.io") server)]
    (.on io "connection"
         (fn [client]
           (sync.fiber #(.emit io "messages" (clj->js (db/messages))))
           (.on client "message" (partial add-message io))
           (.on client "disconnect" (fn []))))))
