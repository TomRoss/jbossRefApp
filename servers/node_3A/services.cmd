@ECHO OFF
@REM *************************************************************
@REM ** Usage: services.cmd install|uninstall                   **
@REM *************************************************************

SETLOCAL
SET SET_ENV_ONLY=true
CALL start_node.cmd

CALL ..\WindowsServiceCommon.cmd

SET CMDLINE=%NATIVE_DIR%\sbin\service.bat
SET CMDLINE=%CMDLINE% %*
SET CMDLINE=%CMDLINE% %SERVICE_NAME%

ECHO %CMDLINE%
CALL %CMDLINE%

ENDLOCAL
