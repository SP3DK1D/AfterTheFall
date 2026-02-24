@echo off
setlocal

REM Always run from the folder this script lives in
cd /d "%~dp0"

echo ====================================
echo      AfterTheFall - Quick Start
echo ====================================

echo Compiling game files...
javac src\*.java
if errorlevel 1 (
  echo.
  echo Failed to compile the game.
  echo Make sure Java JDK 17+ is installed and javac is on PATH.
  pause
  exit /b 1
)

echo.
echo Launching game...
java src.Main

if errorlevel 1 (
  echo.
  echo Failed to launch the game.
  echo Make sure Java Runtime/JDK is correctly installed.
  pause
  exit /b 1
)

echo.
echo Game exited. Press any key to close.
pause >nul
