package websocket.commands;

import chess.ChessMove;

import java.util.Objects;


public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MakeMoveCommand)) {
            return false;
        }
        MakeMoveCommand that = (MakeMoveCommand) o;
        return getMove() == that.getMove();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMove());
    }
}
