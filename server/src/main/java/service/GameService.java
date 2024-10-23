package service;

import java.util.ArrayList;
import java.util.UUID;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UnauthorizedException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;

import javax.imageio.plugins.jpeg.JPEGQTable;

public class GameService {
    public ListGamesResult listGames(String authToken, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        if (authDataAccess.getAuth(authToken) != null) {
            ListGamesResult games = new ListGamesResult(gameDataAccess.listAllGames());
            return games;
        } else {
            throw new RuntimeException("Auth token invalid");
        }

    }

    int gameNumber = 1;

    public CreateGameResult createGame(String authToken, String gameName, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        if (authDataAccess.getAuth(authToken) != null) {
            if (gameDataAccess.getGameByName(gameName) == null) {
                //get all data including white username and black username, gameName, and create a game
                int gameID = gameNumber;
                String whiteUsername = null;
                String blackUsername = null;
                ChessGame newGame;
                GameData createdGame = new GameData(gameID, whiteUsername, blackUsername, gameName, newGame);
                gameDataAccess.addGame(createdGame);
                CreateGameResult createdID = new CreateGameResult(gameID);
                gameNumber++;
                return createdID;
            } else {
                throw new UnauthorizedException("A game of that name already exists");
            }
        } else {
            throw new UnauthorizedException("Auth token invalid");
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest, String username, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        if (authDataAccess.getAuth(joinGameRequest.authToken()) != null) {
            if (gameDataAccess.getGameByID(joinGameRequest.gameID()) != null) {
                gameDataAccess.joinGameAsColor(joinGameRequest.color(), joinGameRequest.gameID(), username);
            } else {
                throw new UnauthorizedException("Game does not exist");
            }
        } else {
            throw new UnauthorizedException("Auth token does not exist");
        }
    }
}
