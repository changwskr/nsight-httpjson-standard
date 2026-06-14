rem Applied by ztomcat/start.ps1 on each start (WAR modules require JDK 21)
if exist "%USERPROFILE%\.jdks\temurin-21.0.4" (
    set "JAVA_HOME=%USERPROFILE%\.jdks\temurin-21.0.4"
)
set "JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
set "CATALINA_OPTS=%CATALINA_OPTS% -Xms512m -Xmx1536m -Duser.timezone=Asia/Seoul"
