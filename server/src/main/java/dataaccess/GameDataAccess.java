package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public class GameDataAccess implements GameDataInterface {
    ArrayList<GameData> games = new ArrayList<GameData>();

    public GameDataAccess(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    }

    @Override
    public void addGame(GameData newGame) {
        games.add(newGame);
    }

    @Override
    public void getGame(GameData game) {

    }

    @Override
    public ArrayList<GameData> listGames() {
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
