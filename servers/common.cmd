IF EXIST ../../customize/customize.cmd CALL ../../customize/customize.cmd
IF EXIST ../customize/customize.cmd CALL ../customize/customize.cmd
IF "%JAVA_HOME%"=="" ECHO ERROR: JAVA_HOME must be set. You can set it in ../../customize/customize.cmd
IF "%EAP_HOME%"=="" ECHO ERROR: EAP_HOME must be set. You can set it in ../../customize/customize.cmd
SET JBOSS_HOME=%EAP_HOME%

set JVM_NAME=JBRA_%JBOSS_SERVER_NAME%.exe
REM rename/copy java.exe in JBRA_node_1A.exe
REM to make it works you will need to change script "%EAP_HOME%\bin\standalone.bat" around line 110 to add the JVM_NAME variable
if not exist "%JAVA_HOME%\bin\%JVM_NAME%" (
	ECHO Renaming file "%JAVA_HOME%\bin\java.exe" to "%JAVA_HOME%\bin\%JVM_NAME%"
	copy /V /Y /B "%JAVA_HOME%\bin\java.exe" "%JAVA_HOME%\bin\%JVM_NAME%"
)

REM When not running through a windows service we don't want to be paused when jboss is ending
REM but see later the NOPAUSE var is also use to determine if we are running through a windows service
IF "%SERVICE_NAME%"=="" set NOPAUSE=true

SET PATH=%JAVA_HOME%\bin;%EAP_HOME%\bin;%PATH%
IF EXIST customize.cmd CALL customize.cmd

IF "%MEMORY_HEAP%"=="" SET MEMORY_HEAP=-Xms128M -Xmx128M -XX:MaxPermSize=128M

IF NOT "%JAVA_OPTS_PRE%"=="" SET "JAVA_OPTS=%JAVA_OPTS_PRE% %MEMORY_HEAP%"
IF "%JAVA_OPTS_PRE%"=="" SET "JAVA_OPTS=%MEMORY_HEAP%"
IF NOT "%SET_ENV_ONLY%"=="true" SET "JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,address=5%OFFSET%,server=y,suspend=n -Xdebug"
SET "JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.byteman"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.socket.binding.port-offset=%OFFSET%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.server.base.dir=%JBOSS_BASE_DIR%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.server.name=%JBOSS_SERVER_NAME%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.node.name=%COMPUTERNAME%_%OFFSET%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djgroups.bind_addr=%COMPUTERNAME%"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.udp.fixedport=true"
SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.messaging.group.address=%UDP_ADDR%"
REM use to avoid Warning - unknown codeset (Cp1252) - defaulting to ISO-8859-1
SET "JAVA_OPTS=%JAVA_OPTS% -Djacorb.native_char_codeset=UTF8"
SET "JAVA_OPTS=%JAVA_OPTS% -Djacorb.native_wchar_codeset=UTF16"

REM we are running the EAP instance through the Windows Service so we don't want to display log to console
IF "%NOPAUSE%" == "Y" SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.console.threshold=OFF"
SET "JAVA_OPTS=%JAVA_OPTS% %JAVA_OPTS_POST%"

SET ARGLINE=%ARGLINE_PRE% -b 0.0.0.0
SET ARGLINE=%ARGLINE% -bmanagement 0.0.0.0
SET ARGLINE=%ARGLINE% -u %UDP_ADDR%
SET ARGLINE=%ARGLINE% --server-config standalone-full-ha.xml
SET ARGLINE=%ARGLINE% %ARGLINE_POST%

SET NATIVE_DIR=%EAP_HOME%\modules\system\layers\base\native
SET CUSTOM_MODULE=%CURRENT_DIR%modules
IF EXIST %CUSTOM_MODULE% SET JBOSS_MODULEPATH=%EAP_HOME%\modules;%CUSTOM_MODULE%

ECHO.
ECHO JAVA_HOME=%JAVA_HOME%
ECHO EAP_HOME=%EAP_HOME%
ECHO JBOSS_HOME=%JBOSS_HOME%
ECHO JAVA_OPTS=%JAVA_OPTS%
ECHO ARGLINE=%ARGLINE%
ECHO JBOSS_MODULEPATH=%JBOSS_MODULEPATH%
ECHO.
