# HANGMAN

## Cristian Fabian Infante Canelo - 160004518
## Josehp Camilo Ramos Novoa - 160004531

## Endpoints
- Game/login: Allows the login of a user by entering their name, example: http://localhost:8082/Game/login?nombreJugador=Cristian
- Game/guess: Allows you to make an attempt to guess the word, example: http://localhost:8082/Game/guess?nombreJugador=Cristian&intento=a
- Game/status: Allows you to obtain the current state of the game, example: http://localhost:8082/Game/status

## Generate jar
- mvn clean package

## Docker
- Build: docker build -t hangman:1.0 .
- Run: docker run -p8082:8082 --name hangman hangman:1.0
- Link: http://localhost:8082/swagger-ui/index.html
