package client;

import ServerFacade.ServerFacade;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDataAccess;
import dataaccess.SQLUserDataAccess;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.ListGamesRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;
import server.Server;
import ServerFacade.ResponseException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade = new ServerFacade("http://localhost:8080");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Register")
    public void register() throws ResponseException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
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
        String username = "username";
        String password = "password";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        LoginResult loginResult = serverFacade.login(new LoginRequest("username", "password"));
        Assertions.assertEquals("username", loginResult.username());
    }

    @Test
    @DisplayName("Login failed")
    public void loginFailed() throws ResponseException, DataAccessException {
        String username = "username";
        String password = "password";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.login(new LoginRequest(null, "password")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws ResponseException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertEquals("username", registerResult.username());
    }

    @Test
    @DisplayName("Logout failed")
    public void logoutFailed() throws ResponseException {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("username", "password", "email"));
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.logout(new LogoutRequest("wrongauth")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

//    @Test
//    @DisplayName("List games")
//    public void listGames() throws ResponseException, DataAccessException {
//        RegisterResult registerResult = server.register(new RegisterRequest("username", "password", "email"));
//        int gameID = 1;
//        String whiteUsername = null;
//        String blackUsername = null;
//        String gameName = "chess";
//        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
//        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
//        gameDataAccess.addGame(newGame);
//        ListGamesResult listGamesResult = serverFacade.listGames(new ListGamesRequest(registerResult.authToken()));
//        Assertions.assertTrue(listGamesResult.games().contains(newGame));
//    }

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
}
