@ECHO OFF

SET OFFSET=300
SET JBOSS_SERVER_NAME=node_2A
if "%UDP_ADDR%"=="" SET UDP_ADDR=239.20.1.1
SET CURRENT_DIR=%~dp0
SET JBOSS_BASE_DIR=%CURRENT_DIR%
title %JBOSS_SERVER_NAME%

CALL ..\common.cmd
SET PATH=%JAVA_HOME%\bin;%EAP_HOME%\bin;%PATH%
IF EXIST customize.cmd CALL customize.cmd

SET MEMORY_HEAP=-Xms256M -Xmx256M -XX:MaxPermSize=128M
IF NOT "%JAVA_OPTS_PRE%"=="" SET "JAVA_OPTS=%JAVA_OPTS_PRE% %MEMORY_HEAP%"
IF "%JAVA_OPTS_PRE%"=="" SET "JAVA_OPTS=%MEMORY_HEAP%"
SET "JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,address=5%OFFSET%,server=y,suspend=n -Xdebug"
SET "JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.byteman"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.socket.binding.port-offset=%OFFSET%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.server.base.dir=%JBOSS_BASE_DIR%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.server.name=%JBOSS_SERVER_NAME%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.node.name=%COMPUTERNAME%_%OFFSET%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djgroups.bind_addr=%COMPUTERNAME%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.udp.fixedport=true"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.messaging.group.address=%UDP_ADDR%"
SET "JAVA_OPTS=%JAVA_OPTS% %JAVA_OPTS_POST%"

SET ARGLINE=%ARGLINE_PRE% -b 0.0.0.0
SET ARGLINE=%ARGLINE% -bmanagement 0.0.0.0
SET ARGLINE=%ARGLINE% -u %UDP_ADDR%
SET ARGLINE=%ARGLINE% --server-config standalone-full-ha.xml
SET ARGLINE=%ARGLINE% %ARGLINE_POST%

SET CMDLINE=%EAP_HOME%\bin\standalone.bat %ARGLINE%

IF NOT "%SET_ENV_ONLY%" == "true" ECHO %CMDLINE%
IF NOT "%SET_ENV_ONLY%" == "true" CALL %CMDLINE%

