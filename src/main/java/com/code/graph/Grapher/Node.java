package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 */
public class Node extends com.github.javaparser.ast.Node{
    private List<Edge> goingToTransitions;
    private List<Edge> comingFromTransitions;
    private List<Integer> statementLinesInNodes = new ArrayList<Integer>();

    Node(){}//Blank Constructor

    Node(int lineNumber){statementLinesInNodes.add(lineNumber);}//Constructor with an initial statement

    public <R, A> R accept(GenericVisitor<R, A> genericVisitor, A a) {
        return null;
    }

    public <A> void accept(VoidVisitor<A> voidVisitor, A a) {

    }

    public void addLineNumbers(int lineNumber){ statementLinesInNodes.add(lineNumber); }

    public void addEdgeComingFrom(Edge newEdge){
        comingFromTransitions.add(newEdge);
    }

    public void addEdgeGoingTo(Edge newEdge){
        goingToTransitions.add(newEdge);
    }

    private List<Integer> getLineNumbers(){ return statementLinesInNodes;}

}
