@ECHO OFF
SETLOCAL
IF EXIST ../customize/customMvn.cmd CALL ../customize/customMvn.cmd
IF "%MVN_CMD%"=="" set MVN_CMD=mvn

cd /d %~dp0..\apps
call %MVN_CMD% install -am -pl minibankref1,minibankref2,minibankref3

call deploy1-1.cmd NOBUILD
REM call deploy1-2.cmd NOBUILD
call deploy2-2.cmd NOBUILD
call deploy3-3.cmd NOBUILD

cd /d %~dp0
ENDLOCAL