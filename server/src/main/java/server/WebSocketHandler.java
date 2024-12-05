package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLGameDataAccess;
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
    private Playing playing;
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
            String username = authList.getAuth(connectCommand.getAuthToken()).username();
            GameData game = gameList.getGameByID(connectCommand.getGameID());

            if (game.game().getStatus() == ChessGame.Status.INPROGRESS) {

                if (Objects.equals(game.whiteUsername(), username)) {
                    color = ChessGame.TeamColor.WHITE;
                } else if (Objects.equals(game.blackUsername(), username)) {
                    color = ChessGame.TeamColor.BLACK;
                }
                if (Objects.equals(game.whiteUsername(), username) || Objects.equals(game.blackUsername(), username)) {
                    String message = String.format("%s is now playing as %s", username, color);
                    setCurrentUsername(username);
                    var notification = new NotificationMessage(message);
                    connections.broadcast(username, notification);
                } else {
                    String message = String.format("%s is now observing", username);
                    setCurrentUsername(username);
                    var notification = new NotificationMessage(message);
                    connections.broadcast(username, notification);
                }

                try {
                    connections.broadcastToPlayer(username, new LoadMessage(gameList.getGameByID(connectCommand.getGameID()).game()));
                } catch (Exception e) {
                    var error = new ErrorMessage(String.format("An error occurred when %s attempted to join", getCurrentUsername()));
                    connections.broadcast(getCurrentUsername(), error);
                }
            } else {
                System.out.println(String.format("You cannot make a move because the game is over"));
            }
        } catch (DataAccessException e) {
            System.out.println("Couldn't get username");
        }
    }

    private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws IOException {
        try {
            var game = gameList.getGameByID(makeMoveCommand.getGameID()).game();

            if (game.getStatus() == ChessGame.Status.INPROGRESS) {
                game.makeMove(makeMoveCommand.getMove());
                gameList.updateActualGame(makeMoveCommand.getGameID(), game);

                var message = String.format("%s has moved %s to %s", getCurrentUsername(),
                        makeMoveCommand.getMove().getStartPosition(), makeMoveCommand.getMove().getEndPosition());
                var load = new LoadMessage(game);
                var notification = new NotificationMessage(message);

                connections.broadcast(null, load);
                connections.broadcast(getCurrentUsername(), notification);
                if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    connections.broadcast(null, new NotificationMessage(
                            String.format("%s is now in checkmate", getCurrentUsername())));
                } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(null, new NotificationMessage(
                            String.format("%s is now in checkmate", getCurrentUsername())));
                } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                    connections.broadcast(null, new NotificationMessage(
                            String.format("%s is now in check", getCurrentUsername())));
                } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(null, new NotificationMessage(
                            String.format("%s is now in check", getCurrentUsername())));
                } else if (game.isInStalemate(game.getTeamTurn())) {
                    connections.broadcast(null, new NotificationMessage(String.format("Stalemate has been reached")));
                }
            } else {
                System.out.println(String.format("You cannot make a move because the game is over"));
            }
        } catch (InvalidMoveException e) {
            var error = new ErrorMessage(String.format(
                    "An error occurred when %s attempted a move because the move was invalid", getCurrentUsername()));
            connections.broadcastToPlayer(getCurrentUsername(), error);
        } catch (Exception e) {
            var error = new ErrorMessage(String.format("An error occurred when %s attempted a move", getCurrentUsername()));
            connections.broadcastToPlayer(getCurrentUsername(), error);
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
            gameList.getGameByID(resignCommand.getGameID()).game().setStatus(ChessGame.Status.DONE);
        } catch (Exception e) {
            var error = new ErrorMessage(String.format("An error occurred when %s attempted to resign", getCurrentUsername()));
            try {
                connections.broadcast(getCurrentUsername(), error);
            } catch (Exception ex) {
                System.out.println(String.format("Didn't send resign message"));
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
