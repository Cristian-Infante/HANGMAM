FROM eclipse-mosquitto:20

LABEL author="Cristian Infante"

COPY target/Hangman-0.0.1-SNAPSHOT.jar Hangman.jar

ENTRYPOINT ["java","-jar","Hangman.jar"]

