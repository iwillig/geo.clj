Ivan's mapping api
==================

usage 
------
#. programs should be short
   
   (write-postgis (buffer (read-shapefile "path/to/shapefile.shp") 10 ))


Geometry creation
------------------

create-point 
create-linestring 
create-polygon

fromstr

Geometry operations
-------------------

transform 
valid? 
projected? 

buffer
union
etc... 

Rendering information
--------------------- 
draw - 

render-file
render-streaming

map

Styles - because styles are important for rendering
++++++++++++++++++++++++++++++++++++++++++++++++++++

style-from-geom 
style-from-css 
style-from-sld


Reading from different formats
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




