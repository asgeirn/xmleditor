package com.asgeirnilsen.xmleditor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class XmlEditor {

    public static void main(String...args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        DOMImplementationLS implementationLS = (DOMImplementationLS)implementation.getFeature("LS", "3.0");
        LSSerializer serializer = implementationLS.createLSSerializer();
        XPath xpath = XPathFactory.newInstance().newXPath();
        Map<String, String> changes = new HashMap<String, String>();
        
        for (String arg : args) {
            if (arg.contains(":=")) {
                String[] change = arg.split(":=");
                changes.put(change[0], change[1]);
            } else {
                System.out.println("Processing " + arg + " ...");
                File file = new File(arg);
                Document document = builder.parse(file);
                file.renameTo(new File(arg + ".bak"));
                for (Map.Entry<String, String> edit : changes.entrySet()) {
                    NodeList nodes = (NodeList)xpath.evaluate(edit.getKey(), document, XPathConstants.NODESET);
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        if (edit.getValue().equals("&"))
                        	node.getParentNode().removeChild(node);
                        else
                        	node.setTextContent(edit.getValue());
                    }
                }
                LSOutput output = implementationLS.createLSOutput();
                output.setByteStream(new FileOutputStream(file));
                serializer.write(document,output);
            }
        }
    }
}
