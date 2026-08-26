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

echo "[1/6] Limpiando..."

rm -rf "$BUILD"
rm -rf "$RELEASE"

mkdir -p "$BUILD/classes"
mkdir -p "$BUILD/input"
mkdir -p "$RELEASE"

# ============================
# Compilar
# ============================

echo "[2/6] Compilando Java..."

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

echo "[3/6] Copiando recursos..."

if [ -d "$PROJECT_DIR/res" ]; then
    cp -r "$PROJECT_DIR/res/"* "$BUILD/classes/"
fi

# ============================
# JAR
# ============================

echo "[4/6] Creando JAR..."

printf 'Main-Class: app.Main\n' > "$BUILD/MANIFEST.MF"

jar \
    --create \
    --file "$BUILD/input/RocketTracker.jar" \
    --manifest "$BUILD/MANIFEST.MF" \
    -C "$BUILD/classes" .

# ============================
# jpackage
# ============================

echo "[5/6] Ejecutando jpackage..."

jpackage \
    --type app-image \
    --name RocketTracker \
    --input "$BUILD/input" \
    --main-jar RocketTracker.jar \
    --main-class app.Main \
    --module-path "$JAVAFX/lib" \
    --add-modules javafx.controls \
    --dest "$RELEASE"

# ============================
# Data
# ============================

echo "[6/6] Copiando data..."

mkdir -p "$RELEASE/RocketTracker/data/matches"

if [ -f "$PROJECT_DIR/data/config.json" ]; then
    cp "$PROJECT_DIR/data/config.json" \
       "$RELEASE/RocketTracker/data/config.json"
fi

echo
echo "========================================"
echo "        BUILD COMPLETADA"
echo "========================================"
echo
echo "Aplicación:"
echo "$RELEASE/RocketTracker/"
echo

# ============================
# ZIP
# ============================

echo "Comprimiendo..."

cd "$RELEASE"

rm -f "RocketTracker-Linux.zip"

zip -r \
    "RocketTracker-Linux.zip" \
    "RocketTracker" \
    > /dev/null

echo
echo "ZIP creado:"
echo "$RELEASE/RocketTracker-Linux.zip"
echo

# ============================
# Limpieza
# ============================
echo "Limpiando... "

rm -rf "$RELEASE/RocketTracker"
rm -rf "$BUILD"

echo "Se han eliminado los archivos temporales"