package org.intense;

public enum TokenType {
    LPAREN, RPAREN,
    LLIST, RLIST,
    LBRACE, RBRACE,
    SYMBOL, NUMBER,
    STRING, BOOLEAN,
    QUOTE, QUASIQUOTE,
    UNQUOTE, UNQUOTE_SPLICING,
    DOT, EOF, Closure,
    BIG_INT,
    INT,
    DOUBLE,
    OBJECT,
    ARRAY_INT,
    ARRAY_DOUBLE,
    ARRAY_OBJECT,
    UNDEFINED,
    INVALID
}
