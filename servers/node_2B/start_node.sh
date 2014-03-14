#!/bin/sh


DIRNAME=`dirname "$0"`
CURRENTDIR="`pwd`"

OFFSET="400"
JBOSS_SERVER_NAME="node_2B"

export LAUNCH_JBOSS_IN_BACKGROUND=true

if [ "x$UDP_ADDR" = "x" ]; then
   declare -x UDP_ADDR="239.20.1.1"
fi

declare -x NATIVE_ADMIN_PORT=`expr 5004 + $OFFSET`;

if [ "x$JBOSS_BASE_DIR" = "x" ]; then
    JBOSS_BASE_DIR=`dirname "$0"`
    export JBOSS_PIDFILE=${JBOSS_BASE_DIR}/node-pid
fi

 . $DIRNAME/../common.sh

CMDLINE="$JBOSS_HOME/bin/standalone.sh $ARGLINE"

if [ "$SET_ENV_ONLY" != "true" ]; then
    echo "$CMDLINE"
    $CMDLINE &
fi



