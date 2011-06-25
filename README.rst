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
1. Install Java
Arch linux::

   sudo pacman -S jdk

Install on debian/ubuntu::

   sudo aptitude install sun-java6-jdk


2. Install Maven
Install on Arch Linux::

  sudo pacman -S maven
  
Install on Ubuntu::

  sudo aptitude install maven2

3. Install Leiningen::

Geo.clj requires leiningen. Leiningen is a clojure based wrapper to
maven. Please see the leiningen github project for install
instructions.


4. To install geo.clj and its dependences::

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
