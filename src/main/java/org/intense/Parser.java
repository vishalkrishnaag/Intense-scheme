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

    private ASTNode parse() {
        if (currentToken.getType() == TokenType.LPAREN) {
            advance();
            return this.parseListExpr();
        }
         else {
           return parseAtom();
        }
    }


    public ASTNode parseListExpr() {
        switch (currentToken.getType()) {
            case TokenType.SYMBOL -> {
                // method call (x :y 30 :z "some values") or built in
                ASTNode callNode = parseCallNode();
                consume(TokenType.RPAREN);
                return callNode;
            }
            case LAMBDA -> {
                advance();
                return parseLambda();
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
                advance(); // consume 'define'

                if (currentToken.getType() == TokenType.LPAREN) {
                    // Function definition sugar: (define (f arg1 arg2 ...) body...)
                    consume(TokenType.LPAREN);

                    ASTNode name = parseAtom();              // function name
                    List<String> arguments = parseParamList();  // parse args until R_PAREN
                    consume(TokenType.RPAREN); // close param list

                    // Parse body until closing R_PAREN
                    List<ASTNode> body = parseBody();
                    consume(TokenType.RPAREN); // close define
                    // --- Desugar to (define f (lambda (args...) body...)) ---
                    LambdaNode lambda = new LambdaNode(arguments, body);
                    if(name instanceof AtomNode atom){
                        return new DefNode(atom, lambda);
                    }else {
                        throw new RuntimeException("A define name should be an identifier");
                    }
                } else {
                    // Variable definition: (define x expr)
                    ASTNode name = parseAtom();
                    ASTNode value = parse();

                    consume(TokenType.RPAREN); // close define
                    if(name instanceof AtomNode atomic)
                    {
                        return new DefNode(atomic, value); // variable node
                    }
                    else {
                        throw new RuntimeException("A define name should be an identifier");
                    }

                }
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
            case TokenType.RBRACE, TokenType.RPAREN, RLIST -> {
                throw new RuntimeException("unexpected closing ) or } or ] , got " + currentToken);
            }
            case TokenType.IS -> {
                advance();
                return new ConnectiveNode("is");
            }
            default -> {
                return this.parse();
            }
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
        while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.SYMBOL || currentToken.getType() == TokenType.DOT) {
                ASTNode astNode = parseAtom();
                if (astNode instanceof AtomNode atom) {
                    params.add(atom.getValue().asString());
                }
                else {
                    throw new RuntimeException("A function parameter name should be an identifier");
                }
            } else {
                throw new RuntimeException("Expected parameter name, found " + currentToken);
            }
        }

        return params;
    }

    // (lambda (x y) body...)
    private ASTNode parseLambda() {
        consume(TokenType.LPAREN);
        List<String> params = parseParamList();
        consume(TokenType.RPAREN);

        List<ASTNode> body = parseBody();
        consume(TokenType.RPAREN);

        return new LambdaNode(params, body);
    }

    private List<ASTNode> parseBody() {
        List<ASTNode> body = new ArrayList<>();
        while (currentToken.getType() != TokenType.RPAREN
                && currentToken.getType() != TokenType.EOF) {
            body.add(parse());
        }
        return body;
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
            case NUMBER:
                yield new AtomNode(new NumVal(Double.parseDouble(token.getValue())));
            case STRING:
                yield new AtomNode(new StrVal(token.getValue()));
            case DOT:
                //..symbol format
                yield new AtomNode(new VarVal(parseChainedExpr(token.getValue())));
            case NULL:
                yield new AtomNode(new NullVal());
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
                    // symbol.value
                    yield new AtomNode(new VarVal(parseChainedExpr(token.getValue())));
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

    private String parseChainedExpr(String firstElement) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstElement);

        while (currentToken.getType() == TokenType.DOT) {
            advance(); // consume dot
            sb.append(".");

            if (currentToken.getType() != TokenType.SYMBOL) {
                throw new RuntimeException("Expected SYMBOL after '.'");
            }

            sb.append(currentToken.getValue());
            advance(); // consume the symbol
        }
        return sb.toString();
    }


    private AtomNode parseIfAtom() {
        return switch (currentToken.getType()) {
            case NUMBER -> new AtomNode(new NumVal(Double.parseDouble(currentToken.getValue())));
            case STRING -> new AtomNode(new StrVal(currentToken.getValue()));
            case SYMBOL -> new AtomNode(new VarVal(currentToken.getValue()));
            case NULL -> new AtomNode(new NullVal());
            case BOOLEAN -> new AtomNode(new BoolVal(currentToken.getValue().equals("true")));
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
