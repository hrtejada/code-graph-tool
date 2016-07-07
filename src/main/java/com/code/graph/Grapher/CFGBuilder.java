package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.*;

import java.util.List;

/**
 * Created by Beto on 7/3/16.
 * Adoater for GraphBuilder
 */
public class CFGBuilder implements GraphBuilder {

    CFG cfg;

    public CFGBuilder(CFG newCFG){
        cfg = newCFG;
    }


    public void build(BlockStmt method) {
        List<Statement> statements = method.getStmts();
        Node currNode = new Node();//Gets first statement

        if(!cfg.startNodeSet()){
            StartNode startNode = new StartNode(method);
            cfg.setStartNode(startNode);
            currNode = startNode;
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

        for(Statement statement: statements){

            if(statement instanceof ForStmt){
                System.out.println("For Statement:");
                ForStmt stmt = (ForStmt) statement;
                System.out.println("Initial statement:");
                System.out.println(stmt.getInit());
                System.out.println("Compare statement:");
                System.out.println(stmt.getCompare());
                System.out.println("Update statement:");
                System.out.println(stmt.getUpdate());
                System.out.println("Body:");
                System.out.println(stmt.getBody());
                System.out.println();

            }
            else if(statement instanceof IfStmt){
                System.out.println("If Statement:");
                IfStmt stmt = (IfStmt) statement;
                System.out.println("Conditional statement:");
                System.out.println(stmt.getCondition());
                System.out.println("If then statement:");
                System.out.println(stmt.getThenStmt());
                System.out.println("Else statement:");
                System.out.println(stmt.getElseStmt());
                System.out.println();
            }
            else if(statement instanceof WhileStmt){
                System.out.println("While loop statement:");
                WhileStmt stmt = (WhileStmt) statement;
                System.out.println("Conditional statement:");
                System.out.println(stmt.getCondition());
                System.out.println("Body statement:");
                System.out.println(stmt.getBody());
                System.out.println();
            }
            else{
                System.out.println("Statement:");
                System.out.println(statement);
                System.out.println();
            }
        }
        //Ok so, in Java blocks are executed as a single statement. That's why if you run this, you can see the statement separated by commas and a block is seen as a statement.
        //What's a block in java? https://docs.oracle.com/javase/tutorial/java/nutsandbolts/expressions.html
    }
}
