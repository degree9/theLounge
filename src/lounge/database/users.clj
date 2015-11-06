(ns lounge.database.users
  (:require [lounge.api :as api]
            [lounge.database :as db]
            [cemerick.friend.credentials :as creds]
            [castra.core :refer [defrpc ex *session*]]
            [monger.collection :as mc]
            [monger.util :as mu]))

(defn user [umap] (db/find-map "users" umap))

(defn users
  ([] (db/find-maps "users"))
  ([umap] (db/find-maps "users" umap)))

(defn- users-insert [udoc]
  (db/insert "users" udoc))

(defn find-users
  ([] (users))
  ([umap] (users umap)))

(defn find-user [umap]
  (user umap))

(defn find-user-by-email [email]
  (find-user {:email email}))

(defn find-user-by-username [username]
  (find-user {:username username}))

(defn new-user [username email password]
  (users-insert {:_id (mu/random-uuid)
                    :username username
                    :email email
                    :password (creds/hash-bcrypt password)}))
