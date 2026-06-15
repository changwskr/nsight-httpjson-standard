@echo off
setlocal

set "ROOT=%~dp0.."
cd /d "%ROOT%"

if exist "gradlew.bat" (
    set "GRADLE=gradlew.bat"
) else (
    set "GRADLE=gradle"
)

echo [scripts] Gradle :demo-ui:bootRun  (port 8099)
echo [scripts] OM admin requires om-service on port 8096
call "%GRADLE%" :demo-ui:bootRun
exit /b %ERRORLEVEL%
