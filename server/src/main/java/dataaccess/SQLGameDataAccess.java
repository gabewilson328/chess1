package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameDataInterface {

    public SQLGameDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void addGame(GameData newGame) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        if (newGame.gameName() == null || newGame.game() == null) {
            throw new DataAccessException("Game name and game cannot be null");
        }
        try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, " +
                "blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, newGame.gameID());
            preparedStatement.setString(2, newGame.whiteUsername());
            preparedStatement.setString(3, newGame.blackUsername());
            preparedStatement.setString(4, newGame.gameName());
            var serializer = new Gson();
            String game = serializer.toJson(newGame.game());
            preparedStatement.setString(5, game);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGameByName(String gameName) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, " +
                "blackUsername, gameName, game FROM games WHERE gameName=?")) {
            preparedStatement.setString(1, gameName);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(rs.getInt("gameID"), rs.getString(
                            "whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGameByID(int gameID) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, " +
                "blackUsername, gameName, game FROM games WHERE gameID=?")) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(rs.getInt("gameID"), rs.getString(
                            "whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game);
                } else {
                    throw new SQLException("Game ID does not exist");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listAllGames() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        ArrayList<GameData> allGames = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    allGames.add(new GameData(rs.getInt("gameID"), rs.getString(
                            "whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game));
                }
                return allGames;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        if (username != null) {
            if (color == ChessGame.TeamColor.WHITE) {
                try (var preparedStatement = conn.prepareStatement("UPDATE games SET whiteUsername=? " +
                        "WHERE gameID=?", Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setInt(2, game.gameID());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            }
            if (color == ChessGame.TeamColor.BLACK) {
                try (var preparedStatement = conn.prepareStatement("UPDATE games SET blackUsername=? " +
                        "WHERE gameID=?", Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setInt(2, game.gameID());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            }
        } else {
            throw new DataAccessException("Username cannot be null");
        }
    }

    @Override
    public void joinGameAsColor(ChessGame.TeamColor playerColor, int gameID, String username) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, " +
                "blackUsername, gameName, game FROM games WHERE gameID=?")) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    if (gameID == rs.getInt("gameID")) {
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        GameData gameToUpdate = new GameData(rs.getInt("gameID"), rs.getString(
                                "whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game);
                        updateGame(gameToUpdate, playerColor, username);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
}
