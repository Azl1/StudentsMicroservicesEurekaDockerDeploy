# Этап 1: Сборка приложения и загрузка зависимостей Maven
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код и собираем артефакт
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Извлечение слоев Spring Boot (Layertools) для кэширования [ТЗ 5.1]
FROM eclipse-temurin:21-jre-jammy AS extractor
WORKDIR /layers
COPY --from=builder /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Этап 3: Финальный легковесный образ для запуска
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Создаем системного пользователя для безопасности (чтобы не запускать от root)
RUN useradd -ms /bin/bash springuser && chown -R springuser:springuser /app
USER springuser

# Копируем слои по отдельности. Зависимости меняются редко и будут закэшированы в Docker навечно
COPY --from=extractor /layers/dependencies/ ./
COPY --from=extractor /layers/spring-boot-loader/ ./
COPY --from=extractor /layers/snapshot-dependencies/ ./
COPY --from=extractor /layers/application/ ./

# Открываем стандартный порт Spring Boot
EXPOSE 8761

# Используем официальный JarLauncher для работы со слоями вместо классического -jar
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
