package org.intense;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private final String input;
    private int position;
    private int line;
    private int column;
    private char currentChar;

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("true", TokenType.BOOLEAN);
        KEYWORDS.put("false", TokenType.BOOLEAN);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("val", TokenType.VAL);
        KEYWORDS.put("fun", TokenType.FUN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("quote", TokenType.QUOTE);
        KEYWORDS.put("this", TokenType.THIS);
        KEYWORDS.put("self", TokenType.SELF);
        KEYWORDS.put("package", TokenType.PACKAGE);
        KEYWORDS.put("require", TokenType.REQUIERE);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("null", TokenType.NULL);
        KEYWORDS.put("String", TokenType.STRING_KEYWORD);
        KEYWORDS.put("Int", TokenType.INT_KEYWORD);
        KEYWORDS.put("Double", TokenType.DOUBLE_KEYWORD);
        KEYWORDS.put("Boolean", TokenType.BOOLEAN_KEYWORD);
        KEYWORDS.put("Float", TokenType.FLOAT_KEYWORD);
        KEYWORDS.put("Byte", TokenType.BYTE_KEYWORD);
        KEYWORDS.put("Short", TokenType.SHORT_KEYWORD);
        KEYWORDS.put("Long", TokenType.LONG_KEYWORD);
        KEYWORDS.put("UByte", TokenType.UBYTE_KEYWORD);
        KEYWORDS.put("ULong", TokenType.ULONG_KEYWORD);

    }

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.currentChar = !input.isEmpty() ? input.charAt(0) : '\0';
    }

    private void advance() {
        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        position++;
        currentChar = position < input.length() ? input.charAt(position) : '\0';
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
        if (currentChar == '\n') advance();
    }

    private Token readString() {
        advance();  // skip opening quote
        StringBuilder sb = new StringBuilder();
        int startLine = line, startCol = column;

        while (currentChar != '\0' && currentChar != '"') {
            if (currentChar == '\\') {
                advance();
                switch (currentChar) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    default: sb.append('\\').append(currentChar); break;
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
        int startLine = line, startCol = column;

        while (currentChar != '\0' &&
                (Character.isDigit(currentChar) || currentChar == '.' ||
                        currentChar == '+' || currentChar == '-' ||
                        currentChar == 'e' || currentChar == 'E')) {
            sb.append(currentChar);
            advance();
        }
        return new Token(TokenType.NUMBER, sb.toString(), startLine, startCol);
    }

    private Token readSymbol() {
        StringBuilder sb = new StringBuilder();
        int startLine = line, startCol = column;

        while (currentChar != '\0' &&
                !Character.isWhitespace(currentChar) &&
                !isTerminator(currentChar) &&
                currentChar != ':' && currentChar != '?'&& currentChar != '.') {
            sb.append(currentChar);
            advance();
        }

        String symbol = sb.toString();
        TokenType type = KEYWORDS.getOrDefault(symbol, TokenType.SYMBOL);
        return new Token(type, symbol, startLine, startCol);
    }

    private boolean isTerminator(char c) {
        return c == '(' || c == ')' || c == '"' || c == ';' ||
                c == '[' || c == ']' || c == '{' || c == '}';
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

            int startLine = line, startCol = column;

            switch (currentChar) {
                case '(': advance(); return new Token(TokenType.LPAREN, "(", startLine, startCol);
                case ')': advance(); return new Token(TokenType.RPAREN, ")", startLine, startCol);
                case '[': advance(); return new Token(TokenType.LLIST, "[", startLine, startCol);
                case ']': advance(); return new Token(TokenType.RLIST, "]", startLine, startCol);
                case '{': advance(); return new Token(TokenType.LBRACE, "{", startLine, startCol);
                case '}': advance(); return new Token(TokenType.RBRACE, "}", startLine, startCol);
                case ':': advance(); return new Token(TokenType.COLON, ":", startLine, startCol);
                case '?': advance(); return new Token(TokenType.NULLABLE, "?", startLine, startCol);
                case '.': advance(); return new Token(TokenType.DOT, "?", startLine, startCol);
                case '"': return readString();
                case '+': case '-':
                    if (position + 1 < input.length() && Character.isDigit(input.charAt(position + 1))) {
                        return readNumber();
                    }
                    return readSymbol();
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
        } while (token.getType() != TokenType.EOF);
        return tokens;
    }
}




