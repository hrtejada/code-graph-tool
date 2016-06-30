package com.code.graph;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import java.io.*;
import java.util.*;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sun.glass.ui.EventLoop;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Team 1
 * Code Graph
 *
 */
public class CodeGraph
{
    public static void main( String[] args ) throws IOException, ParseException {

        if (args.length > 0) {//Grabs file argument
            File file = new File(args[0]);
            new Parser().parseJavaFile(file);
        }
        else{
            System.out.println("Error, no java file specified.");
            System.exit(0);
        }
    }

}
