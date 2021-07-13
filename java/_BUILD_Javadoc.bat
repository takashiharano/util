@echo off
cd /d %~dp0
call mvn javadoc:javadoc
pause
