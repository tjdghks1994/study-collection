package com.fastcampus.board.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtTokenNotFoundException extends JwtException {

    public JwtTokenNotFoundException() {
        super("JWT Token is not found!");
    }
}
