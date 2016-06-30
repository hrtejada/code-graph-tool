package com.code.graph.CFG;

import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 */
public class CFG {
    private BlockStmt codeBlock;//This block holds all the statements that are passed by the parser
    private int Start; //This refers to line in which the CFG starts
    private int lineEnd; //This refers to line in which the CFG ends
    private List<Node> nodes;
    private List<Edge> edges;

    public CFG(BlockStmt block) {
        Start = block.getBeginLine();
        lineEnd = block.getEndLine();
    }

    private static void getStatements(){

    }


}
