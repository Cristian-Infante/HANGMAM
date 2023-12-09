# HANGMAN

## Authors
- Cristian Fabian Infante Canelo - 160004518
- Josehp Camilo Ramos Novoa - 160004531

## Description
This project is a REST API that allows you to play the hangman game, it is developed in Java 8 with Spring Boot and Maven, it is also documented with Swagger. The game is played by entering the name of the player and the word to guess, then the player can make guesses until he guesses the word or the game ends.

## Endpoints
- ```Game/login```: Allows the login of a user by entering their name, example: http://localhost:8082/Game/login?nombreJugador=Cristian.

When the user is logged in, should save the name of the user, if the user is already logged in, the system must validate that the user is not already logged in. If is the first player, the system must wait for 30 seconds for another players to log in (Maximum 5 players), if the time is exceeded, the game must start with the players who is logged in. If the maximum number of players is reached, the timer should be canceled and the game starts.
- ```Game/guess```: Allows you to make an attempt to guess the word, example: http://localhost:8082/Game/guess?nombreJugador=Cristian&intento=a.

When the user makes a guess, the system must validate that the user is logged in and that the word to guess is not null. If the user is not logged in, the system must return an error message, if the word to guess is null, the system must return an error message. If the user is logged in and the word to guess is not null, the system must validate that the word to guess is the same as the word to guess, if the word to guess is the same as the word to guess, the system must return a message indicating that the user has won the game, if the word to guess is not the same as the word to guess, the system must return a message indicating that the user has failed the attempt. If the user has won the game, the system must validate that there are still players logged in. If the user has failed the attempt, the system must validate that the user has not exceeded the maximum number of attempts, if the user has exceeded the maximum number of attempts, the system must return a message indicating that the user has lost the game, if the user has not exceeded the maximum number of attempts, the system must return a message indicating that the user has failed the attempt.
- ```Game/status```: Allows you to obtain the current state of the game, example: http://localhost:8082/Game/status.

When the user requests the status of the game, the system must validate that the game is in progress, that it has already finished or that it has no players logged in. If the game is in progress, the system must return the current state of the game, if the game has already finished, the system must return the final state of the game, if the game has no players logged in, the system must return a message indicating that there are no players logged in.

## Configuration and execution

### Jar
- Generate: 
```bash
mvn clean package
```

### Docker
- Build: 
```bash
docker build -t hangman:1.0 .
```
- Run: 
```bash
docker run -p8082:8082 --name hangman hangman:1.0
```
- Link: 
```bash 
http://localhost:8082/swagger-ui/index.html
```
