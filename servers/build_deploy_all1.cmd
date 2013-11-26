@ECHO OFF
SETLOCAL
cd /d %~dp0..\apps
call mvn6 install -am -pl minibankref1

call deploy1-1.cmd NOBUILD

cd /d %~dp0
ENDLOCAL