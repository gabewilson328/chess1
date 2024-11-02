package passoff.dataaccess;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;


public class DatabaseUnitTests {
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
        UserData user = new UserData(username, password, email);
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
                userDataAccess.getUser(username));
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
        LoginRequest loginRequest = new LoginRequest(username, password);
        Assertions.assertTrue(userDataAccess.verifyPassword(loginRequest));
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
                userDataAccess.verifyPassword(new LoginRequest(username, password)));
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
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                authDataAccess.addUser(user));
    }

    @Test
    @DisplayName("addAuth failed")
    public void addAuthFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.addUser(user));
        Assertions.assertEquals("Could not add user", e.getMessage());
    }

    @Test
    @DisplayName("getAuth successful")
    public void getAuth() throws DataAccessException {
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
    @DisplayName("getAuth failed")
    public void getAuthFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.getUser(username));
        Assertions.assertEquals("Could not get user", e.getMessage());
    }

    @Test
    @DisplayName("listAuths successful")
    public void listAuths() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        Assertions.assertTrue(userDataAccess.verifyPassword(loginRequest));
    }

    @Test
    @DisplayName("verifyPassword failed")
    public void listAuthsFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.verifyPassword(new LoginRequest(username, password)));
        Assertions.assertEquals("Password incorrect", e.getMessage());
    }

    @Test
    @DisplayName("deleteAuth successful")
    public void deleteAuth() throws DataAccessException {
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
    @DisplayName("deleteAuth failed")
    public void deleteAuthFail() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () ->
                userDataAccess.getUser(username));
        Assertions.assertEquals("Could not get user", e.getMessage());
    }

    @Test
    @DisplayName("clearAuths successful")
    public void clearAuths() throws DataAccessException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        userDataAccess.deleteAllUsers();
        Assertions.assertNull(userDataAccess.getUser(username));
    }
}
