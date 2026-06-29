#!/usr/bin/env bash
set -euo pipefail

REPO_URL="${REPO_URL:-https://github.com/QueenNirvana/campus-grade.git}"
APP_DIR="${APP_DIR:-/opt/campus-grade-management}"
DB_NAME="${DB_NAME:-campus_grade_management}"
DB_USER="${DB_USER:-campus_grade}"
DB_PASSWORD="${DB_PASSWORD:-CampusGrade@2026!ChangeMe}"
APP_PORT="${APP_PORT:-8080}"
WEB_ROOT="${WEB_ROOT:-/var/www/campus-grade-management}"
SERVER_NAME="${SERVER_NAME:-_}"

if [ "$(id -u)" -ne 0 ]; then
  echo "Please run as root: sudo bash fresh-server-deploy.sh" >&2
  exit 1
fi

echo "==> Installing system dependencies"
apt-get update
apt-get install -y ca-certificates curl git nginx maven

if ! command -v java >/dev/null 2>&1 || ! java -version 2>&1 | awk -F[\".] '/version/ { exit ($2 >= 17 ? 0 : 1) }'; then
  echo "==> Installing JDK 17+"
  apt-get install -y openjdk-21-jdk || apt-get install -y default-jdk || apt-get install -y openjdk-17-jdk
fi

if ! command -v mysql >/dev/null 2>&1; then
  echo "==> Installing MySQL server package"
  if ! apt-get install -y mysql-server; then
    apt-get install -y default-mysql-server
  fi
fi

if ! command -v node >/dev/null 2>&1 || ! node -e "process.exit(Number(process.versions.node.split('.')[0]) >= 18 ? 0 : 1)" >/dev/null 2>&1; then
  echo "==> Installing Node.js 20"
  curl -fsSL https://deb.nodesource.com/setup_20.x | bash -
  apt-get install -y nodejs
fi

echo "==> Enabling services"
systemctl enable --now mysql || systemctl enable --now mariadb
systemctl enable --now nginx

echo "==> Fetching project code"
if [ -d "$APP_DIR/.git" ]; then
  git -C "$APP_DIR" fetch --all --prune
  git -C "$APP_DIR" reset --hard origin/master
else
  rm -rf "$APP_DIR"
  git clone "$REPO_URL" "$APP_DIR"
fi

echo "==> Initializing database"
mysql < "$APP_DIR/database/schema.sql"
mysql <<SQL
CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASSWORD}';
ALTER USER '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASSWORD}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}'@'localhost';
FLUSH PRIVILEGES;
SQL
mysql "$DB_NAME" < "$APP_DIR/database/seed.sql"

echo "==> Writing backend environment"
cat > "$APP_DIR/.env" <<EOF
DB_URL=jdbc:mysql://localhost:3306/${DB_NAME}?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
DB_USERNAME=${DB_USER}
DB_PASSWORD=${DB_PASSWORD}
EOF
chmod 600 "$APP_DIR/.env"

echo "==> Building backend"
cd "$APP_DIR/backend"
mvn -DskipTests clean package
mkdir -p "$APP_DIR/backend"
cp target/campus-grade-management-0.0.1-SNAPSHOT.jar "$APP_DIR/backend/campus-grade-management-0.0.1-SNAPSHOT.jar"

echo "==> Installing backend systemd service"
cat > /etc/systemd/system/campus-grade-management.service <<EOF
[Unit]
Description=Campus Grade Management Spring Boot Backend
After=network.target mysql.service mariadb.service

[Service]
Type=simple
WorkingDirectory=${APP_DIR}/backend
EnvironmentFile=${APP_DIR}/.env
ExecStart=/usr/bin/java -jar ${APP_DIR}/backend/campus-grade-management-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF
systemctl daemon-reload
systemctl enable --now campus-grade-management

echo "==> Building frontend"
cd "$APP_DIR/frontend"
npm ci
npm run build
rm -rf "$WEB_ROOT"
mkdir -p "$WEB_ROOT"
cp -a dist/. "$WEB_ROOT/"

echo "==> Configuring Nginx"
cat > /etc/nginx/sites-available/campus-grade-management <<EOF
server {
    listen 80;
    server_name ${SERVER_NAME};

    root ${WEB_ROOT};
    index index.html;

    location / {
        try_files \$uri \$uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:${APP_PORT}/api/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF
ln -sf /etc/nginx/sites-available/campus-grade-management /etc/nginx/sites-enabled/campus-grade-management
rm -f /etc/nginx/sites-enabled/default
nginx -t
systemctl reload nginx

echo "==> Checking services"
systemctl --no-pager --full status campus-grade-management || true
curl -fsS "http://127.0.0.1:${APP_PORT}/api/auth/login" >/dev/null || true

echo
echo "Deployment finished."
echo "Open: http://$(curl -fsS ifconfig.me || hostname -I | awk '{print $1}')/"
echo "Default accounts:"
echo "  admin / admin123"
echo "  teacher01 / 123456"
echo "  student01 / 123456"
