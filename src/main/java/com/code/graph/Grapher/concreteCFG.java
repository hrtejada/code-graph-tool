package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Stack;

/**
 * Created by Beto on 7/22/16.
 */
public class concreteCFG implements CFGBuilder {

    private CFG cfg;
    BufferedWriter writer = null;
    File dotFile = new File("cfg.txt");

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
            StartNode startNode = new StartNode(method, next);
            cfg.setStartNode(startNode);
        }
    }

    public void buildEnd(Node last){
        Node EndNode = new Node(0);
        last.addEdgeGoingTo(EndNode);
        cfg.setEndNode(EndNode);
    }

    public Node handleConditional(Statement statement, Node currNode){
        Stack<Node> joinNodes = new Stack<Node>();

        if(statement instanceof ForStmt){

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
        return afterHead; //returns start because this the cfg continues after from the start of the for loop.
    }

    public Node forHandler(BlockStmt forBlock, Node start){
        Node forHead = new Node(forBlock.getBeginLine());
        start.addEdgeGoingTo(forHead);//The start of the for loop immediately follows the startNode.

        Node currNode = forHead;//separate head so it does not get lost.


        System.out.println("In forHandler method");
        System.out.println(forBlock.getBeginLine());

        List<Statement> statements = forBlock.getStmts();//get statements in for loop.
        Node firstStatementNode = new Node();//Node immediately after the for loop init.
        currNode.addEdgeGoingTo(firstStatementNode);
        currNode = firstStatementNode;

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
        //Ok so, in Java blocks are executed as a single statement. That's why if you run this, you can see the statement separated by commas and a block is seen as a statement.
        //What's a block in java? https://docs.oracle.com/javase/tutorial/java/nutsandbolts/expressions.html
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


//        if(currNode.getGoingToTransitions().isEmpty()){
//            return;
//        }
//
//        else {
//            System.out.print(currNode.getLineNumbers().toString());
//            System.out.print(" -> ");
//
//            if(currNode.getGoingToTransitions().size() > 1 && currNode.getGoingToTransitions().get(1).isVisited() == false){
//                System.out.println();
//                System.out.print("|");
//                printTree(currNode.getGoingToTransitions().get(1).getToNode());
//                currNode.getGoingToTransitions().get(1).visit();
//            }
//
//            if(currNode.getGoingToTransitions().get(0) != null && currNode.getGoingToTransitions().get(0).isVisited() == false){
//                currNode.getGoingToTransitions().get(0).visit();
//                printTree(currNode.getGoingToTransitions().get(0).getToNode());
//
//            }
//        }
    }

    public void createXML() {
        //Create CFG XML from cfg
    }

    public CFG getCFG() {
        return this.cfg;
    }

//    public void createDotFile(Node start){
//        try {
//
//            // This will output the full path where the file will be written to...just to check
//            System.out.println(dotFile.getCanonicalPath());
//
//            writer = new BufferedWriter(new FileWriter(dotFile));
//
//            Node currNode = start;
//
//            if(currNode.getGoingToTransitions().isEmpty()){
//                return;
//            }
//
//            else {
//                writer.write('"');
//                for(int x = 0; x < currNode.getLineNumbers().size(); x++){
//                    writer.write(currNode.getLineNumbers().toString());
//                }
//                writer.write('"');
//                writer.write(" -> ");
//
//                if(currNode.getGoingToTransitions().size() > 1 && currNode.getGoingToTransitions().get(1).isVisited() == false){
//                    System.out.println();
//                    System.out.print("|");
//                    printTree(currNode.getGoingToTransitions().get(1).getToNode());
//                    currNode.getGoingToTransitions().get(1).visit();
//                }
//
//                if(currNode.getGoingToTransitions().get(0) != null && currNode.getGoingToTransitions().get(0).isVisited() == false){
//                    currNode.getGoingToTransitions().get(0).visit();
//                    printTree(currNode.getGoingToTransitions().get(0).getToNode());
//
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // Close the writer regardless of what happens...
//                writer.close();
//            } catch (Exception e) {
//            }
//        }
//    }

}
