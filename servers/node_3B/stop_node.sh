#!/bin/sh

DIRNAME=`dirname "$0"`
CURRENTDIR="`pwd`"

declare -x SET_ENV_ONLY="true"

 . $DIRNAME/start_node.sh

CMDLINE="$EAP_HOME/bin/jboss-cli.sh"
CMDLINE="$CMDLINE --connect"
CMDLINE="$CMDLINE --controller=localhost:$NATIVE_ADMIN_PORT"
CMDLINE="$CMDLINE --command=:shutdown"

echo "$CMDLINE"
RESPONSE=`$CMDLINE`;
echo "$RESPONSE"
exit 0;

