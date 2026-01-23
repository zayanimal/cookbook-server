#!/bin/bash

# Цвета для вывода
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Имя образа и контейнера
IMAGE_NAME="cookbook-server"
CONTAINER_NAME="cookbook-server-container"
PORT=8080

echo -e "${YELLOW}Сборка Docker образа...${NC}"

# Собираем Docker образ
docker build -t $IMAGE_NAME:latest .

if [ $? -ne 0 ]; then
    echo -e "${RED}Ошибка при сборке образа!${NC}"
    exit 1
fi

echo -e "${GREEN}Образ успешно собран!${NC}"

# Останавливаем и удаляем существующий контейнер, если он есть
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo -e "${YELLOW}Остановка существующего контейнера...${NC}"
    docker stop $CONTAINER_NAME > /dev/null 2>&1
    docker rm $CONTAINER_NAME > /dev/null 2>&1
fi

echo -e "${YELLOW}Запуск контейнера в фоновом режиме...${NC}"

# Запускаем контейнер в фоновом режиме
docker run -d \
    --name $CONTAINER_NAME \
    -p $PORT:8080 \
    --restart unless-stopped \
    $IMAGE_NAME:latest

if [ $? -ne 0 ]; then
    echo -e "${RED}Ошибка при запуске контейнера!${NC}"
    exit 1
fi

echo -e "${GREEN}Контейнер успешно запущен!${NC}"
echo -e "${GREEN}Имя контейнера: ${CONTAINER_NAME}${NC}"
echo -e "${GREEN}Приложение доступно по адресу: http://localhost:${PORT}${NC}"
echo ""
echo "Полезные команды:"
echo "  Просмотр логов: docker logs -f $CONTAINER_NAME"
echo "  Остановка: docker stop $CONTAINER_NAME"
echo "  Запуск: docker start $CONTAINER_NAME"
echo "  Удаление: docker rm -f $CONTAINER_NAME"
