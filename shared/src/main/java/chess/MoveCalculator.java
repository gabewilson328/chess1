package chess;

import java.util.Collection;
import java.util.ArrayList;

public class MoveCalculator {
    public static Collection<ChessMove> getMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {

        //Might have to put ArrayList<ChessMove> something here
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
            return MoveCalculator.pawnMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            Collection<ChessMove> queenMoves = MoveCalculator.rookMoves(piece, board, position);
            queenMoves.addAll(MoveCalculator.bishopMoves(piece, board, position));
            return queenMoves;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return MoveCalculator.kingMoves(piece, board, position);
        }

        return null;
    }

    private static Collection<ChessMove> rookMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        //Moving down
        for (int i = position.getRow() - 1; i >= 1; i--) {
            if (endPosition(piece, board, position, possibleMoves, i)) break;
        }

        //Moving up
        for (int i = position.getRow() + 1; i <= 8; i++) {
            if (endPosition(piece, board, position, possibleMoves, i)) break;
        }

        //Moving left
        for (int i = position.getColumn() - 1; i >= 1; i--) {
            if (leftEndPosition(piece, board, position, possibleMoves, i)) break;
        }

        //Moving right
        for (int i = position.getColumn() + 1; i <= 8; i++) {
            if (leftEndPosition(piece, board, position, possibleMoves, i)) break;
        }

        return possibleMoves;
    }

    private static boolean leftEndPosition(ChessPiece piece, ChessBoard board, ChessPosition position, ArrayList<ChessMove> possibleMoves, int i) {
        ChessPosition endPosition = new ChessPosition(position.getRow(), i);
        return leftEndPositionTaken(piece, board, position, possibleMoves, endPosition);
    }

    private static boolean leftEndPositionTaken(ChessPiece piece, ChessBoard board, ChessPosition position, ArrayList<ChessMove> possibleMoves, ChessPosition endPosition) {
        if (board.getPiece(endPosition) != null) {
            if (board.getPiece(endPosition).getTeamColor() == piece.getTeamColor()) {
                return true;
            }
            possibleMoves.add(new ChessMove(position, endPosition, null));
            return true;
        }
        possibleMoves.add(new ChessMove(position, endPosition, null));
        return false;
    }

    private static boolean endPosition(ChessPiece piece, ChessBoard board, ChessPosition position, ArrayList<ChessMove> possibleMoves, int i) {
        ChessPosition endPosition = new ChessPosition(i, position.getColumn());
        return leftEndPositionTaken(piece, board, position, possibleMoves, endPosition);
    }

    private static Collection<ChessMove> bishopMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        //Moving left and down
        for (int i = 1; position.getRow() - i >= 1 && position.getColumn() - i >= 1; i++) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
            if (leftDownEndPositionTaken(piece, board, position, possibleMoves, endPosition)) break;
        }

        //Moving right and down
        for (int i = 1; position.getRow() - i >= 1 && position.getColumn() + i <= 8; i++) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
            if (leftDownEndPositionTaken(piece, board, position, possibleMoves, endPosition)) break;
        }

        //Moving right and up
        for (int i = 1; position.getRow() + i <= 8 && position.getColumn() + i <= 8; i++) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
            if (leftDownEndPositionTaken(piece, board, position, possibleMoves, endPosition)) break;
        }

        //Moving left and up
        for (int i = 1; position.getRow() + i <= 8 && position.getColumn() - i >= 1; i++) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
            if (leftDownEndPositionTaken(piece, board, position, possibleMoves, endPosition)) break;
        }

        return possibleMoves;
    }

    private static boolean leftDownEndPositionTaken(ChessPiece piece, ChessBoard board, ChessPosition position, ArrayList<ChessMove> possibleMoves, ChessPosition endPosition) {
        if (board.getPiece(endPosition) != null) {
            if (board.getPiece(endPosition).getTeamColor() == piece.getTeamColor()) {
                return true;
            }
            possibleMoves.add(new ChessMove(position, endPosition, null));
            return true;
        }
        possibleMoves.add(new ChessMove(position, endPosition, null));
        return false;
    }

    private static Collection<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        //Moving left and down
        if (position.getRow() > 1 && position.getColumn() > 2) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() > 2 && position.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Moving right and down
        if (position.getRow() > 1 && position.getColumn() < 7) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() > 2 && position.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Moving left and up
        if (position.getRow() < 8 && position.getColumn() > 2) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() < 7 && position.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Moving right and up
        if (position.getRow() < 8 && position.getColumn() < 7) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() < 7 && position.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> pawnMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        //White
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() < 7) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
                if (board.getPiece(endPosition) == null) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                    //Starting
                    if (position.getRow() == 2 && board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn())) == null) {
                        possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn()), null));
                    }
                }
                //Capture left
                if (position.getColumn() > 1) {
                    ChessPosition diagonalLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (board.getPiece(diagonalLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(position, diagonalLeft, null));
                        }
                    }
                }
                //Capture right
                if (position.getColumn() < 8) {
                    ChessPosition diagonalRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (board.getPiece(diagonalRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(position, diagonalRight, null));
                        }
                    }
                }
            }

            if (position.getRow() == 7) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
                if (board.getPiece(endPosition) == null) {
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                }
                //Capture left
                if (position.getColumn() > 1) {
                    ChessPosition diagonalLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (board.getPiece(diagonalLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.QUEEN));
                        }
                    }
                }
                //Capture right
                if (position.getColumn() < 8) {
                    ChessPosition diagonalRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (board.getPiece(diagonalRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.QUEEN));
                        }
                    }
                }
            }

        }

        //Black
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (position.getRow() > 2) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
                if (board.getPiece(endPosition) == null) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                    //Starting
                    if (position.getRow() == 7) {
                        if (board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn())) == null) {
                            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn()), null));
                        }
                    }
                }
                //Capture left
                if (position.getColumn() > 1) {
                    ChessPosition diagonalLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (board.getPiece(diagonalLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(position, diagonalLeft, null));
                        }
                    }
                }
                //Capture right
                if (position.getColumn() < 8) {
                    ChessPosition diagonalRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (board.getPiece(diagonalRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(position, diagonalRight, null));
                        }
                    }
                }
            }

            if (position.getRow() == 2) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
                if (board.getPiece(endPosition) == null) {
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                }
                //Capture left
                if (position.getColumn() > 1) {
                    ChessPosition diagonalLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (board.getPiece(diagonalLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.QUEEN));
                        }
                    }
                }
                //Capture right
                if (position.getColumn() < 8) {
                    ChessPosition diagonalRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (board.getPiece(diagonalRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.QUEEN));
                        }
                    }
                }
            }

        }

        return possibleMoves;
    }

    private static Collection<ChessMove> kingMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        if (position.getRow() > 1 && position.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() - 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() < 8 && position.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() > 1) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() < 8) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() > 1 && position.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }
        if (position.getRow() < 8 && position.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (board.getPiece(endPosition) != null) {
                if (board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }

        return possibleMoves;
    }
}
