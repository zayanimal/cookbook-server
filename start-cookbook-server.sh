#!/bin/bash

./mvnw clean package -DskipTests -B

# Имя образа и контейнера
IMAGE_NAME="cookbook-server"
CONTAINER_NAME="cookbook-server"

# Собираем Docker образ
docker build -t $IMAGE_NAME:latest .

# Останавливаем и удаляем существующий контейнер, если он есть
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo -e "${YELLOW}Остановка существующего контейнера...${NC}"
    docker stop $CONTAINER_NAME > /dev/null 2>&1
    docker rm $CONTAINER_NAME > /dev/null 2>&1
fi

# Запускаем контейнер в фоновом режиме
docker run \
    --name $CONTAINER_NAME \
    -p 8080:8080 \
    -e MONGODB_USERNAME="$MONGODB_USERNAME" \
    -e MONGODB_PASSWORD="$MONGODB_PASSWORD" \
    -e MONGODB_DB_NAME="$MONGODB_DB_NAME" \
    -e JWT_SECRET="$JWT_SECRET" \
    --rm \
    $IMAGE_NAME:latest
