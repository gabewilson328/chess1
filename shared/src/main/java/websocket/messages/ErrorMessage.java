package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

public class ErrorMessage extends ServerMessage {

    private String error;

    public ErrorMessage(String error) {
        super(ServerMessageType.ERROR);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), error);
    }
}