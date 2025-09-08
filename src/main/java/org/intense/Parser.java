package org.intense;

import org.intense.Types.*;
import org.intense.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private final List<ASTNode> nodeList;
    private boolean returnEnabled = false;
    // false means field true means inside a method or if else cond
    private boolean variableScopeIsLocal = false;

    public Parser(Lexer lexer)  {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
        this.nodeList = new ArrayList<>();
        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.RPAREN) {
                throw new RuntimeException("An extra closing bracket but found " + currentToken);
            }
            ASTNode input = this.parse();
            if (input != null)
                this.nodeList.add(input);
        }

    }


    public ASTNode parse()  {
        if (currentToken.getType() == TokenType.LPAREN) {
            advance();
        switch (currentToken.getType()) {
            case TokenType.SYMBOL -> {
                // method call (x :y 30 :z "some values") or built in
                ASTNode callNode = parseCallNode();
                consume(TokenType.RPAREN);
                return callNode;
            }
            case TokenType.IMPORT -> {
                advance();
                Token token = currentToken;
                consume(TokenType.STRING);
                AtomNode casting_var = null;
                if (currentToken.getType() == TokenType.AS) {
                    advance();
                    casting_var = parseIfAtom();
                    advance();
                }
                consume(TokenType.RPAREN);
                return new ImportNode(token.getValue(), casting_var);
            }
            case RETURN -> {
                advance();
                returnEnabled = true;
                ReturnNode returnNode = new ReturnNode(parse());
                consume(TokenType.RPAREN);
                return returnNode;
            }
            case PACKAGE -> {
                advance();
                AtomNode atomNode = parseIfAtom();
                advance();
                consume(TokenType.RPAREN);
                return new PackageNode(atomNode);
            }
            case WHILE -> {
                advance();
                WhileConditionNode whileConditionNode = new WhileConditionNode(parse(), parse());
                consume(TokenType.RPAREN);
                return whileConditionNode;
            }
            case SET -> {
                advance();
                AtomNode first = parseIfAtom();
                advance();
                List<ASTNode> rest = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    rest.add(parse());
                }
                if (rest.isEmpty()) {
                    throw new RuntimeException("(Invalid syntax set assign assignee)");
                }
                consume(TokenType.RPAREN);
                return new SetNode(first, new DataListNode(rest));
            }
            case GET -> {
                advance();
                ASTNode first = parseAtom();
                consume(TokenType.RPAREN);
                return new GetNode(first);
            }
            case TokenType.DEF -> {
                returnEnabled = false;
                variableScopeIsLocal = true;
                advance(); // consume 'define'

                // Two possible forms:
                // (define symbol expr)
                // (define (symbol args...) body...)
                if (currentToken.getType() == TokenType.LPAREN) {
                    // Function definition sugar: (define (f x y) body...)
                    consume(TokenType.LPAREN);
                    AtomNode name = parseIfAtom();     // function name
                    List<String> arguments = parseParamList(); // parse args until RPAREN
                    consume(TokenType.RPAREN);

                    List<ASTNode> body = new ArrayList<>();
                    while (currentToken.getType() != TokenType.RPAREN
                            && currentToken.getType() != TokenType.EOF) {
                        body.add(parse());
                    }
                    consume(TokenType.RPAREN);

                    returnEnabled = false;
                    variableScopeIsLocal = false;
                    return new DefNode(name, arguments, body, true); // function node
                } else {
                    // may be function
                    AtomNode name = parseIfAtom();
                    advance();
                    if(currentToken.getType() == TokenType.LPAREN)
                    {
                        advance();
                        if(currentToken.getType() == TokenType.LAMBDA)
                        {
                            advance();
                            List<String> arguments = parseParamList();
                            consume(TokenType.RPAREN);
                            List<ASTNode> body = new ArrayList<>();
                            while (currentToken.getType() != TokenType.RPAREN
                                    && currentToken.getType() != TokenType.EOF) {
                                body.add(parse());
                            }
                            consume(TokenType.RPAREN);
                            consume(TokenType.RPAREN);
                            return new DefNode(name, arguments, body, true); // function node

                        }
                        else if(currentToken.getType() == TokenType.RPAREN)
                        {
                          advance();
                            List<ASTNode> body = new ArrayList<>();
                            while (currentToken.getType() != TokenType.RPAREN
                                    && currentToken.getType() != TokenType.EOF) {
                                body.add(parse());
                            }
                            consume(TokenType.RPAREN);
                            consume(TokenType.RPAREN);
                            return new DefNode(name,null, body, true); // function node
                        }
                        else {
                            // Variable definition: (define x (expr))
                            ASTNode value = parse();

                            consume(TokenType.RPAREN);
                            consume(TokenType.RPAREN);

                            returnEnabled = false;
                            variableScopeIsLocal = false;
                            return new DefNode(name, value, false); // variable node
                        }
                    }
                    else {
                        // Variable definition: (define x expr)
                        ASTNode value = parse();

                        consume(TokenType.RPAREN);

                        returnEnabled = false;
                        variableScopeIsLocal = false;
                        return new DefNode(name, value, false); // variable node
                    }
                }
            }

            case TokenType.CLASS -> {
                List<ASTNode> extendBlock = new ArrayList<>();
                advance(); // eat class
                List<ASTNode> elements = new ArrayList<>();
                AtomNode atomNode = parseIfAtom();
                advance();
                if (currentToken.getType() == TokenType.LLIST) {
                    advance();
                    if (currentToken.getType() == TokenType.EXTENDS) {
                        advance();
                        while (currentToken.getType() != TokenType.RLIST && currentToken.getType() != TokenType.EOF) {
                            extendBlock.add(parse());
                        }
                    }
                    consume(TokenType.RLIST);
                } else {
                    while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                        elements.add(parse());
                    }
                }
                consume(TokenType.RPAREN);
                return new ClassNode(atomNode, elements, extendBlock);
            }
            case TokenType.INTERFACE -> {
                advance(); // eat interface
                List<ASTNode> elements = new ArrayList<>();
                AtomNode atomNode = parseIfAtom();
                advance();

                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                consume(TokenType.RPAREN);
                return new InterfaceNode(atomNode, elements);
            }
            case TokenType.ENUM -> {
                advance(); // eat enum
                List<ASTNode> elements = new ArrayList<>();
                AtomNode atomNode = parseIfAtom();
                advance();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                consume(TokenType.RPAREN);
                return new EnumNode(atomNode, elements);
            }
            case TokenType.IF -> {
                advance();
                // function name should be a symbol
                ASTNode ifExpr = parse();
                ASTNode ifBody = parse();
                ASTNode elseBody = parse();
                consume(TokenType.RPAREN);
                return new IfConditionNode(ifExpr, ifBody, elseBody);
            }
            case TokenType.LLIST -> {
                advance();
                if (currentToken.getType() == TokenType.SYMBOL) {
                    ASTNode call = parseCallNode();
                    consume(TokenType.RLIST);
                    return call;
                } else {
                    List<ASTNode> elements = new ArrayList<>();
                    while (currentToken.getType() != TokenType.RLIST && currentToken.getType() != TokenType.EOF) {
                        elements.add(parse());
                    }
                    consume(TokenType.RLIST); // eat `[`
                    return new SquareListNode(elements);
                }

            }
            case TokenType.LBRACE -> {
                return parseMapNode();// eat `}`
            }
            case TokenType.RBRACE,TokenType.RPAREN, RLIST -> {
                throw new RuntimeException("unexpected closing ) or } or ] , got " + currentToken);
            }
            case TokenType.IS -> {
                advance();
                return new ConnectiveNode("is");
            }
            default -> {
                return parseAtom();
            }
        }
        }
        else {
            return parseAtom();
        }
    }

    private List<String> parseUsing() {
        List<String> arguments = new ArrayList<>();
        advance();
        if (currentToken.getType() == TokenType.USING) {
            advance();
            while (currentToken.getType() != TokenType.RPAREN &&
                    currentToken.getType() != TokenType.RLIST &&
                    currentToken.getType() != TokenType.EOF) {
                 arguments.add(currentToken.getValue());
                 advance();
                // atom:Type "value"
                consume(TokenType.SYMBOL);
                consume(TokenType.COLON);
                arguments.add(currentToken.getValue());
                advance();
            }
        } else if (currentToken.getType() == TokenType.RLIST) {
            return null;
        } else if (currentToken.getType() == TokenType.RPAREN) {
            return null;
        } else {
            throw new RuntimeException("error in function definition expected a ) or ]");
        }
        return arguments;
    }

    private List<String> parseParamList() {
        List<String> params = new ArrayList<>();

        // keep consuming until RPAREN
        while (currentToken.getType() != TokenType.RPAREN &&
                currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.SYMBOL) {
                params.add(currentToken.getValue());
                advance();
            } else {
                throw new RuntimeException("Expected parameter name, found " + currentToken);
            }
        }

        return params;
    }

    private DataTypeNode parseDataTypes() {
        // type <xyz | p q r s >
        advance(); // eat `<`
        List<ASTNode> values = new ArrayList<>();
        int Count = 1;
        while (Count != 0) {
            if (currentToken.getType() == TokenType.LESS_THAN) {
                Count++;
                values.add(parseDataTypes());
            } else if (currentToken.getType() == TokenType.GREATER_THAN) {
                Count--;
                advance();
            } else {
                values.add(parseAtom());
            }
        }
        return new DataTypeNode(values);
    }

    private ASTNode parseCallNode() {
        ASTNode first = parseAtom();
        List<ASTNode> rest = new ArrayList<>();
        while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
            rest.add(parse());
        }

        return new CallNode(first, rest);
    }


    private ASTNode parseMapNode() {
        consume(TokenType.LBRACE);
        Map<ASTNode, ASTNode> keyMap = new HashMap<>();
        while (currentToken.getType() != TokenType.RBRACE && currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.COLON) {
                advance();
                AtomNode atomNode = parseIfAtom();
                advance();
                ASTNode data = parse();
                if (atomNode.getValue() == null) throw new RuntimeException("Key or value is missing");
                keyMap.put(atomNode, data);
            } else {
                throw new RuntimeException("a map must require a proper :key " + currentToken);
            }

        }
        consume(TokenType.RBRACE);
        return new MapNode(keyMap);
    }


    private ASTNode parseAtom() {
        Token token = currentToken;
        advance();

        return switch (token.getType()) {
            case THIS:
                if (currentToken.getType() == TokenType.DOT) {
                    // symbol.symbol
                    List<ASTNode> elements = new ArrayList<>();
                    while (currentToken.getType() == TokenType.DOT || currentToken.getType() == TokenType.SYMBOL) {
                        if (currentToken.getType() == TokenType.DOT) {
                            advance();
                        }
                        elements.add(parseIfAtom());
                        advance();
                    }
                    yield new LinkingNode(token.getValue(), elements);
                }
                yield new AtomNode(new ThisVal());
            case NUMBER:
                yield new AtomNode(new NumVal(Double.parseDouble(token.getValue())));
            case STRING:
                yield new AtomNode(new StrVal(token.getValue()));
            case NONE:
                yield new AtomNode(new UnitVal());
//            case DOT:
//                yield new AtomNode(TokenType.DOT, token.getValue());
//            case COLON:
//                yield new AtomNode(TokenType.COLON, token.getValue());
//            case NULL:
//                yield new AtomNode(TokenType.NULL, token.getValue());
//            case NULLABLE:
//                yield new AtomNode(TokenType.NULLABLE, token.getValue());
//            case ANY_KEYWORD:
//                yield new AtomNode(TokenType.ANY_KEYWORD, token.getValue());
//            case STRING_KEYWORD:
//                yield new AtomNode(TokenType.STRING_KEYWORD, token.getValue());
//            case INT_KEYWORD:
//                yield new AtomNode(TokenType.INT_KEYWORD, token.getValue());
//            case DOUBLE_KEYWORD:
//                yield new AtomNode(TokenType.DOUBLE_KEYWORD, token.getValue());
//            case BOOLEAN_KEYWORD:
//                yield new AtomNode(TokenType.BOOLEAN_KEYWORD, token.getValue());
//            case FLOAT_KEYWORD:
//                yield new AtomNode(TokenType.FLOAT_KEYWORD, token.getValue());
//            case BYTE_KEYWORD:
//                yield new AtomNode(TokenType.BYTE_KEYWORD, token.getValue());
//            case SHORT_KEYWORD:
//                yield new AtomNode(TokenType.SHORT_KEYWORD, token.getValue());
//            case LONG_KEYWORD:
//                yield new AtomNode(TokenType.LONG_KEYWORD, token.getValue());
//            case UBYTE_KEYWORD:
//                yield new AtomNode(TokenType.UBYTE_KEYWORD, token.getValue());
//            case ULONG_KEYWORD:
//                yield new AtomNode(TokenType.ULONG_KEYWORD, token.getValue());
            case SYMBOL:
                // symbol[key]
                if (currentToken.getType() == TokenType.LLIST) {
                    advance();
                    List<ASTNode> elements = new ArrayList<>();
                    while (currentToken.getType() != TokenType.RLIST && currentToken.getType() != TokenType.EOF) {
                        elements.add(parse());
                    }
                    consume(TokenType.RLIST); // eat `[`
                    yield new ListAccessNode(token.getValue(), new DataListNode(elements));
                } else if (currentToken.getType() == TokenType.DOT) {
                    // symbol.symbol
                    List<ASTNode> elements = new ArrayList<>();
                    while (currentToken.getType() == TokenType.DOT || currentToken.getType() == TokenType.SYMBOL) {
                        if (currentToken.getType() == TokenType.DOT) {
                            advance();
                        }
                        elements.add(parseIfAtom());
                        advance();
                    }
                    yield new LinkingNode(token.getValue(), elements);
                } else if (currentToken.getType() == TokenType.LESS_THAN) {
                    yield new CustomDataTypeNode(token.getValue(), parseDataTypes());
                }

                yield new AtomNode(new VarVal(token.getValue()));
            case BOOLEAN:
                yield new AtomNode(new BoolVal(token.getValue().equals("true")));
            default:
                throw new RuntimeException("Unexpected token: " + token);
        };
    }

    private AtomNode parseIfAtom() {
        return switch (currentToken.getType()) {
            case NUMBER -> new AtomNode(new NumVal(Double.parseDouble(currentToken.getValue())));
            case STRING -> new AtomNode(new StrVal(currentToken.getValue()));
            case SYMBOL -> {
                String identifier = currentToken.getValue();
                if (identifier.contains("-")) {
                    identifier = "in10s_" + identifier.replace("-", "_");
                }
                yield new AtomNode(new VarVal(identifier));
            }
            case BOOLEAN -> new AtomNode(new BoolVal(currentToken.getValue().equals("true")));
//            case COLON -> new AtomNode(TokenType.COLON, currentToken.getValue());
//            case DOT -> new AtomNode(TokenType.DOT, currentToken.getValue());
//            case NULL -> new AtomNode(TokenType.NULL, currentToken.getValue());
//            case NULLABLE -> new AtomNode(TokenType.NULLABLE, currentToken.getValue());
//            case ANY_KEYWORD -> new AtomNode(TokenType.ANY_KEYWORD, currentToken.getValue());
//            case STRING_KEYWORD -> new AtomNode(TokenType.STRING_KEYWORD, currentToken.getValue());
//            case INT_KEYWORD -> new AtomNode(TokenType.INT_KEYWORD, currentToken.getValue());
//            case DOUBLE_KEYWORD -> new AtomNode(TokenType.DOUBLE_KEYWORD, currentToken.getValue());
//            case BOOLEAN_KEYWORD -> new AtomNode(TokenType.BOOLEAN_KEYWORD, currentToken.getValue());
//            case FLOAT_KEYWORD -> new AtomNode(TokenType.FLOAT_KEYWORD, currentToken.getValue());
//            case BYTE_KEYWORD -> new AtomNode(TokenType.BYTE_KEYWORD, currentToken.getValue());
//            case SHORT_KEYWORD -> new AtomNode(TokenType.SHORT_KEYWORD, currentToken.getValue());
//            case LONG_KEYWORD -> new AtomNode(TokenType.LONG_KEYWORD, currentToken.getValue());
//            case UBYTE_KEYWORD -> new AtomNode(TokenType.UBYTE_KEYWORD, currentToken.getValue());
//            case ULONG_KEYWORD -> new AtomNode(TokenType.ULONG_KEYWORD, currentToken.getValue());
            default -> throw new RuntimeException("Unexpected token: " + currentToken);
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
