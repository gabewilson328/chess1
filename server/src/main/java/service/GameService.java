package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;

public class GameService {
    public ListGamesResult listGamesService(String authToken, AuthDataInterface authList,
                                            GameDataInterface gameList) throws UnauthorizedException, DataAccessException {
        if (authList.getAuth(authToken) != null) {
            return new ListGamesResult(gameList.listAllGames());
        } else {
            throw new UnauthorizedException("Auth token invalid");
        }

    }

    public CreateGameResult createGameService(String authToken, String gameName, AuthDataInterface authList,
                                              GameDataInterface gameList) throws UnauthorizedException, DataAccessException {
        if (authList.getAuth(authToken) != null) {
            if (gameList.getGameByName(gameName) == null) {
                //get all data including white username and black username, gameName, and create a game
                String whiteUsername = null;
                String blackUsername = null;
                ChessGame newGame = new ChessGame();
                int gameID = gameList.listAllGames().size() + 1;
                GameData createdGame = new GameData(gameID, whiteUsername, blackUsername, gameName, newGame);
                gameList.addGame(createdGame);
                CreateGameResult createdID = new CreateGameResult(gameID);
                return createdID;
            } else {
                throw new UnauthorizedException("A game of that name already exists");
            }
        } else {
            throw new UnauthorizedException("Auth token invalid");
        }
    }

    public void joinGameService(JoinGameRequest joinGameRequest, AuthDataInterface authList, GameDataInterface gameList)
            throws TakenException, UnauthorizedException, DataAccessException {
        if (authList.getAuth(joinGameRequest.authToken()) != null) {
            if (joinGameRequest.playerColor() != null) {
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
            }
        } else {
            throw new UnauthorizedException("Auth token does not exist");
        }
    }
}
