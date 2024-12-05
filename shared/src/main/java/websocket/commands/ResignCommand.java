package websocket.commands;

import chess.ChessGame;

import java.util.Objects;


public class ResignCommand extends UserGameCommand {

    public ResignCommand(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}
