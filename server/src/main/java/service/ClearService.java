package service;

import model.AuthData;
import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.AuthDataAccess;

public class ClearService {
    public AuthData clear() {
        GameDataAccess.deleteAllGames();
        AuthDataAccess.deleteAuth();
        UserDataAccess.deleteUser();
    }
}
