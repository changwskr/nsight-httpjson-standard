@echo off
rem ztomcat 로컬 JVM 설정 (Tomcat setenv.bat 에서 call)

if not defined JAVA_HOME (
    if exist "%USERPROFILE%\.jdks\temurin-21.0.4" (
        set "JAVA_HOME=%USERPROFILE%\.jdks\temurin-21.0.4"
    )
)

if not defined JAVA_HOME (
    echo [ztomcat] JAVA_HOME is not set. Install JDK 21 or set JAVA_HOME.
    exit /b 1
)

set "CATALINA_OPTS=-Xms512m -Xmx1536m -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Seoul"
