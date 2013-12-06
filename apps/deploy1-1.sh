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
    $MVN_CMD install -am -pl minibankref1
fi

cp ./minibankref1/target/minibankref1-*.ear ../servers/node_1A/deployments
cp ./minibankref1/target/minibankref1-*.ear ../servers/node_1B/deployments

