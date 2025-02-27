#!/bin/sh

if test $# -lt 1; then
	echo " "
	echo "Usage: `basename ${0}` [BROWSER] widget"
	echo "	displays cookie information if any about widget."
	echo " "
	exit 1
fi

if test "$2" = ""; then
	WIDGET=$1
	BROWSER="ff"
else
	BROWSER=$1
	WIDGET=$2
fi

if test "$BROWSER" = "ff" -o "$BROWSER" = "firefox"; then
	if test -x /bin/cygpath; then
		cat /cygdrive/c/Documents\ and\ Settings/${LOGNAME}/Application\ Data/Mozilla/Firefox/Profiles/*.default/cookies.*\
			| grep -ai ${WIDGET}
	else
		cat ${HOME}/.mozilla/firefox/*.default/cookies.*\
			| grep -ai ${WIDGET}
        fi
elif test "$BROWSER" = "mozilla"; then
	cat ${HOME}/.mozilla/Default\ User/*.slt/cookies.txt | grep -ai ${WIDGET}
elif test "$BROWSER" = "netscape"; then
	cat /cygdrive/c/Documents\ and\ Settings/${LOGNAME}/Application\ Data/Netscape/NSB/Profiles/*.default/cookies.txt\
                | grep -ai ${WIDGET}
elif test "$BROWSER" = "ie"; then
	cat /cygdrive/c/Documents\ and\ Settings/${LOGNAME}/Cookies/${LOGNAME}*.txt\
		| while read -d "*" a b c d e f g h;
		do echo $a $b $c $d $e $f $g $h; done | grep -ai ${WIDGET}
elif test "$BROWSER" = "safari"; then
	cat /cygdrive/c/Documents\ and\ Settings/${LOGNAME}/Application\ Data/Apple\ Computer/Cookies.plist\
                | grep -ai ${WIDGET}
else
	echo "browser $BROWSER unknown"
fi
