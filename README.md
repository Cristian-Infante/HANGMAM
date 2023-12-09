# HANGMAN

## Authors
- Cristian Fabian Infante Canelo - 160004518
- Josehp Camilo Ramos Novoa - 160004531

## Description
This project is a REST API that allows you to play the hangman game, it is developed in Java 8 with Spring Boot and Maven, it is also documented with Swagger. The game is played by entering the name of the player and the word to guess, then the player can make guesses until he guesses the word or the game ends.

## Endpoints
- ```Game/login```: Allows the login of a user by entering their name, example: http://localhost:8082/Game/login?nombreJugador=Cristian.
When the user is logged in, should save the name of the user and the word to guess.
- ```Game/guess```: Allows you to make an attempt to guess the word, example: http://localhost:8082/Game/guess?nombreJugador=Cristian&intento=a.
When the user makes a guess, the system must validate that the user is logged in and that the word to guess is not null.
- ```Game/status```: Allows you to obtain the current state of the game, example: http://localhost:8082/Game/status.
When the user requests the status of the game, the system must validate that the game is in progress, that it has already finished or that it has not players logged in.

## Jar
- Generate: ```mvn clean package```

## Docker
- Build: ```docker build -t hangman:1.0 .```
- Run: ```docker run -p8082:8082 --name hangman hangman:1.0```
- Link: ```bash http://localhost:8082/swagger-ui/index.html```
