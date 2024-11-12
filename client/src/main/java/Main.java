import chess.*;
import ServerFacade.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        new REPL("http://localhost:8080").run();
        PrintBoard.printBoard(new ChessGame());
    }
}