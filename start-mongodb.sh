#!/bin/bash

# Определяем путь к директории данных
# Если запускается на сервере, используем путь /home/zayanimal/projects/cookbook-server/data
# Иначе используем локальный путь
if [ -d "/home/zayanimal/projects/cookbook-server/data" ]; then
  DATA_DIR="/home/zayanimal/projects/cookbook-server/data"
else
  DATA_DIR="/Users/zayanimal/Desktop/projects/cookbook-server/data"
fi

echo "Используется директория данных: $DATA_DIR"

# Создаем директорию, если она не существует
mkdir -p "$DATA_DIR"

# Устанавливаем правильные права доступа для bitnami/mongodb (UID 1001)
# Проверяем, запущен ли скрипт от root или с sudo
if [ "$EUID" -eq 0 ]; then
  echo "Установка прав доступа для директории данных..."
  # Устанавливаем права рекурсивно для всех файлов и поддиректорий
  chown -R 1001:1001 "$DATA_DIR" 2>/dev/null || {
    echo "Предупреждение: Не удалось установить владельца. Продолжаем..."
  }
  # Устанавливаем права: 755 для директорий, 644 для файлов
  find "$DATA_DIR" -type d -exec chmod 755 {} \; 2>/dev/null || true
  find "$DATA_DIR" -type f -exec chmod 644 {} \; 2>/dev/null || true
  echo "Права доступа установлены: $(ls -ld "$DATA_DIR")"
else
  echo "Внимание: Скрипт запущен не от root."
  echo "Текущие права доступа: $(ls -ld "$DATA_DIR")"
  
  # Проверяем владельца директории
  DIR_OWNER=$(stat -c '%U:%G' "$DATA_DIR" 2>/dev/null || stat -f '%Su:%Sg' "$DATA_DIR" 2>/dev/null)
  echo "Владелец директории: $DIR_OWNER"
  
  # Если директория не пустая и содержит файлы с неправильными правами, это может быть проблемой
  if [ "$(ls -A "$DATA_DIR" 2>/dev/null)" ]; then
    echo "Директория не пустая. Проверьте права доступа файлов внутри."
    echo ""
    echo "ВАЖНО: Для корректной работы MongoDB необходимо установить права доступа:"
    echo "  sudo chown -R 1001:1001 $DATA_DIR"
    echo "  sudo find $DATA_DIR -type d -exec chmod 755 {} \\;"
    echo "  sudo find $DATA_DIR -type f -exec chmod 644 {} \\;"
    echo ""
    echo "Или очистите директорию и запустите скрипт с sudo:"
    echo "  sudo rm -rf $DATA_DIR/*"
    echo "  sudo ./start-mongodb.sh"
  else
    echo "Директория пустая. Установите права доступа перед запуском:"
    echo "  sudo chown -R 1001:1001 $DATA_DIR"
  fi
fi

# Останавливаем и удаляем существующий контейнер, если он запущен
echo "Остановка существующего контейнера (если запущен)..."
docker stop cookbook-mongodb 2>/dev/null || true
docker rm cookbook-mongodb 2>/dev/null || true

# Запускаем контейнер MongoDB
# НЕ используем --user, так как bitnami/mongodb сам управляет пользователем
# Используем правильные переменные окружения для bitnami
echo "Запуск контейнера MongoDB..."
CONTAINER_ID=$(docker run -d \
  --name cookbook-mongodb \
  -p 27017:27017 \
  -e MONGODB_ROOT_USER=admin \
  -e MONGODB_ROOT_PASSWORD=admin123 \
  -e MONGODB_DATABASE=cookbook \
  -e MONGODB_USERNAME=admin \
  -e MONGODB_PASSWORD=admin123 \
  -v "$DATA_DIR:/bitnami/mongodb" \
  --rm \
  bitnami/mongodb:latest)

if [ $? -eq 0 ]; then
  echo "Контейнер запущен с ID: $CONTAINER_ID"
  echo "Ожидание инициализации MongoDB (это может занять некоторое время)..."
  echo "Для просмотра логов выполните: docker logs -f cookbook-mongodb"
  
  # Ждем несколько секунд и проверяем статус
  sleep 5
  if docker ps | grep -q cookbook-mongodb; then
    echo "Контейнер работает. Проверка логов..."
    docker logs --tail 20 cookbook-mongodb
  else
    echo "ОШИБКА: Контейнер не запущен. Проверьте логи:"
    docker logs cookbook-mongodb 2>&1 | tail -30
  fi
else
  echo "ОШИБКА: Не удалось запустить контейнер"
  exit 1
fi

echo ""
echo "MongoDB контейнер должен быть доступен на порту 27017"
echo "Директория данных: $DATA_DIR"