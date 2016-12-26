(ns guestbook.client
  (:require
    [cljsjs.socket-io]
    [mount.core :refer [defstate]]
    [reagent.core :as r]))

(defstate socket :start (js/io))
(def messages (r/atom []))

(defn home-page []
  (r/with-let [message (r/atom {})]
    [:div "Welcome to Guestbook!"
     [:h2 "Messages"]
     [:ul
      (for [{:keys [name message time]} @messages]
        ^{:key time}
        [:li name " says " message " on " (str (js/Date. time))])]
     [:hr]
     [:h2 "leave a message"]
     [:div
      [:input
       {:type        :text
        :name        "name"
        :placeholder "name"
        :on-change   #(swap! message assoc :name (-> % .-target .-value))}]
      [:input
       {:type        :text
        :name        "message"
        :placeholder "message"
        :on-change   #(swap! message assoc :message (-> % .-target .-value))}]
      [:button
       {:on-click #(.emit @socket "message" (clj->js @message))}
       "add message"]]]))

(defn mount-components []
  (r/render-component [#'home-page] (.getElementById js/document "app")))

(defn init []
  (.on @socket "message" #(swap! messages conj (js->clj % :keywordize-keys true)))
  (.on @socket "messages" #(reset! messages (js->clj % :keywordize-keys true)))
  (mount-components))

(init)
