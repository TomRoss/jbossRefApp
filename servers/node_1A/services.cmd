@ECHO OFF
@REM *************************************************************
@REM ** Usage: services.cmd install|uninstall                   **
@REM *************************************************************

SETLOCAL
SET SET_ENV_ONLY=true
CALL start_node.cmd

SET SERVICE_NAME=EAP6_%JBOSS_SERVER_NAME%
SET SERVICE_DESC=JBoss_Enterprise_Application_Server_%SERVICE_NAME%

SET CMDLINE=%NATIVE_DIR%\sbin\service.bat
SET CMDLINE=%CMDLINE% %*
SET CMDLINE=%CMDLINE% /name %SERVICE_NAME%
SET CMDLINE=%CMDLINE% /desc %SERVICE_DESC%
SET CMDLINE=%CMDLINE% /loglevel INFO
SET CMDLINE=%CMDLINE% /base %JBOSS_BASE_DIR%
SET CMDLINE=%CMDLINE% /startpath %JBOSS_BASE_DIR%
SET CMDLINE=%CMDLINE% /stoppath %JBOSS_BASE_DIR%
SET CMDLINE=%CMDLINE% /startscript start_node.cmd
SET CMDLINE=%CMDLINE% /stopscript stop_node.cmd
SET CMDLINE=%CMDLINE% /logpath %JBOSS_BASE_DIR%\log
REM SET CMDLINE=%CMDLINE% /startup
REM SET CMDLINE=%CMDLINE% /serviceuser myUser
REM SET CMDLINE=%CMDLINE% /servicepass myPassword

ECHO %CMDLINE%
CALL %CMDLINE%

ENDLOCAL
