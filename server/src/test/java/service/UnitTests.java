package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UnauthorizedException;
import dataaccess.UserDataAccess;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;

public class UnitTests {
    @Test
    @DisplayName("Register successfully")
    public void registerUser() throws UnauthorizedException {
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
        Assertions.assertNotNull(userList.getUser(username));
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

        UnauthorizedException e = Assertions.assertThrows(UnauthorizedException.class, () -> userService.registerService(registerRequest, userList, authList));
        Assertions.assertEquals("User already exists", e.getMessage());
    }

    @Test
    @DisplayName("Login successfully")
    public void login() throws UnauthorizedException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        String fakeAuth = "this shouldn't show";
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        UserData user = new UserData(username, password, email);
        userList.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult expectedResult = new LoginResult(fakeAuth, username);
        LoginResult result = userService.loginService(loginRequest, userList, authList);
        Assertions.assertEquals(expectedResult.username(), result.username(), "Output is not what was expected");
    }

    @Test
    @DisplayName("Login failed")
    public void loginFail() {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        String fakeAuth = "this shouldn't show";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();
        try {
            RegisterResult registered = userService.registerService(registerRequest, userList, authList);
        } catch (Exception e) {

        }

        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult expectedResult = new LoginResult(fakeAuth, username);

        try {
            LoginResult result = userService.loginService(loginRequest, userList, authList);

            Assertions.assertEquals(expectedResult.username(), result.username(), "Output is not what was expected");

        } catch (Exception e) {

        }
    }

    @Test
    @DisplayName("Logout successfully")
    public void logout() throws UnauthorizedException {
        String username = "testusername";
        String password = "testpassword";
        String email = "test@email.com";
        String fakeAuth = "this shouldn't show";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        UserDataAccess userList = new UserDataAccess();
        AuthDataAccess authList = new AuthDataAccess();
        UserService userService = new UserService();

        RegisterResult registered = userService.registerService(registerRequest, userList, authList);


        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult expectedResult = new LoginResult(fakeAuth, username);


        LoginResult result = userService.loginService(loginRequest, userList, authList);

        userService.logoutService(result.authToken(), authList);


    }


}
