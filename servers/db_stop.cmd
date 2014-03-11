@echo off
setlocal

call common.cmd

REM ####################### H2 CONF    ########################################
SET CMD_LINE_H2=%JAVA_HOME%\bin\java -Dh2.socketConnectTimeout=1000 -Dh2.socketConnectRetry=1 -cp %EAP_HOME%\modules\system\layers\base\com\h2database\h2\main\* org.h2.tools.Server -tcpShutdown tcp://localhost:9092 %*


REM ####################### DERBY CONF ########################################
set DERBY_HOME=%JAVA_HOME%\db
set PATH=%DERBY_HOME%\bin;%PATH%
SET CMD_LINE_DERBY=%DERBY_HOME%\bin\stopNetworkServer.bat %*
SET DERBY_OPTS=-Dderby.system.home=./db



ECHO CMD_LINE=%CMD_LINE_H2% 
call %CMD_LINE_H2%

endlocal

