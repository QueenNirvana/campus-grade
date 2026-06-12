#!/usr/bin/env bash
set -euo pipefail

ROOT_USER="${MYSQL_ROOT_USER:-root}"
DB_NAME="campus_grade_management"

if [ ! -f "database/schema.sql" ] || [ ! -f "database/seed.sql" ]; then
  echo "Run this script from the project root so database/schema.sql and database/seed.sql can be found." >&2
  exit 1
fi

echo "This will recreate database: $DB_NAME"
read -r -p "Continue? Type YES: " confirm
if [ "$confirm" != "YES" ]; then
  echo "Canceled."
  exit 0
fi

mysql -u "$ROOT_USER" -p < database/schema.sql
mysql -u "$ROOT_USER" -p "$DB_NAME" < database/seed.sql

echo "Database imported: $DB_NAME"
