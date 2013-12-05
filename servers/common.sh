#!/bin/sh

DIRNAME=`dirname "$0"`
CURRENTDIR="`pwd`"

if [ -f "../../customize/customize.sh"  ]; then
  . ../../customize/customize.sh
fi

if [ -f "../customize/customize.sh"  ]; then
  . ../customize/customize.sh
fi

if [ "x$JAVA_HOME" = "x" ]; then
    echo "ERROR: JAVA_HOME must be set. You can set it in ../../customize/customize.sh"
fi

if [ "x$EAP_HOME" = "x" ]; then
   
   if [ "x$JBOSS_HOME" = "x" ]; then
      echo "WARNING: Setting EAP_HOME to RPM installation default path (/usr/share/jbossas). You can set it in ../../customize/customize.sh"  
      declare -x EAP_HOME="/usr/share/jbossas"
   else
      echo "WARNING: Setting EAP_HOME to JBOSS_HOME. You can set it in ../../customize/customize.sh"  
      declare -x EAP_HOME="$JBOSS_HOME"
   fi
fi

if [ "x$JBOSS_HOME" = "x" ]; then
    declare -x JBOSS_HOME="$EAP_HOME"
fi

declare -x PATH="$JAVA_HOME/bin:$EAP_HOME/bin:$PATH"

if [ -f "./customize.sh"  ]; then
  . ./customize/customize.sh
fi

if [ "x$MEMORY_HEAP" = "x" ]; then
    MEMORY_HEAP="-Xms128M -Xmx128M -XX:MaxPermSize=128M"
fi

if [ "x$JAVA_OPTS_PRE" = "x" ]; then
   JAVA_OPTS="$MEMORY_HEAP"
else
   JAVA_OPTS="$JAVA_OPTS_PRE $MEMORY_HEAP"
fi

if [ "$SET_ENV_ONLY" = "true" ]; then
    echo "Mode: set_env_only"
else 
   JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=5$OFFSET,server=y,suspend=n -Xdebug"
fi

JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=org.jboss.byteman"
JAVA_OPTS="$JAVA_OPTS -Djboss.socket.binding.port-offset=$OFFSET"
JAVA_OPTS="$JAVA_OPTS -Djboss.server.base.dir=$JBOSS_BASE_DIR"
JAVA_OPTS="$JAVA_OPTS -Djboss.server.name=$JBOSS_SERVER_NAME"
JAVA_OPTS="$JAVA_OPTS -Djboss.node.name=$COMPUTERNAME_$OFFSET"
JAVA_OPTS="$JAVA_OPTS -Djgroups.bind_addr=$COMPUTERNAME"
JAVA_OPTS="$JAVA_OPTS -Djboss.udp.fixedport=true"
JAVA_OPTS="$JAVA_OPTS -Djboss.messaging.group.address=$UDP_ADDR"

echo "NOPAUSE = $NOPAUSE"
if [ "x$NOPAUSE" = "Y" ]; then
    JAVA_OPTS="$JAVA_OPTS -Djboss.console.threshold=OFF"
fi

declare -x JAVA_OPTS="$JAVA_OPTS $JAVA_OPTS_POST"

ARGLINE="$ARGLINE_PRE -b 0.0.0.0"
ARGLINE="$ARGLINE -bmanagement 0.0.0.0"
ARGLINE="$ARGLINE -u $UDP_ADDR"
ARGLINE="$ARGLINE --server-config standalone-full-ha.xml"
declare -x ARGLINE="$ARGLINE $ARGLINE_POST"

declare -x NATIVE_DIR="$EAP_HOME/modules/system/layers/base/native"

echo ""
echo "JAVA_HOME=$JAVA_HOME"
echo "EAP_HOME=$EAP_HOME"
echo "JBOSS_HOME=$JBOSS_HOME"
echo "JAVA_OPTS=$JAVA_OPTS"
echo "ARGLINE=$ARGLINE"
echo ""
