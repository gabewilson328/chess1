package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataInterface {
    public void addGame(GameData game);

    public GameData getGame(String gameName);

    public ArrayList<GameData> listAllGames();

    public void updateGame(GameData game);

    public void deleteAllGames();
}
