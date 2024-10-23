package service;

import dataaccess.AuthDataAccess;
import dataaccess.UnauthorizedException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.util.UUID;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest, UserDataAccess userList, AuthDataAccess authList) {
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

    public LoginResult login(LoginRequest loginRequest, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        if (userDataAccess.getUser(loginRequest.username()) != null && userDataAccess.verifyPassword(loginRequest)) {
            String myAuth = UUID.randomUUID().toString();
            AuthData returningUserAuth = new AuthData(myAuth, loginRequest.username());
            authDataAccess.addAuth(returningUserAuth);
            LoginResult loginResult = new LoginResult(myAuth, loginRequest.username());
            return loginResult;
        } else {
            throw new UnauthorizedException("Username/password invalid");
        }
    }

    public void logout(String authToken, AuthDataAccess authDataAccess) {
        if (authDataAccess.getAuth(authToken) != null) {
            authDataAccess.deleteAuth(authToken);
        } else {
            throw new RuntimeException("Auth token does not exist");
        }
    }
}
