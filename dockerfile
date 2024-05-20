FROM gradle:7.5.1-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

COPY src ./src

RUN gradle clean build -x test

RUN ls -l build/libs/

FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/build/libs/profile-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
