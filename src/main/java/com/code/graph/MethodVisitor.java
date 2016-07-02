package com.code.graph;

/**
 * Created by Beto on 6/30/16.
 */

import com.code.graph.CFG.CFG;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

/**
 * Simple visitor implementation for visiting MethodDeclaration nodes.
 */
public class MethodVisitor extends VoidVisitorAdapter {

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        /* here you can access the attributes of the method.
        this method will be called for all methods in this
        CompilationUnit, including inner class methods */

        BlockStmt block = new BlockStmt(); //In java methods are treated as blocks.
        block = n.getBody();

        CFG cfg = new CFG(block);
        super.visit(n, arg); //Makes a note of the methods visited by marking them through VoidVisitorAdapter


    }

    @Override
    public void visit(IfStmt m, Object arg){
        /* here you can access the attributes of the if statments.
        This method will be called for all if statements in this
        CompilationUnit, including inner class methods */
    }

    @Override
    public void visit(ForeachStmt m, Object arg){
        /* here you can access the attributes of the foreach statments.
        This method will be called for all foreach statements in this
        CompilationUnit, including inner class methods */
    }

    @Override
    public void visit(ForStmt m, Object arg){
        /* here you can access the attributes of the for statments.
        This method will be called for all for statements in this
        CompilationUnit, including inner class methods */
    }

    @Override
    public void visit(WhileStmt m, Object arg){
        /* here you can access the attributes of the while statments.
        This method will be called for all while statements in this
        CompilationUnit, including inner class methods */
    }

    @Override
    public void visit(BreakStmt m, Object arg){
        /* here you can access the attributes of the while statments.
        This method will be called for all while statements in this
        CompilationUnit, including inner class methods */
    }

    @Override
    public void visit(SwitchStmt m, Object arg){
        /* here you can access the attributes of the while statments.
        This method will be called for all while statements in this
        CompilationUnit, including inner class methods */
    }

}
