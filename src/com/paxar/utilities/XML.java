package com.paxar.utilities;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class XML {
	
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder; 
    private Document doc;
    
	public XML() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * getTagValue will get a child Node value (tagName) from the
	 * passed in Element (firstElement).
	 */
    public String getTagValue(Element firstElement, String tagName){
    	
    	NodeList nodeList;
    	
        nodeList = firstElement.getElementsByTagName(tagName);
        Element fieldElement = (Element)nodeList.item(0);

        nodeList = fieldElement.getChildNodes();
        
        if (nodeList.getLength() > 0){
	        return ((Node)nodeList.item(0)).getNodeValue().trim().toString();
        }else{
        	return "";
        }

    }
    

    /*
	 * getTagValueFromList will get an array list for (tagName) from the
	 * passed in Element (firstElement).
	 */
    public ArrayList getTagValueFromList(Element firstElement, String tagName){
    	ArrayList sizeList = new ArrayList();
    	String retString = "";
    	NodeList nodeList, valueNodeList;
    	
        nodeList = firstElement.getElementsByTagName("value");
        firstElement.getNodeValue();
        
        for (int i = 0; i <= nodeList.getLength(); i++){
        	

	        Element fieldElement = (Element)nodeList.item(i);
	
	        if (fieldElement != null){
	        	valueNodeList = fieldElement.getChildNodes();
	        }else{
	        	return sizeList;
	        }
	        

	        sizeList.add(((Node)valueNodeList.item(0)).getNodeValue().trim());
        }
        return sizeList;
    }
    
    public String getTagValueFromString(String xmlMessage, String tagName){
	    NodeList listOfFields;
	    NodeList nodeList;

	    try{
	    	// clear the list.
	    	
	       docBuilderFactory = DocumentBuilderFactory.newInstance();
	       docBuilder = docBuilderFactory.newDocumentBuilder(); 
	       
	       doc = docBuilder.parse(new InputSource(new StringReader(xmlMessage)));
	       
	       listOfFields = doc.getElementsByTagName(tagName);
	       
		   for(int s=0; s<listOfFields.getLength() ; s++){
	
	
		        Node firstUPCNode = listOfFields.item(s);
		        if(firstUPCNode.getNodeType() == Node.ELEMENT_NODE){
		        	
	                Element firstFieldElement = (Element)firstUPCNode;
	
	                // Fill the Field structure.
	                return this.getTagValue(firstFieldElement, tagName);
		        }
		   }

		}catch (Exception e){
	        System.out.println("Error");
	                  System.err.println(e.toString());
		}
		return "";
	 }
	    




     public void saveImageToFile(String artifact, String outFile){
            Base64 decoder = new Base64();
            byte[] base64Decoded = null;

            FileInputStream in = null;
            FileOutputStream out = null;
            
            base64Decoded = decoder.decode(artifact.getBytes());

            try{
	           
	            out = new FileOutputStream(outFile);
                out.write(base64Decoded);
                out.close();

            }catch (FileNotFoundException  e){
            	System.out.println("XML: FileNotFoundException - Error writing to file " + outFile);
            }catch (IOException f){
            	System.out.println("XML: IOException - Error writing to file " + outFile);
            }
     }
}
