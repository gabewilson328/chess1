package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;

public class GameService {
    public ArrayList<> listGames(AuthData authToken) {
        if (AuthDataAccess.getAuth(authToken) != null) {
            GameDataAccess.listGames();
        } else {
            throw new RuntimeException("Auth token invalid");
        }

    }

    public AuthData createGame(AuthData authToken, GameData gameName) {
        if (AuthDataAccess.getAuth(authToken) != null) {
            if (GameDataAccess.getGame(gameName) == null) {
                GameDataAccess.createGame(gameName);
            } else {
                throw new RuntimeException("A game of that name already exists")
            }
        } else {
            throw new RuntimeException("Auth token invalid");
        }
    }

    public void joinGame(AuthData authToken, GameData playerColor, GameData gameID) {
        if (AuthDataAccess.getAuth(authToken) != null) {
            if (GameDataAccess.getGame(gameID) == null) {
                GameDataAccess.updateGame(gameID);
            } else {
                throw new RuntimeException("Game does not exist");
            }
        } else {
            throw new RuntimeException("Auth token does not exist");
        }
    }
}
