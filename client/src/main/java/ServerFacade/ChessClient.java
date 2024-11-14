package ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;

import chess.ChessGame;
import com.google.gson.Gson;
import request.*;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private String currentAuth = null;
    ArrayList<Integer> gameIDs = new ArrayList<Integer>();

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logout();
                    case "quit" -> "quit";
                    default -> help();
                };
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            state = State.SIGNEDIN;
            RegisterResult registerResult = server.register(new RegisterRequest(params[0], params[1], params[2]));
            setUserAuth(registerResult.authToken());
            return String.format("Logged in as %s", params[0]);
        }
        return "Expected: <username>, <password>, <email>";
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            state = State.SIGNEDIN;
            LoginResult loginResult = server.login(new LoginRequest(params[0], params[1]));
            setUserAuth(loginResult.authToken());
            return String.format("Logged in as %s", params[0]);
        }
        return "Expected: <username> <password>";
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            CreateGameResult createGameResult = server.createGame(new CreateGameRequest(getUserAuth(), params[0]));
            setGameNumber(createGameResult.gameID());
            return String.format("Game Number: %s", createGameResult.gameID());
        }
        return "Expected: <gameName>";
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(new ListGamesRequest(getUserAuth()));
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) {
        assertSignedIn();
        if (params.length == 2) {
            try {
                var id = Integer.parseInt(params[0]);
                ChessGame.TeamColor color = null;
                if (params[1] != null) {
                    if (params[1].equals("WHITE")) {
                        color = ChessGame.TeamColor.WHITE;
                    } else if (params[1].equals("BLACK")) {
                        color = ChessGame.TeamColor.BLACK;
                    }
                    server.joinGame(new JoinGameRequest(getUserAuth(), color, id));
                    PrintBoard.printBoard(new ChessGame());
                    return String.format("You have joined the game");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid game ID");
            } catch (ResponseException e) {
                System.out.print("Unsuccessful joining game");
            }
        }
        return null;
    }

    public String observeGame(String... params) {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                //server.joinGame(new JoinGameRequest(getUserAuth(), null, id));
                PrintBoard.printBoard(new ChessGame());
            } catch (NumberFormatException e) {
                System.out.println("Invalid game ID");
            }
        }
        return null;
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout(new LogoutRequest(getUserAuth()));
        return String.format("Logged out");
    }

    public String clear() throws ResponseException {
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() {
        if (state == State.SIGNEDOUT) {
            System.out.print("You must sign in");
        }
    }

    private void setUserAuth(String authToken) throws ResponseException {
        currentAuth = authToken;
    }

    private String getUserAuth() {
        return currentAuth;
    }

    private void setGameNumber(int gameID) {
        gameIDs.add(gameID);
    }
}