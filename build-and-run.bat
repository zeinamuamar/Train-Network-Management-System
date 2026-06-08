@echo off
REM Automatic build and run script for Train Network Management System
REM This script compiles all Java files and runs the application

cd /d "%~dp0src"

REM Compile all Java files
echo Compiling Java files...
javac -encoding UTF-8 *.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo.
    echo COMPILATION FAILED - Please check the errors above
    echo.
    pause
    exit /b 1
)

echo.
echo Compilation successful! Running application...
echo.

REM Run the application
java -cp . Main

REM If the application closes, show a pause message
pause
