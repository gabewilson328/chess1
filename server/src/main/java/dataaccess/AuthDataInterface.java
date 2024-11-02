package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDataInterface {
    public void addAuth(AuthData authData) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public ArrayList<AuthData> listAllAuths() throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;

    public void deleteAllAuth() throws DataAccessException;
}
