#!/bin/sh

DIRNAME=`dirname "$0"`
CURRENTDIR="`pwd`"

if [ -f "../customize/customMvn.sh"  ]; then
  . ../customize/customMvn.sh
fi

if [ "x$MVN_CMD" = "x" ]; then
    MVN_CMD="mvn"
fi

cd $CURRENTDIR/../apps

$MVN_CMD install -am -pl minibankref1

 . ./deploy1-1.sh NOBUILD

cd $CURRENTDIR

exit 0;
