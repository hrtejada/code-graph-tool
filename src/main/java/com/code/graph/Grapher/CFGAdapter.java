package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.*;

import java.util.List;

/**
 * Created by Beto on 7/3/16.
 * Adoater for GraphBuilder
 */
public class CFGAdapter implements GraphBuilder {

    CFG cfg;

    public CFGAdapter(CFG newCFG){
        cfg = newCFG;
    }


    public void build(BlockStmt method) {
        List<Statement> statements = method.getStmts();
        Node currNode = new Node();//Gets first statement

        if(!cfg.startNodeSet()){
            //cfg.setStartNode();
        }

        for(Statement statement: statements){
            statements.remove(statement);
            if(statement instanceof ForStmt){
                forHandler((BlockStmt) statement);
            }

            if(statement instanceof IfStmt){
                System.out.println("This is an if statement!");
            }

            if(statement instanceof WhileStmt){
                System.out.println("This is a while loop statement");
            }

        }
    }


    public void createXML() {
        //Create CFG XML from cfg
    }

    private void forHandler(BlockStmt stmt){
        List<Statement> statements = stmt.getStmts();
        for(Statement currStatement : statements){

        }
    }

    //This method will be removed. ONly here to show that program parses and grabs statements appropriatley.
    public void printContents(BlockStmt method){
        List<Statement> statements = method.getStmts();

        int statementIterator = 1;
        for(Statement statement: statements){

            System.out.println(statementIterator + " ");
            System.out.println(statement);
            statementIterator++;
            if(statement instanceof ForStmt){
                ForStmt forStmnt = (ForStmt) statement;
                forStmnt.getBody();
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
}
