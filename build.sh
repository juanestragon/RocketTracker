#!/usr/bin/env bash

set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

JAVAFX="$PROJECT_DIR/lib/javafx-sdk-21.0.12"

BUILD="$PROJECT_DIR/build"
RELEASE="$PROJECT_DIR/release"

echo "========================================"
echo "        Rocket Tracker - BUILD"
echo "========================================"

# ============================
# Limpiar
# ============================

echo "[1/5] Limpiando..."

rm -rf "$BUILD"
rm -rf "$RELEASE"

mkdir -p "$BUILD/classes"
mkdir -p "$BUILD/input"
mkdir -p "$RELEASE"

# ============================
# Compilar
# ============================

echo "[2/5] Compilando Java..."

find "$PROJECT_DIR/src" \
    -name "*.java" \
    > "$BUILD/sources.txt"

javac \
    --module-path "$JAVAFX/lib" \
    --add-modules javafx.controls \
    -d "$BUILD/classes" \
    @"$BUILD/sources.txt"

# ============================
# Recursos
# ============================

echo "[3/5] Copiando recursos..."

if [ -d "$PROJECT_DIR/res" ]; then
    cp -r "$PROJECT_DIR/res/"* "$BUILD/classes/"
fi

# ============================
# JAR
# ============================

echo "[4/5] Creando JAR..."

printf 'Main-Class: app.Main\n' > "$BUILD/MANIFEST.MF"

jar \
    --create \
    --file "$BUILD/input/RocketTracker.jar" \
    --manifest "$BUILD/MANIFEST.MF" \
    -C "$BUILD/classes" .

# ============================
# jpackage
# ============================

echo "[5/5] Ejecutando jpackage..."

jpackage \
    --type app-image \
    --name RocketTracker \
    --input "$BUILD/input" \
    --main-jar RocketTracker.jar \
    --main-class app.Main \
    --module-path "$JAVAFX/lib" \
    --add-modules javafx.controls,javafx.fxml,javafx.graphics,java.net.http \
    --java-options "-Djava.library.path=$JAVAFX/lib" \
    --dest "$RELEASE"

cd "$RELEASE/RocketTracker"

mkdir "data"
mkdir "data/matches"

cat > RocketTracker <<'EOF'
#!/bin/sh
cd "$(dirname "$0")"
exec ./bin/RocketTracker
EOF

chmod +x RocketTracker

# ============================
# ZIP
# ============================


read -p "¿Deseas comprimir la aplicación? (y/n): " respuesta

case "$respuesta" in
    [Yy]* )
        echo "Comprimiendo paquete..."
        cd ".."
        rm -f "RocketTracker-Linux.zip"
        zip -r \
            "RocketTracker-Linux.zip" \
            "RocketTracker" \
            > /dev/null
        echo
        echo "ZIP creado:"
        echo "$RELEASE/RocketTracker-Linux.zip"
        echo
        read -p "¿Deseas borrar la aplicación no comprimida? (y/n): " respuesta2
        case "$respuesta2" in
            [Yy]* )
                echo "Borrando carpeta..."
                rm -rf "$RELEASE/RocketTracker"
                ;;
            [Nn]* )
                echo "Operación omitida."
                # Pon aquí los comandos si responde No
                ;;
            * )
                echo "Respuesta no válida. Cancelando."
                ;;
        esac
        ;;
    [Nn]* )
        echo "Operación omitida."
        # Pon aquí los comandos si responde No
        ;;
    * )
        echo "Respuesta no válida. Cancelando."
        ;;
esac



echo
echo "========================================"
echo "        BUILD COMPLETADA"
echo "========================================"
echo
echo "Aplicación:"
echo "$RELEASE/RocketTracker/"
echo

## ============================
## Limpieza
## ============================

echo "Limpiando... "

rm -rf "$BUILD"

echo "Se han eliminado los archivos temporales"