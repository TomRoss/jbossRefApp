if '%1' == 'NOBUILD' goto NOBUILD
call mvn6 install -am -pl minibankref3
:NOBUILD
copy /Y %~dp0minibankref3\target\minibankref3-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_3A\deployments
copy /Y %~dp0minibankref3\target\minibankref3-3.0.0-SNAPSHOT.ear %~dp0..\servers\node_3B\deployments
