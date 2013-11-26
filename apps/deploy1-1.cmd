if '%1' == 'NOBUILD' goto NOBUILD
call mvn6 install -am -pl minibankref1
:NOBUILD
copy /Y %~dp0minibankref1\target\minibankref1-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_1A\deployments
copy /Y %~dp0minibankref1\target\minibankref1-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_1B\deployments
