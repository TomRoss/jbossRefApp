@ECHO OFF
SETLOCAL

SET OFFSET=200
SET UDP_ADDR=239.10.1.1
SET JBOSS_SERVER_NAME=node_1B
title %JBOSS_SERVER_NAME%

SET "JAVA_OPTS=-Xms256M -Xmx256M -XX:MaxPermSize=128M"
SET "JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,address=5%OFFSET%,server=y,suspend=n -Xdebug"
SET "JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.byteman"

CALL ..\common.cmd

SET CURRENT_DIR=%~dp0
SET JBOSS_BASE_DIR=%CURRENT_DIR%
SET PATH=%JAVA_HOME%\bin;%EAP_HOME%\bin;%PATH%
IF EXIST customize.cmd CALL customize.cmd

SET CMDLINE=%EAP_HOME%\bin\standalone.bat
SET CMDLINE=%CMDLINE% -Djboss.socket.binding.port-offset=%OFFSET%
SET CMDLINE=%CMDLINE% -Djboss.server.base.dir=%JBOSS_BASE_DIR%
SET CMDLINE=%CMDLINE% -Djboss.server.name=%JBOSS_SERVER_NAME%
SET CMDLINE=%CMDLINE% -Djboss.node.name=%COMPUTERNAME%_%OFFSET%
SET CMDLINE=%CMDLINE% -b 0.0.0.0
SET CMDLINE=%CMDLINE% -bmanagement 0.0.0.0
SET CMDLINE=%CMDLINE% -Djgroups.bind_addr=%COMPUTERNAME%
SET CMDLINE=%CMDLINE% -u %UDP_ADDR%
SET CMDLINE=%CMDLINE% -Djboss.udp.fixedport=true
SET CMDLINE=%CMDLINE% -Djboss.messaging.group.address=%UDP_ADDR%
SET CMDLINE=%CMDLINE% --server-config standalone-full-ha.xml

ECHO %CMDLINE%
CALL %CMDLINE%

ENDLOCAL
