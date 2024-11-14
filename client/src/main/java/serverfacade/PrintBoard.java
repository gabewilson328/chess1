package serverfacade;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class PrintBoard {
    public static void printBoard(ChessGame game) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawChessBoardFromWhite(out, game);
        out.print(RESET_BG_COLOR);
        out.println();
        out.println();

        drawChessBoardFromBlack(out, game);
        out.print(RESET_BG_COLOR);
        out.println();
        out.println();
    }

    private static void drawHeaders(PrintStream out, String[] headers) {
        for (int boardCol = 0; boardCol < 8; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(EMPTY);
    }

    private static void drawHeader(PrintStream out, String header) {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("   " + header);
    }

    private static void drawChessBoardFromWhite(PrintStream out, ChessGame game) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(SET_BG_COLOR_LIGHT_GREY);
        drawHeaders(out, headers);
        out.print(RESET_BG_COLOR);
        out.println();
        for (int row = 8; row >= 1; row--) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(" " + row + " ");
            drawPiecesFromWhite(out, game, row);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" " + row + " ");
            out.print(RESET_BG_COLOR);
            out.println();
        }
        out.print(SET_BG_COLOR_LIGHT_GREY);
        drawHeaders(out, headers);
    }

    private static void drawChessBoardFromBlack(PrintStream out, ChessGame game) {
        String[] headers = {"h", "g", "f", "e", "d", "c", "b", "a"};
        out.print(SET_BG_COLOR_LIGHT_GREY);
        drawHeaders(out, headers);
        out.print(RESET_BG_COLOR);
        out.println();
        for (int row = 1; row <= 8; row++) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(" " + row + " ");
            drawPiecesFromBlack(out, game, row);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" " + row + " ");
            out.print(RESET_BG_COLOR);
            out.println();
        }
        out.print(SET_BG_COLOR_LIGHT_GREY);
        drawHeaders(out, headers);
    }

    private static void drawPiecesFromWhite(PrintStream out, ChessGame game, int row) {
        int squareColor = row + 1;
        for (int col = 1; col <= 8; col++) {
            squareColor = drawPieces(out, game, row, squareColor, col);
        }
    }

    private static void drawPiecesFromBlack(PrintStream out, ChessGame game, int row) {
        int squareColor = row;
        for (int col = 8; col >= 1; col--) {
            squareColor = drawPieces(out, game, row, squareColor, col);
        }
    }

    private static int drawPieces(PrintStream out, ChessGame game, int row, int squareColor, int col) {
        if (squareColor % 2 == 0) {
            out.print(SET_BG_COLOR_BLACK);
        } else if (squareColor % 2 == 1) {
            out.print(SET_BG_COLOR_WHITE);
        }
        if (game.getBoard().getPiece(new ChessPosition(row, col)) != null) {
            out.print(SET_TEXT_COLOR_BLUE);
            printPlayer(out, game.getBoard().getPiece(new ChessPosition(row, col)));
        } else {
            out.print(EMPTY);
        }
        squareColor++;
        return squareColor;
    }

    private static void printPlayer(PrintStream out, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLUE);
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                out.print(WHITE_ROOK);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                out.print(WHITE_KNIGHT);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                out.print(WHITE_BISHOP);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                out.print(WHITE_KING);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                out.print(WHITE_QUEEN);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                out.print(WHITE_PAWN);
            }
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_RED);
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                out.print(BLACK_ROOK);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                out.print(BLACK_KNIGHT);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                out.print(BLACK_BISHOP);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                out.print(BLACK_KING);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                out.print(BLACK_QUEEN);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                out.print(BLACK_PAWN);
            }
        }
    }
}