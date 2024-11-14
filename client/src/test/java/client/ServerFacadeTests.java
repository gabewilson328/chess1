package client;

import serverfacade.ServerFacade;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDataAccess;
import model.GameData;
import org.junit.jupiter.api.*;
import request.*;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;
import server.Server;
import serverfacade.ResponseException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() {
        try {
            serverFacade.clearApplication();
        } catch (ResponseException e) {
            System.out.print("Couldn't clear application");
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Register")
    public void register() throws ResponseException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest(
                "username", "password", "email"));
        Assertions.assertEquals("username", registerResult.username());
    }

    @Test
    @DisplayName("Register failed")
    public void registerFailed() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.register(new RegisterRequest("username", "password", "email")));
        Assertions.assertEquals("Error: already taken", e.getMessage());
    }

    @Test
    @DisplayName("Login")
    public void login() throws ResponseException, DataAccessException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest(
                "username", "password", "email"));
        serverFacade.logout(new LogoutRequest(registerResult.authToken()));
        LoginResult loginResult = serverFacade.login(new LoginRequest("username", "password"));
        Assertions.assertEquals("username", loginResult.username());
    }

    @Test
    @DisplayName("Login failed")
    public void loginFailed() throws ResponseException, DataAccessException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest(
                "username", "password", "email"));
        serverFacade.logout(new LogoutRequest(registerResult.authToken()));
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.login(new LoginRequest(null, "password")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws ResponseException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.logout(new LogoutRequest(registerResult.authToken()));
    }

    @Test
    @DisplayName("Logout failed")
    public void logoutFailed() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        ResponseException e = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(new LogoutRequest("wrongauth")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

    @Test
    @DisplayName("List games")
    public void listGames() throws ResponseException, DataAccessException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        GameData[] games = serverFacade.listGames(new ListGamesRequest(registerResult.authToken()));
        Assertions.assertEquals(1, games.length);
    }

    @Test
    @DisplayName("List games failed")
    public void listGamesFailed() throws ResponseException, DataAccessException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.listGames(new ListGamesRequest("wrongauth")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

    @Test
    @DisplayName("Create game")
    public void createGame() throws ResponseException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        String gameName = "chess";
        CreateGameResult createGameResult = serverFacade.createGame(new CreateGameRequest(registerResult.authToken(), gameName));
        Assertions.assertEquals(1, createGameResult.gameID());
    }

    @Test
    @DisplayName("Create game failed")
    public void createGameFailed() throws ResponseException, DataAccessException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.createGame(new CreateGameRequest(registerResult.authToken(), gameName)));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

    @Test
    @DisplayName("Join game")
    public void joinGame() throws ResponseException, DataAccessException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        serverFacade.joinGame(new JoinGameRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, 1));
        Assertions.assertEquals("username", gameDataAccess.getGameByName(gameName).whiteUsername());
    }

    @Test
    @DisplayName("Join game failed")
    public void joinGameFailed() throws ResponseException, DataAccessException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        int gameID = 1;
        String whiteUsername = "not null";
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.joinGame(new JoinGameRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, 1)));
        Assertions.assertEquals("Error: already taken", e.getMessage());
    }
}
