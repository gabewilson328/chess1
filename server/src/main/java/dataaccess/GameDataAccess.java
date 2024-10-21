package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public class GameDataAccess implements GameDataInterface {
    ArrayList<GameData> games = new ArrayList<GameData>();

    public GameDataAccess(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    }

    public GameDataAccess(String gameName) {
    }

    @Override
    public void addGame(GameData newGame) {
        games.add(newGame);
    }

    @Override
    public GameData getGame(String gameName) {
        for (GameData game : games) {
            if (game.getGameName() == gameName) {
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
    public void deleteAllGames() {
        games.clear();
    }

}
