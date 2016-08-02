package com.code.graph.Grapher;

/**
 * Created by Beto on 6/26/16.
 */
public class Edge{
    private Node from;
    private Node to;

    Edge(Node f, Node t){
        from = f;
        to = t;
    }

    public Node getToNode(){
        return to;
    }


}
