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
            isConditional(statement);
        }
    }

    private Boolean isConditional(Statement statement) {
        if(statement instanceof ForStmt){
            System.out.println("In build method");
            System.out.println(((ForStmt) statement).getBody());
            BlockStmt newBlock = (BlockStmt) ((ForStmt) statement).getBody();
            forHandler(newBlock);
            return true;
        }
        else if(statement instanceof IfStmt){
            BlockStmt newIfBlock = (BlockStmt) ((IfStmt) statement).getThenStmt();
            ifHandler(newIfBlock);

            Statement elseStatement = ((IfStmt) statement).getElseStmt();
            BlockStmt newElseBlock = (BlockStmt) ((IfStmt) elseStatement).getThenStmt();
            elseHandler(newElseBlock);


            return true;

        }
        else if(statement instanceof WhileStmt){
            BlockStmt newBlock = (BlockStmt) ((WhileStmt) statement).getBody();
            forHandler(newBlock);

            return true;
        }

        else{
            return false;
        }
    }

    private void elseHandler(BlockStmt elseBlock) {
        System.out.println("In elseHandler method");
        System.out.println(elseBlock.toString());
        List<Statement> statements = elseBlock.getStmts();

        printStatements(statements);//FOr testing, remove later
    }

    private void ifHandler(BlockStmt ifBlock) {
        System.out.println("In ifHandler method");
        System.out.println(ifBlock.toString());
        List<Statement> statements = ifBlock.getStmts();

        printStatements(statements);//FOr testing, remove later
    }


    private void forHandler(BlockStmt forBlock){
        System.out.println("In forHandler method");
        List<Statement> statements = forBlock.getStmts();

        printStatements(statements);//FOr testing, remove later

        for(Statement currStatement : statements){
            if(isConditional(currStatement)){
                //Build something
            }
            else{
                //Continue adding statements to Node
            }
        }
        //Make edge to go back to top of forloop
    }

    private void printStatements(List<Statement> statements){
        int x = 1;
        for(Statement currStatement : statements){
            System.out.println("Statemnt number " + x + ": ");
            System.out.println(currStatement.toString());
            x++;
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

    public void createXML() {
        //Create CFG XML from cfg
    }
}
