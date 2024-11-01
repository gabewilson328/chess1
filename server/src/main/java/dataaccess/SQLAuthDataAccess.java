package dataaccess;

import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class SQLAuthDataAccess implements AuthDataInterface {


    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO allAuthData (authToken, username) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.setString(2, authData.username());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while adding a user to the database");
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
            throw new DataAccessException("An error occurred while verifying the authToken");
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
            throw new DataAccessException("An error occurred while listing the authTokens");
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("DELETE FROM allAuthData WHERE authToken=?")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while deleting the authToken");
        }
    }

    @Override
    public void deleteAllAuth() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("DROP TABLE allAuthData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An error occurred while deleting the authToken database");
        }
    }


}