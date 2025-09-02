FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*SNAPSHOT.jar app.jar

COPY start.sh start.sh
RUN chmod +x start.sh

ENTRYPOINT ["./start.sh"]