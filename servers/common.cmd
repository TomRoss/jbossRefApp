SET CURRENT_COMMON_DIR=%~dp0
REM clean up variable you can set default values in customize.cmd
SET MINIBANK_DB_DIALECT=
SET MINIBANK_DB_URL=
SET MINIBANK_DB_USER=
SET MINIBANK_DB_PWD=
SET MINIBANK_DB_DRIVER_NAME=
SET MINIBANK_DB_NO_TX_SEPARATE_POOLS=
SET MINIBANK_DB_WRAP_XA_RESOURCE=
IF "%MINIBANK_DB_TYPE%"=="" SET MINIBANK_DB_TYPE=H2EMBEDDED

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
REM call the customize.ps1 of the node
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

IF "%MINIBANK_DB_TYPE%"=="ORACLE" GOTO setdboracle
IF "%MINIBANK_DB_TYPE%"=="H2" GOTO setdbh2
IF "%MINIBANK_DB_TYPE%"=="H2EMBEDDED" GOTO setdbh2
IF "%MINIBANK_DB_TYPE%"=="DERBY" GOTO setdbderby
GOTO setdbh2

:setdboracle
if "%MINIBANK_DB_DIALECT%"=="" SET MINIBANK_DB_DIALECT=org.hibernate.dialect.Oracle10gDialect
if "%MINIBANK_DB_URL%"=="" SET MINIBANK_DB_URL=jdbc:oracle:thin:...
if "%MINIBANK_DB_USER%"=="" SET MINIBANK_DB_USER=aUser
if "%MINIBANK_DB_PWD%"=="" SET MINIBANK_DB_PWD=aPassword
if "%MINIBANK_DB_DRIVER_NAME%"=="" SET MINIBANK_DB_DRIVER_NAME=oracle-XA
if "%MINIBANK_DB_NO_TX_SEPARATE_POOLS%"=="" SET MINIBANK_DB_NO_TX_SEPARATE_POOLS=true
if "%MINIBANK_DB_WRAP_XA_RESOURCE%"=="" SET MINIBANK_DB_WRAP_XA_RESOURCE=true
GOTO endsetdb

:setdbh2
if "%MINIBANK_DB_DIALECT%"=="" SET MINIBANK_DB_DIALECT=org.hibernate.dialect.H2Dialect
if "%MINIBANK_DB_URL%"=="" IF "%MINIBANK_DB_TYPE%"=="H2" SET MINIBANK_DB_URL=jdbc:h2:tcp://localhost:9092/minibank;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=60000;MULTI_THREADED=true
if "%MINIBANK_DB_URL%"=="" IF "%MINIBANK_DB_TYPE%"=="H2EMBEDDED" SET MINIBANK_DB_URL=jdbc:h2:data/minibank/minibank;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=60000;MULTI_THREADED=true
if "%MINIBANK_DB_USER%"=="" SET MINIBANK_DB_USER=sa
if "%MINIBANK_DB_PWD%"=="" SET MINIBANK_DB_PWD=sa
if "%MINIBANK_DB_DRIVER_NAME%"=="" SET MINIBANK_DB_DRIVER_NAME=h2
if "%MINIBANK_DB_NO_TX_SEPARATE_POOLS%"=="" SET MINIBANK_DB_NO_TX_SEPARATE_POOLS=false
if "%MINIBANK_DB_WRAP_XA_RESOURCE%"=="" SET MINIBANK_DB_WRAP_XA_RESOURCE=true
GOTO endsetdb

:setdbderby
if "%MINIBANK_DB_DIALECT%"=="" SET MINIBANK_DB_DIALECT=org.hibernate.dialect.DerbyTenSevenDialect
if "%MINIBANK_DB_URL%"=="" SET MINIBANK_DB_URL=ServerName=localhost;PortNumber=1527;DatabaseName=minibank;ConnectionAttributes=create=true
if "%MINIBANK_DB_USER%"=="" SET MINIBANK_DB_USER=
if "%MINIBANK_DB_PWD%"=="" SET MINIBANK_DB_PWD=
if "%MINIBANK_DB_DRIVER_NAME%"=="" SET MINIBANK_DB_DRIVER_NAME=derby
if "%MINIBANK_DB_NO_TX_SEPARATE_POOLS%"=="" SET MINIBANK_DB_NO_TX_SEPARATE_POOLS=false
if "%MINIBANK_DB_WRAP_XA_RESOURCE%"=="" SET MINIBANK_DB_WRAP_XA_RESOURCE=true
GOTO endsetdb

:endsetdb
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.dialect=%MINIBANK_DB_DIALECT%"
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.url=%MINIBANK_DB_URL%"
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.user=%MINIBANK_DB_USER%"
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.pwd=%MINIBANK_DB_PWD%"
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.driver.name=%MINIBANK_DB_DRIVER_NAME%"
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.no.tx.separate.pools=%MINIBANK_DB_NO_TX_SEPARATE_POOLS%"
SET "JAVA_OPTS=%JAVA_OPTS% -Dminibank.db.wrap.xa.resource=%MINIBANK_DB_WRAP_XA_RESOURCE%"

REM we are running the EAP instance through the Windows Service so we don't want to display log to console
IF "%NOPAUSE%" == "Y" SET "JAVA_OPTS=%JAVA_OPTS% -Djboss.console.threshold=OFF"
SET "JAVA_OPTS=%JAVA_OPTS% %JAVA_OPTS_POST%"

SET ARGLINE=%ARGLINE_PRE% -b 0.0.0.0
SET ARGLINE=%ARGLINE% -bmanagement 0.0.0.0
SET ARGLINE=%ARGLINE% -u %UDP_ADDR%
SET ARGLINE=%ARGLINE% --server-config standalone-full-ha.xml
SET ARGLINE=%ARGLINE% %ARGLINE_POST%

SET NATIVE_DIR=%EAP_HOME%\modules\system\layers\base\native

SET JBOSS_MODULEPATH=%EAP_HOME%\modules
IF EXIST %CUSTOM_MODULE_DIR% SET JBOSS_MODULEPATH=%JBOSS_MODULEPATH%;%CUSTOM_MODULE_DIR%
SET MODULE_DIR=%CURRENT_DIR%..\modules
IF EXIST %MODULE_DIR% SET JBOSS_MODULEPATH=%JBOSS_MODULEPATH%;%MODULE_DIR%

ECHO.
ECHO JAVA_HOME=%JAVA_HOME%
ECHO EAP_HOME=%EAP_HOME%
ECHO JBOSS_HOME=%JBOSS_HOME%
ECHO JAVA_OPTS=%JAVA_OPTS%
ECHO ARGLINE=%ARGLINE%
ECHO JBOSS_MODULEPATH=%JBOSS_MODULEPATH%
ECHO.

REM create a file containing db info: used by the tests to use the right database
SET PROP_FILE=%CURRENT_COMMON_DIR%minibankdb.properties
ECHO MINIBANK_DB_TYPE=%MINIBANK_DB_TYPE%> %PROP_FILE%
ECHO MINIBANK_DB_DIALECT=%MINIBANK_DB_DIALECT%>> %PROP_FILE%
ECHO MINIBANK_DB_URL=%MINIBANK_DB_URL%>> %PROP_FILE%
ECHO MINIBANK_DB_USER=%MINIBANK_DB_USER%>> %PROP_FILE%
ECHO MINIBANK_DB_PWD=%MINIBANK_DB_PWD%>> %PROP_FILE%
ECHO MINIBANK_DB_DRIVER_NAME=%MINIBANK_DB_DRIVER_NAME%>> %PROP_FILE%
ECHO MINIBANK_DB_NO_TX_SEPARATE_POOLS=%MINIBANK_DB_NO_TX_SEPARATE_POOLS%>> %PROP_FILE%
ECHO MINIBANK_DB_WRAP_XA_RESOURCE=%MINIBANK_DB_WRAP_XA_RESOURCE%>> %PROP_FILE%

REM remove variables
SET MINIBANK_DB_TYPE=
SET MINIBANK_DB_DIALECT=
SET MINIBANK_DB_URL=
SET MINIBANK_DB_USER=
SET MINIBANK_DB_PWD=
SET MINIBANK_DB_DRIVER_NAME=
SET MINIBANK_DB_NO_TX_SEPARATE_POOLS=
SET MINIBANK_DB_WRAP_XA_RESOURCE=
