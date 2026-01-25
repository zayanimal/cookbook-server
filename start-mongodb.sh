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
  -e MONGODB_ROOT_USER=admin \
  -e MONGODB_ROOT_PASSWORD=admin123 \
  -e MONGODB_DATABASE=cookbook \
  -e MONGODB_USERNAME=admin \
  -e MONGODB_PASSWORD=admin123 \
  -v "$DATA_DIR:/bitnami/mongodb" \
  --rm \
  bitnami/mongodb:latest
