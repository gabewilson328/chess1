package server;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLGameDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;
import model.Playing;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private String currentUsername;
    private SQLGameDataAccess gameList;
    private SQLAuthDataAccess authList;
    private ChessGame.TeamColor color;

    public WebSocketHandler(SQLAuthDataAccess authList, SQLGameDataAccess gameList) {
        this.gameList = gameList;
        this.authList = authList;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(new Gson().fromJson(message, ConnectCommand.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMoveCommand.class), session);
            case LEAVE -> leave(new Gson().fromJson(message, LeaveCommand.class));
            case RESIGN -> resign(new Gson().fromJson(message, ResignCommand.class));
        }
    }

    private void connect(ConnectCommand connectCommand, Session session) throws IOException {
        try {
            AuthData authData = authList.getAuth(connectCommand.getAuthToken());
            if (authData != null) {
                String username = authData.username();
                int gameID = connectCommand.getGameID();
                GameData game = gameList.getGameByID(gameID);

                if (game.game().getStatus() == ChessGame.Status.INPROGRESS) {
                    connections.add(username, gameID, session);
                    if (Objects.equals(game.whiteUsername(), username)) {
                        color = ChessGame.TeamColor.WHITE;
                    } else if (Objects.equals(game.blackUsername(), username)) {
                        color = ChessGame.TeamColor.BLACK;
                    }
                    if (Objects.equals(game.whiteUsername(), username) || Objects.equals(game.blackUsername(), username)) {
                        String message = String.format("%s is now playing as %s", username, color);
                        var notification = new NotificationMessage(message);
                        connections.broadcast(username, gameID, notification);
                    } else {
                        String message = String.format("%s is now observing", username);
                        var notification = new NotificationMessage(message);
                        connections.broadcast(username, gameID, notification);
                    }

                    try {
                        connections.sendToPlayer(username, gameID, new LoadMessage(gameList.getGameByID(gameID).game()));
                    } catch (Exception e) {
                        var error = new ErrorMessage(String.format("An error occurred when %s attempted to join", username));
                        connections.broadcast(username, gameID, error);
                    }
                } else {
                    connections.sendToPlayer(username, gameID,
                            new ErrorMessage(String.format("You cannot make a move because the game is over")));
                }
            } else {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(String.format("Bad auth token"))));
            }

        } catch (DataAccessException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(String.format("Couldn't get username"))));
        }
    }

    private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws IOException {
        int gameID = makeMoveCommand.getGameID();
        try {
            AuthData authData = authList.getAuth(makeMoveCommand.getAuthToken());
            if (authData != null) {
                String username = authData.username();
                try {
                    var game = gameList.getGameByID(makeMoveCommand.getGameID()).game();
                    if (game.getStatus() == ChessGame.Status.INPROGRESS) {
                        ChessPiece piece = game.getBoard().getPiece(makeMoveCommand.getMove().getStartPosition());
                        if (Objects.equals(gameList.getGameByID(makeMoveCommand.getGameID()).whiteUsername(), username)) {
                            color = ChessGame.TeamColor.WHITE;
                        } else if (Objects.equals(gameList.getGameByID(makeMoveCommand.getGameID()).blackUsername(), username)) {
                            color = ChessGame.TeamColor.BLACK;
                        }
                        if (piece.getTeamColor() == color) {
                            game.makeMove(makeMoveCommand.getMove());
                            gameList.updateActualGame(gameID, game);

                            var message = String.format("%s has moved %s to %s", username,
                                    makeMoveCommand.getMove().getStartPosition(), makeMoveCommand.getMove().getEndPosition());
                            var load = new LoadMessage(game);
                            var notification = new NotificationMessage(message);

                            connections.broadcast(null, gameID, load);
                            connections.broadcast(username, gameID, notification);
                        } else {
                            var error = new ErrorMessage(String.format(
                                    "You can't make a move for the other team"));
                            connections.sendToPlayer(username, gameID, error);
                        }

                        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                            connections.broadcast(null, gameID, new NotificationMessage(
                                    String.format("%s is now in checkmate", username)));
                        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                            connections.broadcast(null, gameID, new NotificationMessage(
                                    String.format("%s is now in checkmate", username)));
                        } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                            connections.broadcast(null, gameID, new NotificationMessage(
                                    String.format("%s is now in check", username)));
                        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                            connections.broadcast(null, gameID, new NotificationMessage(
                                    String.format("%s is now in check", username)));
                        } else if (game.isInStalemate(game.getTeamTurn())) {
                            connections.broadcast(null, gameID, new NotificationMessage(
                                    String.format("Stalemate has been reached")));
                        }
                    } else {
                        System.out.println(String.format("You cannot make a move because the game is over"));
                    }
                } catch (InvalidMoveException e) {
                    var error = new ErrorMessage(String.format(
                            "An error occurred when %s attempted a move because the move was invalid", username));
                    connections.sendToPlayer(username, gameID, error);
                } catch (Exception e) {
                    var error = new ErrorMessage(String.format("An error occurred when %s attempted a move", username));
                    connections.sendToPlayer(username, gameID, error);
                }
            } else {
                System.out.println(String.format("Bad auth token"));
                //try {
                //var error = new ErrorMessage(String.format(
                //"An error occurred: bad auth token"));
                //connections.sendToPlayer(username, gameID, error);
                //} catch () {

                //}
            }
        } catch (DataAccessException e) {
            System.out.println("Die");
        }
    }

    private void leave(LeaveCommand leaveCommand) throws IOException {
        int gameID = leaveCommand.getGameID();
        try {
            String username = authList.getAuth(leaveCommand.getAuthToken()).username();
            try {
                connections.remove(username, gameID);
                var message = String.format("%s left the game", username);
                var notification = new NotificationMessage(message);
                connections.broadcast(username, gameID, notification);
                setCurrentUsername(null);
                GameData game = gameList.getGameByID(gameID);
                if (Objects.equals(game.whiteUsername(), username)) {
                    color = ChessGame.TeamColor.WHITE;
                } else if (Objects.equals(game.blackUsername(), username)) {
                    color = ChessGame.TeamColor.BLACK;
                }
                gameList.updateGame(game, color, null);
            } catch (Exception e) {
                var error = new ErrorMessage(String.format("An error occurred when you attempted to leave"));
                connections.sendToPlayer(username, gameID, error);
            }
        } catch (DataAccessException e) {
            System.out.println("Die");
        }
    }

    public void resign(ResignCommand resignCommand) {
        int gameID = resignCommand.getGameID();
        try {
            String username = authList.getAuth(resignCommand.getAuthToken()).username();
            try {
                var message = String.format("%s has resigned. The game is now over", username);
                var notification = new NotificationMessage(message);
                connections.broadcast(username, gameID, notification);
                ChessGame game = gameList.getGameByID(gameID).game();
                game.setStatus(ChessGame.Status.DONE);
                gameList.updateActualGame(gameID, game);
                gameList.updateGameName(gameID, gameList.getGameByID(gameID).gameName() + " - DONE");
            } catch (Exception e) {
                var error = new ErrorMessage(String.format("An error occurred when %s attempted to resign", username));
                try {
                    connections.broadcast(username, gameID, error);
                } catch (Exception ex) {
                    System.out.println(String.format("Didn't send resign message"));
                }
            }
        } catch (DataAccessException e) {
            System.out.println("Die");
        }
    }

    public void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public enum GameStatus {
        INPROGRESS,
        ENDED
    }
}
