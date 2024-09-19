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
            return MoveCalculator.knightMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return MoveCalculator.rookMoves(piece, board, position) + MoveCalculator.bishopMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return MoveCalculator.kingMoves(piece, board, position);
        }
        return null;
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
        outerLoop:
        for (int i = position.getRow(); i > 0; i--) {
            for (int j = position.getColumn(); j > 0; j--) {
                if ((position.getColumn() - position.getRow()) == j - i) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, j)).getTeamColor()) {
                            break outerLoop;
                        }
                        possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                        break outerLoop;
                    }
                    possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
            }
        }

        // Moving left and up
        outerLoop:
        for (int i = position.getRow(); i <= 8; i++) {
            for (int j = position.getColumn(); j > 0; j--) {
                if ((position.getColumn() - position.getRow()) == j - i) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, j)).getTeamColor()) {
                            break outerLoop;
                        }
                        possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                        break outerLoop;
                    }
                    possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
            }
        }

        // Moving right and up
        outerLoop:
        for (int i = position.getRow(); i <= 8; i++) {
            for (int j = position.getColumn(); j <= 8; j++) {
                if ((position.getColumn() - position.getRow()) == j - i) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, j)).getTeamColor()) {
                            break outerLoop;
                        }
                        possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                        break outerLoop;
                    }
                    possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
            }
        }

        // Moving right and down
        outerLoop:
        for (int i = position.getRow(); i > 0; i--) {
            for (int j = position.getColumn(); j <= 8; j++) {
                if ((position.getColumn() - position.getRow()) == j - i) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (piece.getTeamColor() == board.getPiece(new ChessPosition(i, j)).getTeamColor()) {
                            break outerLoop;
                        }
                        possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                        break outerLoop;
                    }
                    possibleMoves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
            }
        }







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

    private static Collection<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving down and left
        if (position.getRow() > 2 && position.getColumn() > 1) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn() - 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() - 2, position.getColumn() - 1), null));
            }
        }
        if (position.getRow() > 1 && position.getColumn() > 2) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 2)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() - 1, position.getColumn() - 2), null));
            }
        }

        // Moving up and left
        if (position.getRow() < 7 && position.getColumn() > 1) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn() - 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() + 2, position.getColumn() - 1), null));
            }
        }
        if (position.getRow() < 8 && position.getColumn() > 2) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 2)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() + 1, position.getColumn() - 2), null));
            }
        }

        // Moving down and right
        if (position.getRow() > 2 && position.getColumn() < 8) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn() + 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() - 2, position.getColumn() + 1), null));
            }
        }
        if (position.getRow() > 1 && position.getColumn() < 7) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 2)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() - 1, position.getColumn() + 2), null));
            }
        }

        // Moving up and right
        if (position.getRow() < 7 && position.getColumn() < 8) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn() + 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() + 2, position.getColumn() + 1), null));
            }
        }
        if (position.getRow() < 8 && position.getColumn() < 7) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 2)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getColumn() + 1, position.getColumn() + 2), null));
            }
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> kingMoves(ChessPiece piece, ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving left and down
        if (position.getRow() > 1 && position.getColumn() > 1) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), null));
            }
        }

        // Moving left
        if (position.getColumn() > 1) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow(), position.getColumn() - 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() - 1), null));
            }
        }

        // Moving left and up
        if (position.getRow() < 8 && position.getColumn() > 1) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), null));
            }
        }

        // Moving down
        if (position.getRow() > 1) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), null));
            }
        }

        // Moving up
        if (position.getRow() < 8) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), null));
            }
        }

        // Moving right and down
        if (position.getRow() > 1 && position.getColumn() < 8) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), null));
            }
        }

        // Moving right
        if (position.getColumn() < 8) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + 1), null));
            }
        }

        // Moving right and up
        if (position.getRow() < 8 && position.getColumn() < 8) {
            if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)).getTeamColor()) {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), null));
            }
        }

        return possibleMoves;
    }
}
