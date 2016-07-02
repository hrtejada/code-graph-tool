package com.code.graph.CFG;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 */
public class CFG extends VoidVisitorAdapter{
    private BlockStmt codeBlock;//This block holds all the statements that are passed by the parser
    private int Start; //This refers to line in which the CFG starts
    private int End; //This refers to line in which the CFG ends
    private Node StartNode;
    private Node EndNode;
    private List<Node> nodes;//Might not neeeeed this
    private List<Edge> edges;//Same as above
    private List<Statement> statements;//Also same haha

    public CFG(BlockStmt block) {
        Start = block.getBeginLine();
        End = block.getEndLine();
        codeBlock = block;
        build();
    }

    private void build(){

        statements = codeBlock.getStmts();

        int statementIterator = 1;
        for(Statement statement: statements){
            System.out.println(statementIterator + " ");
            System.out.println(statement);
            statementIterator++;
            if(statement instanceof ForStmt){
                System.out.println("This is a for loop statement!");
            }
            if(statement instanceof IfStmt){
                System.out.println("This is an if statement!");
            }
            if(statement instanceof WhileStmt){
                System.out.println("This is a while loop statement");
            }
        }
        //Ok so, in Java blocks are executed as a single statement. That's why if you run this, you can see the statement separated by commas and a block is seen as a statement.
        //What's a block in java? https://docs.oracle.com/javase/tutorial/java/nutsandbolts/expressions.html

        
    }

    private void createNode(){

    }


    private List<Statement> getSatements(){
        return statements;
    }

    private int getStart(){
        return Start;
    }

    private int getEnd(){
        return End;
    }
}
