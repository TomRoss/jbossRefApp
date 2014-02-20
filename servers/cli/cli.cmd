@ECHO OFF
SETLOCAL
SET SET_ENV_ONLY=true
call ..\common.cmd

SET NATIVE_ADMIN_PORT=10099
SET JBOSS_SERVER_NAME=cli

title %JBOSS_SERVER_NAME%

SET NOPAUSE=true

IF not "%1"=="gui" XCOPY /Y %EAP_HOME%\standalone\configuration\standalone-full-ha.xml .\configuration

IF not "%1"=="gui" start /D%~dp0 start_node.cmd

IF not "%1"=="gui" pause 

SET JAVA_OPTS=%JAVA_OPTS% -Djboss.cli.log.file=%~dp0log\jboss-cli.log
REM SET JAVA_OPTS=%JAVA_OPTS% -Djboss.cli.log.level=DEBUG
SET JAVA_OPTS=%JAVA_OPTS% -Dvault.mask.passwd=toto
SET JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,address=4100,server=y,suspend=n -Xdebug

SET CMDLINE=%EAP_HOME%\bin\jboss-cli.bat
SET CMDLINE=%CMDLINE% -Dvault.mask.passwd=toto
SET CMDLINE=%CMDLINE% --connect
SET CMDLINE=%CMDLINE% --controller=localhost:%NATIVE_ADMIN_PORT%
IF "%1"=="gui" SET CMDLINE=%CMDLINE% --gui
IF not "%1"=="gui" SET CMDLINE=%CMDLINE% --file=CLI_node_1A.txt

ECHO %CMDLINE%
call %CMDLINE%

ENDLOCAL
