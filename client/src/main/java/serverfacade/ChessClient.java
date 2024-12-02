package serverfacade;

import java.util.Arrays;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLGameDataAccess;
import model.GameData;
import request.*;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;
import websocket.ServerMessageHandler;
import websocket.WebSocketFacade;

public class ChessClient {
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;
    private final String serverUrl;
    private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
    private Playing playing = Playing.NOTPLAYING;
    private String currentAuth = null;
    private ChessGame currentGame;
    private int currentGameID;
    private ChessGame.TeamColor currentColor;
    private SQLAuthDataAccess authList;
    private SQLGameDataAccess gameList;

    public ChessClient(String serverUrl, ServerMessageHandler serverMessageHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.serverMessageHandler = serverMessageHandler;
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
            } else if (playing == Playing.PLAYING) {
                return switch (cmd) {
                    case "redraw" -> redrawBoard();
                    case "highlight" -> highlightMoves();
                    case "move" -> makeMove(params);
                    case "resign" -> resign();
                    case "leave" -> leave();
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
            RegisterResult registerResult = server.register(new RegisterRequest(params[0], params[1], params[2]));
            setUserAuth(registerResult.authToken());
            state = State.SIGNEDIN;
            return String.format("Logged in as %s", params[0]);
        }
        return "Expected: <username>, <password>, <email>";
    }

    public String login(String... params) {
        if (params.length == 2) {
            try {
                LoginResult loginResult = server.login(new LoginRequest(params[0], params[1]));
                setUserAuth(loginResult.authToken());
                state = State.SIGNEDIN;
                return String.format("Logged in as %s", params[0]);
            } catch (ResponseException e) {
                return String.format("Incorrect username or password");
            }
        }
        return "Expected: <username> <password>";
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            CreateGameResult createGameResult = server.createGame(new CreateGameRequest(getUserAuth(), params[0]));
            return String.format("Game ID: %s", createGameResult.gameID());
        }
        return "Expected: <gameName>";
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(new ListGamesRequest(getUserAuth()));
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game.gameID() + ". " + game.gameName() + "  WHITE: "
                    + game.whiteUsername() + "  BLACK: " + game.blackUsername())).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) {
        assertSignedIn();
        if (params.length == 2) {
            try {
                var id = Integer.parseInt(params[0]);
                WebSocketFacade wsfacade = new WebSocketFacade(serverUrl, serverMessageHandler);
                if (params[1] != null) {
                    if (params[1].equalsIgnoreCase("WHITE")) {
                        currentColor = ChessGame.TeamColor.WHITE;
                        server.joinGame(new JoinGameRequest(getUserAuth(), currentColor, id));
                        wsfacade.connect(getUserAuth(), );
                        PrintBoard.printBoard(stuff from ws class);
                        return String.format("You have joined the game");
                    } else if (params[1].equalsIgnoreCase("BLACK")) {
                        currentColor = ChessGame.TeamColor.BLACK;
                        server.joinGame(new JoinGameRequest(getUserAuth(), currentColor, id));
                        PrintBoard.printBoard(new ChessGame());
                        return String.format("You have joined the game");
                    } else {
                        return String.format("Please choose WHITE or BLACK");
                    }

                }
            } catch (NumberFormatException e) {
                return String.format("Invalid game ID");
            } catch (ResponseException e) {
                return String.format("Unsuccessful joining game");
            }
        }
        return "";
    }

    public String observeGame(String... params) {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                //server.joinGame(new JoinGameRequest(getUserAuth(), null, id));
                PrintBoard.printBoard(new ChessGame());
            } catch (NumberFormatException e) {
                return String.format("Invalid game ID");
            }
        }
        return String.format("");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout(new LogoutRequest(getUserAuth()));
        return String.format("Logged out");
    }

    private String redrawBoard() {
        PrintBoard.printBoard(currentGame);
        return String.format("");
    }

    private String highlightMoves(String... params) {
        if (params.length == 1) {
            try {
                String[] square = params[0].split("");
                square[0] = square[0].toLowerCase();
                int row = Integer.parseInt(square[1]);
                int col = switch (square[0]) {
                    case "a" -> 1;
                    case "b" -> 2;
                    case "c" -> 3;
                    case "d" -> 4;
                    case "e" -> 5;
                    case "f" -> 6;
                    case "g" -> 7;
                    case "h" -> 8;
                    default -> 0;
                };

                ChessPosition position = new ChessPosition(row, col);
                PrintBoard.printHighlightedBoard(currentGame, currentGame.getBoard().getPiece(position), position);
                return String.format("");
            } catch (Exception e) {
                return String.format("Invalid square");
            }
        }
        return String.format("Invalid square");
    }

    private String makeMove(String[] params) {
        if (params.length == 2) {
            try {
                String[] startPosition = params[0].split("");
                startPosition[0] = startPosition[0].toLowerCase();
                int startRow = Integer.parseInt(startPosition[1]);
                int startCol = getColumn(startPosition[0]);

                String[] endPosition = params[1].split("");
                endPosition[0] = endPosition[0].toLowerCase();
                int endRow = Integer.parseInt(startPosition[1]);
                int endCol = getColumn(endPosition[0]);

                currentGame.makeMove(new ChessMove(
                        new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), null));
                ws = new WebSocketFacade(serverUrl, serverMessageHandler);
                ws.makeMove(getUserAuth(), object of type ChessMove);
            } catch (Exception e) {
                return String.format("Move could not be made");
            }
        } else if (params.length == 3) {
            params[2] = params[2].toLowerCase();
            try {
                ChessPiece.PieceType promotionPiece = switch (params[2]) {
                    case "queen" -> ChessPiece.PieceType.QUEEN;
                    case "rook", "castle" -> ChessPiece.PieceType.ROOK;
                    case "bishop" -> ChessPiece.PieceType.BISHOP;
                    case "knight" -> ChessPiece.PieceType.KNIGHT;
                    default -> null;
                };
                if (promotionPiece == null) {
                    return String.format("Promotion piece invalid");
                }
                String[] startPosition = params[0].split("");
                startPosition[0] = startPosition[0].toLowerCase();
                int startRow = Integer.parseInt(startPosition[1]);
                int startCol = getColumn(startPosition[0]);

                String[] endPosition = params[1].split("");
                endPosition[0] = endPosition[0].toLowerCase();
                int endRow = Integer.parseInt(startPosition[1]);
                int endCol = getColumn(endPosition[0]);

                currentGame.makeMove(new ChessMove(
                        new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), promotionPiece));
                ws = new WebSocketFacade(serverUrl, serverMessageHandler);
                ws.makeMove(getUserAuth(), move);
            } catch (Exception e) {
                return String.format("Move could not be made");
            }
        }
        return String.format("Invalid move");
    }

    private String resign() {

    }

    private String leave() {
        try {
            Where should I get the game from in order to call updateGame? Can I declare a GameData here in the client when a game is created and then update it in tandem with the real game?
            GameData gameBeingPlayed = gameList.getGameByID();
            Send something to the websocketfacade to update the game so that the username of that color is null;
            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
            ws.leave(getUserAuth(), );
            playing = Playing.NOTPLAYING;
            return String.format("%s has left the game", authList.getAuth(currentAuth).username());
        } catch (Exception e) {
            return String.format("Could not leave game");
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        } else if (playing == Playing.PLAYING) {
            return """
                    redraw - the board
                    highlight - legal moves
                    move - a piece
                    resign - the game
                    leave - the game
                    help - with possible commands
                    """;
        } else {
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
    }

    private int getColumn(String column) {
        int col = switch (column) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> 0;
        };
        return col;
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

    private void setCurrentGame(ChessGame game) {
        currentGame = game;
    }

    private void setCurrentGameID(int gameID) {
        currentGameID = gameID;
    }

    private int getCurrentGameID() {
        return currentGameID;2
    }
}