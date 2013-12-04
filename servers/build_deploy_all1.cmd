@ECHO OFF
SETLOCAL
IF EXIST ../customize/customMvn.cmd CALL ../customize/customMvn.cmd
IF "%MVN_CMD%"=="" set MVN_CMD=mvn

cd /d %~dp0..\apps
call %MVN_CMD% install -am -pl minibankref1

call deploy1-1.cmd NOBUILD

cd /d %~dp0
ENDLOCAL