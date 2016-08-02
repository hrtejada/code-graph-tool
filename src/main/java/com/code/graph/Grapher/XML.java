package com.code.graph.Grapher;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Daniel on 7/22/2016.
 */
public class XML {

    private final File newFile = new File("C:\\Users\\Daniel\\Desktop\\test");

    private FileWriter fw;
    private BufferedWriter bw;

    private CFG cfg;
    private Stack<XMLTags> endTags = new Stack<XMLTags>();

    private XMLTags currTag;

//    public static void main(String[] args)
//    {
//        try {
//            new XML();
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }

//    public XML()
//    {
//        Node start = new Node(11);
//        Node end = new Node(12);
//
//        CFG cfg = new CFG();
//        cfg.setEndNode(end);
//        cfg.setStartNode(start);
//
//        Node node1 = new Node(1);
//        Node node2 = new Node(2);
//        Node node3 = new Node(3);
//        Node join = new Node(0);
//
//        start.addEdgeGoingTo(node1);
//
//        node1.addEdgeGoingTo(node2);
//        node1.addEdgeGoingTo(node3);
//
//        node2.addEdgeGoingTo(join);
//
//        node3.addEdgeGoingTo(join);
//
//        join.addEdgeGoingTo(end);
//
//        this.cfg = cfg;
//
//        createXML();
//    }

    public XML(CFG cfg) throws FileNotFoundException {
        this.cfg = cfg;
    }

    public void createXML()
    {
        createFile();

        goThroughCFG(cfg.getStartNode());

        while(!endTags.isEmpty())
        {
            currTag = endTags.pop();
            printLine(currTag.getEndTag());
        }
        closeWriter();

        resetAllNodesVisit(cfg.getStartNode());
    }

    private void createFile()
    {
        try
        {
            // if file doesnt exists, then create it
            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            fw = new FileWriter(newFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWriter()
    {
        try
        {
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void printLine(String line)
    {
        try
        {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goThroughCFG(Node currNode)
    {
        if (currNode.getGoingToTransitions().isEmpty()) //END empty Node
        {
            currTag = new XMLTags(currNode.getLineNumbers().toString(), "END");
            endTags.push(currTag);

        }
        else if (currNode == cfg.getStartNode()) //START Node
        {
            XMLTags startNode = new XMLTags(currNode.getLineNumbers().toString(), "CFG");
            endTags.push(startNode);
            printLine(startNode.getStartTag());

            goThroughCFG(currNode.getGoingToTransitions().get(0).getToNode());
        }
        else {
            XMLTags nodeTag = new XMLTags(currNode.getLineNumbers().toString(), "NODE");
            endTags.push(nodeTag);
            printLine(nodeTag.getStartTag());

            Node firstNode;
            Node secondNode = null;

            firstNode = currNode.getGoingToTransitions().get(0).getToNode();
            currTag = new XMLTags(firstNode.getLineNumbers().toString(), "EDGE");
            printLine(currTag.getStartTag() + currTag.getEndTag());

            if (currNode.getGoingToTransitions().size() > 1)//there is a second edge
            {
                secondNode = currNode.getGoingToTransitions().get(1).getToNode();
                currTag = new XMLTags(secondNode.getLineNumbers().toString(), "EDGE");
                printLine(currTag.getStartTag() + currTag.getEndTag());
            }


            currTag = endTags.pop();
            printLine(currTag.getEndTag());

            if(!firstNode.isVisited()) {
                goThroughCFG(firstNode);
            }
            if (secondNode != null && !secondNode.isVisited())
            {
                goThroughCFG(secondNode);
            }

            currNode.visit();
        }
    }

    private ArrayList<Node> check = new ArrayList<Node>();
    private void resetAllNodesVisit(Node currNode)
    {
        currNode.resetVisit();

        check.add(currNode);

        for (int i = 0; i < currNode.getGoingToTransitions().size(); i++)
        {
            Node next = currNode.getGoingToTransitions().get(i).getToNode();
            if (!check.contains(next))
                resetAllNodesVisit(next);
        }
    }
}
