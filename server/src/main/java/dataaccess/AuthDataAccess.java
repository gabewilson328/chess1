package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class AuthDataAccess implements AuthDataInterface {

    ArrayList<AuthData> allAuthData = new ArrayList<AuthData>();

    public AuthDataAccess() {
    }

    @Override
    public void addAuth(AuthData userAuth) {
        allAuthData.add(userAuth);
    }

    //these two methods just take an authtoken but getAuth returns an AuthData
    @Override
    public AuthData getAuth(String authToken) {
        for (AuthData datum : allAuthData) {
            if (datum.authToken() == authToken) {
                return datum;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        for (AuthData datum : allAuthData) {
            if (datum.authToken() == authToken) {
                allAuthData.remove(datum);
            }
        }
    }

    @Override
    public void deleteAllAuth() {
        allAuthData.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthDataAccess that = (AuthDataAccess) o;
        return Objects.equals(allAuthData, that.allAuthData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(allAuthData);
    }
}
