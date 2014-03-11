@echo off
setlocal

call common.cmd


REM ####################### H2 CONF    ########################################
SET CMD_LINE_H2=%JAVA_HOME%\bin\java -cp %EAP_HOME%\modules\system\layers\base\com\h2database\h2\main\* org.h2.tools.Server -tcp -baseDir ./db/minibank %*


REM ####################### DERBY CONF ########################################
set DERBY_HOME=%JAVA_HOME%\db
set PATH=%DERBY_HOME%\bin;%PATH%
REM see http://docs.oracle.com/javadb/10.8.3.0/adminguide/
REM see also: http://docs.oracle.com/javadb/10.8.3.0/ref/
REM JTA and XA support : http://docs.oracle.com/javadb/10.8.3.0/ref/rrefjta16677.html
REM By default the server will be listening on port 1527 but this can be changed via the -p option.
REM By default Derby will only accept connections from the localhost otherwise you can do -h 0.0.0.0
SET CMD_LINE_DERBY=%DERBY_HOME%\bin\startNetworkServer.bat -noSecurityManager %*
REM SET DERBY_OPTS=-Dderby.drda.traceDirectory=./db
SET DERBY_OPTS=-Dderby.system.home=./db
REM see: http://db.apache.org/derby/docs/10.8/devguide/cdevconcepts16400.html
SET DERBY_OPTS=%DERBY_OPTS% -Dderby.locks.deadlockTrace=true
REM SET DERBY_OPTS=%DERBY_OPTS% -Dderby.locks.waitTimeout=600
REM SET DERBY_OPTS=%DERBY_OPTS% -Dderby.locks.deadlockTimeout=550


ECHO CMD_LINE=%CMD_LINE_H2% 
call %CMD_LINE_H2%

endlocal

