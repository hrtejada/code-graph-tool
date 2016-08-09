package com.code.graph;

/**
 * Created by Beto on 6/30/16.
 */

import com.code.graph.Grapher.CFG;
import com.code.graph.Grapher.CFGBuilder;
import com.code.graph.Grapher.CFGdirector;
import com.code.graph.Grapher.concreteCFG;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Simple visitor implementation for visiting MethodDeclaration nodes.
 */
public class Visitor extends VoidVisitorAdapter {

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        /* here you can access the attributes of the method.
        this method will be called for all methods in this
        CompilationUnit, including inner class methods */

        //Builds a CFG for each method visited
        CFG newCFG = new CFG();
        CFGBuilder graph = new concreteCFG(newCFG);

        CFGdirector director = new CFGdirector(graph);
        director.buildCFG(n);
        //graph.outputToFile();
        super.visit(n, arg); //Makes a note of the methods visited by marking them through VoidVisitorAdapter


    }


}
