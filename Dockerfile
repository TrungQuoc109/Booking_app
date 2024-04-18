# Sử dụng một image base chứa Java để build ứng dụng
FROM openjdk:11-jre-slim

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file build.gradle và các file khác cần thiết
COPY build.gradle .
COPY gradle.properties .
COPY settings.gradle .
COPY gradlew .
COPY gradlew.bat .

# Sao chép thư mục Gradle wrapper
COPY gradle gradle

# Sao chép mã nguồn của ứng dụng vào thư mục /app
COPY src src

# Chạy lệnh Gradle để build ứng dụng
RUN ./gradlew build

# Sao chép file JAR đã được build vào thư mục /app
COPY build/libs/*.jar app.jar

# Cổng mà ứng dụng sẽ chạy trên container
EXPOSE 8080

# Lệnh để khởi động ứng dụng Spring Boot khi container được chạy
ENTRYPOINT ["java","-jar","app.jar"]
