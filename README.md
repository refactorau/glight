GLight
======

Copyright 2014 Steve Dalton <steve@refactor.com.au>, Refactor Pty Ltd

Groovy based build light controller. In true minimalist fashion this script only works with

* Delcom 904006 Build Light (G2)
* Jenkins
* Ubuntu 14.04. 

Please fork and adapt if you find it useful.

Prerequisites
-------------

You need the native libraries. Everything else is handled by jni wrappers in javahidapi. On Ubuntu:

	sudo apt-get install libhidapi

Acknowledgements
----------------

Thanks to the following

* https://code.google.com/p/javahidapi - great little wrapper for the hid libraries
* https://github.com/cibuddy - works beautifully but is so heavyweight for what I needed and is OSGI (yuk - sorry OSGI lovers). The code was useful to work out the codes though!
* Andrew Purcell for loaning me the Delcom light to play with
