package com.code.graph.Grapher;

/**
 * Created by Beto on 6/26/16.
 */
public class Edge {
    private Node from;
    private Node to;

    Edge(Node f, Node t){
        from = f;
        to = t;
    }

    private Node getFromNode(){
        return from;
    }

    private Node getToNode(){
        return to;
    }

    private void changeToNode(Node newTo){
        to = newTo;
    }

    private void changeFromNode(Node newFrom){
        from = newFrom;
    }

}
