package org.intense;

import java.io.File;
import java.nio.file.Files;

public class intenseCompiler {

    public void compile(File inputFile, File outputDir) throws Exception {
        if (inputFile.isFile()) {
            try {
                String content = new String(Files.readAllBytes(inputFile.toPath()));

                Lexer lexer = new Lexer(content);
                SymbolTable environment = new SymbolTable(null);
                Parser parser = new Parser(lexer, environment);
                var astNodes = parser.getParseTree();
                TreeWalk treeWalk = new TreeWalk(environment);

                String nameWithoutExtension = getNameWithoutExtension(inputFile.getName());
                File newFile = new File(outputDir, nameWithoutExtension + ".kt");

                treeWalk.generateKotlinFile(astNodes, newFile.getPath());

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            throw new Exception("Input file is required");
        }
    }

    private String getNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) return fileName;
        return fileName.substring(0, dotIndex);
    }
}





