@echo off
title Smart Lost and Found System
cd /d "%~dp0"

echo Launching Smart Lost and Found System...

if exist "SmartLostAndFound.exe" (
    start "" "SmartLostAndFound.exe"
    exit /b 0
)

if exist "%USERPROFILE%\.jdk17\bin\javaw.exe" (
    start "" "%USERPROFILE%\.jdk17\bin\javaw.exe" -jar "SmartLostAndFound.jar"
    exit /b 0
)

where javaw >nul 2>nul
if %errorlevel% equ 0 (
    start "" javaw -jar "SmartLostAndFound.jar"
    exit /b 0
)

where java >nul 2>nul
if %errorlevel% equ 0 (
    java -jar "SmartLostAndFound.jar"
    exit /b 0
)

echo ERROR: Java Runtime Environment not found.
pause
