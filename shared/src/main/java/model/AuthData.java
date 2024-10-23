package model;


import chess.ChessGame;

import java.util.Objects;

public record AuthData(String authToken, String username) {
}