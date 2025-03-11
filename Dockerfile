FROM maven:3.9.5-openjdk-21 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
