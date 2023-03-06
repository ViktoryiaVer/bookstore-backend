FROM openjdk:17-jdk-alpine
LABEL author='Viktoria_V'
WORKDIR /app
COPY build/libs/bookstore-app.jar bookstore-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/bookstore-app.jar"]