package passoff.dataaccess;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import service.ClearService;


public class DatabaseUnitTests {
    @BeforeAll
    public static void makeTables() {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.createDatabase();
        } catch (DataAccessException e) {
            new RuntimeException();
        }
    }

    @BeforeEach
    public void clearTables() {
        try {
            ClearService clearService = new ClearService();
            SQLUserDataAccess userList = new SQLUserDataAccess();
            SQLAuthDataAccess authList = new SQLAuthDataAccess();
            SQLGameDataAccess gameList = new SQLGameDataAccess();
            clearService.clearService(gameList, authList, userList);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    @DisplayName("addUser successful")
    public void addUser() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        Assertions.assertEquals(username, userDataAccess.getUser(username));
    }

    @Test
    @DisplayName("addUser failed")
    public void addUserFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(null, password, email);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.addUser(user));
        Assertions.assertEquals("Column 'username' cannot be null", e.getMessage());
    }

    @Test
    @DisplayName("getUser successful")
    public void getUser() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        String gottenUsername = userDataAccess.getUser(username);
        Assertions.assertEquals(username, gottenUsername);
    }

    @Test
    @DisplayName("getUser failed")
    public void getUserFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        Assertions.assertNull(userDataAccess.getUser("wrongusername"));
    }

    @Test
    @DisplayName("verifyPassword successful")
    public void verifyPassword() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, BCrypt.hashpw(password, BCrypt.gensalt()), email);
        userDataAccess.addUser(user);
        Assertions.assertTrue(userDataAccess.verifyPassword(username, password));
    }

    @Test
    @DisplayName("verifyPassword failed")
    public void verifyPasswordFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, BCrypt.hashpw(password, BCrypt.gensalt()), email);
        userDataAccess.addUser(user);
        Assertions.assertFalse(userDataAccess.verifyPassword(username, "wrongpassword"));
    }

    @Test
    @DisplayName("clearUsers successful")
    public void clearUsers() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        userDataAccess.deleteAllUsers();
        Assertions.assertNull(userDataAccess.getUser("username"));
    }

    @Test
    @DisplayName("addAuth successful")
    public void addAuth() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(authToken, username);
        authDataAccess.addAuth(auth);
        Assertions.assertEquals(auth, authDataAccess.getAuth(authToken));
    }

    @Test
    @DisplayName("addAuth failed")
    public void addAuthFail() throws DataAccessException {
        String username = "testusername";
        String authToken = "ajdal;skdjg;alkj";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(null, username);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.addAuth(auth));
        Assertions.assertEquals("Column 'authToken' cannot be null", e.getMessage());
    }

    @Test
    @DisplayName("getAuth successful")
    public void getAuth() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(authToken, username);
        authDataAccess.addAuth(auth);
        Assertions.assertEquals(auth, authDataAccess.getAuth(authToken));
    }

    @Test
    @DisplayName("getAuth failed")
    public void getAuthFail() throws DataAccessException {
        String username = "testusername";
        String authToken = "ajdal;skdjg;alkj";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(authToken, username);
        authDataAccess.addAuth(auth);
        Assertions.assertNull(authDataAccess.getAuth("wrongusername"));
    }

    @Test
    @DisplayName("listAuths successful")
    public void listAuths() throws DataAccessException {
        String username1 = "testusername1";
        String authToken1 = "kjdlsks;lgja";
        String username2 = "testusername2";
        String authToken2 = "sjadglk;asdg";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth1 = new AuthData(username1, authToken1);
        AuthData auth2 = new AuthData(username2, authToken2);
        authDataAccess.addAuth(auth1);
        authDataAccess.addAuth(auth2);
        Assertions.assertEquals(2, authDataAccess.listAllAuths().size());
        Assertions.assertTrue(authDataAccess.listAllAuths().contains(auth1));
        Assertions.assertTrue(authDataAccess.listAllAuths().contains(auth2));
    }

    @Test
    @DisplayName("listAuths failed")
    public void listAuthsFail() throws DataAccessException {
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        Assertions.assertTrue(authDataAccess.listAllAuths().isEmpty());
    }

    @Test
    @DisplayName("deleteAuth successful")
    public void deleteAuth() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(authToken, username);
        authDataAccess.addAuth(auth);
        authDataAccess.deleteAuth(authToken);
        Assertions.assertNull(authDataAccess.getAuth(authToken));
    }

    @Test
    @DisplayName("deleteAuth failed")
    public void deleteAuthFail() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(authToken, username);
        authDataAccess.addAuth(auth);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.deleteAuth("wrongauth"));
        Assertions.assertEquals("Could not delete auth", e.getMessage());
    }

    @Test
    @DisplayName("clearAuths successful")
    public void clearAuths() throws DataAccessException {
        String username1 = "testusername1";
        String authToken1 = "kjdlsks;lgja";
        String username2 = "testusername2";
        String authToken2 = "sjadglk;asdg";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth1 = new AuthData(username1, authToken1);
        AuthData auth2 = new AuthData(username2, authToken2);
        authDataAccess.addAuth(auth1);
        authDataAccess.addAuth(auth2);
        authDataAccess.deleteAllAuth();
        Assertions.assertTrue(authDataAccess.listAllAuths().isEmpty());
    }

    @Test
    @DisplayName("addGame successful")
    public void addGame() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        Assertions.assertEquals(1, gameDataAccess.getGameByName(gameName).gameID());
    }

    @Test
    @DisplayName("addGame failed")
    public void addGameFail() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, null);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                gameDataAccess.addGame(newGame));
        Assertions.assertEquals("Game name and game cannot be null", e.getMessage());
    }

    @Test
    @DisplayName("getGame by GameID successful")
    public void getGameByGameID() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        Assertions.assertEquals(newGame, gameDataAccess.getGameByID(gameID));
    }

    @Test
    @DisplayName("getGame by GameID failed")
    public void getGameByGameIDFail() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                gameDataAccess.getGameByID(2));
        Assertions.assertEquals("Game ID does not exist", e.getMessage());
    }

    @Test
    @DisplayName("getGame by GameName successful")
    public void getGameByGameName() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        Assertions.assertEquals(newGame, gameDataAccess.getGameByName(gameName));
    }

    @Test
    @DisplayName("getGame by GameName failed")
    public void getGameByGameNameFail() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        Assertions.assertNull(gameDataAccess.getGameByName("not chess"));
    }

    @Test
    @DisplayName("listGames successful")
    public void listGames() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        int gameID2 = 2;
        String whiteUsername2 = null;
        String blackUsername2 = null;
        String gameName2 = "chess2";
        GameData newGame2 = new GameData(gameID2, whiteUsername2, blackUsername2, gameName2, new ChessGame());
        gameDataAccess.addGame(newGame2);
        Assertions.assertEquals(2, gameDataAccess.listAllGames().size());
        Assertions.assertTrue(gameDataAccess.listAllGames().contains(newGame));
        Assertions.assertTrue(gameDataAccess.listAllGames().contains(newGame2));
    }

    @Test
    @DisplayName("listGames failed")
    public void listGamesFail() throws DataAccessException {
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        Assertions.assertTrue(gameDataAccess.listAllGames().isEmpty());
    }


    @Test
    @DisplayName("updateGame successful")
    public void updateGame() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        gameDataAccess.updateGame(newGame, ChessGame.TeamColor.WHITE, "myusername");
        Assertions.assertEquals("myusername", gameDataAccess.getGameByID(1).whiteUsername());
    }

    @Test
    @DisplayName("updateGame failed")
    public void updateGameFail() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
        gameDataAccess.addGame(newGame);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                gameDataAccess.updateGame(newGame, ChessGame.TeamColor.WHITE, null));
        Assertions.assertEquals("Username cannot be null", e.getMessage());
    }

    @Test
    @DisplayName("clearGames successful")
    public void clearGames() throws DataAccessException {
        int gameID1 = 1;
        String whiteUsername1 = null;
        String blackUsername1 = null;
        String gameName1 = "chess1";
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        GameData newGame = new GameData(gameID1, whiteUsername1, blackUsername1, gameName1, new ChessGame());
        gameDataAccess.addGame(newGame);
        int gameID2 = 2;
        String whiteUsername2 = null;
        String blackUsername2 = null;
        String gameName2 = "chess2";
        GameData newGame2 = new GameData(gameID2, whiteUsername2, blackUsername2, gameName2, new ChessGame());
        gameDataAccess.addGame(newGame2);
        gameDataAccess.deleteAllGames();
        Assertions.assertTrue(gameDataAccess.listAllGames().isEmpty());
    }
}
