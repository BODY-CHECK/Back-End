FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*SNAPSHOT.jar app.jar
COPY springboot/start.sh start.sh
COPY firebase/firebase-service-account.json config/firebase-service-account.json

RUN chmod +x start.sh

ENTRYPOINT ["./start.sh"]