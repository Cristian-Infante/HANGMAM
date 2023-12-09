FROM openjdk:22-slim

LABEL author="Cristian Infante"

COPY target/Hangman-0.0.1-SNAPSHOT.jar /app/Hangman.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/app/Hangman.jar"]

