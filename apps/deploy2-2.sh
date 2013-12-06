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
    $MVN_CMD install -am -pl minibankref2
fi

cp ./minibankref2/target/minibankref2-*.ear ../servers/node_2A/deployments
cp ./minibankref2/target/minibankref2-*.ear ../servers/node_2B/deployments

