(ns guestbook.app
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [guestbook.core-test]))

(doo-tests 'guestbook.core-test)


