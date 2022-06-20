FROM openjdk:8-jdk-alpine
COPY . /app
WORKDIR /app
RUN ./gradlew build
EXPOSE 8080
ENTRYPOINT ["java","-jar","build/libs/abcase-0.0.1-SNAPSHOT.jar"]