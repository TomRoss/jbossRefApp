IF EXIST ../customize/customMvn.cmd CALL ../customize/customMvn.cmd
IF "%MVN_CMD%"=="" set MVN_CMD=mvn

if '%1' == 'NOBUILD' goto NOBUILD
call %MVN_CMD% install -am -pl minibankref2
:NOBUILD
copy /Y %~dp0minibankref2\target\minibankref2-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_2A\deployments
copy /Y %~dp0minibankref2\target\minibankref2-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_2B\deployments
