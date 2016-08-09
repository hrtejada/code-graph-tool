package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.*;

import java.util.List;

/**
 * Created by Beto on 7/3/16.
 * Adoater for GraphBuilder
 */
public interface CFGBuilder  {

    void buildStart(BlockStmt method, Node next);

    void buildEnd(Node last);

    Node handleConditional(Statement statement, Node currNode);

    Node ifElseHandler(BlockStmt ifBlock, Node ifStart);

    Node ifElseHandler(ExpressionStmt ifBlock, Node ifStart);

    Node forHandler(ExpressionStmt expressionFor, Node start);

    Node forHandler(BlockStmt forBlock, Node start);

    void outputToFile();

    CFG getCFG();

}
