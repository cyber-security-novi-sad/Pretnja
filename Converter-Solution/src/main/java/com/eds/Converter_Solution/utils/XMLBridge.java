package com.eds.Converter_Solution.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.eds.Converter_Solution.model.input.ThreatModel;
import com.eds.Converter_Solution.model.output.Diagram;

public class XMLBridge {

	@SuppressWarnings("unused")
 	public static void checkWellFormness(File inputDiagramFile) throws ParserConfigurationException, SAXException, IOException {
		
 		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 		
		DocumentBuilder builder = factory.newDocumentBuilder();	
		InputStream stream = new FileInputStream(inputDiagramFile);
		
		Document document = builder.parse(stream);
 	}

	public static ThreatModel readInputDiagram(File inputDiagramFile) throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(ThreatModel.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ThreatModel inputDiagram = (ThreatModel) jaxbUnmarshaller.unmarshal(inputDiagramFile);
		
		return inputDiagram;
	}
	
	public static void writeXMLDiagram(Diagram outputDiagram, File outputFile) throws JAXBException {
		
        JAXBContext jaxbContext = JAXBContext.newInstance(Diagram.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(outputDiagram, outputFile);		
		
	}
}