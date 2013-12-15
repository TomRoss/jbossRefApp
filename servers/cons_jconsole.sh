#!/bin/sh

 . ./common.sh

# To make it works you need to change the jconsole.sh script in order to be able to give parameter: add $* at the end of line 95
CMDLINE="$EAP_HOME/bin/jconsole.sh"
# CMDLINE="$CMDLINE service:jmx:remoting-jmx://localhost:5104"
CMDLINE="$CMDLINE $*" 
# Don't need user/password (admin/Passw0rd) to connect to the JConsole!

echo "$CMDLINE"
 $CMDLINE

