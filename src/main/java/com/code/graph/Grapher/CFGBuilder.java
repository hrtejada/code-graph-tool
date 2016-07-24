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

    void elseHandler(BlockStmt elseBlock);

    void elseHandler(ExpressionStmt elseBlock);

    void ifHandler(BlockStmt ifBlock);

    void ifHandler(ExpressionStmt ifBlock);

    Node forHandler(ExpressionStmt expressionFor, Node start);

    Node forHandler(BlockStmt forBlock, Node start);

    void printStatements(List<Statement> statements);

    //This method will be removed. ONly here to show that program parses and grabs statements appropriatley.
    void printContents(BlockStmt method);

    void printTree(Node start);

    void createXML();

    CFG getCFG();

}
