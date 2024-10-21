package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDataAccess implements AuthDataInterface {

    ArrayList<AuthData> allAuthData = new ArrayList<AuthData>();

    public AuthDataAccess(String authToken, String username) {

    }

    public AuthDataAccess(String authToken) {

    }

    //this method takes an authdata
    @Override
    public void addAuth(AuthData userAuth) {
        allAuthData.add(userAuth);
    }

    //these two methods just take an authtoken but getAuth returns an AuthData
    @Override
    public AuthData getAuth(String authToken) {
        for (AuthData datum : allAuthData) {
            if (datum.getAuthToken() == authToken) {
                return datum;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        for (AuthData datum : allAuthData) {
            if (datum.getAuthToken() == authToken) {
                allAuthData.remove(datum);
            }
        }
    }
}
