package com.code.graph.Grapher;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;

/**
 * Created by Beto on 7/9/16.
 */
public class statementConverter {
    private List<Statement> statements;
    private BlockStmt convertedStatement;

    public statementConverter(){}

    public statementConverter(ForStmt forBlock){
        Statement Body = forBlock.getBody();
        convertedStatement = convert(Body);
        statements = convertedStatement.getStmts();
    }

    public BlockStmt convert(Statement Body){
        BlockStmt newBlock = new BlockStmt();

        return newBlock;
    }

    public List<Statement> getStatements(){
        return statements;
    }
}
