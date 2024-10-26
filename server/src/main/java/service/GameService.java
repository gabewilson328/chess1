package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;

public class GameService {
    public ListGamesResult listGamesService(String authToken, AuthDataAccess authList, GameDataAccess gameList) throws UnauthorizedException {
        if (authList.getAuth(authToken) != null) {
            return new ListGamesResult(gameList.listAllGames());
        } else {
            throw new UnauthorizedException("Auth token invalid");
        }

    }

    int gameNumber = 1;

    public CreateGameResult createGameService(String authToken, String gameName, AuthDataAccess authList,
                                              GameDataAccess gameList) throws UnauthorizedException {
        if (authList.getAuth(authToken) != null) {
            if (gameList.getGameByName(gameName) == null) {
                //get all data including white username and black username, gameName, and create a game
                int gameID = gameNumber;
                String whiteUsername = null;
                String blackUsername = null;
                ChessGame newGame = new ChessGame();
                GameData createdGame = new GameData(gameID, whiteUsername, blackUsername, gameName, newGame);
                gameList.addGame(createdGame);
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

    public void joinGameService(JoinGameRequest joinGameRequest, AuthDataAccess authList, GameDataAccess gameList)
            throws TakenException, UnauthorizedException {
        if (authList.getAuth(joinGameRequest.authToken()) != null) {
            if (gameList.getGameByID(joinGameRequest.gameID()) != null) {
                String username = authList.getAuth(joinGameRequest.authToken()).username();
                boolean whiteEmpty = gameList.getGameByID(joinGameRequest.gameID()).whiteUsername() == null;
                boolean blackEmpty = gameList.getGameByID(joinGameRequest.gameID()).blackUsername() == null;
                if (whiteEmpty && joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                    gameList.joinGameAsColor(joinGameRequest.playerColor(), joinGameRequest.gameID(), username);
                } else if (blackEmpty && joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK) {
                    gameList.joinGameAsColor(joinGameRequest.playerColor(), joinGameRequest.gameID(), username);
                } else {
                    throw new TakenException("Color isn't available");
                }

            } else {
                throw new UnauthorizedException("Game does not exist");
            }
        } else {
            throw new UnauthorizedException("Auth token does not exist");
        }
    }
}
