package service;

import java.util.ArrayList;
import java.util.UUID;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;

public class GameService {//help moving gameservice back to service because i fell asleep and accidentally changed it

    public ArrayList<GameData> listGames(String authToken) {
        AuthDataAccess authDataAccess = new AuthDataAccess(authToken);
        if (authDataAccess.getAuth(authToken) != null) {
            return listAllGames(); //how to call this function
        } else {
            throw new RuntimeException("Auth token invalid");
        }

    }

    public String createGame(String authToken, String gameName) {
        AuthDataAccess authDataAccess = new AuthDataAccess(authToken);
        GameDataAccess gameDataAccess = new GameDataAccess(gameName);

        if (authDataAccess.getAuth(authToken) != null) {
            if (gameDataAccess.getGame(gameName) == null) {
                //get all data including white username and black username, gameName, and create a game
                String gameID = UUID.randomUUID().toString();
                String whiteUsername = "";
                String blackUsername = "";
                ChessGame newGame;
                return gameID; //this is wrong but I don't know how to return text plus this
            } else {
                throw new RuntimeException("A game of that name already exists");
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
