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
    private List<Statement> content;

    Node(){}//Blank Constructor

    public <R, A> R accept(GenericVisitor<R, A> genericVisitor, A a) {
        return null;
    }

    public <A> void accept(VoidVisitor<A> voidVisitor, A a) {

    }

    Node(Statement statement){
        content.add(statement);
    }//Constructor with an initial statement

    public void addEdgeComingFrom(Edge newEdge){
        comingFromTransitions.add(newEdge);
    }

    public void addEdgeGoingTo(Edge newEdge){
        goingToTransitions.add(newEdge);
    }

    private List<Statement> getContent(){
        return content;
    }

}
