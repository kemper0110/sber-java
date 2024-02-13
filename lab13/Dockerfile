FROM node:21-alpine as frontend-build
ENV YARN_VERSION=4.1.0
RUN apk update && apk upgrade && apk add --no-cache libc6-compat && apk add dumb-init
RUN corepack enable && corepack prepare yarn@${YARN_VERSION}
WORKDIR /usr/app
COPY frontend/ .
RUN yarn install
RUN yarn build


FROM eclipse-temurin:21-jdk-alpine as backend-build
WORKDIR /usr/app
COPY . .
COPY --from=frontend-build /usr/app/dist src/main/resources/static
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/usr/app/target/dependency
COPY --from=backend-build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=backend-build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=backend-build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","org.danil.lab13.Lab13Application"]