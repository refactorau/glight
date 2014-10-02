# GLight

Copyright 2014 Steve Dalton <steve@refactor.com.au>, Refactor Pty Ltd

Groovy based build light controller. In true minimalist fashion this script only works with

* Delcom 904006 Build Light (G2)
* Jenkins
* Ubuntu 14.04. 

Please fork and adapt if you find it useful.

## Prerequisites

### Debian/Ubuntu

Create a file called /etc/udev/rules.d/85-delcom.rules containing the following

	SUBSYSTEM=="usb", ATTRS{idVendor}=="0fc5", ATTRS{idProduct}=="b080", ACTION=="add", SYMLINK+="delcom", MODE="0666", GROUP="dalts"

Replace "dalts" with your own user group. Now reboot to make sure it takes effect. This will give you write access this device on the USB port.

Depending on what version of Ubuntu you are running you may need the hidapi library - I found on my 14.04 box it worked fine, my 13.10 box that was upgraded to 14.04 needed it though. 

        sudo apt-get install libhidapi

Everything else is handled by jni wrappers in javahidapi.

### Mac OSX

TODO

## Common Problems

	Exception in thread "main" com.codeminders.hidapi.HIDDeviceNotFoundException

Make sure you install your native HID library (see prequisites)

	Exception in thread "main" java.lang.NullPointerException: Cannot invoke method sendFeatureReport() on null object

## Acknowledgements

Thanks to the following

* https://code.google.com/p/javahidapi - great little wrapper for the hid libraries
* https://github.com/cibuddy/cibuddy - works beautifully but is so heavyweight for what I needed and is OSGI (yuk - sorry OSGI lovers). The code was useful to work out the codes though!
* Andrew Purcell for loaning me the Delcom light to play with
