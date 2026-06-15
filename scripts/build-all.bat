@echo off
setlocal

set "ROOT=%~dp0.."
cd /d "%ROOT%"

if exist "gradlew.bat" (
    set "GRADLE=gradlew.bat"
) else (
    set "GRADLE=gradle"
)

echo [scripts] Gradle clean build bootWar
call "%GRADLE%" clean build bootWar
if errorlevel 1 exit /b 1

echo.
echo [scripts] Done.
exit /b 0
