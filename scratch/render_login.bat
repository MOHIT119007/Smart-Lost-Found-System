@echo off
set "JDK_BIN=%USERPROFILE%\.jdk17\bin"
"%JDK_BIN%\javac.exe" -encoding UTF-8 -cp ".;lib\mysql-connector-j.jar;src" -d scratch scratch\RenderLoginPreview.java
if %errorlevel% neq 0 exit /b %errorlevel%
"%JDK_BIN%\java.exe" -cp ".;lib\mysql-connector-j.jar;src;scratch" RenderLoginPreview
