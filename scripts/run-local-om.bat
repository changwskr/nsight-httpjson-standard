@echo off
setlocal

set "ROOT=%~dp0.."
cd /d "%ROOT%"

if exist "gradlew.bat" (
    set "GRADLE=gradlew.bat"
) else (
    set "GRADLE=gradle"
)

echo [scripts] Gradle :om-service:bootRun  (port 8096)
call "%GRADLE%" :om-service:bootRun
exit /b %ERRORLEVEL%
