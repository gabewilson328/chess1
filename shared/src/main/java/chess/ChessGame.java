package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {

    TeamColor turn = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> okMoves = new ArrayList<ChessMove>();
        ChessBoard fakeBoard;
        ChessPiece myPiece = board.getPiece(startPosition);
        if (myPiece == null) {
            return null;
        }
        okMoves.addAll(myPiece.pieceMoves(board, startPosition));
        for (ChessMove aMove : okMoves) {
            try {
                fakeBoard = (ChessBoard) board.clone();
                //make move on fakeBoard by deleting piece and adding it too endPosition
                ChessPosition endPosition = aMove.getEndPosition();
                fakeBoard.addPiece(endPosition, myPiece);
                fakeBoard.addPiece(startPosition, null);

                if (isInCheck(myPiece.getTeamColor(), fakeBoard)) {
                    okMoves.remove(aMove);
                }
            } catch (CloneNotSupportedException e) {
            }

        }
        return okMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        board.addPiece(endPosition, board.getPiece(startPosition));
        board.addPiece(startPosition, null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> enemyMoves = new ArrayList<ChessMove>();
        ChessPosition kingPosition = null;
        //find position of my king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentSquare = new ChessPosition(i, j);
                if (board.getPiece(currentSquare) != null) {
                    if (board.getPiece(currentSquare).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currentSquare).getTeamColor() == teamColor) {
                        kingPosition = new ChessPosition(i, j);
                    }
                    if (board.getPiece(currentSquare).getTeamColor() == TeamColor.BLACK) {
                        enemyMoves.addAll(board.getPiece(currentSquare).pieceMoves(board, currentSquare));
                    }
                }
            }
        }
        //iterate through possible moves of all of enemy's pieces and look at endPosition of those pieces
        for (ChessMove aMove : enemyMoves) {
            if (aMove.getEndPosition() == kingPosition) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard fakeBoard) {
        Collection<ChessMove> enemyMoves = new ArrayList<ChessMove>();
        ChessPosition kingPosition = null;
        //find position of my king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentSquare = new ChessPosition(i, j);
                if (fakeBoard.getPiece(currentSquare) != null) {
                    if (fakeBoard.getPiece(currentSquare).getPieceType() == ChessPiece.PieceType.KING && fakeBoard.getPiece(currentSquare).getTeamColor() == teamColor) {
                        kingPosition = new ChessPosition(i, j);
                    }
                    if (fakeBoard.getPiece(currentSquare).getTeamColor() != teamColor) {
                        enemyMoves.addAll(fakeBoard.getPiece(currentSquare).pieceMoves(fakeBoard, currentSquare));
                    }
                }
            }
        }

        //iterate through possible moves of all of enemy's pieces and look at endPosition of those pieces
        for (ChessMove aMove : enemyMoves) {
            if (aMove.getEndPosition() == kingPosition) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> allValidMoves = new ArrayList<ChessMove>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentSquare = new ChessPosition(i, j);
                if (board.getPiece(currentSquare).getTeamColor() == teamColor) {
                    allValidMoves.addAll(validMoves(currentSquare));
                }
            }
        }
        if (isInCheck(teamColor) && allValidMoves.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allValidMoves = new ArrayList<ChessMove>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentSquare = new ChessPosition(i, j);
                if (board.getPiece(currentSquare).getTeamColor() == teamColor) {
                    allValidMoves.addAll(validMoves(currentSquare));
                }
            }
        }
        if (!isInCheck(teamColor) && allValidMoves.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
