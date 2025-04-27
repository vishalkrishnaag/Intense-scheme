package org.intense.Ast;

import org.intense.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Interpreter {
    Environment environment;
    public Interpreter(Environment environment) {
        this.environment = environment;
    }

    public void run(List<ASTNode> astNodes) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Object>> results = new ArrayList<>();
        for (ASTNode node : astNodes) {
            Callable<Object> task = () ->node.eval(environment);
            results.add(executor.submit(task));
        }

        for (Future<Object> future : results) {
            Object result = future.get();  // safely unwrap
                System.out.println(result);
        }
        executor.shutdown();

    }

}
