@echo off
setlocal

call common.cmd
set DERBY_HOME=%JAVA_HOME%\db
set PATH=%DERBY_HOME%\bin;%PATH%

REM see http://docs.oracle.com/javadb/10.8.3.0/adminguide/
REM see also: http://docs.oracle.com/javadb/10.8.3.0/ref/
REM JTA and XA support : http://docs.oracle.com/javadb/10.8.3.0/ref/rrefjta16677.html


REM By default the server will be listening on port 1527 but this can be changed via the -p option.
REM By default Derby will only accept connections from the localhost otherwise you can do -h 0.0.0.0
SET CMD_LINE=%DERBY_HOME%\bin\stopNetworkServer.bat
REM SET CMD_LINE=%CMD_LINE% -noSecurityManager
SET CMD_LINE=%CMD_LINE% %*

REM SET DERBY_OPTS=-Dderby.drda.traceDirectory=./db
SET DERBY_OPTS=-Dderby.system.home=./db

ECHO CMD_LINE=%CMD_LINE% 
call %CMD_LINE%

REM Usage: NetworkServerControl <commands>
REM Commands:
REM start [-h <host>] [-p <portnumber>] [-noSecurityManager] [-ssl <sslmode>]
REM shutdown [-h <host>][-p <portnumber>] [-ssl <sslmode>] [-user <username>] [-password <password>]
REM ping [-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM sysinfo [-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM runtimeinfo [-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM logconnections {on|off} [-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM maxthreads <max>[-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM timeslice <milliseconds>[-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM trace {on|off} [-s <session id>][-h <host>][-p <portnumber>] [-ssl <sslmode>]
REM tracedirectory <traceDirectory>[-h <host>][-p <portnumber>] [-ssl <sslmode>]

endlocal

