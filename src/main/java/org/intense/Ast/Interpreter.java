package org.intense.Ast;

import org.intense.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Interpreter {
    Environment environment;
    public Interpreter(Environment environment) {
        this.environment = environment;
    }

    public void run(List<ASTNode> astNodes) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Object>> results = new ArrayList<>();
        ASTNode.env = environment;
        for (ASTNode node : astNodes) {
            results.add(executor.submit(node::eval));
        }

        for (Future<Object> future : results) {
            Object result = future.get();  // safely unwrap
            if (result != null) {
                System.out.println(result);
            } else {
                System.err.println("Error: Ups :) time to rethink the logic");
            }
        }
        executor.shutdown();

    }

}
