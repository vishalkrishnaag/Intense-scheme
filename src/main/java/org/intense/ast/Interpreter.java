package org.intense.ast;

import org.intense.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Interpreter {
    SymbolTable environment;
    public Interpreter(SymbolTable environment) {
        this.environment = environment;
    }

    public void run(List<ASTNode> astNodes) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Object>> results = new ArrayList<>();
        for (ASTNode node : astNodes) {
            System.out.println("exec node -> "+node);
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
