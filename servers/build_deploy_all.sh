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

$MVN_CMD install -am -pl minibankref1,minibankref2,minibankref3

 ./deploy1-1.sh NOBUILD
 ./deploy1-2.sh NOBUILD
 ./deploy2-2.sh NOBUILD
 ./deploy3-3.sh NOBUILD

cd $CURRENTDIR

exit 0;
