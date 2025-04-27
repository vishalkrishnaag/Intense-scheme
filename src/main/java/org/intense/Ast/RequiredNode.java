package org.intense.Ast;

import org.intense.Environment;
import org.intense.Lexer;
import org.intense.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RequiredNode extends ASTNode {
    String dependency;
    public RequiredNode(String value) {
        this.dependency = value;
    }

    /**
     * @param env Environment which handles this
     * @return object
     */
    @Override
    public Object eval(Environment env) {
        // Step 1: Convert the string path to a Path object (works across platforms)
        Path path = Paths.get(dependency);


        // Step 2: Check if the file or folder exists
        File file = path.toFile();

        if (file.exists()) {
            try {
            // Check if it's a directory or file
            if (file.isDirectory()) {
                System.out.println("The path is a directory: " + file.getAbsolutePath());
            } else if (file.isFile()) {
                System.out.println("executing file: " + file.getAbsolutePath());
                String content = new String(Files.readAllBytes(file.toPath()));
                Lexer lexer = new Lexer(content);
                Parser parser = new Parser(lexer);
                List<ASTNode> astNodes = parser.getParseTree();
                Environment environment = new Environment(env);
                Interpreter interpreter = new Interpreter(environment);
                interpreter.run(astNodes);
            } else {
                System.out.println("The path exists, but it's neither a file nor a directory.");
            }
            } catch (ExecutionException | InterruptedException | IOException e) {
                System.err.println("Failed to read file: " + e.getMessage());
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("The path does not exist: " + file.getAbsolutePath());
        }
        return null;
    }
}
