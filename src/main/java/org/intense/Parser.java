package org.intense;
import org.intense.Ast.ASTNode;
import org.intense.Ast.AtomNode;
import org.intense.Ast.ListNode;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private final List<String> functionDefinitions = new ArrayList<>();
    private Token currentToken;
    private List<ASTNode> nodeList;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
        this.nodeList = new ArrayList<>();
        while (currentToken.type != TokenType.EOF) {
            this.nodeList.add(this.parse());
        }

    }


    public ASTNode parse() {
        if (currentToken.type == TokenType.LPAREN) {
            consume(TokenType.LPAREN);
            List<ASTNode> elements = new ArrayList<>();
            while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                elements.add(parse());
            }
            consume(TokenType.RPAREN); // eat `)`
            return new ListNode(elements);
        } else if (currentToken.type == TokenType.LLIST) {
            consume(TokenType.LLIST);
            List<ASTNode> elements = new ArrayList<>();
            while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                elements.add(parse());
            }
            consume(TokenType.RLIST); // eat `[`
            return new ListNode(elements);
        }
        else if (currentToken.type == TokenType.LBRACE) {
            consume(TokenType.LBRACE);
            List<ASTNode> elements = new ArrayList<>();
            while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                elements.add(parse());
            }
            consume(TokenType.RBRACE); // eat `}`
            return new ListNode(elements);
        }
        else if (currentToken.type == TokenType.RPAREN ||
                currentToken.type == TokenType.RLIST ||
                currentToken.type == TokenType.RBRACE) {
            throw new RuntimeException("unexpected closing ) or } or ]");
        } else {
            return parseAtom();
        }
    }

    private ASTNode parseAtom() {
        Token token = currentToken;
        consume(token.type);

        return switch (token.type) {
            case NUMBER:
                yield new AtomNode(TokenType.NUMBER, token.value);
            case STRING:
                yield new AtomNode(TokenType.STRING, token.value);
            case SYMBOL:
                yield new AtomNode(TokenType.SYMBOL, token.value);
            case BOOLEAN:
                yield new AtomNode(TokenType.BOOLEAN, token.value);
            default:
                throw new RuntimeException("Unexpected token: " + token);
        };
    }

    private void consume(TokenType type) {
        if (currentToken.type != type) {
            throw new RuntimeException("Expected " + type + " but found " + currentToken.type);
        }
        currentToken = lexer.nextToken();
    }

    public List<ASTNode> getParseTree() {
        return this.nodeList;
    }
}
