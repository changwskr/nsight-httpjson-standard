@echo off
setlocal

set "ROOT=%~dp0.."
set "OUT_DIR=%ROOT%\bin"

cd /d "%ROOT%"

if "%~1"=="-h" goto usage
if "%~1"=="--help" goto usage
if "%~1"=="help" goto usage

if exist "gradlew.bat" set "GRADLE=gradlew.bat"
if not exist "gradlew.bat" set "GRADLE=gradle"

if "%~1"=="" goto build_all

set "CODE=%~1"
call :build_one %CODE%
if errorlevel 1 exit /b 1
goto copy_done

:build_all
echo ==^> Gradle bootWar: all business modules
call %GRADLE% :cc-service:bootWar :ic-service:bootWar :pc-service:bootWar :bc-service:bootWar :ms-service:bootWar :sv-service:bootWar :pd-service:bootWar :cm-service:bootWar :eb-service:bootWar :ep-service:bootWar :bp-service:bootWar :bd-service:bootWar :ss-service:bootWar :cs-service:bootWar :ct-service:bootWar :mg-service:bootWar :om-service:bootWar
if errorlevel 1 exit /b 1

if not exist "bin" mkdir "bin"

call :copy_war cc
call :copy_war ic
call :copy_war pc
call :copy_war bc
call :copy_war ms
call :copy_war sv
call :copy_war pd
call :copy_war cm
call :copy_war eb
call :copy_war ep
call :copy_war bp
call :copy_war bd
call :copy_war ss
call :copy_war cs
call :copy_war ct
call :copy_war mg
call :copy_war om
goto copy_done

:build_one
echo ==^> Gradle bootWar: %1
call %GRADLE% :%1-service:bootWar
if errorlevel 1 exit /b 1
if not exist "bin" mkdir "bin"
call :copy_war %1
exit /b 0

:copy_war
set "SRC=%1-service\build\libs\%1.war"
set "DEST=bin\%1.war"
if not exist "%SRC%" goto copy_war_missing
copy /Y "%SRC%" "%DEST%" >nul
echo   copied -^> bin\%1.war
exit /b 0

:copy_war_missing
echo ERROR: WAR not found: %CD%\%SRC%
exit /b 1

:copy_done
echo.
echo ==^> Done. WAR files in bin\
dir /b "bin\*.war" 2>nul
exit /b 0

:usage
echo Usage:
echo   bin\build-wars.bat           build all business WAR files
echo   bin\build-wars.bat sv        build one module
echo.
echo Output: bin\{code}.war
exit /b 0
