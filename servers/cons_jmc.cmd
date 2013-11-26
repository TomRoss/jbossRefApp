@ECHO OFF
rem -------------------------------------------------------------------------
rem jconsole script for Windows
rem -------------------------------------------------------------------------
rem
rem A script for running jconsole with the remoting-jmx libraries on the classpath. 
rem see http://docs.oracle.com/javase/7/docs/technotes/tools/windows/jmc.html
rem $Id$

SETLOCAL
call common.cmd
set VISUALVM_HOME=%JAVA_HOME%
echo "JBOSS_HOME = %JBOSS_HOME%"
set DIRNAME=

if "%OS%" == "Windows_NT" (
  set "PROGNAME=%~nx0%"
) else (
  set "PROGNAME=jdr.bat"
)

rem Find jboss-modules.jar, or we can't continue
if exist "%JBOSS_HOME%\jboss-modules.jar" (
    set "RUNJAR=%JBOSS_HOME%\jboss-modules.jar"
) else (
  echo Could not locate "%JBOSS_HOME%\jboss-modules.jar".
  echo Please check that you are in the bin directory when running this script.
  goto END
)

rem Set default module root paths
if "x%JBOSS_MODULEPATH%" == "x" (
  set  "JBOSS_MODULEPATH=%JBOSS_HOME%\modules\system\layers\base"
)

rem Setup The Classpath
set CLASSPATH=

call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\remoting-jmx\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\remoting3\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\logging\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\xnio\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\xnio\nio\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\sasl\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\marshalling\main
call :SearchForJars %JBOSS_MODULEPATH%\org\jboss\marshalling\river\main

echo "CLASSPATH = %CLASSPATH%"  
echo "JBOSS_MODULEPATH = %JBOSS_MODULEPATH%"
echo "%VISUALVM_HOME%\bin\jmc.exe" "-J-Xbootclasspath/a:" "%CLASSPATH%" %*

"%VISUALVM_HOME%\bin\jmc.exe" --launcher.appendVmargs -vmargs -Xbootclasspath/a:%CLASSPATH% %*

:END
goto :EOF

:SearchForJars
pushd %1
for %%j in (*.jar) do call :ClasspathAdd %1\%%j
popd
goto :EOF

:ClasspathAdd
SET CLASSPATH=%CLASSPATH%;%1


:EOF
ENDLOCAL
