package org.intense;

import org.intense.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            ASTNode input = this.parse();
            if (input != null)
                this.nodeList.add(input);
        }

    }

    public ASTNode parse() {
        switch (currentToken.type) {
            case TokenType.LPAREN -> {
                advance();
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                    elements.add(parse());
                }
                consume(TokenType.RPAREN); // eat `)`
                return new DataListNode(elements);
            }
            case TokenType.COLON -> {
                advance();
                AtomNode atom = parseIfAtom();
                advance();
                atom.setType(TokenType.MAP_KEY);
                return atom;
            }
            case TokenType.REQUIERE -> {
                advance();
                Token token = currentToken;
                consume(TokenType.STRING);
                return new RequiredNode(token.value);

            }
            case TokenType.DEF -> {
                advance();
                AtomNode atom = parseIfAtom();
                // function name should be a symbol
                consume(TokenType.SYMBOL);
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                    elements.add(parse());
                }
                return new DefNode(atom, new DataListNode(elements));
            }
            case TokenType.IF -> {
                advance();
                // function name should be a symbol
                ASTNode ifExpr = parse();
                ASTNode ifBody = parse();
                ASTNode elseBody = parse();
                return new IfConditionNode(ifExpr, ifBody, elseBody);
            }
            case TokenType.LLIST -> {
                advance();
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.type != TokenType.RLIST && currentToken.type != TokenType.EOF) {
                    elements.add(parse());
                }
                consume(TokenType.RLIST); // eat `[`
                return new DataListNode(elements);
            }
            case TokenType.LBRACE -> {
                return parseMapNode();// eat `}`
            }
            case TokenType.SYMBOL -> {
                // method call (x {:y 30}) or built in
                return parseCallNode();
            }
            case  TokenType.RBRACE -> {
                throw new RuntimeException("unexpected closing ) or } or ] , got "+currentToken);
            }
            case TokenType.RPAREN , RLIST -> {
                return null;
            }
            default -> {
                return parseAtom();
            }
        }
    }

    private ASTNode parseCallNode() {
        ASTNode first = parseAtom();
        ASTNode rest = parse();
        return new CallNode(first, rest);
    }


    private ASTNode parseMapNode() {
        consume(TokenType.LBRACE);
        Map<String, ASTNode> keyMap = new HashMap<>();
        while (currentToken.type != TokenType.RBRACE && currentToken.type != TokenType.EOF) {
            if (currentToken.type == TokenType.COLON) {
                advance();
                AtomNode atomNode = parseIfAtom();
                advance();
                ASTNode data = parse();
                if (atomNode.getValue() == null) throw new RuntimeException("Key or value is missing");
                keyMap.put(atomNode.getValue(), data);
            } else {
                throw new RuntimeException("a map must require a proper :key " + currentToken);
            }

        }
        consume(TokenType.RBRACE);
        return new MapNode(keyMap);
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
                // symbol[:key]
                if(currentToken.type == TokenType.LLIST)
                {
                    advance();
                    List<ASTNode> elements = new ArrayList<>();
                    while (currentToken.type != TokenType.RLIST && currentToken.type != TokenType.EOF) {
                        elements.add(parse());
                    }
                    consume(TokenType.RLIST); // eat `[`
                    yield new ListAccessNode(token.value,new DataListNode(elements));
                }
                yield new AtomNode(TokenType.SYMBOL,token.value);
            case BOOLEAN:
                yield new AtomNode(TokenType.BOOLEAN, token.value);
            default:
                throw new RuntimeException("Unexpected token: " + token);
        };
    }

    private AtomNode parseIfAtom() {
        return switch (currentToken.type) {
            case NUMBER:
                yield new AtomNode(TokenType.NUMBER, currentToken.value);
            case STRING:
                yield new AtomNode(TokenType.STRING, currentToken.value);
            case SYMBOL:
                yield new AtomNode(TokenType.SYMBOL, currentToken.value);
            case BOOLEAN:
                yield new AtomNode(TokenType.BOOLEAN, currentToken.value);
            default:
                throw new RuntimeException("Unexpected token: " + currentToken);
        };
    }

    private void consume(TokenType type) {
        if (currentToken.type != type) {
            throw new RuntimeException("Expected " + type + " but found " + currentToken.type);
        }
        currentToken = lexer.nextToken();
    }

    private void advance() {
        currentToken = lexer.nextToken();
    }

    public List<ASTNode> getParseTree() {
        return this.nodeList;
    }
}
