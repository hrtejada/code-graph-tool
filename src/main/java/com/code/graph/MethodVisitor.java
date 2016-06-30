package com.code.graph;

/**
 * Created by Beto on 6/30/16.
 */

import com.code.graph.CFG.CFG;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Simple visitor implementation for visiting MethodDeclaration nodes.
 */
public class MethodVisitor extends VoidVisitorAdapter {

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        // here you can access the attributes of the method.
        // this method will be called for all methods in this
        // CompilationUnit, including inner class methods
        System.out.println(n.getName());//Gets method names.
        //System.out.println(n.getBody());//Gets contents of method.

        List<Statement> statements;

        //Test to see if we can get contents of method and set them in a block.
        System.out.print("Statements are: ");
        BlockStmt block = new BlockStmt();
        block = n.getBody();

        statements = block.getStmts();
        int x =0;
        for(Statement statement: statements){
            System.out.println(x + " ");
            System.out.println(statement);
            x++;
        }
        //Ok so, in Java blocks are executed as a single statement. That's why if you run this, you can see the statement separated by commas and a block is seen as a statement.
        //What's a block in java? https://docs.oracle.com/javase/tutorial/java/nutsandbolts/expressions.html

        CFG cfg = new CFG(block);
        super.visit(n, arg);//Makes a note of the methods visited by marking them through VoidVisitorAdapter


    }
}
