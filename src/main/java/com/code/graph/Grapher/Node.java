package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 */
public class Node extends com.github.javaparser.ast.Node{
    private List<Edge> goingToTransitions = new ArrayList<Edge>();
    private List<Integer> statementLinesInNodes = new ArrayList<Integer>();

    Node(){}//Blank Constructor

    Node(int lineNumber){statementLinesInNodes.add(lineNumber);}//Constructor with an initial statement

    public <R, A> R accept(GenericVisitor<R, A> genericVisitor, A a) {
        return null;
    }

    public <A> void accept(VoidVisitor<A> voidVisitor, A a) {

    }

    public void addLineNumbers(int lineNumber){ statementLinesInNodes.add(lineNumber); }

    public void addEdgeGoingTo(Node goingTo){
        Edge newEdge = new Edge(this, goingTo);
        goingToTransitions.add(newEdge);
    }

    public List<Integer> getLineNumbers(){ return statementLinesInNodes;}

    public List<Edge> getGoingToTransitions(){
        return goingToTransitions;
    }

}
