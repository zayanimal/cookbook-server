#!/bin/bash

set -e

# ---------------------------------------------------------------------------
# Конфигурация (можно переопределить через переменные окружения или .env)
# ---------------------------------------------------------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Загружаем .env если он есть
if [ -f "$SCRIPT_DIR/.env" ]; then
  export $(grep -v '^#' "$SCRIPT_DIR/.env" | xargs)
fi

MONGO_CONTAINER="${MONGO_CONTAINER:-cookbook-mongodb}"
MONGO_HOST="${MONGODB_HOST:-localhost}"
MONGO_PORT="${MONGODB_PORT:-27017}"
MONGO_USER="${MONGODB_USERNAME:-admin}"
MONGO_PASS="${MONGODB_PASSWORD:-admin123}"
MONGO_DB="${MONGODB_DB_NAME:-cookbook}"

BACKUP_DIR="${SCRIPT_DIR}/backups"
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_NAME="backup_${MONGO_DB}_${TIMESTAMP}"
BACKUP_PATH="${BACKUP_DIR}/${BACKUP_NAME}"

# ---------------------------------------------------------------------------
# Создаём директорию для бэкапов
# ---------------------------------------------------------------------------
mkdir -p "$BACKUP_DIR"

echo "=== MongoDB Backup ==="
echo "База данных : $MONGO_DB"
echo "Контейнер   : $MONGO_CONTAINER"
echo "Директория  : $BACKUP_DIR"
echo "Имя бэкапа  : $BACKUP_NAME"
echo ""

# ---------------------------------------------------------------------------
# Проверяем, запущен ли контейнер
# ---------------------------------------------------------------------------
if docker ps --format '{{.Names}}' | grep -q "^${MONGO_CONTAINER}$"; then
  echo "Запуск mongodump внутри контейнера..."
  docker exec "$MONGO_CONTAINER" mongodump \
    --username "$MONGO_USER" \
    --password "$MONGO_PASS" \
    --authenticationDatabase admin \
    --db "$MONGO_DB" \
    --out "/tmp/${BACKUP_NAME}"

  echo "Копирование дампа на хост..."
  docker cp "${MONGO_CONTAINER}:/tmp/${BACKUP_NAME}" "$BACKUP_PATH"

  # Удаляем временный дамп из контейнера
  docker exec "$MONGO_CONTAINER" rm -rf "/tmp/${BACKUP_NAME}"
else
  echo "Контейнер $MONGO_CONTAINER не запущен. Пробуем подключиться напрямую..."
  if ! command -v mongodump &>/dev/null; then
    echo "Ошибка: mongodump не найден. Установите mongodb-database-tools или запустите контейнер."
    exit 1
  fi
  mongodump \
    --host "$MONGO_HOST:$MONGO_PORT" \
    --username "$MONGO_USER" \
    --password "$MONGO_PASS" \
    --authenticationDatabase admin \
    --db "$MONGO_DB" \
    --out "$BACKUP_PATH"
fi

# ---------------------------------------------------------------------------
# Архивируем и удаляем исходную папку
# ---------------------------------------------------------------------------
echo "Архивирование..."
tar -czf "${BACKUP_PATH}.tar.gz" -C "$BACKUP_DIR" "$BACKUP_NAME"
rm -rf "$BACKUP_PATH"

echo ""
echo "Готово: ${BACKUP_PATH}.tar.gz"
echo "Размер: $(du -sh "${BACKUP_PATH}.tar.gz" | cut -f1)"

# ---------------------------------------------------------------------------
# Ротация: оставляем только 10 последних бэкапов
# ---------------------------------------------------------------------------
KEEP=10
BACKUP_COUNT=$(ls -1 "${BACKUP_DIR}"/backup_*.tar.gz 2>/dev/null | wc -l)
if [ "$BACKUP_COUNT" -gt "$KEEP" ]; then
  echo ""
  echo "Ротация: удаляем старые бэкапы (оставляем $KEEP)..."
  ls -1t "${BACKUP_DIR}"/backup_*.tar.gz | tail -n +"$((KEEP + 1))" | xargs rm -f
fi
