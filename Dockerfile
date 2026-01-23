# Этап сборки
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Устанавливаем Maven (если не установлен)
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Копируем файлы проекта
COPY pom.xml .

# Загружаем зависимости (кэшируем для ускорения сборки)
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests -B

# Этап запуска
FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

# Копируем собранный JAR файл
COPY --from=build /app/target/cookbook-server-*.jar app.jar

# Открываем порт (обычно Spring Boot использует 8080)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
