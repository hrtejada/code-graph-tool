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

    public CFG() {
    }

    public void setStartNode(Node start){
        StartNode = start;
    }

    public void setEndNode(Node end) {
        EndNode = end;
    }

    //receives a node that is holds a reference to a chunk of nodes. Iteratre over to find the last node
    public void setNodes(Node node) {
        //Visit the chunk of nodes and find the curr node and set it.
    }

    public void joinNodes(Node firstNode, Node secondNode) {
        Node Join = new Node(0);

        firstNode.addEdgeGoingTo(Join);
        secondNode.addEdgeGoingTo(Join);

        currNode = Join;
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
