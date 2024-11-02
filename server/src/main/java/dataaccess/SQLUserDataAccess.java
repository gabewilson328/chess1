package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;

import javax.xml.crypto.Data;
import java.sql.*;

import java.util.ArrayList;
import java.util.Objects;

import static dataaccess.DatabaseManager.getConnection;

public class SQLUserDataAccess implements UserDataInterface {

    public SQLUserDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void addUser(UserData newUser) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, newUser.username());
            preparedStatement.setString(2, BCrypt.hashpw(newUser.password(), BCrypt.gensalt()));
            preparedStatement.setString(3, newUser.email());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not add user");
        }
    }

    @Override
    public String getUser(String username) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT username, email, password FROM users WHERE username=?")) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    if (username.equals(rs.getString("username"))) {
                        return username;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not get user");
        }
        return null;
    }

    @Override
    public boolean verifyPassword(LoginRequest loginRequest) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT password FROM users WHERE PASSWORD=?")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    if (BCrypt.checkpw(loginRequest.password(), rs.getString("password"))) {
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Password incorrect");
        }
        return false;
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("DROP TABLE users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete users");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `username` String NOT NULL,
              `password` String NOT NULL,
              `email` String NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
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
