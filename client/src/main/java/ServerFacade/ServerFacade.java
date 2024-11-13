package ServerFacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import request.*;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, registerRequest, null, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, null, LoginResult.class);
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, logoutRequest, logoutRequest.authToken(), null);
    }

    public GameData[] listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        var path = "/game";
        record listGameResponse(GameData[] games) {
        }
        var response = this.makeRequest("GET", path, listGamesRequest, listGamesRequest.authToken(), listGameResponse.class);
        return response.games();
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, createGameRequest, createGameRequest.authToken(), CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, joinGameRequest, joinGameRequest.authToken(), AuthData.class);
    }

    public void clearApplication() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
