package websocket.commands;

import chess.ChessGame;
import model.Playing;

public class ConnectCommand extends UserGameCommand {

    public ConnectCommand(String authToken, int gameID, String username, Playing status, ChessGame.TeamColor color) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}