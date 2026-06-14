@echo off
setlocal
cd /d "%~dp0.."
gradle :demo-ui:bootRun
