package org.intense;

import com.google.googlejavaformat.Input;
import org.intense.Symbols.ClassSymbol;
import org.intense.Symbols.FunctionSymbol;
import org.intense.Symbols.ValSymbol;
import org.intense.Symbols.VarSymbol;
import org.intense.Types.CustomDataType;
import org.intense.Types.GenericType;
import org.intense.Types.Type;
import org.intense.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final Lexer lexer;
    private final List<String> functionDefinitions = new ArrayList<>();
    private Token currentToken;
    private final List<ASTNode> nodeList;
    private final SymbolTable env;
    private Map<String, Type> FieldList =new HashMap<>();
    private Map<String, FunctionSymbol> methodList=new HashMap<>();
    private boolean classEnabled=false;
    private boolean returnEnabled=false;

    public Parser(Lexer lexer,SymbolTable env) {
        this.env = env;
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
        this.nodeList = new ArrayList<>();
        while (currentToken.getType() != TokenType.EOF) {
            ASTNode input = this.parse();
            if (input != null)
                this.nodeList.add(input);
        }

    }

    private void throwClassNotEnabled(){
        if(!classEnabled)
        {
            throw new RuntimeException(currentToken+" is only allowed inside a class");
        }
    }

    public ASTNode parse() {
        switch (currentToken.getType()) {
            case TokenType.LPAREN -> {
                advance();
                List<ASTNode> elements = new ArrayList<>();
                if (currentToken.getType() == TokenType.SYMBOL)
                {
                    // method call (x :y 30 :z "some values") or built in
                   ASTNode callNode= parseCallNode();
                    consume(TokenType.RPAREN);
                    return callNode;
                }
                else {
                    while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                        elements.add(parse());
                    }
                    consume(TokenType.RPAREN); // eat `)`
                    return new DataListNode(elements);
                }

            }
            case TokenType.IMPORT -> {
                advance();
                Token token = currentToken;
                consume(TokenType.STRING);
                AtomNode casting_var = null;
                if(currentToken.getType()==TokenType.AS)
                {
                    advance();
                    casting_var = parseIfAtom();
                    advance();
                }
                return new ImportNode(token.getValue(),casting_var);
            }
            case RETURN -> {
                advance();
                returnEnabled = true;
                return new ReturnNode(parse());
            }
            case PACKAGE -> {
                advance();
                AtomNode atomNode = parseIfAtom();
                advance();
               return new PackageNode(atomNode.getValue());
            }
            case WHILE -> {
                throwClassNotEnabled();
                advance();
               return new WhileConditionNode(parse(),parse());
            }
            case SET -> {
                throwClassNotEnabled();
                advance();
                AtomNode first = parseIfAtom();
                advance();
                List<ASTNode> rest = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    rest.add(parse());
                }
                if(rest.isEmpty())
                {
                    throw new RuntimeException("(Invalid syntax set assign assignee)");
                }
              return new SetNode(first,new DataListNode(rest));
            }
            case GET -> {
                throwClassNotEnabled();
                advance();
                ASTNode first = parseAtom();
                return new GetNode(first);
            }
            case TokenType.DEF -> {
                returnEnabled = false;
                throwClassNotEnabled();
                advance();
                AtomNode atom = parseIfAtom();
                List<ASTNode> elements = new ArrayList<>();
                List<DefArgumentNode> arguments = null;
                // function name should be a symbol
                consume(TokenType.SYMBOL);
                consume(TokenType.COLON);
                ASTNode dataType = parseAtom();
                boolean question = currentToken.getType() == TokenType.NULLABLE;
                if (question) {
                    advance();
                }
                if(currentToken.getType()==TokenType.LPAREN)
                {
                   arguments= parseUsing();
                    consume(TokenType.RPAREN);
                } else if (currentToken.getType()==TokenType.LLIST) {
                    arguments=parseUsing();
                    consume(TokenType.RLIST);
                }
                else {
                    throw new RuntimeException("expected an argument list , if no argument list then [] is expected");
                }
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                if(arguments!=null)
                {
                    env.define(atom.getValue(),new FunctionSymbol(arguments.size(),new GenericType()));
                }
                else{
                    env.define(atom.getValue(),new FunctionSymbol(0,new GenericType()));
                }
                if(question && !returnEnabled)
                {
                    throw new RuntimeException("a return statement is expected in method "+atom.getValue());
                }
                returnEnabled = false;
                return new DefNode(atom,dataType,arguments,elements,question);
            }
            case TokenType.CLASS ->{
                classEnabled = true;
                advance(); // eat class
                List<ASTNode> elements = new ArrayList<>();
                AtomNode atomNode = parseIfAtom();
                advance();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                env.define(atomNode.getValue(),new  ClassSymbol(atomNode.getValue(),null,FieldList,methodList,new GenericType()));
              return new ClassNode(atomNode,elements);
            }
            case TokenType.ENUM ->{
                advance(); // eat enum
                List<ASTNode> elements = new ArrayList<>();
               AtomNode atomNode = parseIfAtom();
                advance();
                while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                return new EnumNode(atomNode,elements);
            }
            case TokenType.VAL -> {
                throwClassNotEnabled();
                advance();
                AtomNode atom = parseIfAtom();
                // val x:String?
                consume(TokenType.SYMBOL);
                AtomNode dataType = null;
                if(currentToken.getType() == TokenType.COLON)
                {
                    advance();
                    dataType = parseIfAtom();
                    advance();
                }
                boolean question = currentToken.getType() == TokenType.NULLABLE;
                if (question) {
                    advance();
                }
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN &&
                        currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                if(dataType!=null) {
                    env.defineV(atom.getValue(), new ValSymbol(new CustomDataType()));
                }
                return new ValNode(atom, dataType, new DataListNode(elements), question);
            }
            case TokenType.VAR -> {
                throwClassNotEnabled();
                advance();
                AtomNode atom = parseIfAtom();
                // var
                AtomNode dataType = null;
                consume(TokenType.SYMBOL);
                if(currentToken.getType() == TokenType.COLON)
                {
                    advance();
                    dataType = parseIfAtom();
                    advance();
                }
                boolean question = currentToken.getType() == TokenType.NULLABLE;
                if (question) {
                    advance();
                }
                List<ASTNode> elements = new ArrayList<>();
                while (currentToken.getType() != TokenType.RPAREN &&
                        currentToken.getType() != TokenType.EOF) {
                    elements.add(parse());
                }
                if(dataType!=null)
                {
                    env.defineV(atom.getValue(),new VarSymbol(new CustomDataType()));
                }
                return new VarNode(atom,dataType, new DataListNode(elements), question);
            }
            case TokenType.IF -> {
                throwClassNotEnabled();
                advance();
                // function name should be a symbol
                ASTNode ifExpr = parse();
                ASTNode ifBody = parse();
                ASTNode elseBody = parse();
                return new IfConditionNode(ifExpr, ifBody, elseBody);
            }
            case TokenType.LLIST -> {
                advance();
                if(currentToken.getType()==TokenType.SYMBOL)
                {
                    ASTNode call = parseCallNode();
                    consume(TokenType.RLIST);
                    return call;
                }
                else {
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
            case TokenType.RBRACE -> {
                throw new RuntimeException("unexpected closing ) or } or ] , got " + currentToken);
            }
            case TokenType.RPAREN, RLIST -> {
                return null;
            }
            default -> {
                return parseAtom();
            }
        }
    }

    private List<DefArgumentNode> parseUsing(){
        List<DefArgumentNode> arguments = new ArrayList<>();
        advance();
        if(currentToken.getType()==TokenType.USING)
        {
            advance();
            while (currentToken.getType() != TokenType.RPAREN &&
                    currentToken.getType() != TokenType.RLIST &&
                    currentToken.getType() != TokenType.EOF) {
                AtomNode argAtom = parseIfAtom();
                // atom:Type "value"
                consume(TokenType.SYMBOL);
                consume(TokenType.COLON);
                ASTNode argDataType = parseAtom();
                boolean question = currentToken.getType() == TokenType.NULLABLE;
                if (question) {
                    advance();
                }
                arguments.add(new DefArgumentNode(argAtom,argDataType,question));
            }
        } else if (currentToken.getType()==TokenType.RLIST ) {
            return null;
        }else if (currentToken.getType()==TokenType.RPAREN ) {
            return null;
        }
        else {
            throw new RuntimeException("error in function definition expected a ) or ]");
        }
        return arguments;
    }

    private DataTypeNode parseDataTypes() {
        // type <xyz | p q r s >
        advance(); // eat `<`
        List<ASTNode> values = new ArrayList<>();
            int Count =1;
            while (Count != 0) {
                if(currentToken.getType() == TokenType.LESS_THAN) {
                    Count++;
                    values.add(parseDataTypes());
                }
                else if(currentToken.getType() == TokenType.GREATER_THAN) {
                    Count--;
                    advance();
                }
                else {
                    values.add(parseAtom());
                }
            }
        return new DataTypeNode(values);
    }

    private ASTNode parseCallNode() {
        ASTNode first = parseAtom();
        boolean is_std = (currentToken.getType() == TokenType.NULLABLE);
        if (is_std) {
            advance();
        }
        List<ASTNode> rest = new ArrayList<>();
        while (currentToken.getType() != TokenType.RPAREN && currentToken.getType() != TokenType.EOF) {
            rest.add(parse());
        }

        return new CallNode(first, is_std, rest);
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
            case NONE:
                yield new AtomNode(TokenType.NONE, "Unit");
            case DOT:
                yield new AtomNode(TokenType.DOT, token.getValue());
            case COLON:
                yield new AtomNode(TokenType.COLON, token.getValue());
            case NULL:
                yield new AtomNode(TokenType.NULL, token.getValue());
            case NULLABLE:
                yield new AtomNode(TokenType.NULLABLE, token.getValue());
            case ANY_KEYWORD:
                yield new AtomNode(TokenType.ANY_KEYWORD,token.getValue());
            case STRING_KEYWORD:
                yield new AtomNode(TokenType.STRING_KEYWORD, token.getValue());
            case INT_KEYWORD:
                yield new AtomNode(TokenType.INT_KEYWORD, token.getValue());
            case DOUBLE_KEYWORD:
                yield new AtomNode(TokenType.DOUBLE_KEYWORD, token.getValue());
            case BOOLEAN_KEYWORD:
                yield new AtomNode(TokenType.BOOLEAN_KEYWORD, token.getValue());
            case FLOAT_KEYWORD:
                yield new AtomNode(TokenType.FLOAT_KEYWORD, token.getValue());
            case BYTE_KEYWORD:
                yield new AtomNode(TokenType.BYTE_KEYWORD, token.getValue());
            case SHORT_KEYWORD:
                yield new AtomNode(TokenType.SHORT_KEYWORD, token.getValue());
            case LONG_KEYWORD:
                yield new AtomNode(TokenType.LONG_KEYWORD, token.getValue());
            case UBYTE_KEYWORD:
                yield new AtomNode(TokenType.UBYTE_KEYWORD, token.getValue());
            case ULONG_KEYWORD:
                yield new AtomNode(TokenType.ULONG_KEYWORD, token.getValue());
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
                } else if (currentToken.getType()==TokenType.DOT) {
                    // symbol.symbol
                    List<String> elements = new ArrayList<>();
                    while (currentToken.getType() == TokenType.DOT || currentToken.getType() == TokenType.SYMBOL) {
                        if(currentToken.getType() == TokenType.DOT)
                        {
                            elements.add(".");
                            advance();
                        }
                        elements.add(parseIfAtom().getValue());
                        advance();
                    }
                    yield new LinkingNode(token.getValue(),elements);
                } else if (currentToken.getType()==TokenType.LESS_THAN) {
                   yield new CustomDataTypeNode(token.getValue(),parseDataTypes());
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
            case NUMBER -> new AtomNode(TokenType.NUMBER, currentToken.getValue());
            case STRING -> new AtomNode(TokenType.STRING, currentToken.getValue());
            case SYMBOL -> {
                String identifier = currentToken.getValue();
                if (identifier.contains("-")) {
                    identifier = "in10s_" + identifier.replace("-", "_");
                }
                yield new AtomNode(TokenType.SYMBOL, identifier);
            }
            case BOOLEAN -> new AtomNode(TokenType.BOOLEAN, currentToken.getValue());
            case COLON -> new AtomNode(TokenType.COLON, currentToken.getValue());
            case DOT -> new AtomNode(TokenType.DOT, currentToken.getValue());
            case NULL -> new AtomNode(TokenType.NULL, currentToken.getValue());
            case NULLABLE -> new AtomNode(TokenType.NULLABLE, currentToken.getValue());
            case ANY_KEYWORD -> new AtomNode(TokenType.ANY_KEYWORD, currentToken.getValue());
            case STRING_KEYWORD -> new AtomNode(TokenType.STRING_KEYWORD, currentToken.getValue());
            case INT_KEYWORD -> new AtomNode(TokenType.INT_KEYWORD, currentToken.getValue());
            case DOUBLE_KEYWORD -> new AtomNode(TokenType.DOUBLE_KEYWORD, currentToken.getValue());
            case BOOLEAN_KEYWORD -> new AtomNode(TokenType.BOOLEAN_KEYWORD, currentToken.getValue());
            case FLOAT_KEYWORD -> new AtomNode(TokenType.FLOAT_KEYWORD, currentToken.getValue());
            case BYTE_KEYWORD -> new AtomNode(TokenType.BYTE_KEYWORD, currentToken.getValue());
            case SHORT_KEYWORD -> new AtomNode(TokenType.SHORT_KEYWORD, currentToken.getValue());
            case LONG_KEYWORD -> new AtomNode(TokenType.LONG_KEYWORD, currentToken.getValue());
            case UBYTE_KEYWORD -> new AtomNode(TokenType.UBYTE_KEYWORD, currentToken.getValue());
            case ULONG_KEYWORD -> new AtomNode(TokenType.ULONG_KEYWORD, currentToken.getValue());
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
