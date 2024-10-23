package dataaccess;

import model.AuthData;

public interface AuthDataInterface {
    public void addAuth(AuthData authData);

    public AuthData getAuth(String authToken);

    public void deleteAuth(String authToken);

    public void deleteAllAuth();
}
