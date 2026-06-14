@echo off
setlocal enabledelayedexpansion
set "ZTOMCAT_HOME=%~dp0"
set "PROJECT_HOME=%ZTOMCAT_HOME%.."
set "CATALINA_HOME=%ZTOMCAT_HOME%apache-tomcat-10.1.34"
set "WEBAPPS=%CATALINA_HOME%\webapps"

if /i "%~1"=="help" goto :usage
if /i "%~1"=="/?" goto :usage
if /i "%~1"=="-h" goto :usage

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

set "GRADLE_TASKS="
set "DEPLOY_ENTRIES="
set "CLEAN_CTX="

:collect_args
if "%~1"=="" goto :args_done
if /i "%~1"=="all" (
    set "GRADLE_TASKS="
    set "DEPLOY_ENTRIES="
    set "CLEAN_CTX="
    goto :args_done
)
call :resolve_code "%~1"
if errorlevel 1 exit /b 1
shift
goto :collect_args

:args_done
if not defined GRADLE_TASKS (
    set "GRADLE_TASKS=bootWar"
    set "DEPLOY_ALL=1"
    set "DEPLOY_ENTRIES=cc-service:cc.war ic-service:ic.war pc-service:pc.war bc-service:bc.war ms-service:ms.war sv-service:sv.war pd-service:pd.war cm-service:cm.war eb-service:eb.war ep-service:ep.war bp-service:bp.war bd-service:bd.war ss-service:ss.war cs-service:cs.war ct-service:ct.war mg-service:mg.war om-service:om.war common-updownload:ud.war common-etc:et.war"
    echo [ztomcat] Building all WAR files ...
) else (
    set "DEPLOY_ALL=0"
    echo [ztomcat] Building selected WAR^(s^): !GRADLE_TASKS!
)

pushd "%PROJECT_HOME%"
call "%GRADLE%" !GRADLE_TASKS!
if errorlevel 1 (
    popd
    exit /b 1
)
popd

echo [ztomcat] Removing stale exploded directories ...
if "!DEPLOY_ALL!"=="1" (
    for %%W in (cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et) do (
        if exist "%WEBAPPS%\%%W" rmdir /s /q "%WEBAPPS%\%%W" 2>nul
    )
) else (
    for %%W in (!CLEAN_CTX!) do (
        if exist "%WEBAPPS%\%%W" rmdir /s /q "%WEBAPPS%\%%W" 2>nul
    )
)

echo [ztomcat] Copying WAR files to webapps ...
for %%M in (!DEPLOY_ENTRIES!) do (
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

if "!DEPLOY_ALL!"=="1" (
    echo [ztomcat] Done. Restart Tomcat if it is already running.
) else (
    echo [ztomcat] Done. Tomcat running: /{code} context redeploys automatically ^(~15s^).
)
exit /b 0

:usage
echo Usage:
echo   deploy-wars.bat              Build and deploy all 19 WARs
echo   deploy-wars.bat all          Same as above
echo   deploy-wars.bat sv           Build and deploy one code ^(e.g. sv.war -^> /sv^)
echo   deploy-wars.bat sv cc ud     Build and deploy multiple codes
echo.
echo Codes: cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et
exit /b 0

:resolve_code
set "CODE=%~1"
if /i "%CODE%"=="cc" set "GRADLE_TASKS=!GRADLE_TASKS! :cc-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! cc-service:cc.war" & set "CLEAN_CTX=!CLEAN_CTX! cc" & exit /b 0
if /i "%CODE%"=="ic" set "GRADLE_TASKS=!GRADLE_TASKS! :ic-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! ic-service:ic.war" & set "CLEAN_CTX=!CLEAN_CTX! ic" & exit /b 0
if /i "%CODE%"=="pc" set "GRADLE_TASKS=!GRADLE_TASKS! :pc-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! pc-service:pc.war" & set "CLEAN_CTX=!CLEAN_CTX! pc" & exit /b 0
if /i "%CODE%"=="bc" set "GRADLE_TASKS=!GRADLE_TASKS! :bc-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! bc-service:bc.war" & set "CLEAN_CTX=!CLEAN_CTX! bc" & exit /b 0
if /i "%CODE%"=="ms" set "GRADLE_TASKS=!GRADLE_TASKS! :ms-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! ms-service:ms.war" & set "CLEAN_CTX=!CLEAN_CTX! ms" & exit /b 0
if /i "%CODE%"=="sv" set "GRADLE_TASKS=!GRADLE_TASKS! :sv-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! sv-service:sv.war" & set "CLEAN_CTX=!CLEAN_CTX! sv" & exit /b 0
if /i "%CODE%"=="pd" set "GRADLE_TASKS=!GRADLE_TASKS! :pd-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! pd-service:pd.war" & set "CLEAN_CTX=!CLEAN_CTX! pd" & exit /b 0
if /i "%CODE%"=="cm" set "GRADLE_TASKS=!GRADLE_TASKS! :cm-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! cm-service:cm.war" & set "CLEAN_CTX=!CLEAN_CTX! cm" & exit /b 0
if /i "%CODE%"=="eb" set "GRADLE_TASKS=!GRADLE_TASKS! :eb-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! eb-service:eb.war" & set "CLEAN_CTX=!CLEAN_CTX! eb" & exit /b 0
if /i "%CODE%"=="ep" set "GRADLE_TASKS=!GRADLE_TASKS! :ep-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! ep-service:ep.war" & set "CLEAN_CTX=!CLEAN_CTX! ep" & exit /b 0
if /i "%CODE%"=="bp" set "GRADLE_TASKS=!GRADLE_TASKS! :bp-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! bp-service:bp.war" & set "CLEAN_CTX=!CLEAN_CTX! bp" & exit /b 0
if /i "%CODE%"=="bd" set "GRADLE_TASKS=!GRADLE_TASKS! :bd-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! bd-service:bd.war" & set "CLEAN_CTX=!CLEAN_CTX! bd" & exit /b 0
if /i "%CODE%"=="ss" set "GRADLE_TASKS=!GRADLE_TASKS! :ss-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! ss-service:ss.war" & set "CLEAN_CTX=!CLEAN_CTX! ss" & exit /b 0
if /i "%CODE%"=="cs" set "GRADLE_TASKS=!GRADLE_TASKS! :cs-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! cs-service:cs.war" & set "CLEAN_CTX=!CLEAN_CTX! cs" & exit /b 0
if /i "%CODE%"=="ct" set "GRADLE_TASKS=!GRADLE_TASKS! :ct-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! ct-service:ct.war" & set "CLEAN_CTX=!CLEAN_CTX! ct" & exit /b 0
if /i "%CODE%"=="mg" set "GRADLE_TASKS=!GRADLE_TASKS! :mg-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! mg-service:mg.war" & set "CLEAN_CTX=!CLEAN_CTX! mg" & exit /b 0
if /i "%CODE%"=="om" set "GRADLE_TASKS=!GRADLE_TASKS! :om-service:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! om-service:om.war" & set "CLEAN_CTX=!CLEAN_CTX! om" & exit /b 0
if /i "%CODE%"=="ud" set "GRADLE_TASKS=!GRADLE_TASKS! :common-updownload:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! common-updownload:ud.war" & set "CLEAN_CTX=!CLEAN_CTX! ud" & exit /b 0
if /i "%CODE%"=="et" set "GRADLE_TASKS=!GRADLE_TASKS! :common-etc:bootWar" & set "DEPLOY_ENTRIES=!DEPLOY_ENTRIES! common-etc:et.war" & set "CLEAN_CTX=!CLEAN_CTX! et" & exit /b 0
echo [ztomcat] Unknown code: %CODE%
echo [ztomcat] Codes: cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et
exit /b 1
