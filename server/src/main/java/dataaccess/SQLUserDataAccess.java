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
    @Override
    public void addUser(UserData newUser) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, newUser.username());
            preparedStatement.setString(2, BCrypt.hashpw(newUser.password(), BCrypt.gensalt()));
            preparedStatement.setString(3, newUser.email());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while adding a user to the database");
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
            throw new DataAccessException("An error occurred while retrieving the user");
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
            throw new DataAccessException("An error occurred while verifying the password");
        }
        return false;
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("DROP TABLE users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while deleting the user database");
        }
    }
}
