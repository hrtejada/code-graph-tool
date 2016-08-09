package com.code.graph.Grapher;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * Created by Beto on 7/3/16.
 */
public class startNode extends Node{
    //private int startLine;
    private String cfgName;

    startNode(BlockStmt block, Node next, MethodDeclaration m){
        super(block.getBeginLine());
        cfgName = m.getName();
        this.addEdgeGoingTo(next);
    }

    public String getCFGName(){
        return  cfgName;
    }
}
