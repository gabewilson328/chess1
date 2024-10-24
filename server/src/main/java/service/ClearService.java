package service;

import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.AuthDataAccess;

public class ClearService {
    public void clearService(GameDataAccess gameList, AuthDataAccess authList, UserDataAccess userList) {
        gameList.deleteAllGames();
        authList.deleteAllAuth();
        userList.deleteAllUsers();
    }
}
