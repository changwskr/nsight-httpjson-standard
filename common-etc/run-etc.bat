@echo off
setlocal
cd /d "%~dp0.."
gradle :common-etc:bootRun
