@ECHO OFF
SETLOCAL
IF EXIST ../customize/customMvn.cmd CALL ../customize/customMvn.cmd
IF "%MVN_CMD%"=="" set MVN_CMD=mvn

cd /d %~dp0..\apps
call %MVN_CMD% install -am -pl minibankrefSecurity

copy /Y %~dp0..\apps\minibankrefSecurity\target\minibankrefSecurity-*.jar %~dp0\node_2A\modules\org\app\minibank\security\main
copy /Y %~dp0..\apps\minibankrefSecurity\target\minibankrefSecurity-*.jar %~dp0\node_2B\modules\org\app\minibank\security\main

cd /d %~dp0
ENDLOCAL