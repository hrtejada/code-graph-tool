package com.code.graph.Grapher;

/**
 * Created by Beto on 7/22/16.
 */
public interface CFGplan {

    void setStartNode(Node start);

    void setEndNode(Node end);

    void setNode(Node node);

    void joinNodes(Node firstNode, Node secondNode);

}
