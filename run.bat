@echo off
REM File Integrity Checker - Run Script for Windows
REM This script compiles and runs the JavaFX application

echo ========================================
echo File Integrity Checker - CIS 256
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven found! Running with Maven...
    echo.
    call mvn clean javafx:run
) else (
    echo Maven not found. Please use one of these methods:
    echo.
    echo METHOD 1: Install Maven
    echo   Download from: https://maven.apache.org/download.cgi
    echo   Add to PATH and restart terminal
    echo.
    echo METHOD 2: Use IDE
    echo   - Open this folder in IntelliJ IDEA or Eclipse
    echo   - Let it import as Maven project
    echo   - Right-click FileIntegrityChecker.java and select "Run"
    echo.
    echo METHOD 3: Use VS Code
    echo   - Install "Extension Pack for Java"
    echo   - Install "JavaFX Support" extension
    echo   - Open folder in VS Code
    echo   - Press F5 to run
    echo.
    pause
)

