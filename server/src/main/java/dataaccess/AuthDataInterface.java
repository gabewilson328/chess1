package dataaccess;

import model.AuthData;

public interface AuthDataInterface {
    public void addAuth(AuthData authToken);

    public AuthData getAuth(AuthData authToken);

    public void deleteAuth(AuthData authToken);
}
