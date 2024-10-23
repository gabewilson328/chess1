package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.Objects;

public class GameDataAccess implements GameDataInterface {
    ArrayList<GameData> games = new ArrayList<GameData>();

    public GameDataAccess() {
    }

    @Override
    public void addGame(GameData newGame) {
        games.add(newGame);
    }

    @Override
    public GameData getGameByName(String gameName) {
        for (GameData game : games) {
            if (game.gameName() == gameName) {
                return game;
            }
        }
        return null;
    }

    @Override
    public GameData getGameByID(int gameID) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }


    @Override
    public ArrayList<GameData> listAllGames() {
        return games;
    }

    @Override
    public void updateGame(GameData game) {

    }

    @Override
    public void joinGameAsColor(ChessGame.TeamColor playerColor, int gameID, String username) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                if (playerColor == ChessGame.TeamColor.WHITE) {
                    game.whiteUsername() = username;
                } else if (playerColor == ChessGame.TeamColor.BLACK) {
                    game.blackUsername() = username;
                }
            }
        }
    }

    @Override
    public void deleteAllGames() {
        games.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDataAccess that = (GameDataAccess) o;
        return Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(games);
    }
}
