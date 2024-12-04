package serverfacade;

import java.util.Arrays;
import java.util.Scanner;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLGameDataAccess;
import model.GameData;
import model.Playing;
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
    private String currentUsername;
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
            } else if (playing == Playing.OBSERVING) {
                return switch (cmd) {
                    case "redraw" -> redrawBoard();
                    case "highlight" -> highlightMoves();
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
            setCurrentUsername(registerResult.username());
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
                setCurrentUsername(loginResult.username());
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
                        setCurrentColor(ChessGame.TeamColor.WHITE);
                        server.joinGame(new JoinGameRequest(getUserAuth(), getCurrentColor(), id));
                        wsfacade.connect(getUserAuth(), id, getCurrentUsername(), playing.PLAYING, getCurrentColor());
                        setCurrentGameID(id);
                        return String.format("You have joined the game");
                    } else if (params[1].equalsIgnoreCase("BLACK")) {
                        setCurrentColor(ChessGame.TeamColor.BLACK);
                        server.joinGame(new JoinGameRequest(getUserAuth(), getCurrentColor(), id));
                        playing = Playing.PLAYING;
                        wsfacade.connect(getUserAuth(), id, getCurrentUsername(), playing.PLAYING, getCurrentColor());
                        setCurrentGameID(id);
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
        return String.format("");
    }

    public String observeGame(String... params) {
        assertSignedIn();
        try {
            if (params.length == 1) {
                WebSocketFacade wsfacade = new WebSocketFacade(serverUrl, serverMessageHandler);
                try {
                    var id = Integer.parseInt(params[0]);
                    wsfacade.connect(getUserAuth(), id, getCurrentUsername(), playing.OBSERVING, getCurrentColor());
                    setCurrentGameID(id);
                    playing = Playing.OBSERVING;
                } catch (NumberFormatException e) {
                    return String.format("Invalid game ID");
                }
            }
        } catch (ResponseException e) {
            return String.format("An error occurred");
        }
        return String.format("");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout(new LogoutRequest(getUserAuth()));
        setCurrentUsername(null);
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
                int row = Integer.parseInt(square[1]);
                int col = getColumn(square[0].toLowerCase());

                ChessPosition position = new ChessPosition(row, col);
                if (currentGame.getBoard().getPiece(position).getTeamColor() == getCurrentColor()) {
                    PrintBoard.printHighlightedBoard(currentGame, currentGame.getBoard().getPiece(position), position);
                    return String.format("");
                } else {
                    return String.format("You must choose a piece of your color");
                }
            } catch (Exception e) {
                return String.format("Invalid square");
            }
        }
        return String.format("You must choose a piece");
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

                ChessMove move = new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), null);
                currentGame.makeMove(move); is it okay to do this here or does it have to be in the websocket facade?
                gameList.updateActualGame(getCurrentGameID(), currentGame);
                ws = new WebSocketFacade(serverUrl, serverMessageHandler);
                ws.makeMove(getUserAuth(), getCurrentGameID(), move);

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

                ChessMove move = new ChessMove(
                        new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), promotionPiece);
                currentGame.makeMove(move);
                gameList.updateActualGame(getCurrentGameID(), currentGame);
                ws = new WebSocketFacade(serverUrl, serverMessageHandler);
                ws.makeMove(getUserAuth(), getCurrentGameID(), move);
            } catch (Exception e) {
                return String.format("Move could not be made");
            }
        }
        return String.format("Invalid move");
    }

    private String resign() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(String.format("Are you sure? [YES|NO]"));
        String line = scanner.nextLine();
        scanner.close();
        if (line.equalsIgnoreCase("YES")) {
            try {
                ws = new WebSocketFacade(serverUrl, serverMessageHandler);
                ws.resign(getUserAuth(), getCurrentGameID(), getCurrentColor());
                gameList.updateGameName(getCurrentGameID(), gameList.getGameByID(getCurrentGameID()).gameName() + " - DONE");
                return String.format("You have resigned");
            } catch (Exception e) {
                return String.format("Unsuccessful resigning");
            }
        } else {
            return String.format("Make sure to be certain if you want to resign");
        }
    }

    private String leave() {
        try {
            GameData gameBeingPlayed = gameList.getGameByID(getCurrentGameID());
            gameList.updateGame(gameBeingPlayed, getCurrentColor(), null);
            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
            ws.leave(getUserAuth(), getCurrentGameID(), getCurrentColor());
            playing = Playing.NOTPLAYING;
            return String.format("");
        } catch (Exception e) {
            return String.format("Could not leave game");
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - create an account
                    login <USERNAME> <PASSWORD> - play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        } else if (playing == Playing.PLAYING) {
            return """
                    redraw - the board
                    highlight <PIECE_POSITION> - see legal moves
                    move <CURRENT_POSITION> <NEW_POSITION> - move a piece
                    move <CURRENT_POSITION> <NEW_POSITION> <PROMOTION_PIECE> - promote a pawn
                    resign - the game
                    leave - the game
                    help - with possible commands
                    """;
        } else if (playing == Playing.OBSERVING) {
            return """
                    redraw - the board
                    highlight <PIECE_POSITION> - see legal moves
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
        return currentGameID;
    }

    private void setCurrentColor(ChessGame.TeamColor color) {
        currentColor = color;
    }

    private ChessGame.TeamColor getCurrentColor() {
        return currentColor;
    }

    private void setCurrentUsername(String username) {
        currentUsername = username;
    }

    private String getCurrentUsername() {
        return currentUsername;
    }
}