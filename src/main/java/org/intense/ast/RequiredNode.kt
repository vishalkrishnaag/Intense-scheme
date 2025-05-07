package org.intense.ast;

import org.intense.SymbolTable;

class RequiredNode(value: String) : ASTNode() {
   lateinit var dependency: String
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

    override fun  eval(env:SymbolTable) :String {
        // Step 1: Convert the string path to a Path object (works across platforms)
//        Path path = Paths.get(dependency);


        // Step 2: Check if the file or folder exists
//        File file = path.toFile();
//
//        if (file.exists()) {
//            try {
//            // Check if it's a directory or file
//            if (file.isDirectory()) {
//                System.out.println("The path is a directory: " + file.getAbsolutePath());
//            } else if (file.isFile()) {
//                System.out.println("executing file: " + file.getAbsolutePath());
//                String content = new String(Files.readAllBytes(file.toPath()));
//                Lexer lexer = new Lexer(content);
//                Parser parser = new Parser(lexer);
//                List<ASTNode> astNodes = parser.getParseTree();
//                SymbolTable environment = new SymbolTable(env);
//                Interpreter interpreter = new Interpreter(environment);
//                interpreter.run(astNodes);
//            } else {
//                System.out.println("The path exists, but it's neither a file nor a directory.");
//            }
//            } catch (ExecutionException | InterruptedException | IOException e) {
//                System.err.println("Failed to read file: " + e.getMessage());
//                throw new RuntimeException(e);
//            }
//
//        } else {
//            System.out.println("The path does not exist: " + file.getAbsolutePath());
//        }
        return "";
    }
}
