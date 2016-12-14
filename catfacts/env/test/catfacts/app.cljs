(ns catfacts.app
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [catfacts.core-test]))

(doo-tests 'catfacts.core-test)


