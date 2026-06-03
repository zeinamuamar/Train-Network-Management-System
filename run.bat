@echo off
REM Compile and run the Train Network Management System splash screen.
cd /d "%~dp0"
echo Working directory: %CD%
echo Make sure you have saved src\SplashScreen.java before running.
echo File timestamp:
for %%F in (src\SplashScreen.java) do echo     %%~tF
echo Current src\SplashScreen.java contents:
echo -------------------------------------------------------------
type src\SplashScreen.java
echo -------------------------------------------------------------
echo Command: javac src\*.java
echo Compiling all Java sources in src...
javac src\*.java
if %errorlevel% neq 0 (
    echo.
    echo Compilation failed. Check the error messages above.
    exit /b %errorlevel%
)
echo Command: java -cp src SplashScreen
echo Running SplashScreen...
java -cp src SplashScreen
echo Finished running SplashScreen.
echo If you make changes, save and run this script again.
