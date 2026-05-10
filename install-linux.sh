#!/usr/bin/env bash
set -euo pipefail

APP_NAME="lisbon"
INSTALL_DIR="/opt/lisbon"
CONFIG_DIR="/etc/lisbon"
LOG_DIR="/var/log/lisbon"
DB_NAME="lisbon"
DB_USER="lisbon"
DB_PASSWORD=""
GAME_PORT="30000"
MUS_PORT="12322"
RCON_PORT="12309"
WEB_BACKEND_PORT="8080"
PUBLIC_HOST=""
SKIP_BUILD="false"
NON_INTERACTIVE="false"
JAVA17_HOME=""

usage() {
    cat <<USAGE
Usage: sudo ./install-linux.sh --host example.com [options]

Options:
  --host HOST                  Public hostname or IP used by the web/client config
  --install-dir DIR            Install directory (default: /opt/lisbon)
  --db-name NAME               MariaDB database name (default: lisbon)
  --db-user USER               MariaDB application user (default: lisbon)
  --db-password PASSWORD       MariaDB application password (generated if omitted)
  --game-port PORT             Emulator game port (default: 30000)
  --mus-port PORT              Emulator MUS port (default: 12322)
  --web-backend-port PORT      Lisbon-Web backend port behind Nginx (default: 8080)
  --skip-build                 Use existing ./dist instead of running ./gradlew packageDist
  --non-interactive            Fail instead of prompting for missing required values
  -h, --help                   Show this help
USAGE
}

die() {
    echo "ERROR: $*" >&2
    exit 1
}

log() {
    echo "==> $*"
}

require_root() {
    if [ "$(id -u)" -ne 0 ]; then
        die "run this installer as root, for example: sudo ./install-linux.sh --host example.com"
    fi
}

require_systemd() {
    command -v systemctl >/dev/null 2>&1 || die "systemctl was not found; this installer requires a systemd Linux environment"
}

require_identifier() {
    local name="$1"
    local value="$2"
    [[ "$value" =~ ^[A-Za-z0-9_]+$ ]] || die "$name must contain only letters, numbers, and underscores"
}

require_port() {
    local name="$1"
    local value="$2"
    [[ "$value" =~ ^[0-9]+$ ]] || die "$name must be a numeric TCP port"
    [ "$value" -ge 1 ] && [ "$value" -le 65535 ] || die "$name must be between 1 and 65535"
}

parse_args() {
    while [ "$#" -gt 0 ]; do
        case "$1" in
            --host) PUBLIC_HOST="${2:-}"; shift 2 ;;
            --install-dir) INSTALL_DIR="${2:-}"; shift 2 ;;
            --db-name) DB_NAME="${2:-}"; shift 2 ;;
            --db-user) DB_USER="${2:-}"; shift 2 ;;
            --db-password) DB_PASSWORD="${2:-}"; shift 2 ;;
            --game-port) GAME_PORT="${2:-}"; shift 2 ;;
            --mus-port) MUS_PORT="${2:-}"; shift 2 ;;
            --web-backend-port) WEB_BACKEND_PORT="${2:-}"; shift 2 ;;
            --skip-build) SKIP_BUILD="true"; shift ;;
            --non-interactive) NON_INTERACTIVE="true"; shift ;;
            -h|--help) usage; exit 0 ;;
            *) die "unknown argument: $1" ;;
        esac
    done
}

prompt_missing_values() {
    if [ -z "$PUBLIC_HOST" ]; then
        if [ "$NON_INTERACTIVE" = "true" ]; then
            die "--host is required when --non-interactive is used"
        fi

        read -r -p "Public host or IP for Lisbon: " PUBLIC_HOST
    fi

    [ -n "$PUBLIC_HOST" ] || die "--host is required"
}

normalize_host() {
    PUBLIC_HOST="${PUBLIC_HOST#http://}"
    PUBLIC_HOST="${PUBLIC_HOST#https://}"
    PUBLIC_HOST="${PUBLIC_HOST%%/*}"
    PUBLIC_HOST="${PUBLIC_HOST%%:*}"
    [ -n "$PUBLIC_HOST" ] || die "public host cannot be empty"
}

detect_package_manager() {
    if command -v apt-get >/dev/null 2>&1; then
        PKG_MANAGER="apt"
    elif command -v dnf >/dev/null 2>&1; then
        PKG_MANAGER="dnf"
    elif command -v yum >/dev/null 2>&1; then
        PKG_MANAGER="yum"
    elif command -v pacman >/dev/null 2>&1; then
        PKG_MANAGER="pacman"
    elif command -v zypper >/dev/null 2>&1; then
        PKG_MANAGER="zypper"
    else
        die "unsupported Linux package manager; expected apt, dnf, yum, pacman, or zypper"
    fi
}

install_packages() {
    log "Installing Java 17, MariaDB, Nginx, and support tools"

    case "$PKG_MANAGER" in
        apt)
            apt-get update
            DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-17-jdk-headless mariadb-server mariadb-client nginx openssl unzip ca-certificates
            ;;
        dnf)
            dnf install -y java-17-openjdk-devel mariadb-server mariadb nginx openssl unzip ca-certificates
            ;;
        yum)
            yum install -y java-17-openjdk-devel mariadb-server mariadb nginx openssl unzip ca-certificates
            ;;
        pacman)
            pacman -Sy --noconfirm jdk17-openjdk mariadb nginx openssl unzip ca-certificates
            ;;
        zypper)
            zypper --non-interactive install java-17-openjdk-devel mariadb mariadb-client nginx openssl unzip ca-certificates
            ;;
    esac
}

mariadb_service_name() {
    if systemctl list-unit-files mariadb.service >/dev/null 2>&1; then
        echo "mariadb"
    elif systemctl list-unit-files mysql.service >/dev/null 2>&1; then
        echo "mysql"
    else
        echo "mariadb"
    fi
}

initialize_mariadb_if_needed() {
    if [ "$PKG_MANAGER" = "pacman" ] && [ ! -d /var/lib/mysql/mysql ]; then
        log "Initializing MariaDB data directory"
        if command -v mariadb-install-db >/dev/null 2>&1; then
            mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
        elif command -v mysql_install_db >/dev/null 2>&1; then
            mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
        fi
    fi
}

start_core_services() {
    MARIADB_SERVICE="$(mariadb_service_name)"
    initialize_mariadb_if_needed
    systemctl enable --now "$MARIADB_SERVICE"
    systemctl enable --now nginx
}

detect_java17() {
    local candidates=()
    local java_path

    if java_path="$(command -v java 2>/dev/null)"; then
        candidates+=("$java_path")
    fi

    while IFS= read -r java_path; do
        candidates+=("$java_path")
    done < <(find /usr/lib/jvm /usr/lib64/jvm -type f -path '*/bin/java' 2>/dev/null | sort)

    for java_path in "${candidates[@]}"; do
        if "$java_path" -version 2>&1 | grep -q 'version "17\.'; then
            JAVA17_HOME="$(cd "$(dirname "$java_path")/.." && pwd)"
            return
        fi
    done

    die "Java 17 was not found after package installation"
}

build_distribution() {
    local repo_dir="$1"

    if [ "$SKIP_BUILD" = "true" ]; then
        log "Skipping Gradle build"
        [ -f "$repo_dir/dist/Lisbon-Server.jar" ] || die "--skip-build was used, but dist/Lisbon-Server.jar does not exist"
        [ -x "$repo_dir/dist/web/bin/Lisbon-Web" ] || die "--skip-build was used, but dist/web/bin/Lisbon-Web does not exist"
        return
    fi

    log "Building Lisbon distribution"
    [ -x "$repo_dir/gradlew" ] || chmod +x "$repo_dir/gradlew"
    (cd "$repo_dir" && JAVA_HOME="$JAVA17_HOME" PATH="$JAVA17_HOME/bin:$PATH" ./gradlew packageDist)
}

create_runtime_user() {
    if ! id "$APP_NAME" >/dev/null 2>&1; then
        local nologin_shell="/usr/sbin/nologin"
        [ -x "$nologin_shell" ] || nologin_shell="/sbin/nologin"
        useradd --system --home "$INSTALL_DIR" --shell "$nologin_shell" "$APP_NAME"
    fi
}

install_files() {
    local repo_dir="$1"
    local dist_dir="$repo_dir/dist"

    log "Installing application files"
    [ -d "$dist_dir" ] || die "distribution directory not found: $dist_dir"
    [ -f "$dist_dir/Lisbon-Server.jar" ] || die "missing packaged emulator jar: $dist_dir/Lisbon-Server.jar"
    [ -x "$dist_dir/web/bin/Lisbon-Web" ] || die "missing packaged web launcher: $dist_dir/web/bin/Lisbon-Web"
    [ -f "$dist_dir/tools/lisbon.sql" ] || die "missing database dump: $dist_dir/tools/lisbon.sql"

    mkdir -p "$INSTALL_DIR" "$CONFIG_DIR" "$LOG_DIR"
    find "$INSTALL_DIR" -mindepth 1 -maxdepth 1 -exec rm -rf {} +
    (cd "$dist_dir" && tar cf - .) | (cd "$INSTALL_DIR" && tar xpf -)

    chmod +x "$INSTALL_DIR/start.sh" 2>/dev/null || true
    chmod +x "$INSTALL_DIR/web/bin/Lisbon-Web" 2>/dev/null || true

    chown -R "$APP_NAME:$APP_NAME" "$INSTALL_DIR" "$LOG_DIR"
}

mysql_client() {
    if command -v mariadb >/dev/null 2>&1; then
        echo "mariadb"
    elif command -v mysql >/dev/null 2>&1; then
        echo "mysql"
    else
        die "MariaDB/MySQL client was not found after package installation"
    fi
}

mysql_sql_string() {
    printf "%s" "$1" | sed -e 's/\\/\\\\/g' -e "s/'/''/g"
}

configure_database() {
    local mysql
    mysql="$(mysql_client)"
    MYSQL_ROOT=("$mysql" -u root)

    log "Configuring MariaDB database and user"
    "${MYSQL_ROOT[@]}" -e "SELECT 1" >/dev/null 2>&1 || die "could not connect to MariaDB as root; configure local root socket access and rerun"

    local escaped_password
    escaped_password="$(mysql_sql_string "$DB_PASSWORD")"

    "${MYSQL_ROOT[@]}" <<SQL
CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS '$DB_USER'@'localhost' IDENTIFIED BY '$escaped_password';
CREATE USER IF NOT EXISTS '$DB_USER'@'127.0.0.1' IDENTIFIED BY '$escaped_password';
ALTER USER '$DB_USER'@'localhost' IDENTIFIED BY '$escaped_password';
ALTER USER '$DB_USER'@'127.0.0.1' IDENTIFIED BY '$escaped_password';
GRANT ALL PRIVILEGES ON \`$DB_NAME\`.* TO '$DB_USER'@'localhost';
GRANT ALL PRIVILEGES ON \`$DB_NAME\`.* TO '$DB_USER'@'127.0.0.1';
FLUSH PRIVILEGES;
SQL

    import_sql_file "$INSTALL_DIR/tools/lisbon.sql"
    import_sql_file "$INSTALL_DIR/tools/groups.sql"

    if [ -d "$INSTALL_DIR/tools/migrations" ]; then
        while IFS= read -r migration; do
            import_sql_file "$migration"
        done < <(find "$INSTALL_DIR/tools/migrations" -type f -name '*.sql' | sort)
    fi

    update_site_settings
}

import_sql_file() {
    local file="$1"
    [ -f "$file" ] || return 0
    log "Importing $(basename "$file")"
    "${MYSQL_ROOT[@]}" "$DB_NAME" < "$file"
}

upsert_setting_sql() {
    local key="$1"
    local value="$2"
    local escaped_value
    escaped_value="$(mysql_sql_string "$value")"
    printf "INSERT INTO settings (\`setting\`, \`value\`) VALUES ('%s', '%s') ON DUPLICATE KEY UPDATE \`value\` = VALUES(\`value\`);\n" "$key" "$escaped_value"
}

update_site_settings() {
    local site_url="http://$PUBLIC_HOST"
    local settings_sql
    settings_sql="$(mktemp)"

    {
        upsert_setting_sql "site.path" "$site_url"
        upsert_setting_sql "static.content.path" "$site_url"
        upsert_setting_sql "email.static.content.path" "$site_url"
        upsert_setting_sql "loader.game.ip" "$PUBLIC_HOST"
        upsert_setting_sql "loader.game.port" "$GAME_PORT"
        upsert_setting_sql "loader.mus.ip" "$PUBLIC_HOST"
        upsert_setting_sql "loader.mus.port" "$MUS_PORT"
        upsert_setting_sql "loader.dcr" "$site_url/dcr/r26_20080915_0408_7984_61ccb5f8b8797a3aba62c1fa2ca80169/habbo.dcr"
        upsert_setting_sql "loader.external.variables" "$site_url/gamedata/external_variables.txt?"
        upsert_setting_sql "loader.external.texts" "$site_url/gamedata/external_texts.txt?"
    } > "$settings_sql"

    "${MYSQL_ROOT[@]}" "$DB_NAME" < "$settings_sql"
    rm -f "$settings_sql"
}

write_configs() {
    log "Writing Lisbon configuration"

    cat > "$CONFIG_DIR/server.ini" <<EOF_SERVER
[Server]
server.bind=0.0.0.0
server.port=$GAME_PORT

[Rcon]
rcon.bind=127.0.0.1
rcon.port=$RCON_PORT

[Mus]
mus.bind=0.0.0.0
mus.port=$MUS_PORT

[Database]
mysql.hostname=127.0.0.1
mysql.port=3306
mysql.username=$DB_USER
mysql.password=$DB_PASSWORD
mysql.database=$DB_NAME

[Logging]
log.received.packets=false
log.sent.packets=false

[Console]
debug=false
EOF_SERVER

    cat > "$CONFIG_DIR/webserver-config.ini" <<EOF_WEB
[Site]
site.directory=$INSTALL_DIR/tools/www

[Global]
bind.ip=127.0.0.1
bind.port=$WEB_BACKEND_PORT

[Rcon]
rcon.ip=127.0.0.1
rcon.port=$RCON_PORT

[Database]
mysql.hostname=127.0.0.1
mysql.port=3306
mysql.username=$DB_USER
mysql.password=$DB_PASSWORD
mysql.database=$DB_NAME

[Template]
template.directory=$INSTALL_DIR/tools/www-tpl
template.name=default-en

page.encoding=utf-8
EOF_WEB

    cp "$INSTALL_DIR/config/log4j.properties" "$CONFIG_DIR/log4j.properties"
    cp "$INSTALL_DIR/config/log4j.web.properties" "$CONFIG_DIR/log4j.web.properties"

    chown -R root:"$APP_NAME" "$CONFIG_DIR"
    chmod 0750 "$CONFIG_DIR"
    chmod 0640 "$CONFIG_DIR"/*

    ln -sfn "$CONFIG_DIR/server.ini" "$INSTALL_DIR/config/server.ini"
    ln -sfn "$CONFIG_DIR/webserver-config.ini" "$INSTALL_DIR/config/webserver-config.ini"
    ln -sfn "$CONFIG_DIR/log4j.properties" "$INSTALL_DIR/config/log4j.properties"
    ln -sfn "$CONFIG_DIR/log4j.web.properties" "$INSTALL_DIR/config/log4j.web.properties"
    chown -h "$APP_NAME:$APP_NAME" "$INSTALL_DIR"/config/*.ini "$INSTALL_DIR"/config/*.properties
}

write_systemd_units() {
    log "Writing systemd service units"

    cat > /etc/systemd/system/lisbon-server.service <<EOF_SERVER_SERVICE
[Unit]
Description=Lisbon Emulator Server
After=network-online.target $MARIADB_SERVICE.service
Wants=network-online.target
Requires=$MARIADB_SERVICE.service

[Service]
Type=simple
User=$APP_NAME
Group=$APP_NAME
WorkingDirectory=$INSTALL_DIR
Environment=JAVA_HOME=$JAVA17_HOME
Environment=LISBON_SERVER_CONFIG=$CONFIG_DIR/server.ini
Environment=LOG_DIR=$LOG_DIR
ExecStart=$JAVA17_HOME/bin/java -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar $INSTALL_DIR/Lisbon-Server.jar
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF_SERVER_SERVICE

    cat > /etc/systemd/system/lisbon-web.service <<EOF_WEB_SERVICE
[Unit]
Description=Lisbon Web Server
After=network-online.target $MARIADB_SERVICE.service lisbon-server.service
Wants=network-online.target
Requires=$MARIADB_SERVICE.service

[Service]
Type=simple
User=$APP_NAME
Group=$APP_NAME
WorkingDirectory=$INSTALL_DIR
Environment=LISBON_WEB_CONFIG=$CONFIG_DIR/webserver-config.ini
Environment=JAVA_HOME=$JAVA17_HOME
Environment=LOG_DIR=$LOG_DIR
ExecStart=$INSTALL_DIR/web/bin/Lisbon-Web
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF_WEB_SERVICE

    systemctl daemon-reload
    systemctl enable lisbon-server.service lisbon-web.service
}

write_nginx_config() {
    log "Writing Nginx reverse proxy configuration"

    local nginx_conf="/etc/nginx/conf.d/lisbon.conf"

    if [ -d /etc/nginx/sites-available ]; then
        nginx_conf="/etc/nginx/sites-available/lisbon"
    fi

    cat > "$nginx_conf" <<EOF_NGINX
server {
    listen 80;
    server_name $PUBLIC_HOST;

    client_max_body_size 50m;

    location / {
        proxy_pass http://127.0.0.1:$WEB_BACKEND_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF_NGINX

    if [ -d /etc/nginx/sites-enabled ]; then
        ln -sfn "$nginx_conf" /etc/nginx/sites-enabled/lisbon
        rm -f /etc/nginx/sites-enabled/default
    fi

    nginx -t
    systemctl reload nginx
}

start_lisbon_services() {
    log "Starting Lisbon services"
    systemctl restart lisbon-server.service
    systemctl restart lisbon-web.service
}

print_summary() {
    cat <<SUMMARY

Lisbon installation complete.

Web:        http://$PUBLIC_HOST/
Install:    $INSTALL_DIR
Config:     $CONFIG_DIR
Logs:       $LOG_DIR
Database:   $DB_NAME
DB user:    $DB_USER
Game port:  $GAME_PORT
MUS port:   $MUS_PORT

Useful commands:
  systemctl status lisbon-server lisbon-web nginx $MARIADB_SERVICE
  journalctl -u lisbon-server -f
  journalctl -u lisbon-web -f
SUMMARY
}

main() {
    parse_args "$@"
    require_root
    require_systemd
    prompt_missing_values
    normalize_host
    require_identifier "--db-name" "$DB_NAME"
    require_identifier "--db-user" "$DB_USER"
    require_port "--game-port" "$GAME_PORT"
    require_port "--mus-port" "$MUS_PORT"
    require_port "--web-backend-port" "$WEB_BACKEND_PORT"

    if [ -z "$DB_PASSWORD" ]; then
        DB_PASSWORD="$(openssl rand -hex 24)"
    fi

    local repo_dir
    repo_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

    detect_package_manager
    install_packages
    detect_java17
    start_core_services
    build_distribution "$repo_dir"
    create_runtime_user
    install_files "$repo_dir"
    configure_database
    write_configs
    write_systemd_units
    write_nginx_config
    start_lisbon_services
    print_summary
}

main "$@"
