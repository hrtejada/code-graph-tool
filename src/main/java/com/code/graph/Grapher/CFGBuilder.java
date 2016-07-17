package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.*;
import com.sun.deploy.util.BlackList;

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
        }

        for(Statement statement: statements){
            isConditional(statement);
        }
    }


    /* Method to handle conditional statements such as If.
    *
    *
    * */
    private Boolean isConditional(Statement statement) {

        if(statement instanceof ForStmt){

            Statement expressionStatementCheck = ((ForStmt) statement).getBody();
            Statement nestedBlockCheck = ((ForStmt) statement).getBody();

            if(expressionStatementCheck instanceof ExpressionStmt){//In case if does not have brackets
                forHandler((ExpressionStmt) expressionStatementCheck);
            }

            else if (nestedBlockCheck instanceof ForStmt){
                BlockStmt newNestedBlock = (BlockStmt) ((ForStmt) nestedBlockCheck).getBody();
                forHandler(newNestedBlock);
            }

            else {
                BlockStmt newBlock = (BlockStmt) ((ForStmt) statement).getBody();
                forHandler(newBlock);
            }

            return true;
        }

        else if(statement instanceof IfStmt){//////Refractor this hahahahaha

            Statement expressionStatementCheck = ((IfStmt) statement).getThenStmt();
            if (expressionStatementCheck  instanceof ExpressionStmt){//In case if does not have brackets
                ifHandler((ExpressionStmt) expressionStatementCheck);
            }
            else {
                BlockStmt newIfBlock = (BlockStmt) ((IfStmt) statement).getThenStmt();
                System.out.println(newIfBlock.getBeginLine());
                ifHandler(newIfBlock);

                Statement elsePart = ((IfStmt) statement).getElseStmt();
                while (elsePart != null)
                {
                    if(elsePart instanceof ExpressionStmt){//In case else doesn not have brackets.
                        elseHandler((ExpressionStmt) elsePart);
                        break;
                    }

                    else if (elsePart instanceof BlockStmt) {//this is an "else".
                        BlockStmt elseStmt = (BlockStmt) elsePart;
                        elseHandler(elseStmt);
                        break;
                    }
                    else {//this is an "else if".

                        //Get if then statement and send to if handler.
                        BlockStmt elseIfStmt = (BlockStmt) ((IfStmt)elsePart).getThenStmt();
                        ifHandler(elseIfStmt);

                        //Then you get the else statement and assign to elsePart.
                        elsePart = ((IfStmt) elsePart).getElseStmt();
                    }
                }
            }

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
        System.out.println();
    }

    private void elseHandler(ExpressionStmt elseBlock) {
        System.out.println("In elseHandler (exprs) method");
        System.out.println(elseBlock.toString());
        System.out.println(elseBlock.getExpression());//For testing, remove later.
        System.out.println();
    }


    private void ifHandler(BlockStmt ifBlock) {
        System.out.println("In ifHandler method");
        System.out.println(ifBlock.toString());
        List<Statement> statements = ifBlock.getStmts();

        printStatements(statements);//FOr testing, remove later
        System.out.println();
    }

    private void ifHandler(ExpressionStmt ifBlock) {
        System.out.println("In ifHandler (Exprs) method");
        System.out.println(ifBlock.getExpression());//For testing, remove later.
        System.out.println();
    }

    private void forHandler(ExpressionStmt expressionFor) {
        System.out.println("In forHandler (Exprs) method");
        System.out.println(expressionFor.getExpression());//For testing, remove later.
        System.out.println();
    }

    private void forHandler(BlockStmt forBlock){
        System.out.println("In forHandler method");
        List<Statement> statements = forBlock.getStmts();
        System.out.println(forBlock.getBeginLine());

        printStatements(statements);//FOr testing, remove later
        System.out.println();

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
            System.out.println(currStatement.getBeginLine());
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
