package com.code.graph.Grapher;

import com.code.graph.Visitor;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 * This class is an adaptee of the graphAdapter class.
 */
public class CFG implements CFGBuilder{
    private Node StartNode;
    private Node EndNode;

    public CFG() {
    }

    public void build(BlockStmt method) {
        List<Statement> statements = method.getStmts();
        Node currNode = new Node();//Gets first statement

        if(startNodeSet() == false){
            StartNode startNode = new StartNode(method, currNode);
            setStartNode(startNode);
        }

        for(Statement statement: statements){
            if(isConditional(statement) == true) {
                currNode = handleConditional(statement, currNode);
            }
            else{
                currNode.addLineNumbers(statement.getBeginLine());
            }

        }

        EndNode = new Node(0);
        currNode.addEdgeGoingTo(EndNode);

        printTree(getStartNode());
    }

    public Boolean isConditional(Statement statement) {
        if(statement instanceof IfStmt){
            return true;
        }

        else if(statement instanceof ForStmt){
            return true;
        }

        else if(statement instanceof WhileStmt){
            return true;
        }

        else{
            return false;
        }
    }

    public Node handleConditional(Statement statement, Node currNode){

        if(statement instanceof ForStmt){

            Statement expressionStatementCheck = ((ForStmt) statement).getBody();
            Statement nestedBlockCheck = ((ForStmt) statement).getBody();

            if(expressionStatementCheck instanceof ExpressionStmt){//In case if does not have brackets
                currNode = forHandler((ExpressionStmt) expressionStatementCheck, currNode);
            }

            else if (nestedBlockCheck instanceof ForStmt){
                BlockStmt newNestedBlock = (BlockStmt) ((ForStmt) nestedBlockCheck).getBody();
                currNode = forHandler(newNestedBlock, currNode);
            }

            else {
                BlockStmt newBlock = (BlockStmt) ((ForStmt) statement).getBody();
                currNode = forHandler(newBlock, currNode);
            }
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
                        System.out.println(elseIfStmt.getBeginLine());
                        ifHandler(elseIfStmt);

                        //Then you get the else statement and assign to elsePart.
                        elsePart = ((IfStmt) elsePart).getElseStmt();
                    }
                }
            }
        }

        else if(statement instanceof WhileStmt){
            //BlockStmt newBlock = (BlockStmt) ((WhileStmt) statement).getBody();
            //forHandler(newBlock);
        }

        else{
            System.out.println(statement.getBeginLine());
            System.out.println(statement.toString());
        }
        return currNode;
    }

    public void elseHandler(BlockStmt elseBlock) {
        System.out.println("In elseHandler method");
        System.out.println(elseBlock.toString());
        List<Statement> statements = elseBlock.getStmts();

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
    }

    public void elseHandler(ExpressionStmt elseBlock) {
        System.out.println("In elseHandler (exprs) method");
        System.out.println(elseBlock.toString());
        System.out.println(elseBlock.getExpression());//For testing, remove later.
        System.out.println();
    }

    public void ifHandler(BlockStmt ifBlock) {
        System.out.println("In ifHandler method");
        System.out.println(ifBlock.toString());
        List<Statement> statements = ifBlock.getStmts();

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
    }

    public void ifHandler(ExpressionStmt ifBlock) {
        System.out.println("In ifHandler (Exprs) method");
        System.out.println(ifBlock.getExpression());//For testing, remove later.
        System.out.println();
    }

    public Node forHandler(ExpressionStmt expressionFor, Node start) {
        Node currNode = new Node(expressionFor.getBeginLine());
        start.addEdgeGoingTo(currNode);
        currNode.addEdgeGoingTo(start);//Becase there is only one expression, this node loops back to beginning.

        System.out.println("In forHandler (Exprs) method");
        System.out.println(expressionFor.getExpression());//For testing, remove later.
        System.out.println();

        return start; //returns start because this the cfg continues after from the start of the for loop.
    }

    public Node forHandler(BlockStmt forBlock, Node start){
        Node currNode = new Node(forBlock.getBeginLine());
        start.addEdgeGoingTo(currNode);//The start of the for loop immediately follows the currNode.

        System.out.println("In forHandler method");
        List<Statement> statements = forBlock.getStmts();
        System.out.println(forBlock.getBeginLine());

        printStatements(statements);//FOr testing, remove later
        System.out.println();

        for(Statement currStatement : statements){
            if(isConditional(currStatement)){
                currNode = handleConditional(currStatement, currNode);//Build something
            }
            else{
                currNode.addLineNumbers(currStatement.getBeginLine());
                //Continue adding statements to Node
            }
        }
        currNode.addEdgeGoingTo(start);//Because the last node loops back to beginning.
        return currNode;//Make edge to go back to top of forloop

    }

    public void printStatements(List<Statement> statements){
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

    public void printTree(Node currNode){

        if(currNode.getGoingToTransitions().isEmpty()){
            System.out.println("End");
            return;
        }

        else{
            System.out.print(currNode.getLineNumbers().toString());
            System.out.print(" -> ");

            if(currNode.getGoingToTransitions().get(0).isVisited() == false){
                currNode.getGoingToTransitions().get(0).visit();
                printTree(currNode.getGoingToTransitions().get(0).getToNode());

            }

            if (currNode.getGoingToTransitions().get(1) != null)
            {
                if(currNode.getGoingToTransitions().get(1).isVisited() == false){
                    System.out.println();
                    System.out.print("|");
                    printTree(currNode.getGoingToTransitions().get(1).getToNode());
                    currNode.getGoingToTransitions().get(1).visit();
                }
            }
        }

//        return;
    }


    public void createXML() {
        //Create CFG XML from cfg
    }

    public boolean startNodeSet() {
        if(StartNode != null){
            return true;
        }
        else{
            return false;
        }
    }

    public void setStartNode(Node start){
        StartNode = start;
    }

    public Node getStartNode(){
        return StartNode;
    }
}
