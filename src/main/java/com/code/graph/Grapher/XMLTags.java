package com.code.graph.Grapher;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by Daniel on 7/24/2016.
 */
public class XMLTags {

    private String id;
    private String type;
    private String endTag;
    private String startTag;

    public XMLTags(String id, String type)
    {
        if (type.equalsIgnoreCase("CFG"))
        {
            startTag = "\n<" + type + " id = '" + id + "'>";
            endTag = "\n</" + type + ">";
        }
        if (type.equalsIgnoreCase("NODE"))
        {
            startTag = "\n\t<" + type + " id = '" + id + "'>";
            endTag = "\n\t</" + type + ">";
        }
        if (type.equalsIgnoreCase("EDGE"))
        {
            startTag = "\n\t\t<" + type + ">" + id;
            endTag = "</" + type + ">";
        }
        if (type.equalsIgnoreCase("END"))
        {
            startTag = "\n\t</END_NODE id = '" + id + "'>";
            endTag = "\n\t</END_NODE id = '" + id + "'>";
        }
    }

    public String getStartTag()
    {
        return startTag;
    }

    public String getEndTag()
    {
        return endTag;
    }
}
