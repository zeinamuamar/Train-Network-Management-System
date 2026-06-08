@echo off
REM Compile and run the Train Network Management System (run from repo root)
cd /d "%~dp0"
echo.
echo Compiling all Java sources under src\...
javac -encoding UTF-8 src\*.java
if %errorlevel% neq 0 (
    echo.
    echo Compilation failed. Check the error messages above.
    pause
    exit /b %errorlevel%
)
echo Compilation successful!
echo.
echo Running Train Network Management System...
echo.
java -cp src Main
echo.
echo Application closed. If you make changes, save and run this script again.
