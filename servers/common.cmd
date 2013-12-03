IF EXIST ../../customize/customize.cmd CALL ../../customize/customize.cmd
IF EXIST ../customize/customize.cmd CALL ../customize/customize.cmd
IF "%JAVA_HOME%"=="" ECHO ERROR: JAVA_HOME must be set. You can set it in ../../customize/customize.cmd
IF "%EAP_HOME%"=="" ECHO ERROR: EAP_HOME must be set. You can set it in ../../customize/customize.cmd
SET JBOSS_HOME=%EAP_HOME%

ECHO.
ECHO JAVA_HOME=%JAVA_HOME%
ECHO EAP_HOME=%EAP_HOME%
ECHO JBOSS_HOME=%JBOSS_HOME%
ECHO.