package com.code.graph.Grapher;

import com.code.graph.Visitor;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 * This class is an adaptee of the graphAdapter class.
 */
public class CFG{
    private Node StartNode;
    private Node EndNode;
    private List<Node> nodes;//Might not neeeeed this
    private List<Edge> edges;//Same as above
    private List<Statement> statements;//Also same haha

    public CFG() {
    }


    public void addNode() {
    }

    public void addEdge() {

    }

    private List<Statement> getSatements(){
        return statements;
    }

    private int getStartLine(){
        return StartNode.getBeginLine();
    }

    private int getEnd(){
        return EndNode.getEndLine();
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

}
