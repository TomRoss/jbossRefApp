#!/bin/sh

DIRNAME=`dirname "$0"`
CURRENTDIR="`pwd`"

# -------------------------------------------------------------------------
# Java Mission Control script f
# -------------------------------------------------------------------------
#
# A script for running jmc with the remoting-jmx libraries on the classpath. 
# see http://docs.oracle.com/javase/7/docs/technotes/tools/windows/jmc.html

 . ./common.sh
VISUALVM_HOME="$JAVA_HOME"
echo "JBOSS_HOME = $JBOSS_HOME"

# Find jboss-modules.jar, or we can't continue
if [ -f "$JBOSS_HOME/jboss-modules.jar"  ]; then
   RUNJAR="$JBOSS_HOME/jboss-modules.jar"
else
   echo "Could not locate $JBOSS_HOME/jboss-modules.jar."
   echo "Please check that you are in the bin directory when running this script."
   exit 1;
fi

# Set default module root paths
if [ "x$JBOSS_MODULEPATH" = "x" ]; then
    BOSS_MODULEPATH="$JBOSS_HOME/modules/system/layers/base"
fi

# Setup The Classpath
MODULES="org/jboss/remoting-jmx org/jboss/remoting3 org/jboss/logging org/jboss/xnio org/jboss/xnio/nio org/jboss/sasl org/jboss/marshalling org/jboss/marshalling/river org/jboss/as/cli org/jboss/staxmapper org/jboss/as/protocol org/jboss/dmr org/jboss/as/controller-client org/jboss/threads"

for MODULE in $MODULES
do
    for JAR in `cd "$JBOSS_MODULEPATH/system/layers/base/$MODULE/main/" && ls -1 *.jar`
    do
        CLASSPATH="$CLASSPATH:$JBOSS_MODULEPATH/system/layers/base/$MODULE/main/$JAR"
    done
done

echo "CLASSPATH = $CLASSPATH"  
echo "JBOSS_MODULEPATH = $JBOSS_MODULEPATH"
echo "$VISUALVM_HOME/bin/jmc -J-Xbootclasspath/a: $CLASSPATH $*"

`$VISUALVM_HOME/bin/jmc --launcher.appendVmargs -vmargs -Xbootclasspath/a:$CLASSPATH% $* &`


exit 0;
