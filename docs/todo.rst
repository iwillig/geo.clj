to do list
==========

* tests
 - i'd greatly prefer if these were all unit tests. in other words, i
   wouldn't like waiting for reading an actual shape file or reading
   from a database

* fix leaking database connections (if you look at the inferior lisp
 buffer, you'll see geotools complaining about this)


* use clojure style
 http://www.assembla.com/wiki/show/clojure/Clojure_Library_Coding_Standards
* dynamic feature type creation from context
 - once this is done, we should rework the api to make use of it

* what do do with utilities? I would really like to put these general
 utilities in a separate package, but I haven't thought of a good name
 for it yet. For the time being, I've put something in a utils.clj
 file.

* more documentation
