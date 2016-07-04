package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * Created by Beto on 7/2/16.
 */
public interface GraphBuilder {


    void build(BlockStmt method);

    void createXML();

    void printContents(BlockStmt method);

}
