package com.intelligrape.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ConcurrentExecuter {
    public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {

	final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

	final List<Future<String>> resultList = new ArrayList<Future<String>>();
	
	for (int i = 1; i < 21; i++) {
	    
	    final UpdateFile update = new UpdateFile("Hello-" + i);
	    final Future<String> result = executor.submit(update);

	    resultList.add(result);
	}

	executor.shutdown();
	
    }

}
