#!/bin/sh
set -eu

DIST_DIR=$(CDPATH= cd -- "$(dirname "$0")" && pwd)
cd "$DIST_DIR"

mkdir -p logs

if [ -n "${JAVA_HOME:-}" ]; then
    JAVA_EXE="$JAVA_HOME/bin/java"
else
    JAVA_EXE="$(command -v java 2>/dev/null || true)"
fi

if [ -z "${JAVA_EXE:-}" ]; then
    for candidate in \
        "/mnt/c/Program Files/Java"/jdk-*/bin/java.exe \
        "/c/Program Files/Java"/jdk-*/bin/java.exe
    do
        if [ -x "$candidate" ]; then
            JAVA_EXE="$candidate"
            break
        fi
    done
fi

if [ -z "${JAVA_EXE:-}" ]; then
    echo "java not found. Set JAVA_HOME or add java to PATH." >&2
    exit 1
fi

exec "$JAVA_EXE" -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar Lisbon-Server.jar "$@"
