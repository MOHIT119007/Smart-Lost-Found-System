@echo off
setlocal enabledelayedexpansion
title Building Smart Lost and Found System
cd /d "%~dp0"

echo ===================================================
echo  Building Smart Lost and Found Desktop Application
echo ===================================================

set "JDK_BIN=%USERPROFILE%\.jdk17\bin"
if not exist "%JDK_BIN%\javac.exe" (
    where javac >nul 2>nul
    if %errorlevel% neq 0 (
        echo ERROR: JDK 17 javac not found. Run setup_jdk.ps1 or install Java 17+.
        pause
        exit /b 1
    )
    set "JAVAC_CMD=javac"
    set "JAR_CMD=jar"
) else (
    set "JAVAC_CMD=%JDK_BIN%\javac.exe"
    set "JAR_CMD=%JDK_BIN%\jar.exe"
)

echo [1/3] Cleaning previous build...
if exist "bin" rd /s /q "bin"
mkdir "bin"

echo [2/3] Compiling Java source files...
dir /s /b src\*.java > sources.txt
"%JAVAC_CMD%" -encoding UTF-8 -d bin -cp ".;lib\mysql-connector-j.jar;src" @sources.txt
if %errorlevel% neq 0 (
    echo Compilation failed!
    if exist sources.txt del sources.txt
    pause
    exit /b 1
)
if exist sources.txt del sources.txt
if exist "src\resources" xcopy /s /e /y "src\resources" "bin\resources\" >nul

echo [3/3] Packaging SmartLostAndFound.jar...
set "STAGING=%TEMP%\jar_staging"
if exist "%STAGING%" rd /s /q "%STAGING%"
mkdir "%STAGING%"

pushd "%STAGING%"
"%JAR_CMD%" -xf "%~dp0lib\mysql-connector-j.jar"
popd
xcopy /s /e /y "bin\*" "%STAGING%\" >nul

if not exist "release" mkdir "release"
"%JAR_CMD%" --create --file "release\SmartLostAndFound.jar" --main-class "Main" -C "%STAGING%" .
rd /s /q "%STAGING%" >nul 2>nul

echo ===================================================
echo  BUILD SUCCESSFUL!
echo  Runnable JAR: release\SmartLostAndFound.jar
echo  Windows Executable: release\SmartLostAndFound.exe
echo ===================================================
pause
