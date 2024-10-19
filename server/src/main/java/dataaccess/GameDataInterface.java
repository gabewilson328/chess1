package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataInterface {
    public void addGame(GameData game);

    public void getGame(GameData game);

    public ArrayList<GameData> listGames();

    public void updateGame(GameData game);

    public void deleteAllGames();
}
