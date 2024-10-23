package dataaccess;

import model.UserData;
import request.LoginRequest;

public interface UserDataInterface {
    public void addUser(UserData newUser);

    public UserData getUser(String username);

    public boolean verifyPassword(LoginRequest loginRequest);

    public void deleteAllUsers();
}
