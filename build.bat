@echo off
setlocal enabledelayedexpansion

set "PROJECT_DIR=%~dp0"
:: Quita la barra final si existe
if "%PROJECT_DIR:~-1%"=="\" set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"

set "JAVAFX=%PROJECT_DIR%\lib\javafx-sdk-21.0.12"
set "BUILD=%PROJECT_DIR%\build"
set "RELEASE=%PROJECT_DIR%\release"

echo ========================================
echo         Rocket Tracker - BUILD (Windows)
echo ========================================

:: ============================
:: Limpiar
:: ============================

echo [1/5] Limpiando...

if exist "%BUILD%" rd /s /q "%BUILD%"
if exist "%RELEASE%" rd /s /q "%RELEASE%"

mkdir "%BUILD%\classes"
mkdir "%BUILD%\input"
mkdir "%RELEASE%"

:: ============================
:: Compilar
:: ============================

echo [2/5] Compilando Java...

dir /s /b "%PROJECT_DIR%\src\*.java" > "%BUILD%\sources.txt"

javac ^
    --module-path "%JAVAFX%\lib" ^
    --add-modules javafx.controls ^
    -d "%BUILD%\classes" ^
    @"%BUILD%\sources.txt"

if %ERRORLEVEL% NEQ 0 (
    echo Error en la compilación de Java.
    exit /b %ERRORLEVEL%
)

:: ============================
:: Recursos
:: ============================

echo [3/5] Copiando recursos...

if exist "%PROJECT_DIR%\res" (
    xcopy /E /I /Y "%PROJECT_DIR%\res\*" "%BUILD%\classes\" >nul
)

:: ============================
:: JAR
:: ============================

echo [4/5] Creando JAR...

echo Main-Class: app.Main> "%BUILD%\MANIFEST.MF"

jar ^
    --create ^
    --file "%BUILD%\input\RocketTracker.jar" ^
    --manifest "%BUILD%\MANIFEST.MF" ^
    -C "%BUILD%\classes" .

:: ============================
:: jpackage
:: ============================

echo [5/5] Ejecutando jpackage...

jpackage ^
    --type app-image ^
    --name RocketTracker ^
    --input "%BUILD%\input" ^
    --main-jar RocketTracker.jar ^
    --main-class app.Main ^
    --module-path "%JAVAFX%\lib" ^
    --add-modules javafx.controls,javafx.fxml,javafx.graphics,java.net.http ^
    --java-options "-Djava.library.path=%JAVAFX%\lib" ^
    --dest "%RELEASE%"

cd /d "%RELEASE%\RocketTracker"

if not exist "data\matches" mkdir "data\matches"

:: En Windows creamos un lanzador batch en la raíz para evitar errores de permisos de symlinks
powershell -NoProfile -Command "$ws = New-Object -ComObject WScript.Shell; $s = $ws.CreateShortcut('%~dp0RocketTracker.lnk'); $s.TargetPath = '%~dp0bin\RocketTracker.exe'; $s.WorkingDirectory = '%~dp0'; $s.Save()"

:: ============================
:: ZIP
:: ============================

set /p respuesta="¿Deseas comprimir la aplicación? (y/n): "

if /i "%respuesta%"=="y" (
    echo Comprimiendo paquete...
    cd /d "%RELEASE%"
    if exist "RocketTracker-Win.zip" del /f /q "RocketTracker-Win.zip"

    powershell -Command "Compress-Archive -Path 'RocketTracker' -DestinationPath 'RocketTracker-Win.zip'"

    echo.
    echo ZIP creado:
    echo %RELEASE%\RocketTracker-Win.zip
    echo.

    set /p respuesta2="¿Deseas borrar la carpeta no comprimida? (y/n): "
    if /i "!respuesta2!"=="y" (
        echo Borrando carpeta...
        rd /s /q "%RELEASE%\RocketTracker"
    ) else (
        echo Operación omitida.
    )
) else (
    echo Operación omitida.
)

echo.
echo ========================================
echo         BUILD COMPLETADA
echo ========================================
echo.
echo Aplicación:
echo %RELEASE%\RocketTracker\
echo.

:: ============================
:: Limpieza
:: ============================

echo Limpiando...

if exist "%BUILD%" rd /s /q "%BUILD%"

echo Se han eliminado los archivos temporales.
pause