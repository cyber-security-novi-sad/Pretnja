package com.eds.Converter_Solution.utils;

import java.util.List;

import javax.xml.bind.JAXBElement;

import com.eds.Converter_Solution.model.output.BlockElement;
import com.eds.Converter_Solution.model.output.BlockShape;
import com.eds.Converter_Solution.model.output.Diagram;
import com.eds.Converter_Solution.model.output.Flow;
import com.eds.Converter_Solution.model.output.Shape;
import com.eds.Converter_Solution.model.output.Diagram.Boundaries;
import com.eds.Converter_Solution.model.output.Diagram.Elements;
import com.eds.Converter_Solution.model.output.Diagram.Flows;
import com.eds.Converter_Solution.model.output.Diagram.Sections;

public class DiagramBuilder {

	private List<JAXBElement<? extends BlockElement>> convertedElements;
	private List<JAXBElement<? extends Flow>> convertedFlows;
	private List<JAXBElement<? extends Shape>> convertedBoundaries;
	private List<JAXBElement<? extends BlockShape>> convertedSections;

	public DiagramBuilder(Converter converter) {
		this.convertedElements = converter.getConvertedElements();
		this.convertedFlows = converter.getConvertedFlows();
		this.convertedBoundaries = converter.getConvertedBoundaries();
		this.convertedSections = converter.getConvertedSections();
	}

	
	public Diagram buildDiagram() {
		Diagram output = new Diagram();
		
		Elements elements = new Elements();
		Flows flows = new Flows();
		Boundaries boundaries = new Boundaries();
		Sections sections = new Sections();
		
		elements.getOrWebApplicationOrWebServiceOrWebServer().addAll(convertedElements);
		flows.getOrBinaryOrHttpOrHttps().addAll(convertedFlows);
		boundaries.getOrInternetBoundaryOrMachineBoundaryOrLocalDMZBoundary().addAll(convertedBoundaries);
		sections.getOrInternetDmzOrOfficeNetworkOrSharedNetwork().addAll(convertedSections);
				
		output.setElements(elements);
		output.setFlows(flows);
		output.setBoundaries(boundaries);
		output.setSections(sections);
		
		return output;
	}
	
	
	
}
