package com.code.graph.Grapher;

/**
 * Created by Beto on 6/26/16.
 */
public class Edge{
    private Node from;
    private Node to;
    private Boolean visit = false;

    Edge(Node f, Node t){
        from = f;
        to = t;
    }

    public Node getFromNode(){
        return from;
    }

    public Node getToNode(){
        return to;
    }

    public void visit(){
        visit = true;
    }

    public Boolean isVisited(){ return visit;}

    private void changeToNode(Node newTo){
        to = newTo;
    }

    private void changeFromNode(Node newFrom){
        from = newFrom;
    }

}
