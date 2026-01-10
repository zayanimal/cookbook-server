#!/bin/bash

# Определяем IP адрес хоста для доступа из контейнера
# На macOS/Windows используем host.docker.internal, на Linux - IP шлюза по умолчанию
if [[ "$OSTYPE" == "darwin"* ]] || [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
    HOST_IP="host.docker.internal"
    ADD_HOST_FLAG="--add-host=host.docker.internal:host-gateway"
else
    # Для Linux определяем IP шлюза по умолчанию
    HOST_IP=$(ip route | grep default | awk '{print $3}' | head -1)
    if [ -z "$HOST_IP" ]; then
        HOST_IP="172.17.0.1"  # Fallback на стандартный IP Docker bridge
    fi
    ADD_HOST_FLAG=""
fi

# Создаем директорию для конфигурации nginx, если её нет
mkdir -p nginx-config

# Создаем конфигурационный файл nginx с динамическим IP хоста
cat > nginx-config/nginx.conf << EOF
events {
    worker_connections 1024;
}

http {
    upstream backend {
        server ${HOST_IP}:8080;
    }

    # Map для определения CORS origin (возвращаем origin из запроса или 'http://localhost' если нет)
    # Используем конкретный origin вместо '*' для совместимости с credentials
    map \$http_origin \$cors_origin {
        default \$http_origin;
        '' 'http://localhost';
    }

    server {
        listen 9000;
        server_name localhost;

        # Обработка preflight запросов (OPTIONS)
        location / {
            # Обработка OPTIONS запросов
            if (\$request_method = 'OPTIONS') {
                add_header 'Access-Control-Allow-Origin' \$cors_origin always;
                add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, PATCH, OPTIONS' always;
                add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization,Accept,Origin' always;
                add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range' always;
                add_header 'Access-Control-Allow-Credentials' 'true' always;
                add_header 'Access-Control-Max-Age' 1728000 always;
                add_header 'Vary' 'Origin' always;
                add_header 'Content-Type' 'text/plain; charset=utf-8' always;
                add_header 'Content-Length' 0 always;
                return 204;
            }

            # CORS заголовки для всех остальных запросов
            add_header 'Access-Control-Allow-Origin' \$cors_origin always;
            add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, PATCH, OPTIONS' always;
            add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization,Accept,Origin' always;
            add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range' always;
            add_header 'Access-Control-Allow-Credentials' 'true' always;
            add_header 'Vary' 'Origin' always;

            proxy_pass http://backend;
            proxy_http_version 1.1;
            proxy_set_header Upgrade \$http_upgrade;
            proxy_set_header Connection 'upgrade';
            proxy_set_header Host \$host;
            proxy_set_header X-Real-IP \$remote_addr;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto \$scheme;
            proxy_cache_bypass \$http_upgrade;
        }
    }
}
EOF

# Проверяем, запущен ли уже контейнер
CONTAINER_NAME="cookbook-nginx-proxy"
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Контейнер $CONTAINER_NAME уже запущен. Останавливаем..."
    docker stop $CONTAINER_NAME
    docker rm $CONTAINER_NAME
fi

# Запускаем контейнер nginx
echo "Запуск Nginx прокси-сервера на порту 9000..."
echo "Бэкенд будет доступен через ${HOST_IP}:8080"

# Собираем команду запуска Docker
DOCKER_ARGS=(
    -d
    --name "$CONTAINER_NAME"
    -p "9000:9000"
    -v "$(pwd)/nginx-config/nginx.conf:/etc/nginx/nginx.conf:ro"
    --restart unless-stopped
)

# Добавляем флаг --add-host для macOS/Windows
if [ -n "$ADD_HOST_FLAG" ]; then
    DOCKER_ARGS+=(--add-host "host.docker.internal:host-gateway")
fi

DOCKER_ARGS+=(nginx:alpine)

docker run "${DOCKER_ARGS[@]}"

if [ $? -eq 0 ]; then
    echo "Nginx прокси-сервер успешно запущен!"
    echo "Прокси работает на http://localhost:9000"
    echo "Запросы перенаправляются на http://localhost:8080"
    echo "CORS заголовки настроены для всех источников"
else
    echo "Ошибка при запуске контейнера"
    exit 1
fi
