@ECHO OFF
rem -------------------------------------------------------------------------
rem jconsole script for Windows
rem -------------------------------------------------------------------------
rem
rem A script for running jconsole with the remoting-jmx libraries on the classpath. 
rem see http://refcardz.dzone.com/refcardz/java-profiling-visualvm
rem $Id$

SETLOCAL
call common.cmd
set VISUALVM_HOME=%JAVA_HOME%
echo "JBOSS_HOME = %JBOSS_HOME%"

if "%OS%" == "Windows_NT" (
  set "PROGNAME=%~nx0%"
) else (
  set "PROGNAME=jdr.bat"
)

set "CLASSPATH=%JAVA_HOME%\lib\jconsole.jar"
set "CLASSPATH=%CLASSPATH%;%JAVA_HOME%\lib\tools.jar"
set "CLASSPATH=%CLASSPATH%;%JBOSS_HOME%\bin\client\jboss-cli-client.jar"

echo "CLASSPATH = %CLASSPATH%"  
echo "%VISUALVM_HOME%\bin\jmc.exe" --launcher.appendVmargs -vmargs -Xbootclasspath/a:%CLASSPATH% %*

"%VISUALVM_HOME%\bin\jmc.exe" --launcher.appendVmargs -vmargs -Xbootclasspath/a:%CLASSPATH% %*

ENDLOCAL
