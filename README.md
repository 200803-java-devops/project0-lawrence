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

## Testing
```
mvn test
```

## Implementation details
The project is implemented as three parts: the login server, the game server, and the client.  
The client connects to the login server, and prompts the user to register or login. If the client logs in, 
then the client receives a token. This token is presented to the game server to join a game. After joining a game, the client
waits until all slots are filled. All gameplay is performed on the command line.  
The login server simply waits for the client to connect and handles a login or registration request. On a successful login, the login server generates
a login token, puts the token in a connected database, and sends the client the token.  
The game server is a multithreaded application that provides the game logic. When enough clients have connected, the game server launches a 
thread to handle the game logic and one thread to handle each connection. The game logic thread and connection threads communicate using
a thread-safe intermediate object.
