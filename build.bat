@echo off
setlocal EnableExtensions EnableDelayedExpansion

:: ============================
:: Rutas
:: ============================

set "PROJECT_DIR=%~dp0"
if "%PROJECT_DIR:~-1%"=="" set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"

set "JAVAFX=%PROJECT_DIR%\lib\javafx-sdk-21.0.12"
set "BUILD=%PROJECT_DIR%\build"
set "RELEASE=%PROJECT_DIR%\release"

echo ========================================
echo         Rocket Tracker - BUILD (Windows)
echo ========================================
echo.

:: ============================
:: Comprobaciones
:: ============================

if not exist "%PROJECT_DIR%\src" (
echo ERROR: No se encontro la carpeta src.
exit /b 1
)

if not exist "%JAVAFX%\lib" (
echo ERROR: No se encontro JavaFX.
echo Ruta esperada: %JAVAFX%\lib
exit /b 1
)

where javac >nul 2>&1
if errorlevel 1 (
echo ERROR: javac no se encuentra en el PATH.
exit /b 1
)

where jar >nul 2>&1
if errorlevel 1 (
echo ERROR: jar no se encuentra en el PATH.
exit /b 1
)

where jpackage >nul 2>&1
if errorlevel 1 (
echo ERROR: jpackage no se encuentra en el PATH.
exit /b 1
)

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

dir /s /b "%PROJECT_DIR%\src*.java" > "%BUILD%\sources.txt"

for %%A in ("%BUILD%\sources.txt") do (
if %%~zA EQU 0 (
echo ERROR: No se encontraron archivos Java en src.
exit /b 1
)
)

javac ^
--module-path "%JAVAFX%\lib" ^
--add-modules javafx.controls,javafx.fxml,javafx.graphics,java.net.http ^
-encoding UTF-8 ^
-d "%BUILD%\classes" ^
@"%BUILD%\sources.txt"

if errorlevel 1 (
echo.
echo ERROR: Error en la compilacion de Java.
exit /b 1
)

:: ============================
:: Recursos
:: ============================

echo [3/5] Copiando recursos...

if exist "%PROJECT_DIR%\res" (
xcopy "%PROJECT_DIR%\res*" "%BUILD%\classes" /E /I /Y >nul

```
if errorlevel 1 (
    echo ERROR: No se pudieron copiar los recursos.
    exit /b 1
)
```

) else (
echo ADVERTENCIA: No existe la carpeta res.
)

:: ============================
:: JAR
:: ============================

echo [4/5] Creando JAR...

(
echo Main-Class: app.Main
) > "%BUILD%\MANIFEST.MF"

jar ^
--create ^
--file "%BUILD%\input\RocketTracker.jar" ^
--manifest "%BUILD%\MANIFEST.MF" ^
-C "%BUILD%\classes" .

if errorlevel 1 (
echo ERROR: No se pudo crear el JAR.
exit /b 1
)

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

if errorlevel 1 (
echo ERROR: jpackage ha fallado.
exit /b 1
)

:: ============================
:: Preparar directorios
:: ============================

if not exist "%RELEASE%\RocketTracker\data\matches" (
mkdir "%RELEASE%\RocketTracker\data\matches"
)

:: ============================
:: Crear acceso directo
:: ============================

echo Creando acceso directo...

powershell -NoProfile -Command ^
"$ws = New-Object -ComObject WScript.Shell; ^
$s = $ws.CreateShortcut('%RELEASE%\RocketTracker\RocketTracker.lnk'); ^
$s.TargetPath = '%RELEASE%\RocketTracker\bin\RocketTracker.exe'; ^
$s.WorkingDirectory = '%RELEASE%\RocketTracker'; ^
$s.Save()"

if errorlevel 1 (
echo ADVERTENCIA: No se pudo crear el acceso directo.
)

:: ============================
:: ZIP
:: ============================

echo.
set /p respuesta="¿Deseas comprimir la aplicación? (y/n): "

if /i "!respuesta!"=="y" (
echo.
echo Comprimiendo paquete...

```
if exist "%RELEASE%\RocketTracker-Win.zip" (
    del /f /q "%RELEASE%\RocketTracker-Win.zip"
)

powershell -NoProfile -Command ^
    "Compress-Archive -Path '%RELEASE%\RocketTracker' -DestinationPath '%RELEASE%\RocketTracker-Win.zip' -Force"

if errorlevel 1 (
    echo ERROR: No se pudo crear el ZIP.
    exit /b 1
)

echo.
echo ZIP creado:
echo %RELEASE%\RocketTracker-Win.zip
echo.

set /p respuesta2="¿Deseas borrar la carpeta no comprimida? (y/n): "

if /i "!respuesta2!"=="y" (
    echo Borrando carpeta...
    rd /s /q "%RELEASE%\RocketTracker"
) else (
    echo Carpeta conservada.
)
```

) else (
echo Compresion omitida.
)

:: ============================
:: Limpieza
:: ============================

echo.
echo Limpiando archivos temporales...

if exist "%BUILD%" rd /s /q "%BUILD%"

echo.
echo ========================================
echo         BUILD COMPLETADA
echo ========================================
echo.
echo Aplicacion:
echo %RELEASE%\RocketTracker
echo.
echo Se han eliminado los archivos temporales.
echo.

pause
