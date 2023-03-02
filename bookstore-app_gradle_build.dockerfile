FROM gradle:jdk17
WORKDIR /app
COPY --chown=gradle:gradle build.gradle .
COPY --chown=gradle:gradle src src
RUN gradle build -x test
EXPOSE 8080
ENTRYPOINT ["java","-jar","build/libs/bookstore-app.jar"]