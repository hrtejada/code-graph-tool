package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * Created by Beto on 7/3/16.
 */
public class StartNode extends Node{
    private int startLine;
    private String cfgName;

    StartNode(BlockStmt block, Node next){
        startLine = block.getBeginLine();
        cfgName = block.toString();
        Edge newEdge = new Edge(this, next);
        this.addEdgeGoingTo(next);
    }
}
