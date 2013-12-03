@ECHO OFF

SET ACTION=%1
IF "%ACTION%"=="" goto error

SET CURRENT_PATH=%~dp0
cd /d %CURRENT_PATH%node_1A
call services.cmd %ACTION%

cd /d %CURRENT_PATH%node_1B
call services.cmd %ACTION%

cd /d %CURRENT_PATH%node_2A
call services.cmd %ACTION%

cd /d %CURRENT_PATH%node_2B
call services.cmd %ACTION%

cd /d %CURRENT_PATH%node_3A
call services.cmd %ACTION%

cd /d %CURRENT_PATH%node_3B
call services.cmd %ACTION%

cd ..
goto end

:error
ECHO PARAMETER MUST BE install OR uninstall

:end