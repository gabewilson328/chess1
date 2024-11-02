package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;

public class UnitTests {
    @Test
    @DisplayName("Register successful")
    public void registerUser() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        RegisterResult result = userService.registerService(registerRequest, userList, authList);

        Assertions.assertEquals(username, result.username(), "Username is wrong");
        Assertions.assertNotNull(result.authToken(), "Auth token is null");
        Assertions.assertNotNull(userList.getUser(username), "Didn't store the username");
    }

    @Test
    @DisplayName("Register failed")
    public void registerUserFail() {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);

        UnauthorizedException e = Assertions.assertThrows(UnauthorizedException.class, () ->
                userService.registerService(registerRequest, userList, authList));
        Assertions.assertEquals("User already exists", e.getMessage());
    }

    @Test
    @DisplayName("Login successful")
    public void login() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult result = userService.loginService(loginRequest, userList, authList);
        Assertions.assertEquals(username, result.username(), "Username is wrong");
        Assertions.assertNotNull(result.authToken(), "Auth token is null");
        Assertions.assertNotNull(userList.getUser(username), "Didn't store the username");
    }

    @Test
    @DisplayName("Login failed")
    public void loginFail() throws UnauthorizedException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, "passwordfail");
        UnauthorizedException e = Assertions.assertThrows(UnauthorizedException.class, () ->
                userService.loginService(loginRequest, userList, authList));
        Assertions.assertEquals("Username/password invalid", e.getMessage());
    }

    @Test
    @DisplayName("Logout successful")
    public void logout() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        AuthData userToStay = new AuthData(
                "arandomauthtokentotestifitwilldeleteallofthemorjusttheoneit'ssupposedto", "otherusername");
        authList.addAuth(userToStay);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult result = userService.loginService(loginRequest, userList, authList);
        userService.logoutService(result.authToken(), authList);
        Assertions.assertNull(authList.getAuth(result.authToken()));
        Assertions.assertNotNull(authList.getAuth(userToStay.authToken()));
    }

    @Test
    @DisplayName("Logout failed")
    public void logoutFail() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult result = userService.loginService(loginRequest, userList, authList);
        UnauthorizedException e = Assertions.assertThrows(UnauthorizedException.class, () ->
                userService.logoutService("jsdlgakjsdakjsd;lfkjas", authList));
        Assertions.assertEquals("Auth token does not exist", e.getMessage());
    }

    @Test
    @DisplayName("List games successful")
    public void listGames() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);
        ChessGame gameone = new ChessGame();
        ChessGame gametwo = new ChessGame();
        ChessGame gamethree = new ChessGame();
        GameData game1 = new GameData(1, "whiteguy1", "blackguy1", "firstgame", gameone);
        GameData game2 = new GameData(2, "whiteguy2", "blackguy2", "secondgame", gametwo);
        GameData game3 = new GameData(3, "whiteguy3", "blackguy3", "thirdgame", gamethree);
        GameDataAccess gameList = new GameDataAccess();
        GameService gameService = new GameService();
        gameList.addGame(game1);
        gameList.addGame(game2);
        gameList.addGame(game3);
        ListGamesResult result = gameService.listGamesService(loginResult.authToken(), authList, gameList);
        for (GameData aGame : result.games()) {
            if (aGame.gameID() == 1) {
                Assertions.assertEquals(game1, aGame, "Games are not displaying correctly");
            } else if (aGame.gameID() == 2) {
                Assertions.assertEquals(game2, aGame, "Games are not displaying correctly");
            } else if (aGame.gameID() == 3) {
                Assertions.assertEquals(game3, aGame, "Games are not displaying correctly");
            }
        }
        Assertions.assertFalse(result.games().isEmpty(), "List games function comes back empty");
    }

    @Test
    @DisplayName("List games failed")
    public void listGamesFail() throws UnauthorizedException, DataAccessException {
        AuthDataAccess authList = getAuthDataAccess();
        ChessGame gameone = new ChessGame();
        ChessGame gametwo = new ChessGame();
        ChessGame gamethree = new ChessGame();
        GameData game1 = new GameData(1, "whiteguy1", "blackguy1", "firstgame", gameone);
        GameData game2 = new GameData(2, "whiteguy2", "blackguy2", "secondgame", gametwo);
        GameData game3 = new GameData(3, "whiteguy3", "blackguy3", "thirdgame", gamethree);
        GameDataAccess gameList = new GameDataAccess();
        GameService gameService = new GameService();
        gameList.addGame(game1);
        gameList.addGame(game2);
        gameList.addGame(game3);
        UnauthorizedException e = Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.listGamesService("ksjdlakjsldgkjslkjs", authList, gameList));
        Assertions.assertEquals("Auth token invalid", e.getMessage());
    }

    private static AuthDataAccess getAuthDataAccess() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);
        return authList;
    }

    @Test
    @DisplayName("Create game successful")
    public void createGame() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);

        GameDataAccess gameList = new GameDataAccess();
        GameService gameService = new GameService();
        CreateGameResult result = gameService.createGameService(loginResult.authToken(), "myGame", authList, gameList);
        ListGamesResult listOfGames = gameService.listGamesService(loginResult.authToken(), authList, gameList);
        for (GameData aGame : listOfGames.games()) {
            Assertions.assertEquals("myGame", aGame.gameName(), "Game is not saving correctly");
            Assertions.assertNull(aGame.whiteUsername(), "Game isn't joinable");
            Assertions.assertNull(aGame.blackUsername(), "Game isn't joinable");
        }
    }

    @Test
    @DisplayName("Create game failed")
    public void createGameFail() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);

        GameDataAccess gameList = new GameDataAccess();
        GameService gameService = new GameService();

        ChessGame gameone = new ChessGame();
        GameData game1 = new GameData(1, "whiteguy1", "blackguy1", "firstgame", gameone);
        gameList.addGame(game1);

        UnauthorizedException e = Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.createGameService(loginResult.authToken(), "firstgame", authList, gameList));
        Assertions.assertEquals("A game of that name already exists", e.getMessage());
    }

    @Test
    @DisplayName("Join game successful")
    public void joinGame() throws UnauthorizedException, TakenException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);

        ChessGame gameone = new ChessGame();
        ChessGame gametwo = new ChessGame();
        ChessGame gamethree = new ChessGame();
        GameData game1 = new GameData(1, null, null, "firstgame", gameone);
        GameData game2 = new GameData(2, null, null, "secondgame", gametwo);
        GameData game3 = new GameData(3, null, null, "thirdgame", gamethree);
        GameDataAccess gameList = new GameDataAccess();
        GameService gameService = new GameService();
        gameList.addGame(game1);
        gameList.addGame(game2);
        gameList.addGame(game3);

        JoinGameRequest joinGameRequest = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.WHITE, 2);
        gameService.joinGameService(joinGameRequest, authList, gameList);

        Assertions.assertNull(gameList.getGameByID(2).blackUsername(), "Joining wrong team");
        Assertions.assertEquals(loginResult.username(), gameList.getGameByID(2).whiteUsername(), "Didn't join game");
    }

    @Test
    @DisplayName("Join game failed")
    public void joinGameFail() throws UnauthorizedException, TakenException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);

        ChessGame gameone = new ChessGame();
        ChessGame gametwo = new ChessGame();
        ChessGame gamethree = new ChessGame();
        GameData game1 = new GameData(1, "whiteguy1", "blackguy1", "firstgame", gameone);
        GameData game2 = new GameData(2, "whiteguy2", "blackguy2", "secondgame", gametwo);
        GameData game3 = new GameData(3, "whiteguy3", "blackguy3", "thirdgame", gamethree);
        GameDataAccess gameList = new GameDataAccess();
        GameService gameService = new GameService();
        gameList.addGame(game1);
        gameList.addGame(game2);
        gameList.addGame(game3);

        JoinGameRequest joinGameRequest = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.WHITE, 2);
        TakenException e = Assertions.assertThrows(TakenException.class, () ->
                gameService.joinGameService(joinGameRequest, authList, gameList));
        Assertions.assertEquals("Color isn't available", e.getMessage());
    }

    @Test
    @DisplayName("Clear successful")
    public void clear() throws UnauthorizedException, DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        UserDataInterface userList = new UserDataAccess();
        AuthDataInterface authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.loginService(loginRequest, userList, authList);
        ChessGame gameone = new ChessGame();
        GameData game1 = new GameData(1, "whiteguy", "blackguy", "thegame", gameone);
        GameDataInterface gameList = new GameDataAccess();
        GameService gameService = new GameService();
        gameList.addGame(game1);

        ClearService clearService = new ClearService();
        clearService.clearService(gameList, authList, userList);

        Assertions.assertTrue(gameList.listAllGames().isEmpty());
        Assertions.assertTrue(authList.listAllAuths().isEmpty());
        Assertions.assertNull(userList.getUser(username));
    }
}
