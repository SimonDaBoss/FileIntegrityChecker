@echo off
REM Manual compilation script (if JavaFX SDK is downloaded separately)
REM You need to download JavaFX SDK from: https://gluonhq.com/products/javafx/

echo ========================================
echo File Integrity Checker - Manual Compile
echo ========================================
echo.

REM Set your JavaFX SDK path here
set JAVAFX_PATH=C:\javafx-sdk-21\lib

REM Check if JavaFX path exists
if not exist "%JAVAFX_PATH%" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    echo.
    echo Please either:
    echo   1. Download JavaFX SDK from https://gluonhq.com/products/javafx/
    echo   2. Extract it and set JAVAFX_PATH in this script
    echo   3. OR use IntelliJ IDEA which handles everything automatically!
    echo.
    echo RECOMMENDED: Use IntelliJ IDEA Community Edition (FREE)
    echo   - Download from: https://www.jetbrains.com/idea/download/
    echo   - Open this folder in IntelliJ
    echo   - Right-click FileIntegrityChecker.java and select Run
    echo   - Done!
    echo.
    pause
    exit /b 1
)

set SRC_DIR=src\main\java
set OUT_DIR=out
set RESOURCES=src\main\resources

echo Creating output directory...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

echo Copying resources...
if exist "%RESOURCES%" (
    xcopy /Y /E "%RESOURCES%" "%OUT_DIR%"
)

echo Compiling Java files...
javac --module-path "%JAVAFX_PATH%" ^
      --add-modules javafx.controls,javafx.fxml ^
      -d "%OUT_DIR%" ^
      "%SRC_DIR%\module-info.java" ^
      "%SRC_DIR%\com\cis256\fileintegrity\HashUtility.java" ^
      "%SRC_DIR%\com\cis256\fileintegrity\FileIntegrityChecker.java"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
    echo.
    echo To run the application, use: run-manual.bat
    echo.
) else (
    echo.
    echo ========================================
    echo Compilation failed!
    echo ========================================
    echo.
    echo Please check:
    echo   - Java JDK 17+ is installed
    echo   - JAVAFX_PATH is correct in this script
    echo   - No syntax errors in the code
    echo.
)

pause

