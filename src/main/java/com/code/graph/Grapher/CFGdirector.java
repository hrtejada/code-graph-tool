package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.*;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Beto on 7/23/16.
 */
public class CFGdirector {

    private CFGBuilder builder;

    public CFGdirector(CFGBuilder newBuilder){
        this.builder = newBuilder;
    }

    public void buildCFG(BlockStmt method) {
        List<Statement> statements = method.getStmts();
        Node currNode = new Node();
        builder.buildStart(method, currNode);

        for(Statement statement: statements){
            if(isConditional(statement) == true) {
                currNode = builder.handleConditional(statement, currNode);
            }
            else{
                currNode.addLineNumbers(statement.getBeginLine());
            }
        }

        builder.buildEnd(currNode);
        //builder.createXML();
        builder.printTree(builder.getCFG().getStartNode());
        System.out.println(" End");
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

    public CFG getCFG(){
        return this.builder.getCFG();
    }

}
