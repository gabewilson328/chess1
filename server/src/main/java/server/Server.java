package server;

import com.google.gson.Gson;
import dataaccess.*;
import request.*;
import result.*;
import service.UserService;
import service.GameService;
import service.ClearService;
import spark.*;
import server.WebSocketHandler;

public class Server {

    private SQLUserDataAccess userList;
    private SQLAuthDataAccess authList;
    private SQLGameDataAccess gameList;
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    private WebSocketHandler webSocketHandler;

    public Server() {
        try {
            userList = new SQLUserDataAccess();
            authList = new SQLAuthDataAccess();
            gameList = new SQLGameDataAccess();
            userService = new UserService();
            gameService = new GameService();
            clearService = new ClearService();
            webSocketHandler = new WebSocketHandler(authList, gameList);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        var serializer = new Gson();
        try {
            RegisterRequest registerRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            if (registerRequest.username() != null && registerRequest.password() != null && registerRequest.email() != null) {
                String result = serializer.toJson(userService.registerService(registerRequest, userList, authList));
                res.status(200);
                res.body(result);
                return result;
            }
            ErrorResults error = new ErrorResults("Error: bad request");
            res.status(400);
            return serializer.toJson(error);
        } catch (UnauthorizedException e) {
            ErrorResults error = new ErrorResults("Error: already taken");
            res.status(403);
            return serializer.toJson(error);
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }

    private Object login(Request req, Response res) {
        var serializer = new Gson();
        try {
            LoginRequest loginRequest = serializer.fromJson(req.body(), LoginRequest.class);
            String result = serializer.toJson(userService.loginService(loginRequest, userList, authList));
            res.status(200);
            res.body(result);
            return result;
        } catch (UnauthorizedException e) {
            ErrorResults error = new ErrorResults("Error: unauthorized");
            res.status(401);
            return serializer.toJson(error);
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }

    private Object logout(Request req, Response res) {
        var serializer = new Gson();
        try {
            LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
            userService.logoutService(logoutRequest.authToken(), authList);
            res.status(200);
            res.body("{}");
            return "{}";
        } catch (UnauthorizedException e) {
            ErrorResults error = new ErrorResults("Error: unauthorized");
            res.status(401);
            return serializer.toJson(error);
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }

    private Object listGames(Request req, Response res) {
        var serializer = new Gson();
        try {
            ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
            String result = serializer.toJson(gameService.listGamesService(listGamesRequest.authToken(), authList, gameList));
            res.status(200);
            res.body(result);
            return result;
        } catch (UnauthorizedException e) {
            ErrorResults error = new ErrorResults("Error: unauthorized");
            res.status(401);
            return serializer.toJson(error);
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }

    private Object createGame(Request req, Response res) {
        var serializer = new Gson();
        try {
            CreateGameRequest preCreateGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
            CreateGameRequest createGameRequest = new CreateGameRequest(req.headers("authorization"), preCreateGameRequest.gameName());
            if (createGameRequest.authToken() != null && createGameRequest.gameName() != null) {
                String result = serializer.toJson(gameService.createGameService(createGameRequest.authToken(),
                        createGameRequest.gameName(), authList, gameList));
                res.status(200);
                res.body(result);
                return result;
            }
            ErrorResults error = new ErrorResults("Error: bad request");
            res.status(400);
            return serializer.toJson(error);
        } catch (UnauthorizedException e) {
            ErrorResults error = new ErrorResults("Error: unauthorized");
            res.status(401);
            return serializer.toJson(error);
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }

    private Object joinGame(Request req, Response res) {
        var serializer = new Gson();
        try {
            JoinGameRequest preJoinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
            JoinGameRequest joinGameRequest = new JoinGameRequest(req.headers("authorization"),
                    preJoinGameRequest.playerColor(), preJoinGameRequest.gameID());
            if (joinGameRequest.authToken() != null && joinGameRequest.playerColor() != null && joinGameRequest.gameID() != null) {
                gameService.joinGameService(joinGameRequest, authList, gameList);
                res.status(200);
                res.body("{}");
                return "{}";
            }
            ErrorResults error = new ErrorResults("Error: bad request");
            res.status(400);
            return serializer.toJson(error);
        } catch (UnauthorizedException e) {
            ErrorResults error = new ErrorResults("Error: unauthorized");
            res.status(401);
            return serializer.toJson(error);
        } catch (TakenException e) {
            ErrorResults error = new ErrorResults("Error: already taken");
            res.status(403);
            return serializer.toJson(error);
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }

    private Object clear(Request req, Response res) {
        var serializer = new Gson();
        try {
            clearService.clearService(gameList, authList, userList);
            res.body("{}");
            res.status(200);
            return "{}";
        } catch (Exception e) {
            ErrorResults error = new ErrorResults("Error: " + e.getMessage());
            res.status(500);
            return serializer.toJson(error);
        }
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
