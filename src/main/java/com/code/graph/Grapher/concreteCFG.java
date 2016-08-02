package com.code.graph.Grapher;

import com.code.graph.XML;
import com.github.javaparser.ast.stmt.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Stack;

/**
 * Created by Beto on 7/22/16.
 */
public class concreteCFG implements CFGBuilder {

    private CFG cfg;

    public concreteCFG(CFG newCFG){
        this.cfg = newCFG;
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

    public void buildStart(BlockStmt method, Node next){
        if(cfg.startNodeSet() == false){
            startNode startNode = new startNode(method, next);
            cfg.setStartNode(startNode);
        }
    }

    public void buildEnd(Node last){
        if(last.getLineNumbers().isEmpty()){
            last.addLineNumbers(0);
            cfg.setEndNode(last);
        }
        else{
            Node EndNode = new Node(0);
            last.addEdgeGoingTo(EndNode);
            cfg.setEndNode(EndNode);
        }
    }

    public Node handleConditional(Statement statement, Node currNode){
        Stack<Node> joinNodes = new Stack<Node>();

        if(statement instanceof ForStmt){
            if(!currNode.getLineNumbers().isEmpty()){
                Node temp = new Node();
                currNode.addEdgeGoingTo(temp);
                currNode = temp;
            }

            Statement expressionStatementCheck = ((ForStmt) statement).getBody();
            Statement nestedBlockCheck = ((ForStmt) statement).getBody();

            if(expressionStatementCheck instanceof ExpressionStmt){//In case if does not have brackets
                currNode = forHandler((ExpressionStmt) expressionStatementCheck, currNode);
            }

            else if (nestedBlockCheck instanceof ForStmt){
                //Duplicate code needed. newNestedBlock is cast as a statement to check if it is a for loop.
                //Types do not match if not cast into statement
                BlockStmt newNestedBlock = (BlockStmt) ((ForStmt) nestedBlockCheck).getBody();
                currNode = forHandler(newNestedBlock, currNode);
            }

            else {
                BlockStmt newBlock = (BlockStmt) ((ForStmt) statement).getBody();
                currNode = forHandler(newBlock, currNode);
            }
        }

        else if(statement instanceof IfStmt){//////Refractor this hahahahaha

            // Node that begins the if-else. used to link additional else nodes
            currNode.addLineNumbers(statement.getBeginLine());
            Node ifStartNode = currNode;

            Statement expressionStatementCheck = ((IfStmt) statement).getThenStmt();
            if (expressionStatementCheck  instanceof ExpressionStmt){//In case if does not have brackets. And single IF.
                currNode = ifElseHandler((ExpressionStmt) expressionStatementCheck, ifStartNode);
            }

            else {
                BlockStmt newIfBlock = (BlockStmt) ((IfStmt) statement).getThenStmt();
                System.out.println(newIfBlock.getBeginLine());
                joinNodes.push(ifElseHandler(newIfBlock, currNode));


                Statement elsePart = ((IfStmt) statement).getElseStmt();
                while (elsePart != null)
                {
                    if(elsePart instanceof ExpressionStmt){//In case else does not have brackets.
                        //currNode = cfg.joinNodes(ifElseHandler(newIfBlock, ifStartNode), ifElseHandler((ExpressionStmt) elsePart, ifStartNode));
                        //elseHandler((ExpressionStmt) elsePart);//Moved to the above method call
                        joinNodes.push(ifElseHandler((ExpressionStmt) elsePart, ifStartNode));
                        elsePart = null;//Beto changed from break to this.
                    }

                    else if (elsePart instanceof BlockStmt) {//this is an "else".
                        BlockStmt elseStmt = (BlockStmt) elsePart;
                        //currNode = cfg.joinNodes(ifElseHandler(newIfBlock, ifStartNode), ifElseHandler(elseStmt, ifStartNode));
                        //elseHandler(elseStmt);//Moved to the above method call
                        joinNodes.push(ifElseHandler(elseStmt, ifStartNode));
                        elsePart = null;//Beto changed from break to this.
                    }
                    else {//this is an "else if". //This will kill me...

                        //Get if then statement and send to if handler.
                        BlockStmt elseIfStmt = (BlockStmt) ((IfStmt)elsePart).getThenStmt();
                        System.out.println(elseIfStmt.getBeginLine());

                        Node elseIfStartNode = new Node(elseIfStmt.getBeginLine());
                        ifStartNode.addEdgeGoingTo(elseIfStartNode);
                        joinNodes.push(ifElseHandler(elseIfStmt, elseIfStartNode));
                        //currNode = cfg.joinNodes(ifElseHandler(newIfBlock, ifStartNode), ifElseHandler(elseIfStmt, elseIfStartNode));
                        ifStartNode = elseIfStartNode;

                        //Then you get the else statement and assign to elsePart.
                        elsePart = ((IfStmt) elsePart).getElseStmt();
                    }
                }
                while (joinNodes.size() > 1)
                {
                    Node join = joinNodes.pop();
                    Node block = joinNodes.pop();

                    joinNodes.push(cfg.joinNodes(join, block));
                }

                currNode = joinNodes.pop();
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

    public Node ifElseHandler(BlockStmt ifBlock, Node ifStart) {
        System.out.println("In ifHandler method");
        System.out.println(ifBlock.toString());
        List<Statement> statements = ifBlock.getStmts();

        Node currNode = new Node();
        ifStart.addEdgeGoingTo(currNode);

        printStatements(statements);//For testing, remove later
        System.out.println();

        for(Statement statement : statements){
            if(isConditional(statement)){
                currNode = handleConditional(statement, currNode);
            }
            else{
                currNode.addLineNumbers(statement.getBeginLine());
            }
        }
        return currNode;
    }

    public Node ifElseHandler(ExpressionStmt ifBlock, Node ifStart) {
        Node expressionNode = new Node(ifBlock.getBeginLine());
        ifStart.addEdgeGoingTo(expressionNode);

        System.out.println("In ifHandler (Exprs) method");//Testing
        System.out.println(ifBlock.getExpression());//For testing, remove later.
        System.out.println();

        return cfg.joinNodes(ifStart, expressionNode);
    }

    public Node forHandler(ExpressionStmt expressionFor, Node start) {
        Node forHead = new Node(expressionFor.getBeginLine());
        start.addEdgeGoingTo(forHead);

        Node currNode = forHead;

        Node expressionNode = new Node(expressionFor.getExpression().getBeginLine());
        currNode.addEdgeGoingTo(expressionNode);//Becase there is only one expression, this node loops back to beginning.
        expressionNode.addEdgeGoingTo(forHead);//Loops back

        System.out.println("In forHandler (Exprs) method");
        System.out.println(expressionFor.getExpression());//For testing, remove later.
        System.out.println();

        Node afterHead = new Node();
        forHead.addEdgeGoingTo(afterHead);
        return forHead; //returns start because this the cfg continues after from the start of the for loop.
    }

    public Node forHandler(BlockStmt forBlock, Node start){
        Node forHead = start;
        List<Statement> statements = forBlock.getStmts();//get statements in for loop.
        forHead.addLineNumbers(forBlock.getBeginLine());
        Node firstStatementNode = new Node();//Node immediately after the for loop init.
        forHead.addEdgeGoingTo(firstStatementNode);

        Node currNode = firstStatementNode;//separate head so it does not get lost.


        System.out.println("In forHandler method");
        System.out.println(forBlock.getBeginLine());

        printStatements(statements);//For testing, remove later
        System.out.println();

        for(Statement currStatement : statements){
            if(isConditional(currStatement)){
                currNode = handleConditional(currStatement, currNode);//Build something
            }
            else{
                currNode.addLineNumbers(currStatement.getBeginLine());//Continue adding statements to Node
            }
        }
        currNode.addEdgeGoingTo(forHead);//Because the last node loops back to beginning.
        Node afterHead = new Node();//New empty node to be passed to continue creation
        forHead.addEdgeGoingTo(afterHead);
        return afterHead;

    }

    public void printStatements(List<Statement> statements){
        int x = 1;
        for(Statement currStatement : statements){
            System.out.println("Statemnt number " + x + ": ");
            System.out.println(currStatement.getBeginLine());
            System.out.println(currStatement.toString());
            x++;
        }
    }//Will be removed later.

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
    }

    public void printTree(Node start){
        Node currNode = start;
        Node prev;

        if(currNode.isVisited() == true || currNode.getGoingToTransitions().isEmpty()){
            return;
        }
        else{
            currNode.visit();
            for(int y = 0; y < currNode.getGoingToTransitions().size(); y++){
                System.out.print('"');
                for(int x = 0; x < currNode.getLineNumbers().size(); x++){
                    if (x > 0) {
                        System.out.print(",");

                    }
                    System.out.print(currNode.getLineNumbers().get(x));
                }
                System.out.print('"');

                System.out.print(" -> ");

                System.out.print('"');
                prev = currNode;
                currNode = prev.getGoingToTransitions().get(y).getToNode();
                for(int x = 0; x < currNode.getLineNumbers().size(); x++){
                    if (x > 0) {
                        System.out.print(",");

                    }
                    System.out.print(currNode.getLineNumbers().get(x));
                }
                System.out.print('"');
                System.out.println();
                printTree(currNode);
                currNode = prev;
            }
        }
    }

    public void createXML() {
        try {
            XML xml = new XML(getCFG());
            xml.createXML();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CFG getCFG() {
        return this.cfg;
    }


}
