package org.intense;
import org.intense.Ast.ASTNode;
import org.intense.Ast.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            // File mode
            try {
                String content = new String(Files.readAllBytes(Paths.get(args[0])));
                Lexer lexer = new Lexer(content);
                Parser parser = new Parser(lexer);
                List<ASTNode> astNodes = parser.getParseTree();
                Environment environment = new Environment();
                Interpreter interpreter = new Interpreter(environment);
                interpreter.run(astNodes);

            } catch (IOException | ExecutionException | InterruptedException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        } else {
            // REPL mode
            System.out.println("Intense REPL (type 'exit' to quit)");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("> ");
                try {
                    String input = reader.readLine();
                    if (input == null || input.equalsIgnoreCase("exit")) {
                        break;
                    }

                    long startTime = System.nanoTime();
                    Lexer lexer = new Lexer(input);
                    Parser parser = new Parser(lexer);
                    List<ASTNode> astNodes = parser.getParseTree();
                    Environment environment = new Environment();
                    Interpreter interpreter = new Interpreter(environment);
                    interpreter.run(astNodes);
                    long endTime = System.nanoTime();
                    long durationMicro = (endTime - startTime) / 1_00000;
                    System.out.println("Time taken: " + durationMicro + " µs");
                } catch (IOException e) {
                    System.err.println("Error reading input: " + e.getMessage());
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


