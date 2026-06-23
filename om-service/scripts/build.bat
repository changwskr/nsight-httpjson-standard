@echo off
setlocal enabledelayedexpansion

set "PROJECT_HOME=%~dp0..\.."
for %%I in ("!PROJECT_HOME!") do set "PROJECT_HOME=%%~fI"
set "GRADLE_HOME=C:\Programming(23-08-15)\gradle-8.10.1"
set "GRADLE=!GRADLE_HOME!\bin\gradle.bat"
set "MODULE=om-service"

if defined GRADLE_HOME_OVERRIDE set "GRADLE_HOME=!GRADLE_HOME_OVERRIDE!"
if defined GRADLE_HOME set "GRADLE=!GRADLE_HOME!\bin\gradle.bat"
if not exist "!GRADLE!" for /f "delims=" %%G in ('where gradle.bat 2^>nul') do (
    set "GRADLE=%%G"
    goto :gradle_ok
)
if not exist "!GRADLE!" (
    where gradle >nul 2>&1
    if not errorlevel 1 set "GRADLE=gradle"
)
:gradle_ok
if not exist "!GRADLE!" (
    echo [om-build] gradle not found.
    exit /b 1
)

if /i "%~1"=="help" goto :usage
if /i "%~1"=="/?" goto :usage
if /i "%~1"=="-h" goto :usage

set "TASKS=:common-core:build :common-web:build :om-service:bootWar"
if /i "%~1"=="clean" set "TASKS=clean !TASKS!"
if /i "%~1"=="run" set "TASKS=:!MODULE!:bootWar"

echo [om-build] Stop Gradle daemons...
call gradle --stop >nul 2>&1

cd /d "!PROJECT_HOME!"
echo [om-build] !GRADLE! !TASKS!
call "!GRADLE!" !TASKS!
if errorlevel 1 exit /b %errorlevel%

if /i "%~1"=="run" exit /b 0

set "OUT_FILE=!PROJECT_HOME!\!MODULE!\build\libs\om.war"
echo.
echo [om-build] Build output:
if exist "!OUT_FILE!" (
    echo   [OK] om.war
    dir "!OUT_FILE!"
) else (
    echo   [MISSING] om.war - not found in !MODULE!\build\libs
    exit /b 1
)
exit /b 0

:usage
echo Usage: build.bat [clean^|run]
echo   build.bat        Build om-service om.war
echo   build.bat clean  clean + build
echo   build.bat run    bootRun ^(port 8096^)
exit /b 0
