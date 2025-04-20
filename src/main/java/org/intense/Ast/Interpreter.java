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

        List<Future<Result<Object>>> results = new ArrayList<>();
        for (ASTNode node : astNodes) {
            results.add(executor.submit(() -> node.eval(environment)));
        }

        for (Future<Result<Object>> future : results) {
            Result<Object> result = future.get();  // safely unwrap
            if (result.isSuccess()) {
                System.out.println("Result: " + result.value());
            } else {
                System.err.println("Error: " + result.error().getMessage());
            }
        }
        executor.shutdown();

    }

}
