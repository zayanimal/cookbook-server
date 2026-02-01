#!/bin/bash

# Определяем путь к директории данных
# Если запускается на сервере, используем путь /home/zayanimal/projects/cookbook-server/data
# Иначе используем локальный путь
if [ -d "/home/zayanimal/projects/cookbook-server/data" ]; then
  DATA_DIR="/home/zayanimal/projects/cookbook-server/data"
else
  DATA_DIR="/Users/zayanimal/Desktop/projects/cookbook-server/data"
fi

# Останавливаем и удаляем существующий контейнер, если он запущен
echo "Остановка существующего контейнера (если запущен)..."
docker stop cookbook-mongodb 2>/dev/null || true
docker rm cookbook-mongodb 2>/dev/null || true

docker run -d \
  --name cookbook-mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_DATABASE=cookbook \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin123 \
  -v "$DATA_DIR:/data/db" \
  --rm \
  mongo:latest
