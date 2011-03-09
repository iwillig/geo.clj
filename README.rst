geo.clj
=======
Geo.clj is a clojure library for working with Geospatial data. Its
based on GeoTools and JTS, two Java based libraries. 

Install
----------
git clone git://github.com/iwillig/geo.clj.git
cd geo.clj
lein deps

Use
------------

lein repl
(use 'geo.io)
(viewer (-> (data-store "shp:///path/to/shapefile.shp") (read-features)))

Support
---------

geo.clj@librelist.com.





