package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;

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
            return MoveCalculator.pawnMoves(piece, board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            //Combine Rook and Bishop moves
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
        // Moving down
        for (int i = position.getRow() - 1; i >= 1; i--) {
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
        for (int i = position.getRow() + 1; i <= 8; i++) {
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
        for (int i = position.getColumn() - 1; i >= 1; i--) {
            if (board.getPiece(new ChessPosition(position.getRow(), i)) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(position.getRow(), i)).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), i), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), i), null));
        }
        // Moving right
        for (int i = position.getColumn() + 1; i <= 8; i++) {
            if (board.getPiece(new ChessPosition(position.getRow(), i)) != null) {
                if (piece.getTeamColor() == board.getPiece(new ChessPosition(position.getRow(), i)).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), i), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), i), null));
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> bishopMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving left and down
        for (int i = 1; position.getRow() - i >= 1 && position.getColumn() - i >= 1; i++) {
            if (board.getPiece(new ChessPosition(position.getRow() - i, position.getColumn() - i)) != null) {
                if (piece.getTeamColor() == board.getPiece((new ChessPosition(position.getRow() - i, position.getColumn() - i))).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - i, position.getColumn() - i), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - i, position.getColumn() - i), null));
        }


        // Moving left and up
        for (int i = 1; position.getRow() + i <= 8 && position.getColumn() - i >= 1; i++) {
            if (board.getPiece(new ChessPosition(position.getRow() + i, position.getColumn() - i)) != null) {
                if (piece.getTeamColor() == board.getPiece((new ChessPosition(position.getRow() + i, position.getColumn() - i))).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + i, position.getColumn() - i), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + i, position.getColumn() - i), null));
        }

        // Moving right and up
        for (int i = 1; position.getRow() + i <= 8 && position.getColumn() + i <= 8; i++) {
            if (board.getPiece(new ChessPosition(position.getRow() + i, position.getColumn() + i)) != null) {
                if (piece.getTeamColor() == board.getPiece((new ChessPosition(position.getRow() + i, position.getColumn() + i))).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + i, position.getColumn() + i), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + i, position.getColumn() + i), null));
        }

        // Moving right and down
        for (int i = 1; position.getRow() - i >= 1 && position.getColumn() + i <= 8; i++) {
            if (board.getPiece(new ChessPosition(position.getRow() - i, position.getColumn() + i)) != null) {
                if (piece.getTeamColor() == board.getPiece((new ChessPosition(position.getRow() - i, position.getColumn() + i))).getTeamColor()) {
                    break;
                }
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - i, position.getColumn() + i), null));
                break;
            }
            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - i, position.getColumn() + i), null));
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving down and left
        if (position.getRow() > 2 && position.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn() - 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn() - 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn() - 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn() - 1), null));
            }
        }
        if (position.getRow() > 1 && position.getColumn() > 2) {
            if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 2)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 2)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 2), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 2), null));
            }
        }

        // Moving up and left
        if (position.getRow() < 7 && position.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn() - 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn() - 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn() - 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn() - 1), null));
            }
        }

        if (position.getRow() < 8 && position.getColumn() > 2) {
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
            ChessPiece pieceAt = board.getPiece(endPosition);
            if (pieceAt != null) {
                if (piece.getTeamColor() != pieceAt.getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, endPosition, null));
            }
        }

        // Moving down and right
        if (position.getRow() > 2 && position.getColumn() < 8) {
            if (board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn() + 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn() + 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn() + 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn() + 1), null));
            }
        }
        if (position.getRow() > 1 && position.getColumn() < 7) {
            if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 2)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 2)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 2), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 2), null));
            }
        }

        // Moving up and right
        if (position.getRow() < 7 && position.getColumn() < 8) {
            if (board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn() + 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn() + 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn() + 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn() + 1), null));
            }
        }
        if (position.getRow() < 8 && position.getColumn() < 7) {
            if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 2)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 2)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 2), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 2), null));
            }
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> pawnMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() == 2) {
                if (board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn())) == null && board.getPiece(new ChessPosition((position.getRow()) + 1, position.getColumn())) == null) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn()), null));
                }
            }
            if (position.getRow() < 7) {
                if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())) == null) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), null));
                }
                if (position.getColumn() > 1) {
                    if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)) != null) {
                        if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), null));
                        }
                    }
                }
                if (position.getColumn() < 8) {
                    if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)) != null) {
                        if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), null));
                        }
                    }
                }
            }

            //Pawn promotion
            if (position.getRow() == 7) {
                if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())) == null) {
                    ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                }
                if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)) != null) {
                    ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                    if (board.getPiece(endPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                    }
                }
                if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)) != null) {
                    ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                    if (board.getPiece(endPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (position.getRow() == 7) {
                if (board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn())) == null && board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) == null) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn()), null));
                }
            }
            if (position.getRow() > 2) {
                if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) == null) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), null));
                }
                if (position.getColumn() > 1) {
                    if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)) != null) {
                        if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), null));
                        }
                    }
                }
                if (position.getColumn() < 8) {
                    if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)) != null) {
                        if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), null));
                        }
                    }
                }
            }

            //Pawn promotion
            if (position.getRow() == 2) {
                if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) == null) {
                    ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                }
                if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)) != null) {
                    ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                    if (board.getPiece(endPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                    }
                }
                if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)) != null) {
                    ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                    if (board.getPiece(endPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        }

        return possibleMoves;
    }

    private static Collection<ChessMove> kingMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        // Moving left and down
        if (position.getRow() > 1 && position.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), null));
            }
        }

        // Moving left
        if (position.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() - 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow(), position.getColumn() - 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() - 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() - 1), null));
            }
        }

        // Moving left and up
        if (position.getRow() < 8 && position.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), null));
            }
        }

        // Moving down
        if (position.getRow() > 1) {
            if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), null));
            }
        }

        // Moving up
        if (position.getRow() < 8) {
            if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), null));
            }
        }

        // Moving right and down
        if (position.getRow() > 1 && position.getColumn() < 8) {
            if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), null));
            }
        }

        // Moving right
        if (position.getColumn() < 8) {
            if (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + 1), null));
            }
        }

        // Moving right and up
        if (position.getRow() < 8 && position.getColumn() < 8) {
            if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)) != null) {
                if (piece.getTeamColor() != board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)).getTeamColor()) {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), null));
                }
            } else {
                possibleMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), null));
            }
        }

        return possibleMoves;
    }
}
