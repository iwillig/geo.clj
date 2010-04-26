geoscript.clj
==============

usage 
------
#. programs should be short
   
   (def quakes (create-features {:date String :count String :location Point}))
   (add-feature quakes {:date 1/3/2010 :country "Haiti" :location
   create-point 43 35})  

   (write-postgis (buffer (read-shapefile "path/to/shapefile.shp") 10 ))


geometry.clj
------------------

create-point 
create-linestring 
create-polygon
from-wkt

processing.clj
-------------------

transform 
valid? 
projected? 

buffer
union
etc... 

render.clj
--------------------- 
draw - 

render-file
render-streaming

map

io.clj
------------------------------
read-shapefile 
read-kml
read-gml
read-geojson
read-postgis
read-osm

write-osm
write-shapefile
write-kml
write-gml
write-geojson
write-postgis



