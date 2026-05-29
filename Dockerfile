FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY..
RUN chmod +x mvnw &&./mvnw clean package -DskipTests
EXPOSE 10000
CMD ["java", "-jar", "target/*.jar", "--spring.profiles.active=prod", "--server.port=10000"]