package websocket.commands;

import chess.ChessGame;
import model.Playing;

public class ConnectCommand extends UserGameCommand {

    private final String username;
    private final Playing status;
    private final ChessGame.TeamColor color;

    public ConnectCommand(String authToken, int gameID, String username, Playing status, ChessGame.TeamColor color) {
        super(CommandType.LEAVE, authToken, gameID);
        this.username = username;
        this.color = color;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public Playing getStatus() {
        return status;
    }
}