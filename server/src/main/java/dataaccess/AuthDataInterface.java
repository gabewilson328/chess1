package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDataInterface {
    public void addAuth(AuthData authData);

    public AuthData getAuth(String authToken);

    public ArrayList<AuthData> listAllAuths();

    public void deleteAuth(String authToken);

    public void deleteAllAuth();
}
