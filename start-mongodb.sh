#!/bin/bash

docker run -d \
  --name cookbook-mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin123 \
  -e MONGO_INITDB_DATABASE=cookbook \
  -v /Users/zayanimal/Desktop/projects/cookbook-server/data:/bitnami/mongodb \
  --rm \
  bitnami/mongodb:latest

echo "MongoDB контейнер запущен на порту 27017"