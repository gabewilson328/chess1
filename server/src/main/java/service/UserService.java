package service;

import dataaccess.AuthDataAccess;
import dataaccess.UnauthorizedException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.util.UUID;

public class UserService {
    public RegisterResult registerService(RegisterRequest registerRequest, UserDataAccess userList, AuthDataAccess authList) throws UnauthorizedException {
        if (userList.getUser(registerRequest.username()) == null) {
            UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userList.addUser(user);
            String myAuth = UUID.randomUUID().toString();
            AuthData newUserAuth = new AuthData(myAuth, registerRequest.username());
            authList.addAuth(newUserAuth);
            RegisterResult registerResult = new RegisterResult(myAuth, registerRequest.username());
            return registerResult;
        } else {
            throw new UnauthorizedException("User already exists");
        }
    }

    public LoginResult loginService(LoginRequest loginRequest, UserDataAccess userList, AuthDataAccess authList) throws UnauthorizedException {
        if (userList.getUser(loginRequest.username()) != null && userList.verifyPassword(loginRequest)) {
            String myAuth = UUID.randomUUID().toString();
            AuthData returningUserAuth = new AuthData(myAuth, loginRequest.username());
            authList.addAuth(returningUserAuth);
            LoginResult loginResult = new LoginResult(myAuth, loginRequest.username());
            return loginResult;
        } else {
            throw new UnauthorizedException("Username/password invalid");
        }
    }

    public void logoutService(String authToken, AuthDataAccess authList) throws UnauthorizedException {
        if (authList.getAuth(authToken) != null) {
            authList.deleteAuth(authToken);
        } else {
            throw new UnauthorizedException("Auth token does not exist");
        }
    }
}