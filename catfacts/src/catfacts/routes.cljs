(ns catfacts.routes
  (:require
    [bidi.bidi :as bidi]
    [cljs.reader :as reader]
    [macchiato.response :refer [header not-found ok]]
    [mount.core :refer [defstate]]))

(defstate fs :start (js/require "fs"))
(defstate http-client :start (js/require "request"))

(defn read-file [file]
  (.readFileSync @fs file "utf8"))

(defn catfact [{:keys [facts fact-count]}]
  (let [idx  (rand-int fact-count)
        fact (get-in facts [idx])]
    (str "Cat Fact " (inc idx) ": " fact "\n:cat: :cat: :cat:")))

(defn fact-handler []
  (let [fact-data (-> "catfacts.edn" read-file reader/read-string)
        facts     {:facts      fact-data
                   :fact-count (count fact-data)}]
    (fn [_ res]
      (-> {:response_type "in_channel"
           :text          (catfact facts)}
          (ok)
          (header "Content-Type" "application/javascript")
          (res)))))

(defn http-get [uri callback]
  (@http-client
    #js {:method             "GET"
         :followAllRedirects false
         :uri                uri}
    callback))

(defn gif-handler [_ res]
  (http-get "http://thecatapi.com/api/images/get?format=src&type=gif"
    (fn [_ response _]
      (-> {:response_type "in_channel"
           :attachments
                          [{:fallback  "Cat Gif."
                            :image_url (some-> response .-request .-uri .-href)}]}
          (ok)
          (header "Content-Type" "application/javascript")
          (res)))))

(defn home-handler [_ res]
  (-> "Welcome to Catfacts" ok res))

(defn not-found-handler [req res]
  (-> (str "route " (-> req :user-agent) " was not found!") not-found res))

(defn routes []
  ["/"
   [["" home-handler]
    ["fact" (fact-handler)]
    ["gif" gif-handler]
    [true not-found-handler]]])

(defn router [req res]
  (let [route (->> req :uri (bidi/match-route (routes)) :handler)]
    (route req res)))
