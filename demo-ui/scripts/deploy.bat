@echo off
setlocal enabledelayedexpansion

set "PROJECT_HOME=%~dp0..\.."
for %%I in ("!PROJECT_HOME!") do set "PROJECT_HOME=%%~fI"
set "BIN_DIR=!PROJECT_HOME!\bin"
set "GRADLE_HOME=C:\Programming(23-08-15)\gradle-8.10.1"
set "GRADLE=!GRADLE_HOME!\bin\gradle.bat"
set "MODULE=demo-ui"
set "SRC_JAR=demo-ui.jar"
set "DEST_JAR=demo-ui.jar"

if defined DEMO_UI_BIN set "BIN_DIR=!DEMO_UI_BIN!"

if /i "%~1"=="help" goto :usage
if /i "%~1"=="/?" goto :usage
if /i "%~1"=="-h" goto :usage

if not exist "!BIN_DIR!" mkdir "!BIN_DIR!" 2>nul
if not exist "!BIN_DIR!" goto :bin_missing
goto :bin_ok
:bin_missing
echo [demo-ui-deploy] bin directory not found: !BIN_DIR!
exit /b 1
:bin_ok

if defined GRADLE_HOME_OVERRIDE set "GRADLE_HOME=!GRADLE_HOME_OVERRIDE!"
if defined GRADLE_HOME set "GRADLE=!GRADLE_HOME!\bin\gradle.bat"
if not exist "!GRADLE!" for /f "delims=" %%G in ('where gradle.bat 2^>nul') do (
    set "GRADLE=%%G"
    goto :gradle_ok
)
:gradle_ok
if not exist "!GRADLE!" goto :gradle_missing
goto :gradle_found
:gradle_missing
echo [demo-ui-deploy] gradle not found.
exit /b 1
:gradle_found

cd /d "!PROJECT_HOME!"
echo [demo-ui-deploy] Building !SRC_JAR! ...
call "!GRADLE!" :demo-ui:bootJar
if errorlevel 1 exit /b 1

set "SRC=!PROJECT_HOME!\!MODULE!\build\libs\!SRC_JAR!"
if not exist "!SRC!" (
    echo [demo-ui-deploy] JAR not found: !SRC!
    exit /b 1
)

echo [demo-ui-deploy] Copying !DEST_JAR! to !BIN_DIR! ...
copy /Y "!SRC!" "!BIN_DIR!\!DEST_JAR!" >nul
echo   deployed !DEST_JAR!

echo.
echo [demo-ui-deploy] Verifying deployed JAR ...
if not exist "!BIN_DIR!\!DEST_JAR!" goto :verify_failed
echo   [OK] !DEST_JAR!
dir "!BIN_DIR!\!DEST_JAR!"
echo.
echo [demo-ui-deploy] Done. Start with run-local.bat or: java -jar bin\demo-ui.jar ^(port 8099^).
exit /b 0

:verify_failed
echo [demo-ui-deploy] Verification failed: !DEST_JAR! not found in !BIN_DIR!
exit /b 1

:usage
echo Usage: deploy.bat
echo   Build demo-ui.jar and copy to bin\ ^(staging^)
echo.
echo Target bin directory:
echo   !BIN_DIR!
echo   ^(override: set DEMO_UI_BIN=...^)
exit /b 0
