@ECHO OFF
SETLOCAL
SET SET_ENV_ONLY=true
CALL start_node.cmd

SET CMDLINE=%EAP_HOME%\bin\jboss-cli.bat
SET CMDLINE=%CMDLINE% --connect
SET CMDLINE=%CMDLINE% --controller=localhost:%NATIVE_ADMIN_PORT%
SET CMDLINE=%CMDLINE% --command=:shutdown

ECHO %CMDLINE%
call %CMDLINE%

ENDLOCAL
