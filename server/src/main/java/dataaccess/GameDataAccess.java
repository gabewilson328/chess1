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
            if (Objects.equals(game.gameName(), gameName)) {
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
    public void updateGame(GameData game, ChessGame.TeamColor color, String username) {
        if (color == ChessGame.TeamColor.WHITE) {
            GameData newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            addGame(newGame);
            games.remove(game);
        }
        if (color == ChessGame.TeamColor.BLACK) {
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            addGame(newGame);
            games.remove(game);
        }
    }
    //create a new game, copy all elements of old game into new game except whiteusername/blackusername, delete old game

    @Override
    public void joinGameAsColor(ChessGame.TeamColor playerColor, int gameID, String username) {
        GameData gameToUpdate = null;
        boolean gameExists = false;
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                gameToUpdate = game;
                gameExists = true;
            }
        }
        if (gameExists) {
            updateGame(gameToUpdate, playerColor, username);
        }
    }

    //create a new game, copy all elements of old game into new game except whiteusername/blackusername, delete old game

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
