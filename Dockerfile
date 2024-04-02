# Build Application
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]