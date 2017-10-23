package com.eds.Converter_Solution;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.eds.Converter_Solution.model.input.ThreatModel;
import com.eds.Converter_Solution.model.output.Diagram;
import com.eds.Converter_Solution.utils.Converter;
import com.eds.Converter_Solution.utils.DiagramBuilder;
import com.eds.Converter_Solution.utils.XMLAdjuster;
import com.eds.Converter_Solution.utils.XMLBridge;

public class App {
	
    public static void main(String[] args) {
    	
        if (args.length < 1){
        	System.out.println("ERROR: You have to pass data flow diagram location!");
        	System.out.println("ERROR: Parameter 1 - Location of an input diagram");
            return;
        }
        
        String inputDiagramLocation = args[0];
        String inputDiagramSchemaLocation = "resources\\TM7Schema.xsd";
        String preparedInputDiagramLocation = "temp1.xml";
        String rawOutputDiagramLocation = "temp2.xml";

        File inputDiagramFile = new File(inputDiagramLocation);        
        File inputDiagramSchemaFile = new File(inputDiagramSchemaLocation);
        File preparedInputDiagramFile = new File(preparedInputDiagramLocation);
        File rawOutputDiagramFile = new File(rawOutputDiagramLocation);
        
    	String outputDiagramLocation = inputDiagramFile.getName().substring(0, inputDiagramFile.getName().lastIndexOf(".")) + ".xml";
        File outputDiagramFile = new File(outputDiagramLocation);
                
        
        if (!checkInputDiagram(inputDiagramFile, inputDiagramSchemaFile)) {
        	return;
        }
        
        if (!adjustInputDiagram(inputDiagramFile, preparedInputDiagramFile)) {
        	return;
        }
        
        ThreatModel inputDiagram = readInputDiagram(preparedInputDiagramFile);
        if (inputDiagram == null) {
        	return;
        }
        
        Converter converter = new Converter(inputDiagram);
        if (!converter.convert()) {
        	return;
        }
        
        DiagramBuilder builder = new DiagramBuilder(converter);
        Diagram outputDiagram = builder.buildDiagram();
        
        if (!writeOutputDiagram(outputDiagram, rawOutputDiagramFile)) {
        	return;
        }
        
        if (!adjustOutputDiagram(rawOutputDiagramFile, outputDiagramFile)) {
        	return;
        }       
        
        preparedInputDiagramFile.delete();
        rawOutputDiagramFile.delete();
        
        System.out.println("Info: Done!");
    }
    
    private static boolean checkInputDiagram(File inputDiagramFile, File inputDiagramSchema) {
    	
        try {
        	
			XMLBridge.checkWellFormness(inputDiagramFile);
			
		} catch (ParserConfigurationException e) {
        	System.out.println("ERROR: Could not initialize XML parser!");
			return false;
		} catch (SAXException e) {
        	System.out.println("ERROR: Input diagram is not well formed!");
			return false;
		} catch (IOException e) {
        	System.out.println("ERROR: Could not find input file!");
			return false;
		}	
        
    	return true;
    }
    
    private static boolean adjustInputDiagram(File inputDiagramFile, File preparedInputDiagramFile) {
    	
    	XMLAdjuster adjuster = new XMLAdjuster(inputDiagramFile, preparedInputDiagramFile);
    	
		try {
			adjuster.adjustDiagram();
		} catch (ParserConfigurationException e) {
        	System.out.println("ERROR: XML Document could not be initialized!");
			return false;
		} catch (TransformerFactoryConfigurationError e) {
        	System.out.println("ERROR: Could not initialize XML Transformer!");
			return false;
		} catch (TransformerException e) {
        	System.out.println("ERROR: Could not create prepared diagram file!");
			return false;
		} catch (SAXException e) {
        	System.out.println("ERROR: Input diagram could not be readed as XML!");
			return false;
		} catch (IOException e) {
        	System.out.println("ERROR: Input diagram could not be found!");
			return false;
		}
    	
    	return true;
    }
    
    private static boolean adjustOutputDiagram(File outputDiagramFile, File preparedOutputDiagramFile) {
    	
    	XMLAdjuster adjuster = new XMLAdjuster(outputDiagramFile, preparedOutputDiagramFile);
    	
		adjuster.adjustOutputFile();
    	
    	return true;
    	
    }
    
    private static ThreatModel readInputDiagram(File inputDiagramFile) {    	
    	ThreatModel inputDiagram;
    	
    	try {
			inputDiagram = XMLBridge.readInputDiagram(inputDiagramFile);
		} catch (JAXBException e) {
        	System.out.println("ERROR: Input diagram could not be readed!");
			return null;
		}
    	
    	return inputDiagram;    	
    }
    
    private static boolean writeOutputDiagram(Diagram outputDiagram, File outputFile) {
    	
    	try {
			XMLBridge.writeXMLDiagram(outputDiagram, outputFile);
		} catch (JAXBException e) {
        	System.out.println("ERROR: Output diagram could not be saved!");
			return false;
		}
    	
    	return true;
    }
}
