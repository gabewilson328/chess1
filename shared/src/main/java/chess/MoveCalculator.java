package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    public static Collection<ChessMove> getMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return MoveCalculator.rookMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return MoveCalculator.bishopMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {

        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {

        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return MoveCalculator.rookMoves(piece, board, position) as well as MoveCalculator.bishopMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {

        }
    }

    private static Collection<ChessMove> rookMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving down
        for (int i = position.getRow(); i > 0; i--) {
            if (board.getPiece(new ChessPosition(i, position.getColumn())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getColumn())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
        }
        // Moving up
        for (int i = position.getRow(); i < 7; i++) {
            if (board.getPiece(new ChessPosition(i, position.getColumn())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getColumn())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
        }
        // Moving left
        for (int i = position.getColumn(); i > 0; i--) {
            if (board.getPiece(new ChessPosition(i, position.getRow())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getRow())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
        }
        // Moving right
        for (int i = position.getColumn(); i < 7; i++) {
            if (board.getPiece(new ChessPosition(i, position.getRow())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getRow())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> bishopMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving left and down
        for (int i = position.getRow(); i > 0; i--) {
            int j = i;
            if (board.getPiece(new ChessPosition(i, position.getColumn())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getColumn())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
        }
        // Moving up
        for (int i = position.getRow(); i < 7; i++) {
            if (board.getPiece(new ChessPosition(i, position.getColumn())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getColumn())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getColumn()), null));
        }
        // Moving left
        for (int i = position.getColumn(); i > 0; i--) {
            if (board.getPiece(new ChessPosition(i, position.getRow())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getRow())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
        }
        // Moving right
        for (int i = position.getColumn(); i < 7; i++) {
            if (board.getPiece(new ChessPosition(i, position.getRow())) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, position.getRow())).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(i, position.getRow()), null));
        }

        return possibleMoves;
    }
}
