#!/bin/bash


cd $JBOSS_HOME/bin

./standalone.sh -Djboss.server.base.dir=$JBOSS_HOME/standalone-local  --server-config=standalone-full.xml &


./standalone.sh -Djboss.server.base.dir=$JBOSS_HOME/standalone-mdb  -Djboss.socket.binding.port-offset=100 --server-config=standalone-full.xml &