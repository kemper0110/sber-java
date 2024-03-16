FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /usr/app
COPY . .
VOLUME /tmp
VOLUME /home/gradle/.gradle
RUN ./gradlew build


FROM eclipse-temurin:21-jre-alpine
WORKDIR /usr/app
ARG JAR=/usr/app/build/libs/lab21-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR} app.jar
ENTRYPOINT ["java","-jar","app.jar"]

EXPOSE 8080