#!/usr/bin/env bash
set -euo pipefail

APP_DIR="/opt/campus-grade-management"
JAR="$APP_DIR/backend/campus-grade-management-0.0.1-SNAPSHOT.jar"

if [ ! -f "$APP_DIR/.env" ]; then
  echo "Missing $APP_DIR/.env. Copy deploy/env.example and fill DB credentials first." >&2
  exit 1
fi

set -a
source "$APP_DIR/.env"
set +a

exec java -jar "$JAR"
