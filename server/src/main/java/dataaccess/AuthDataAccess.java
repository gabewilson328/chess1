package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDataAccess implements AuthDataInterface {

    ArrayList<AuthData> authData = new ArrayList<AuthData>();

    public AuthDataAccess(String authToken, String username) {

    }

    @Override
    public void addAuth(AuthData userAuth) {
        authData.add(userAuth);
    }

    @Override
    public AuthData getAuth(AuthData userAuth) {
        if (authData.contains(userAuth)) {
            return userAuth;
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authToken) {
        authData.remove(authToken);
    }
}
