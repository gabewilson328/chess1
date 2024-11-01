package dataaccess;

import model.UserData;
import request.LoginRequest;

public interface UserDataInterface {
    public void addUser(UserData newUser) throws DataAccessException;

    public String getUser(String username);

    public boolean verifyPassword(LoginRequest loginRequest);

    public void deleteAllUsers();
}
