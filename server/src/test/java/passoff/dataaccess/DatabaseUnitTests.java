package passoff.dataaccess;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import result.LoginResult;


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
        Assertions.assertEquals("Could not add user", e.getMessage());
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
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.getUser("wrongusername"));
        Assertions.assertEquals("Could not get user", e.getMessage());
    }

    @Test
    @DisplayName("verifyPassword successful")
    public void verifyPassword() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        Assertions.assertTrue(userDataAccess.verifyPassword(username, BCrypt.hashpw(password, BCrypt.gensalt())));
    }

    @Test
    @DisplayName("verifyPassword failed")
    public void verifyPasswordFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.verifyPassword(username, "wrongpassword"));
        Assertions.assertEquals("Password incorrect", e.getMessage());
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
        Assertions.assertNull(userDataAccess.getUser(username));
    }

    @Test
    @DisplayName("addAuth successful")
    public void addAuth() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(username, authToken);
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
        Assertions.assertEquals("Could not add authToken", e.getMessage());
    }

    @Test
    @DisplayName("getAuth successful")
    public void getAuth() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(username, authToken);
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
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.getAuth("wrongusername"));
        Assertions.assertEquals("Could not get authToken", e.getMessage());
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
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.listAllAuths());
        Assertions.assertEquals("Could not list authTokens", e.getMessage());
    }

    @Test
    @DisplayName("deleteAuth successful")
    public void deleteAuth() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(username, authToken);
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
        AuthData auth = new AuthData(username, authToken);
        authDataAccess.addAuth(auth);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.deleteAuth("wrongauth"));
        Assertions.assertEquals("Could not delete authToken", e.getMessage());
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
        Assertions.assertNull(authDataAccess.getAuth(authToken1));
        Assertions.assertNull(authDataAccess.getAuth(authToken2));
    }

    @Test
    @DisplayName("addGame successful")
    public void addGame() throws DataAccessException {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "chess";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        //AuthData auth = new AuthData(username, authToken);
        //authDataAccess.addAuth(auth);
        //Assertions.assertEquals(auth, authDataAccess.getAuth(authToken));
    }

    @Test
    @DisplayName("addGame failed")
    public void addGameFail() throws DataAccessException {
        String username = "testusername";
        String authToken = "ajdal;skdjg;alkj";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(null, username);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.addAuth(auth));
        Assertions.assertEquals("Could not add authToken", e.getMessage());
    }

    @Test
    @DisplayName("getGame by GameID successful")
    public void getGameByGameID() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(username, authToken);
        authDataAccess.addAuth(auth);
        Assertions.assertEquals(auth, authDataAccess.getAuth(authToken));
    }

    @Test
    @DisplayName("getGame by GameID failed")
    public void getGameByGameIDFail() throws DataAccessException {
        String username = "testusername";
        String authToken = "ajdal;skdjg;alkj";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(authToken, username);
        authDataAccess.addAuth(auth);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.getAuth("wrongusername"));
        Assertions.assertEquals("Could not get authToken", e.getMessage());
    }


    @Test
    @DisplayName("listGames successful")
    public void listGames() throws DataAccessException {
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
    @DisplayName("listGames failed")
    public void listGamesFail() throws DataAccessException {
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.listAllAuths());
        Assertions.assertEquals("Could not list authTokens", e.getMessage());
    }


    @Test
    @DisplayName("deleteGame successful")
    public void deleteGame() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(username, authToken);
        authDataAccess.addAuth(auth);
        authDataAccess.deleteAuth(authToken);
        Assertions.assertNull(authDataAccess.getAuth(authToken));
    }

    @Test
    @DisplayName("deleteGame failed")
    public void deleteGameFail() throws DataAccessException {
        String username = "testusername";
        String authToken = "kjdlsks;lgja";
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        AuthData auth = new AuthData(username, authToken);
        authDataAccess.addAuth(auth);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.deleteAuth("wrongauth"));
        Assertions.assertEquals("Could not delete authToken", e.getMessage());
    }

    @Test
    @DisplayName("clearGames successful")
    public void clearGames() throws DataAccessException {
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
        Assertions.assertNull(authDataAccess.getAuth(authToken1));
        Assertions.assertNull(authDataAccess.getAuth(authToken2));
    }
}
