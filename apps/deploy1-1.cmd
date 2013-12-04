IF EXIST ../customize/customMvn.cmd CALL ../customize/customMvn.cmd
IF "%MVN_CMD%"=="" set MVN_CMD=mvn

if '%1' == 'NOBUILD' goto NOBUILD
call %MVN_CMD% install -am -pl minibankref1
:NOBUILD
copy /Y %~dp0minibankref1\target\minibankref1-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_1A\deployments
copy /Y %~dp0minibankref1\target\minibankref1-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_1B\deployments
