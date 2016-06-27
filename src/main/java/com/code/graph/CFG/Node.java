package com.code.graph.CFG;

import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.*;

/**
 * Created by Beto on 6/26/16.
 */
public class Node extends com.github.javaparser.ast.Node{
    List<Edge> goingToTransitions;
    List<Edge> goingFromTransitions;

    Node(){

    }

    //Huh? haha
    public <R, A> R accept(GenericVisitor<R, A> genericVisitor, A a) {
        return null;
    }

    public <A> void accept(VoidVisitor<A> voidVisitor, A a) {

    }

}
