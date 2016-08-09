package com.code.graph;

import com.code.graph.Grapher.CFG;
import com.code.graph.Grapher.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Daniel on 7/22/2016.
 */
public class Visualizer {

    private final File xmlFile = new File("/Users/Beto/Desktop/cfg.xml");
    private final File dotFile = new File("/Users/Beto/Desktop/cfg.dot");

    private FileWriter fw;
    private BufferedWriter bw;

    private CFG cfg;
    private Stack<XMLTags> endTags = new Stack<XMLTags>();

    private XMLTags currTag;


    public Visualizer(CFG cfg) throws FileNotFoundException {
        this.cfg = cfg;
    }

    public void createXML()
    {
        createFile(xmlFile);

        goThroughCFG(cfg.getStartNode());

        while(!endTags.isEmpty())
        {
            currTag = endTags.pop();
            printLine(currTag.getEndTag());
        }
        closeWriter();

        //resetAllNodesVisit(cfg.getStartNode());
    }

    public void createDot(){
        createFile(dotFile);
        try {
            bw.write("digraph g {");
            bw.newLine();
            dotContents(cfg.getStartNode());
            bw.write("}");
            closeWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createGraphVizPNG();

    }

    private void createGraphVizPNG() {
        String s = null;

        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("dot -T png -O " + dotFile.getAbsolutePath());

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void dotContents(Node start) throws IOException {
        Node currNode = start;
        Node prev;

        if(currNode.isVisited() == true || currNode.getGoingToTransitions().isEmpty()){
            return;
        }
        else{
            currNode.visit();
            for(int y = 0; y < currNode.getGoingToTransitions().size(); y++){
                bw.write('"');
                for(int x = 0; x < currNode.getLineNumbers().size(); x++){
                    if (x > 0) {
                        bw.write(",");

                    }
                    bw.write(currNode.getLineNumbers().get(x).toString());
                }
                bw.write('"');

                bw.write(" -> ");

                bw.write('"');
                prev = currNode;
                currNode = prev.getGoingToTransitions().get(y).getToNode();
                for(int x = 0; x < currNode.getLineNumbers().size(); x++){
                    if (x > 0) {
                        bw.write(",");

                    }
                    bw.write(currNode.getLineNumbers().get(x).toString());
                }
                bw.write('"');
                bw.newLine();
                dotContents(currNode);
                currNode = prev;
            }
        }

    }



    private void createFile(File file)
    {
        try
        {
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile());
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
        currNode.visit();
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

            //currNode.visit();
        }
    }

}
