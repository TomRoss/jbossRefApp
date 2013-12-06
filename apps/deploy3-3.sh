#!/bin/sh

DIRNAME=`dirname "$0"`
CURRENTDIR="`pwd`"

if [ -f "../customize/customMvn.sh"  ]; then
  . ../customize/customMvn.sh
fi

if [ "x$MVN_CMD" = "x" ]; then
    MVN_CMD="mvn"
fi

if [ "$1" != "NOBUILD" ]; then
    $MVN_CMD install -am -pl minibankref3
fi

cp ./minibankref3/target/minibankref3-*.ear ../servers/node_3A/deployments
cp ./minibankref3/target/minibankref3-*.ear ../servers/node_3B/deployments

