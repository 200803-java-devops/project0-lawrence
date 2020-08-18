# project0-lawrence
An simplified, multiplayer blackjack game on the command-line.

## Features
- [x] Allows three players to connect to each other simultaneously.
    - [x] Each player can take their turn simultaneously
- [ ] Allows players to register to keep progress.
    - [x] Has a username database. 
    - [ ] Player's score persists through multiple sessions.
- [ ] Has a leaderboard of high scores.

## Running
To compile:
```
mvn compile -q
```
### Server
Both the game server and the login server must be run.
To run the login server:
```
mvn exec:java@login-server -q
```
To run the game server:
```
mvn exec:java@game-server -q
```

### Client
To run the game client:
```
mvn exec:java@client -q
```


