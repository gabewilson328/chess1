package websocket.commands;

import chess.ChessGame;

import java.util.Objects;


public class LeaveCommand extends UserGameCommand {

    private final ChessGame.TeamColor color;

    public LeaveCommand(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.LEAVE, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveCommand)) {
            return false;
        }
        LeaveCommand that = (LeaveCommand) o;
        return getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor());
    }
}
