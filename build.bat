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
        echo ERROR: JDK 17 javac not found. Run setup_jdk.ps1 first.
        pause
        exit /b 1
    )
    set "JAVAC_CMD=javac"
    set "JAR_CMD=jar"
) else (
    set "JAVAC_CMD=%JDK_BIN%\javac.exe"
    set "JAR_CMD=%JDK_BIN%\jar.exe"
)

echo [1/4] Cleaning previous build...
if exist "bin" rd /s /q "bin"
mkdir "bin"

echo [2/4] Compiling Java source files...
dir /s /b src\*.java > sources.txt
"%JAVAC_CMD%" -encoding UTF-8 -d bin -cp ".;lib\mysql-connector-j.jar;src" @sources.txt
if %errorlevel% neq 0 (
    echo Compilation failed!
    del sources.txt
    pause
    exit /b 1
)
del sources.txt
if exist "src\resources" xcopy /s /e /y "src\resources" "bin\resources\" >nul

echo [3/4] Packaging SmartLostAndFound.jar...
set "STAGING=%TEMP%\jar_staging"
if exist "%STAGING%" rd /s /q "%STAGING%"
mkdir "%STAGING%"

pushd "%STAGING%"
"%JAR_CMD%" -xf "%~dp0lib\mysql-connector-j.jar"
popd
xcopy /s /e /y "bin\*" "%STAGING%\" >nul

"%JAR_CMD%" --create --file "SmartLostAndFound.jar" --main-class "Main" -C "%STAGING%" .
rd /s /q "%STAGING%" >nul 2>nul

echo [4/4] Building SmartLostAndFound.exe...
set "CSC=C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe"
if exist "%CSC%" (
    if exist "app.ico" (
        "%CSC%" /nologo /target:winexe /win32icon:app.ico /out:SmartLostAndFound.exe launcher.cs
    ) else (
        "%CSC%" /nologo /target:winexe /out:SmartLostAndFound.exe launcher.cs
    )
)

echo ===================================================
echo  BUILD SUCCESSFUL!
echo  Executable: SmartLostAndFound.exe
echo  Runnable JAR: SmartLostAndFound.jar
echo ===================================================
pause
