@ECHO OFF

call %~dp0servers\common.cmd
call %~dp0servers\del_all_dirs.cmd

del /Q miniBankkRef.zip
%JAVA_HOME%\bin\jar -cfM miniBankRef.zip -C %~dp0 apps -C %~dp0 servers -C %~dp0 README.txt

rem pause