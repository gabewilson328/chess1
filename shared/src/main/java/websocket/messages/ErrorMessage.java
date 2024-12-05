package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

public class ErrorMessage extends ServerMessage {

    private String errorMessage;

    public ErrorMessage(String error) {
        super(ServerMessageType.ERROR);
        this.errorMessage = error;
    }

    public String getError() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errorMessage);
    }
}