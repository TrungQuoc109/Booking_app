
FROM openjdk:17-oracle


WORKDIR /app

COPY build/libs/*.jar /app/your-spring-boot-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "your-spring-boot-app.jar"]
