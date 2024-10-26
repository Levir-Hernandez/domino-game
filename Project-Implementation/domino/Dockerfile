FROM maven:3.9.6-eclipse-temurin-22 AS maven

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:22

WORKDIR /app

COPY --from=maven /app/target/domino-1.0-SNAPSHOT.jar ./domino.jar

CMD ["java", "-jar", "/app/domino.jar"]