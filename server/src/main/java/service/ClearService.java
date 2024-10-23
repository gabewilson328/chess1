package service;

import model.AuthData;
import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.AuthDataAccess;

public class ClearService {
    public void clear(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess, UserDataAccess userDataAccess) {
        gameDataAccess.deleteAllGames();
        authDataAccess.deleteAllAuth();
        userDataAccess.deleteAllUsers();
    }
}
