package service;

import dataaccess.*;

public class ClearService {
    public void clearService(GameDataInterface gameList, AuthDataInterface authList, UserDataInterface userList) throws DataAccessException {
        gameList.deleteAllGames();
        authList.deleteAllAuth();
        userList.deleteAllUsers();
    }
}
