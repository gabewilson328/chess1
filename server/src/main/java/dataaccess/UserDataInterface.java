package dataaccess;

import model.UserData;

public interface UserDataInterface {
    public void addUser(UserData newUser);

    public String getUsername(UserData user);

    public boolean verifyPassword(UserData user);

    public String getEmail(UserData user);

    public void deleteAllUsers();
}
