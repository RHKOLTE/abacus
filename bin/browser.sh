#!/bin/sh

if test $# -lt 1; then
	echo " "
	echo "Usage: `basename ${0}` [browser] HTML"
	echo "	runs browser using HTML."
	echo " "
	exit 1
fi

if test "$2" = ""; then
	HTML=$1
	BROWSER=ff
else
	BROWSER=$1
	HTML=$2
fi

if test "$BROWSER" = "ff" -o "$BROWSER" = "firefox"; then
	if test -x /bin/cygpath; then
		C:/Program\ Files*/Mozilla\ Firefox/firefox file://`cygpath\
			--windows $(PWD)`/${HTML}
	else
		/usr/bin/firefox file://${PWD}/${HTML}
        fi
elif test "$BROWSER" = "appletviewer"; then
	appletviewer ${HTML}
elif test "$BROWSER" = "jdb"; then
	jdb sun.applet.AppletViewer ${HTML}
elif test "$BROWSER" = "mozilla"; then
	/usr/sfw/bin/mozilla file://${PWD}/${HTML}
elif test "$BROWSER" = "netscape"; then
	C:/Program\ Files*/Netscape/Netscape/Netscp file://`cygpath\
		--windows $(PWD)`/${HTML}
elif test "$BROWSER" = "ie"; then
	C:/Program\ Files/Internet\ Explorer/iexplore file://`cygpath\
		--windows $(PWD)`/${HTML}
elif test "$BROWSER" = "chrome"; then
	C:/Program\ Files*/Google/Chrome/Application/chrome file://`cygpath\
		--windows $(PWD)`/${HTML}
elif test "$BROWSER" = "safari"; then
	C:/Program\ Files*/Safari/Safari file://`cygpath\
		--windows $(PWD)`/${HTML}
else
	echo "browser $BROWSER unknown"
fi
