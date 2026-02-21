# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /project
COPY pom.xml .
COPY lombok.config .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
RUN groupadd -r javauser && \
    useradd -r -g javauser -d /app -s /sbin/nologin javauser && \
    mkdir /app && \
    chown -R javauser:javauser /app
USER javauser
COPY --from=builder /project/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
