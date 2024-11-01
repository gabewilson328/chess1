package dataaccess;

import chess.ChessGame;
import com.google.gson.GsonBuilder;
import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class SQLGameDataAccess implements GameDataInterface {


    @Override
    public void addGame(GameData game) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, game.gameID());
            preparedStatement.setString(2, game.whiteUsername());
            preparedStatement.setString(1, game.blackUsername());
            preparedStatement.setString(2, game.gameName());
            preparedStatement.setChessGame(1, game.game());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while adding a game to the database");
        }
    }

    @Override
    public GameData getGameByName(String gameName) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameName=?")) {
            preparedStatement.setString(1, gameName);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    if (gameName.equals(rs.getString("gameName"))) {
                        var json = rs.getString("game");
                        var builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessGame.class, new ListAdapter());
                        ChessGame game = builder.create().fromJson(json, ChessGame.class);
                        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while retrieving the game info");
        }
        return null;
    }

    @Override
    public GameData getGameByID(int gameID) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?")) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    if (gameID == (rs.getInt("gameID"))) {
                        var json = rs.getString("game");
                        var builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessGame.class, new ListAdapter());
                        ChessGame game = builder.create().fromJson(json, ChessGame.class);
                        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while retrieving the game info");
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listAllGames() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        ArrayList<GameData> allGames = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement("SELECT*FROM games")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var json = rs.getString("game");
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessGame.class, new ListAdapter());
                    ChessGame game = builder.create().fromJson(json, ChessGame.class);
                    allGames.add(new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game));
                }
                return allGames;
            }
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while listing the games");
        }
        return null;
    }

    @Override
    public void updateGame(GameData game, ChessGame.TeamColor color, String username) {

    }

    @Override
    public void joinGameAsColor(ChessGame.TeamColor playerColor, int gameID, String username) {

    }

    @Override
    public void deleteAllGames() {

    }
}
