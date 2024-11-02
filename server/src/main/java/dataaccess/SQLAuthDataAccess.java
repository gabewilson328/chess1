package dataaccess;

import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class SQLAuthDataAccess implements AuthDataInterface {

    public SQLAuthDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO allAuthData (authToken, username) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.setString(2, authData.username());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not add authToken");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT authToken, username FROM allAuthData WHERE authToken=?")) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    if (authToken.equals(rs.getString("authToken"))) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not get authToken");
        }
        return null;
    }

    @Override
    public ArrayList<AuthData> listAllAuths() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        ArrayList<AuthData> allAuths = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement("SELECT*FROM allAuthData")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    allAuths.add(new AuthData(rs.getString("authToken"), rs.getString("username")));
                }
                return allAuths;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not list authTokens");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("DELETE FROM allAuthData WHERE authToken=?")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete authToken");
        }
    }

    @Override
    public void deleteAllAuth() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("DROP TABLE allAuthData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not clear all authTokens");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `authToken` String NOT NULL,
              `username` String NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(username),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
