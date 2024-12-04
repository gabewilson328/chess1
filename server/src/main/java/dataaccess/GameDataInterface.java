package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDataInterface {
    public void addGame(GameData game) throws DataAccessException;

    public GameData getGameByName(String gameName) throws DataAccessException;

    public GameData getGameByID(int gameID) throws DataAccessException;

    public ArrayList<GameData> listAllGames() throws DataAccessException;

    public void updateGame(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException;

    void updateGameName(int gameID, String gameName) throws DataAccessException;

    void updateActualGame(int gameID, ChessGame game) throws DataAccessException;

    public void joinGameAsColor(ChessGame.TeamColor playerColor, int gameID, String username) throws DataAccessException;

    public void deleteAllGames() throws DataAccessException;
}
