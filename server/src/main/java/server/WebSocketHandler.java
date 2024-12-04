package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.SQLGameDataAccess;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.*;
import websocket.messages.*;
import model.Playing;

import java.io.IOException;


public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private String currentUsername;
    private GameStatus gameStatus;
    private SQLGameDataAccess gameList;

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
        connections.add(connectCommand.getUsername(), session);
        if (gameStatus == GameStatus.INPROGRESS) {
            if (connectCommand.getStatus() == Playing.PLAYING) {
                String message = String.format("%s is now playing as %s", connectCommand.getUsername(), connectCommand.getColor());
                setCurrentUsername(connectCommand.getUsername());
                var notification = new NotificationMessage(message);
                connections.broadcast(connectCommand.getUsername(), notification);
            } else if (connectCommand.getStatus() == Playing.OBSERVING) {
                String message = String.format("%s is now observing", connectCommand.getUsername());
                setCurrentUsername(connectCommand.getUsername());
                var notification = new NotificationMessage(message);
                connections.broadcast(connectCommand.getUsername(), notification);
            }
            try {
                connections.broadcastToPlayer(connectCommand.getUsername(), new LoadMessage(gameList.getGameByID(connectCommand.getGameID()).game()));
            } catch (Exception e) {
                var error = new ErrorMessage(String.format("An error occurred when %s attempted to join", getCurrentUsername()));
                connections.broadcast(getCurrentUsername(), error);
            }
        } else {
            System.out.println(String.format("You cannot make a move because the game is over"));
        }
    }

    private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws IOException {
        try {
            if (gameStatus == GameStatus.INPROGRESS) {
                var message = String.format("%s has moved %s to %s", getCurrentUsername(),
                        makeMoveCommand.getMove().getStartPosition(), makeMoveCommand.getMove().getEndPosition());
                var load = new LoadMessage(gameList.getGameByID(makeMoveCommand.getGameID()).game());
                var notification = new NotificationMessage(message);

                call ChessGame.makeMove   Can that just be in the client?
                        call data access, update in database
                connections.broadcast(null, load);
                connections.broadcast(getCurrentUsername(), notification);
                var game = gameList.getGameByID(makeMoveCommand.getGameID()).game();
                if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    connections.broadcast(null, new NotificationMessage(String.format("%s is now in checkmate", getCurrentUsername())));
                } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(null, new NotificationMessage(String.format("%s is now in checkmate", getCurrentUsername())));
                } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                    connections.broadcast(null, new NotificationMessage(String.format("%s is now in check", getCurrentUsername())));
                } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(null, new NotificationMessage(String.format("%s is now in check", getCurrentUsername())));
                } else if (game.allValidMoves(ChessGame.TeamColor.WHITE).isEmpty() && game.allValidMoves(ChessGame.TeamColor.BLACK).isEmpty()) {
                    connections.broadcast(null, new NotificationMessage(String.format("Stalemate has been reached")));
                }
            } else {
                System.out.println(String.format("You cannot make a move because the game is over"));
            }
        } catch (Exception e) {
            var error = new ErrorMessage(String.format("An error occurred when %s attempted a move", getCurrentUsername()));
            connections.broadcast(getCurrentUsername(), error);
        }
    }

    private void leave(LeaveCommand leaveCommand) throws IOException {
        try {
            connections.remove(getCurrentUsername());
            var message = String.format("%s left the game", getCurrentUsername());
            var notification = new NotificationMessage(message);
            connections.broadcast(getCurrentUsername(), notification);
            setCurrentUsername(null);
        } catch (Exception e) {
            var error = new ErrorMessage(String.format("An error occurred when %s attempted to leave", getCurrentUsername()));
            connections.broadcast(getCurrentUsername(), error);
        }
    }

    public void resign(ResignCommand resignCommand) {
        try {
            var message = String.format("%s has resigned. The game is now over", getCurrentUsername());
            var notification = new NotificationMessage(message);
            connections.broadcast(getCurrentUsername(), notification);
            gameStatus = GameStatus.ENDED;
        } catch (Exception e) {
            var error = new ErrorMessage(String.format("An error occurred when %s attempted to resign", getCurrentUsername()));
            try {
                connections.broadcast(getCurrentUsername(), error);
            } catch (Exception ex) {
                System.out.println(String.format("That didn't work"));
            }
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
