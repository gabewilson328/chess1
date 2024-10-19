package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    public AuthData register(UserData user) {
        UserDataAccess userDataAccess = new UserDataAccess(user.getUsername(), user.getPassword(), user.getEmail());
        if (userDataAccess.getUsername(user) == null) {
            userDataAccess.addUser(user);
            String myAuth = UUID.randomUUID().toString();
            AuthData newUserAuth = new AuthData(myAuth, user.getUsername());
            AuthDataAccess authToCreate = new AuthDataAccess(newUserAuth.getAuthToken(), user.getUsername());
            authToCreate.addAuth(newUserAuth);
            return newUserAuth; //
        } else {
            throw new RuntimeException("User already exists");
        }
    }

    public AuthData login(UserData user) {
        UserDataAccess userDataAccess = new UserDataAccess(user.getUsername(), user.getPassword(), user.getEmail());

        if (userDataAccess.getUsername(user) != null && userDataAccess.verifyPassword(user)) {
            String myAuth = UUID.randomUUID().toString();
            AuthData returningUserAuth = new AuthData(myAuth, user.getUsername());
            AuthDataAccess authToCreate = new AuthDataAccess(myAuth, user.getUsername());
            authToCreate.addAuth(returningUserAuth);
            return returningUserAuth;
        } else {
            throw new RuntimeException("Username/password invalid");
        }
    }

    public void logout(AuthData auth) {
        if (AuthDataAccess.getAuth(auth) != null) {
            AuthDataAccess.deleteAuth(auth);
        } else {
            throw new RuntimeException("Auth token does not exist");
        }
    }
}
