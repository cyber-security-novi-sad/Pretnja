package com.eds.Converter_Solution.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLAdjuster {
	
	private File rawFile;
	private File tempDiagramFile;
	private File usableFile;
	
	public XMLAdjuster(File rawFile, File usableFile) {
		this.rawFile = rawFile;
		this.usableFile = usableFile;
		this.tempDiagramFile = new File("tempDiagram.xml");
	}

	public void adjustDiagram() throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, SAXException, IOException {
		prepareInputFile();
		createTemporaryXMLFile();
		tempDiagramFile.delete();
	}	
	
	private void createTemporaryXMLFile() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(tempDiagramFile);
		
		NodeList bordersNodes = doc.getElementsByTagName("Borders");
		NodeList linesNodes = doc.getElementsByTagName("Lines");
		NodeList instancesNodes = doc.getElementsByTagName("ThreatInstances");	
		
        Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = xmlDoc.createElement("ThreatModel");
        
		int bordersNodesLength = bordersNodes.getLength();
		for (int i = 0; i < bordersNodesLength; ++i) {
			Node firstDocImportedNode = xmlDoc.importNode(bordersNodes.item(i), true);
			root.appendChild(firstDocImportedNode);
		}
		
		int linesNodesLength = linesNodes.getLength();
		for (int i = 0; i < linesNodesLength; ++i) {
			Node firstDocImportedNode = xmlDoc.importNode(linesNodes.item(i), true);
			root.appendChild(firstDocImportedNode);
		}
		
		int instancesNodesLength = instancesNodes.getLength();
		for (int i = 0; i < instancesNodesLength; ++i) {
			Node firstDocImportedNode = xmlDoc.importNode(instancesNodes.item(i), true);
			root.appendChild(firstDocImportedNode);
		}
        
        xmlDoc.appendChild(root);

        Source source = new DOMSource(xmlDoc);
        StreamResult result =  new StreamResult(usableFile);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
	}
	
	private void prepareInputFile() {
		String search1 = ":";
		String replacement1 ="";
		String search2 = "xmlns";
		String replacement2 = "ch";
				
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;         
        FileWriter writer = null;
        
        try {
            reader = new BufferedReader(new FileReader(rawFile));             
            String line = reader.readLine();
            
            while (line != null) {
            	builder.append(line + System.lineSeparator());                 
                line = reader.readLine();
            }
            
            String newContent = builder.toString().replaceAll(search1, replacement1);
            newContent = newContent.replaceAll(search2, replacement2);
             
            writer = new FileWriter(tempDiagramFile);             
            writer.write(newContent);

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void adjustOutputFile() {
		String search = "xmlns=\"\"";
		String replacement = "";
				
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;         
        FileWriter writer = null;
        
        try {
            reader = new BufferedReader(new FileReader(rawFile));             
            String line = reader.readLine();
            
            while (line != null) {
            	builder.append(line + System.lineSeparator());                 
                line = reader.readLine();
            }
            
            String newContent = builder.toString().replaceAll(search, replacement);
             
            writer = new FileWriter(usableFile);             
            writer.write(newContent);

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
}
