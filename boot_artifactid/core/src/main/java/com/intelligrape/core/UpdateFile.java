package com.intelligrape.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.felix.scr.annotations.Service;

@Service
public class UpdateFile implements Callable<String> {
    
    private final String str;
    String fullStr = "";

    public UpdateFile(final String str) {
	this.str = str;
    }
    
    @Override
    public String call() throws Exception {
	printToFile(str);
	System.out.println(str);
	return str;
    }

    public static void printToFile(final String str) throws IOException {
	final File file = new File("/media/ttnd/Data/test.txt");
	if (!file.exists()) {
	    file.createNewFile();
	}
	final FileWriter fw = new FileWriter(file.getAbsoluteFile());
	final BufferedWriter bw = new BufferedWriter(fw);
	bw.append(str);
	bw.close();
    }

}
