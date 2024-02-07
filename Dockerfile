# Build Application
FROM openjdk:20-bullseye as build

WORKDIR /app

COPY . .

RUN chmod 755 mvnw

RUN ./mvnw package -DskipTests

#Serve Application
FROM openjdk:20-bullseye as serve

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD [ "java", "--enable-preview", "-jar", "app.jar"]