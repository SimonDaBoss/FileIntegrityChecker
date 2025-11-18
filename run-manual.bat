@echo off
REM Manual run script (after compiling with compile.bat)

echo ========================================
echo File Integrity Checker - Running
echo ========================================
echo.

REM Set your JavaFX SDK path here (same as compile.bat)
set JAVAFX_PATH=C:\javafx-sdk-21\lib
set OUT_DIR=out

if not exist "%JAVAFX_PATH%" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    echo Please edit JAVAFX_PATH in this script or use IntelliJ IDEA instead.
    echo.
    pause
    exit /b 1
)

if not exist "%OUT_DIR%" (
    echo ERROR: Project not compiled yet!
    echo Please run compile.bat first.
    echo.
    pause
    exit /b 1
)

echo Starting File Integrity Checker...
echo.

java --module-path "%JAVAFX_PATH%;%OUT_DIR%" ^
     --add-modules javafx.controls,javafx.fxml ^
     -m com.cis256.fileintegrity/com.cis256.fileintegrity.FileIntegrityChecker

pause

