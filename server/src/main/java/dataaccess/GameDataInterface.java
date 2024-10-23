package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDataInterface {
    public void addGame(GameData game);

    public GameData getGameByName(String gameName);

    public GameData getGameByID(int gameID);

    public ArrayList<GameData> listAllGames();

    public void updateGame(GameData game);

    public void joinGameAsColor(ChessGame.TeamColor playerColor, int gameID, String username);

    public void deleteAllGames();
}
