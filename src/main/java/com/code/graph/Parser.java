package com.code.graph;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;

/**
 * Created by Beto on 6/30/16.
 */
public class Parser extends VoidVisitorAdapter {

    public Parser(){
    }

    public static void parseJavaFile(File File) throws IOException, ParseException {
        String status = "Status: Testing method visitor.";
        System.out.println(status);

        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(File);//Add your arguments in your IDE! Go to your run config and specify the file there.
        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        System.out.println("Visiting Methods in Factorial.java");
        System.out.println();
        new Visitor().visit(cu, null);
        System.out.println();

    }


}
