FROM amazoncorretto:20-alpine as builder
COPY . .

RUN chmod +x gradlew

RUN ./gradlew bootJar


FROM openjdk:20-slim

COPY --from=builder build/libs src

RUN chmod +x ./src/SpringTest.jar

EXPOSE 8000

ENTRYPOINT ["java","-jar" ,"./src/SpringTest.jar"]
