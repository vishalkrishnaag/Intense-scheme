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
        while (currentToken.getType() != TokenType.EOF) {
            ASTNode input = this.parse();
            if (input != null)
                this.nodeList.add(input);
        }

    }

    public ASTNode parse() {
        switch (currentToken.getType()) {
            case TokenType.LPAREN -> {
                advance();
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                consume(TokenType.RPAREN); // eat `)`
                return new DataListNode(elements);
            }
            case TokenType.REQUIERE -> {
                advance();
                Token token = currentToken;
                consume(TokenType.STRING);
                return new RequiredNode(token.getValue());

            }
            case TokenType.FUN -> {
                advance();
                AtomNode atom = parseIfAtom();
                // function name should be a symbol
                consume(TokenType.SYMBOL);
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                return new FunNode(atom, new DataListNode(elements));
            }
            case TokenType.VAL -> {
                advance();
                AtomNode atom = parseIfAtom();
                // function name should be a symbol
                consume(TokenType.SYMBOL);
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                return new ValNode(atom, new DataListNode(elements));
            }
            case TokenType.VAR -> {
                advance();
                AtomNode atom = parseIfAtom();
                // function name should be a symbol
                consume(TokenType.SYMBOL);
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                return new VarNode(atom, new DataListNode(elements));
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
                while (currentToken.getType() != TokenType.RLIST && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                consume(TokenType.RLIST); // eat `[`
                return new DataListNode(elements);
            }
            case TokenType.LBRACE -> {
                return parseMapNode();// eat `}`
            }
            case TokenType.SYMBOL -> {
                // method call (x :y 30 :z "some values") or built in
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
        while (currentToken.getType() != TokenType.RBRACE && currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.COLON) {
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
        consume(token.getType());

        return switch (token.getType()) {
            case NUMBER:
                yield new AtomNode(TokenType.NUMBER, token.getValue());
            case STRING:
                yield new AtomNode(TokenType.STRING, token.getValue());
            case DOT:
                yield new AtomNode(TokenType.DOT,token.getValue());
            case COLON:
                yield new AtomNode(TokenType.COLON,token.getValue());
            case NULL:
                yield new AtomNode(TokenType.NULL,token.getValue());
            case NULLABLE:
                yield new AtomNode(TokenType.NULLABLE,token.getValue());
            case STRING_KEYWORD:
                yield new AtomNode(TokenType.STRING_KEYWORD,token.getValue());
            case INT_KEYWORD:
                yield new AtomNode( TokenType.INT_KEYWORD,token.getValue());
            case DOUBLE_KEYWORD:
                yield new AtomNode( TokenType.DOUBLE_KEYWORD ,token.getValue());
            case BOOLEAN_KEYWORD:
                yield new AtomNode( TokenType.BOOLEAN_KEYWORD ,token.getValue());
            case FLOAT_KEYWORD:
                yield new AtomNode( TokenType.FLOAT_KEYWORD ,token.getValue());
            case BYTE_KEYWORD:
                yield new AtomNode( TokenType.BYTE_KEYWORD ,token.getValue());
            case SHORT_KEYWORD:
                yield new AtomNode( TokenType.SHORT_KEYWORD ,token.getValue());
            case LONG_KEYWORD:
                yield new AtomNode( TokenType.LONG_KEYWORD ,token.getValue());
            case UBYTE_KEYWORD:
                yield new AtomNode( TokenType.UBYTE_KEYWORD ,token.getValue());
            case ULONG_KEYWORD:
                yield new AtomNode( TokenType.ULONG_KEYWORD ,token.getValue());
            case SYMBOL:
                // symbol[:key]
                if(currentToken.getType() == TokenType.LLIST)
                {
                    advance();
                    List<ASTNode> elements = new ArrayList<>();
                    while (currentToken.getType() != TokenType.RLIST && currentToken.getType() != TokenType.EOF) {
                        elements.add(parse());
                    }
                    consume(TokenType.RLIST); // eat `[`
                    yield new ListAccessNode(token.getValue(),new DataListNode(elements));
                }
                yield new AtomNode(TokenType.SYMBOL, token.getValue());
            case BOOLEAN:
                yield new AtomNode(TokenType.BOOLEAN, token.getValue());
            default:
                throw new RuntimeException("Unexpected token: " + token);
        };
    }

    private AtomNode parseIfAtom() {
        return switch (currentToken.getType()) {
            case NUMBER:
                yield new AtomNode(TokenType.NUMBER, currentToken.getValue());
            case STRING:
                yield new AtomNode(TokenType.STRING, currentToken.getValue());
            case SYMBOL:
                yield new AtomNode(TokenType.SYMBOL, currentToken.getValue());
            case BOOLEAN:
                yield new AtomNode(TokenType.BOOLEAN, currentToken.getValue());
            case COLON:
                yield new AtomNode(TokenType.COLON,currentToken.getValue());
            case DOT:
                yield new AtomNode(TokenType.DOT,currentToken.getValue());
            case NULL:
                yield new AtomNode(TokenType.NULL,currentToken.getValue());
            case NULLABLE:
                yield new AtomNode(TokenType.NULLABLE,currentToken.getValue());
            case STRING_KEYWORD:
                yield new AtomNode(TokenType.STRING_KEYWORD,currentToken.getValue());
            case INT_KEYWORD:
                yield new AtomNode( TokenType.INT_KEYWORD,currentToken.getValue());
            case DOUBLE_KEYWORD:
                yield new AtomNode( TokenType.DOUBLE_KEYWORD ,currentToken.getValue());
            case BOOLEAN_KEYWORD:
                yield new AtomNode( TokenType.BOOLEAN_KEYWORD ,currentToken.getValue());
            case FLOAT_KEYWORD:
                yield new AtomNode( TokenType.FLOAT_KEYWORD ,currentToken.getValue());
            case BYTE_KEYWORD:
                yield new AtomNode( TokenType.BYTE_KEYWORD ,currentToken.getValue());
            case SHORT_KEYWORD:
                yield new AtomNode( TokenType.SHORT_KEYWORD ,currentToken.getValue());
            case LONG_KEYWORD:
                yield new AtomNode( TokenType.LONG_KEYWORD ,currentToken.getValue());
            case UBYTE_KEYWORD:
                yield new AtomNode( TokenType.UBYTE_KEYWORD ,currentToken.getValue());
            case ULONG_KEYWORD:
                yield new AtomNode( TokenType.ULONG_KEYWORD ,currentToken.getValue());
            default:
                throw new RuntimeException("Unexpected token: " + currentToken);
        };
    }

    private void consume(TokenType type) {
        if (currentToken.getType() != type) {
            throw new RuntimeException("Expected " + type + " but found " + currentToken.getType());
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
