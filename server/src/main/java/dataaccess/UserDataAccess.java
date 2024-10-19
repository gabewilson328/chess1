package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class UserDataAccess implements UserDataInterface {

    ArrayList<UserData> users = new ArrayList<UserData>();

    public UserDataAccess(String username, String password, String email) {

    }


    @Override
    public void addUser(UserData newUser) {
        users.add(newUser);
    }

    @Override
    public String getUsername(UserData user) {
        for (UserData eachUser : users) {
            if (eachUser.getUsername() == user.getUsername()) {
                return user.getUsername();
            }
        }
        return null;
    }

    @Override
    public boolean verifyPassword(UserData user) {
        for (UserData eachUser : users) {
            if (eachUser.getUsername() == user.getUsername()) {
                if (eachUser.getPassword() == user.getPassword()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getEmail(UserData user) {
        return "";
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
