@echo off
setlocal

call common.cmd

REM ####################### H2 CONF    ########################################
SET CMD_LINE_H2=%JAVA_HOME%\bin\java -cp %EAP_HOME%\modules\system\layers\base\com\h2database\h2\main\* org.h2.tools.Shell -url jdbc:h2:tcp://localhost:9092/minibank -user sa -password sa %*
REM -tcpShutdown tcp://localhost:9092 %*


REM ####################### DERBY CONF ########################################
set DERBY_HOME=%JAVA_HOME%\db
set PATH=%DERBY_HOME%\bin;%PATH%
SET CMD_LINE_DERBY=%DERBY_HOME%\bin\ij.bat
REM SET CMD_LINE_DERBY=%CMD_LINE_DERBY% db_connect.sql
REM SET CMD_LINE_DERBY=%CMD_LINE_DERBY% 
SET CMD_LINE_DERBY=%CMD_LINE_DERBY% %*
SET DERBY_OPTS=-Dderby.system.home=./db
REM SET DERBY_OPTS=%DERBY_OPTS% -Dij.database=jdbc:derby://localhost:1527/minibank
REM run 'db_connect.sql';




ECHO CMD_LINE=%CMD_LINE_H2% 
call %CMD_LINE_H2%


endlocal

