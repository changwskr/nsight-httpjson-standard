@echo off
setlocal

set "ROOT=%~dp0.."
cd /d "%ROOT%"

if exist "gradlew.bat" (
    set "GRADLE=gradlew.bat"
) else (
    set "GRADLE=gradle"
)

echo [scripts] Gradle :sv-service:bootRun  (port 8085)
call "%GRADLE%" :sv-service:bootRun
exit /b %ERRORLEVEL%
