package websocket.commands;

import chess.ChessGame;

import java.util.Objects;


public class ResignCommand extends UserGameCommand {

    private final ChessGame.TeamColor color;

    public ResignCommand(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.RESIGN, authToken, gameID);
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
        if (!(o instanceof ResignCommand)) {
            return false;
        }
        ResignCommand that = (ResignCommand) o;
        return getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor());
    }
}
