@ECHO OFF
SETLOCAL
call common.cmd

@REM To make it works you need to change the jconsole.bat script in order to be able to give parameter: add %* at the end of line 95
SET CMDLINE=%EAP_HOME%\bin\jconsole.bat
REM SET CMDLINE=%CMDLINE% service:jmx:remoting-jmx://localhost:5104
SET CMDLINE=%CMDLINE% %* 
@REM Don't need user/password (admin/Passw0rd) to connect to the JConsole!


ECHO %CMDLINE%
call %CMDLINE%

ENDLOCAL
