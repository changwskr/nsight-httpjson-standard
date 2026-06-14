@echo off
setlocal enabledelayedexpansion
set "ZTOMCAT_HOME=%~dp0"
set "PROJECT_HOME=%ZTOMCAT_HOME%.."
set "CATALINA_HOME=%ZTOMCAT_HOME%apache-tomcat-10.1.34"
set "WEBAPPS=%CATALINA_HOME%\webapps"

if not exist "%CATALINA_HOME%\bin\catalina.bat" (
    echo [ztomcat] Tomcat not found. Run install-tomcat.bat first.
    exit /b 1
)

if exist "%USERPROFILE%\.jdks\temurin-21.0.4" (
    set "JAVA_HOME=%USERPROFILE%\.jdks\temurin-21.0.4"
)

set "GRADLE=C:\Programming(23-08-15)\gradle-8.10.1\bin\gradle.bat"
if not exist "%GRADLE%" (
    where gradle >nul 2>&1
    if errorlevel 1 (
        echo [ztomcat] gradle not found.
        exit /b 1
    )
    set "GRADLE=gradle"
)

echo [ztomcat] Building WAR files ...
pushd "%PROJECT_HOME%"
call "%GRADLE%" bootWar
if errorlevel 1 (
    popd
    exit /b 1
)
popd

echo [ztomcat] Removing stale exploded directories ...
for %%W in (cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et) do (
    if exist "%WEBAPPS%\%%W" rmdir /s /q "%WEBAPPS%\%%W" 2>nul
)

echo [ztomcat] Copying WAR files to webapps ...
for %%M in (
    cc-service:cc.war
    ic-service:ic.war
    pc-service:pc.war
    bc-service:bc.war
    ms-service:ms.war
    sv-service:sv.war
    pd-service:pd.war
    cm-service:cm.war
    eb-service:eb.war
    ep-service:ep.war
    bp-service:bp.war
    bd-service:bd.war
    ss-service:ss.war
    cs-service:cs.war
    ct-service:ct.war
    mg-service:mg.war
    om-service:om.war
    common-updownload:ud.war
    common-etc:et.war
) do (
    for /f "tokens=1,2 delims=:" %%A in ("%%M") do (
        set "SRC=%PROJECT_HOME%\%%A\build\libs\%%B"
        if exist "!SRC!" (
            copy /Y "!SRC!" "%WEBAPPS%\%%B" >nul
            echo   deployed %%B
        ) else (
            echo   missing %%B
        )
    )
)

echo [ztomcat] Done. Restart Tomcat if it is already running.
