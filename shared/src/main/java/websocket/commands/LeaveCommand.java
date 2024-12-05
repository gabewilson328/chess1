package websocket.commands;

import chess.ChessGame;

import java.util.Objects;


public class LeaveCommand extends UserGameCommand {

    public LeaveCommand(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}
