package service;

import dataaccess.*;

public class ClearService {
    public void clearService(SQLGameDataAccess gameList, SQLAuthDataAccess authList, SQLUserDataAccess userList) throws DataAccessException {
        gameList.deleteAllGames();
        authList.deleteAllAuth();
        userList.deleteAllUsers();
    }
}
