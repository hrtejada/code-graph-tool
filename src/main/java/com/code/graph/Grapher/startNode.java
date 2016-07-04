package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * Created by Beto on 7/3/16.
 */
public class startNode extends Node{
    private int startLine;
    private String cfgName;
    startNode(BlockStmt block){
        startLine = block.getBeginLine();
        cfgName = block.toString();
    }
}
