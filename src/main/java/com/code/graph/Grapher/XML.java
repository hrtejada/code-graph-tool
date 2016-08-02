package com.code.graph.Grapher;

import java.io.*;
import java.util.Stack;

/**
 * Created by Daniel on 7/22/2016.
 */
public class XML {

    File newFile = new File("C:\\Users\\Daniel\\Desktop\\test");
    PrintWriter printWriter = new PrintWriter(newFile);

    private CFG cfg;
    private Stack<String> endTags = new Stack<String>();

    String finalString = "";

    public static void main(String[] args)
    {
        Node start = new Node();
        Node end = new Node();

        CFGBuilder cfg = new CFG(start, end);

    }


    public XML(CFG cfg) throws FileNotFoundException {
        this.cfg = cfg;
    }

    public void createXML()
    {
        goThroughCFG(cfg.getStartNode());

        //pop off endTag
//        String endTag = endTags.pop();

        //wirte the endtag to document
//        finalString = finalString + endTag;

        finalString = finalString + endTags.pop();
        System.out.print(finalString + "\n\n\n");

        printWriter.print(finalString);

        print();
    }

    private void print()
    {
        try {

            //String content = "This is the content to write into file";

            //File file = new File("/users/mkyong/filename.txt");

            // if file doesnt exists, then create it
            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(finalString);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goThroughCFG(Node currNode)
    {
        System.out.print(finalString + "\n\n\n");
        if (currNode.getGoingToTransitions().isEmpty()) //empty Node
        {
            XMLTags endNode = new XMLTags(currNode.getLineNumbers().toString(), "END");
//                String finalTag = endTags.pop();
            finalString = finalString + endNode.getStartTag();

        }
        else if (currNode == cfg.getStartNode())
        {
            XMLTags startNode = new XMLTags(currNode.getLineNumbers().toString(), "CFG");
            endTags.push(startNode.getEndTag());
            finalString = finalString + startNode.getStartTag();

            goThroughCFG(currNode.getGoingToTransitions().get(0).getToNode());
        }
        else
        {
            XMLTags nodeTag = new XMLTags(currNode.getLineNumbers().toString(), "NODE");
            endTags.push(nodeTag.getEndTag());
            finalString = finalString + nodeTag.getStartTag();

            Node firstNode = currNode.getGoingToTransitions().get(0).getToNode();
            XMLTags firstTag = new XMLTags(firstNode.getLineNumbers().toString(),"EDGE");
            endTags.push(firstTag.getEndTag());
            finalString = finalString + firstTag.getStartTag() + endTags.pop();

            Node secondNode = null;
            if (currNode.getGoingToTransitions().size() > 1)
            {
                secondNode = currNode.getGoingToTransitions().get(1).getToNode();
                XMLTags secondTag = new XMLTags(secondNode.getLineNumbers().toString(),"EDGE");
                endTags.push(secondTag.getEndTag());
                finalString = finalString + secondTag.getStartTag() + endTags.pop();
            }


            finalString = finalString +  endTags.pop();

            goThroughCFG(firstNode);
            if (secondNode != null)
                goThroughCFG(secondNode);

        }

    }

    private void createCFGTag()
    {
        XMLTags cfgTag = new XMLTags(cfg.getStartNode().getLineNumbers().toString(), "CFG");

        //write the first tag to document
        finalString = cfgTag.getStartTag() + "\n";

        endTags.push(cfgTag.getEndTag());
    }
}
