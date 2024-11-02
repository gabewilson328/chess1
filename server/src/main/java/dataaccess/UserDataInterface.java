package dataaccess;

import model.UserData;
import request.LoginRequest;

public interface UserDataInterface {
    public void addUser(UserData newUser) throws DataAccessException;

    public String getUser(String username) throws DataAccessException;

    public boolean verifyPassword(String username, String password) throws DataAccessException;

    public void deleteAllUsers() throws DataAccessException;
}
