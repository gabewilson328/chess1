package dataaccess;

import model.UserData;
import request.LoginRequest;

import java.util.ArrayList;
import java.util.Objects;

public class UserDataAccess implements UserDataInterface {

    ArrayList<UserData> users = new ArrayList<UserData>();

    public UserDataAccess() {
    }

    @Override
    public void addUser(UserData newUser) {
        users.add(newUser);
    }

    @Override
    public UserData getUser(String username) {
        for (UserData eachUser : users) {
            if (Objects.equals(eachUser.username(), username)) {
                return eachUser;
            }
        }
        return null;
    }

    @Override
    public boolean verifyPassword(LoginRequest loginRequest) {
        for (UserData eachUser : users) {
            if (Objects.equals(eachUser.username(), loginRequest.username())) {
                if (Objects.equals(eachUser.password(), loginRequest.password())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void deleteAllUsers() {
        users.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDataAccess that = (UserDataAccess) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(users);
    }
}
