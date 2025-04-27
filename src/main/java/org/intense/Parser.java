package org.intense;

import org.intense.Ast.*;

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
            this.nodeList.add(this.parse());
        }

    }

    public ASTNode parse() {
        if (currentToken.type == TokenType.LPAREN) {
            advance();
            //
            List<ASTNode> elements = new ArrayList<>();
            while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                elements.add(parse());
            }
            consume(TokenType.RPAREN); // eat `)`
            return new ListNode(elements);
        }
        else if (currentToken.type == TokenType.PACKAGE) {
            advance();
            Token token = currentToken;
            consume(TokenType.STRING);
            return new PackageNode(token.value);
        } else if (currentToken.type == TokenType.REQUIERE) {
            advance();
            Token token = currentToken;
            consume(TokenType.STRING);
            return new RequiredNode(token.value);

        } else if(currentToken.type == TokenType.DEF){
            advance();
            AtomNode atom = parseIfAtom();
            // function name should be a symbol
            consume(TokenType.SYMBOL);

           return new DefNode(atom,parseDefine());
        } else if (currentToken.type == TokenType.SET) {
            advance();
            AtomNode atomNode = parseIfAtom();
            consume(TokenType.SYMBOL);
            return new SetNode(atomNode,parse());

        } else if(currentToken.type == TokenType.IF){
            advance();
            // function name should be a symbol
            ASTNode ifExpr = parse();
            ASTNode ifBody = parse();
            ASTNode elseBody=parse();
            return new IfConditionNode(ifExpr,ifBody,elseBody);
        }
        else if (currentToken.type == TokenType.LLIST) {
            advance();
            List<ASTNode> elements = new ArrayList<>();
            while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
                elements.add(parse());
            }
            consume(TokenType.RLIST); // eat `[`
            return new DataListNode(elements);
        } else if (currentToken.type == TokenType.LBRACE) {
           return parseMapNode();// eat `}`
        } else if(currentToken.type == TokenType.SYMBOL)
        {
            // method call (x {:y 30}) or built in
           return parseCallNode();
        }
        else if (currentToken.type == TokenType.RLIST ||
                currentToken.type == TokenType.RBRACE) {
            throw new RuntimeException("unexpected closing ) or } or ]");
        }
        else if (currentToken.type == TokenType.RPAREN) {
            return null;
        }
        else {
            return parseAtom();
        }
    }

    private ASTNode parseCallNode() {
        ASTNode first = parseAtom();
        ASTNode rest = parse();
        return new CallNode(first,rest);
    }

    private ListNode parseDefine() {
        List<ASTNode> elements = new ArrayList<>();
        while (currentToken.type != TokenType.RPAREN && currentToken.type != TokenType.EOF) {
            elements.add(parse());
        }
        return new ListNode(elements);
    }

    private ASTNode parseMapNode() {
        consume(TokenType.LBRACE);
        Map<String,ASTNode> keyMap = new HashMap<>();
        String key;
        while (currentToken.type != TokenType.RBRACE && currentToken.type != TokenType.EOF) {
                if (currentToken.type == TokenType.COLON) {
                    advance();
                    AtomNode atomNode = parseIfAtom();
                    advance();
                    key = atomNode.value;
                    ASTNode data = parse();
                    if (key == null) throw new RuntimeException("Key or value is missing");
                    keyMap.put(key, data);
                }
                else {
                    throw new RuntimeException("a map must require a proper :key "+currentToken);
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
                yield new AtomNode(TokenType.SYMBOL, token.value);
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
    private void advance(){
        currentToken = lexer.nextToken();
    }

    public List<ASTNode> getParseTree() {
        return this.nodeList;
    }
}
