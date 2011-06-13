geo.clj
=======

Geo.clj is a clojure library for working with Geospatial data. Its
based on GeoTools and JTS.  If you are looking for other GeoTools
based scripting libraries please see the Geoscript project. 

Requirements
------------

#. Java JDK
#. Apache Maven
#. Leiningen

Install
----------
#. Install Java:: 

   # Install on Arch Linux
   sudo pacman -S jdk

   # Install on ubuntu
   sudo aptitude install sun-java6-jdk


#. Install Maven::

  # Install on Arch Linux
  sudo pacman -S maven
  
  # Install on Ubuntu
  sudo aptitude install maven2

#. Install Leiningen::

   # download
   curl -XGET http://
   
   # Install Leiningen 
   lein self-install

#. To install geo.clj and its dependences::

   git clone git://github.com/iwillig/geo.clj.git
   cd geo.clj
   lein deps

Usage
------------
To use geo.clj::

   lein repl
   (use 'geo.io)
   (viewer (-> (data-store "shp:///path/to/shapefile.shp")
      (read-features)) nil)



To do
------

#. Add support for GeoJSON
#. Add support for Grids
#. Add support for David's CSS libraries (Requires a custom build of a
 Scala lib)
#. Write documentation
#. 
