# ---------- Stage 1: build ----------
FROM gradle:8.10-jdk21 AS build
WORKDIR /workspace

# Отключаем :app для Docker-сборки
ENV SKIP_APP=true

# Копируем файлы конфигурации
COPY settings.gradle.kts ./
COPY gradle ./gradle
COPY backend/build.gradle.kts ./backend/

# Скачиваем зависимости (используем системный gradle вместо ./gradlew)
RUN gradle :backend:dependencies --no-daemon || true

# Копируем исходники и собираем
COPY backend/src ./backend/src
RUN gradle :backend:bootJar --no-daemon -x test

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/backend/build/libs/*.jar app.jar

RUN mkdir -p /app/uploads/items

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]