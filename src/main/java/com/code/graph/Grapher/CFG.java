package com.code.graph.Grapher;

import com.code.graph.Visitor;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 * This class is an adaptee of the graphAdapter class.
 */
public class CFG implements CFGstructure{
    private Node StartNode;
    private Node EndNode;
    private Node currNode;
    private static int joinCounter = 0;

    public CFG() {
    }

    public void setStartNode(Node start){
        StartNode = start;
    }

    public void setEndNode(Node end) {
        EndNode = end;
    }

    public Node joinNodes(Node firstNode, Node secondNode) {
        Node Join = new Node(0);
        Join.addLineNumbers(joinCounter);
        joinCounter++;

        firstNode.addEdgeGoingTo(Join);
        secondNode.addEdgeGoingTo(Join);

        return Join;
    }

    public boolean startNodeSet() {
        if(StartNode != null){
            return true;
        }
        else{
            return false;
        }
    }

    public Node getStartNode(){
        return StartNode;
    }

    public Node getEndNode(){
        return EndNode;
    }

    public Node getCurrNode(){
        return currNode;
    }
}
