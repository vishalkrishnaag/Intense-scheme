package org.intense;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;
    private int line;
    private int column;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.currentChar = input.length() > 0 ? input.charAt(0) : '\0';
    }

    private void advance() {
        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        position++;
        if (position < input.length()) {
            currentChar = input.charAt(position);
        } else {
            currentChar = '\0';
        }
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private void skipComment() {
        while (currentChar != '\0' && currentChar != '\n') {
            advance();
        }
        if (currentChar == '\n') {
            advance();
        }
    }

    private Token readString() {
        advance(); // skip opening quote
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startCol = column;

        while (currentChar != '\0' && currentChar != '"') {
            if (currentChar == '\\') {
                advance();
                switch (currentChar) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    default:
                        sb.append('\\').append(currentChar);
                        break;
                }
            } else {
                sb.append(currentChar);
            }
            advance();
        }

        if (currentChar == '"') {
            advance();
            return new Token(TokenType.STRING, sb.toString(), startLine, startCol);
        } else {
            throw new RuntimeException("Unterminated string at line " + startLine + ", column " + startCol);
        }
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startCol = column;

        while (currentChar != '\0' &&
                (Character.isDigit(currentChar) || currentChar == '.' ||
                        currentChar == '+' || currentChar == '-' ||
                        currentChar == 'e' || currentChar == 'E')) {
            sb.append(currentChar);
            advance();
        }

        String numStr = sb.toString();
        return new Token(TokenType.NUMBER, numStr, startLine, startCol);
    }

    private Token readSymbol() {
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startCol = column;

        while (currentChar != '\0' &&
                !Character.isWhitespace(currentChar) &&
                !isTerminator(currentChar)) {
            sb.append(currentChar);
            advance();
        }

        String symbol = sb.toString();

        // Check for special symbols
        return switch (symbol) {
            case "true" -> new Token(TokenType.BOOLEAN, "true", startLine, startCol);
            case "false" -> new Token(TokenType.BOOLEAN, "false", startLine, startCol);
            case "'" -> new Token(TokenType.QUOTE, symbol, startLine, startCol);
            case "`" -> new Token(TokenType.QUASIQUOTE, symbol, startLine, startCol);
            case "," -> new Token(TokenType.UNQUOTE, symbol, startLine, startCol);
            case "~@" -> new Token(TokenType.UNQUOTE_SPLICING, symbol, startLine, startCol);
            case "." -> new Token(TokenType.DOT, symbol, startLine, startCol);
            case ":" -> new Token(TokenType.COLON, symbol, startLine, startCol);
            default -> new Token(TokenType.SYMBOL, symbol, startLine, startCol);
        };
    }

    private boolean isTerminator(char c) {
        return c == '(' || c == ')' || c == '"' || c == ';';
    }

    public Token nextToken() {
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (currentChar == ';') {
                skipComment();
                continue;
            }

            switch (currentChar) {
                case '(':
                    Token lparen = new Token(TokenType.LPAREN, "(", line, column);
                    advance();
                    return lparen;
                case ')':
                    Token rparen = new Token(TokenType.RPAREN, ")", line, column);
                    advance();
                    return rparen;
                case '[':
                    Token llist = new Token(TokenType.LLIST, "[", line, column);
                    advance();
                    return llist;
                case ']':
                    Token rlist = new Token(TokenType.RLIST, "[", line, column);
                    advance();
                    return rlist;
                case '{':
                    Token lbrace = new Token(TokenType.LBRACE, "{", line, column);
                    advance();
                    return lbrace;
                case '}':
                    Token rbrace = new Token(TokenType.RBRACE, "}", line, column);
                    advance();
                    return rbrace;
                case '"':
                    return readString();
                case '+':
                case '-':
                    // Check if this is part of a number
                    if (Character.isDigit(input.charAt(position + 1))) {
                        return readNumber();
                    } else{
                        return readSymbol();
                    }
                default:
                    if (Character.isDigit(currentChar)) {
                        return readNumber();
                    } else {
                        return readSymbol();
                    }
            }
        }

        return new Token(TokenType.EOF, "", line, column);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        do {
            token = nextToken();
            tokens.add(token);
        } while (token.type != TokenType.EOF);
        return tokens;
    }
}
