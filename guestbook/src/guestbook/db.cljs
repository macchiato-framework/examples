(ns guestbook.db
  (:require
    [cljs.nodejs :as node]
    [mount.core :refer [defstate]]))

(def sync (node/require "synchronize"))

(def sqlite3 (node/require "sqlite3"))

(defstate db
  :start (let [db (sqlite3.Database. ":memory:")]
           (.run
             db
             "CREATE TABLE guestbook
                         (id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name VARCHAR(30),
                          message VARCHAR(200),
                          time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);"))
  :stop (.close @db))

(defn add-message [{:keys [name message]}]
  (.run @db "INSERT INTO guestbook (name, message) VALUES (?, ?)" #js [name message]))

(defn messages []
  (-> @db
      (.all "SELECT * FROM guestbook" (sync.defer))
      (sync.await)
      (js->clj :keywordize-keys true)))
