(ns catfacts.routes
  (:require
    [bidi.bidi :as bidi]
    [cljs.reader :as reader]
    [hiccups.runtime]
    [macchiato.fs :as fs]
    [macchiato.util.response :as r]
    [mount.core :refer [defstate]])
  (:require-macros
    [hiccups.core :refer [html]]))

(defstate http-client :start (js/require "request"))

(defn catfact [{:keys [facts fact-count]}]
  (let [idx  (rand-int fact-count)
        fact (get-in facts [idx])]
    (str "Cat Fact " (inc idx) ": " fact "\n:cat: :cat: :cat:")))

(defn fact []
  (let [fact-data (-> "catfacts.edn" fs/slurp reader/read-string)
        facts     {:facts      fact-data
                   :fact-count (count fact-data)}]
    (fn [_ res]
      (-> {:response_type "in_channel"
           :text          (catfact facts)}
          (r/ok)
          (r/header "Content-Type" "application/json")
          (res)))))

(defn http-get [uri callback]
  (@http-client
    #js {:method             "GET"
         :followAllRedirects false
         :uri                uri}
    callback))

(defn gif [_ res _]
  (http-get "http://thecatapi.com/api/images/get?format=src&type=gif"
            (fn [_ response _]
              (-> {:response_type "in_channel"
                   :attachments
                                  [{:fallback  "Cat Gif."
                                    :image_url (some-> response .-request .-uri .-href)}]}
                  (r/ok)
                  (r/header "Content-Type" "application/json")
                  (res)))))

(defn home [req res raise]
  (-> (html
        [:html
         [:body
          [:h2 "Welcome to Catfacts"]]])
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

(defn routes []
  ["/" {""     {:get home}
        "gif"  {:get gif}
        "fact" {:get (fact)}}])

(defn router [req res raise]
  ((:handler (bidi/match-route* (routes) (:uri req) req) not-found)
    req res raise))
