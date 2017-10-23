package com.eds.Converter_Solution.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.eds.Converter_Solution.model.input.ThreatModel;
import com.eds.Converter_Solution.model.input.ThreatModel.Borders.AKeyValueOfguidanyType;
import com.eds.Converter_Solution.model.input.ThreatModel.Borders.AKeyValueOfguidanyType.AValue.Properties.AanyType;
import com.eds.Converter_Solution.model.input.ThreatModel.ThreatInstances.AKeyValueOfstringThreatpcP0PhOB;
import com.eds.Converter_Solution.model.output.BlockElement;
import com.eds.Converter_Solution.model.output.BlockShape;
import com.eds.Converter_Solution.model.output.DataStore;
import com.eds.Converter_Solution.model.output.Diagram.Boundaries.InternetBoundary;
import com.eds.Converter_Solution.model.output.Diagram.Boundaries.MachineBoundary;
import com.eds.Converter_Solution.model.output.Diagram.Elements.Browser;
import com.eds.Converter_Solution.model.output.Diagram.Elements.CloudStorage;
import com.eds.Converter_Solution.model.output.Diagram.Elements.ExternalWebApplication;
import com.eds.Converter_Solution.model.output.Diagram.Elements.ExternalWebService;
import com.eds.Converter_Solution.model.output.Diagram.Elements.FileSystem;
import com.eds.Converter_Solution.model.output.Diagram.Elements.NoSqlDatabase;
import com.eds.Converter_Solution.model.output.Diagram.Elements.SqlDatabase;
import com.eds.Converter_Solution.model.output.Diagram.Elements.ThickClient;
import com.eds.Converter_Solution.model.output.Diagram.Elements.WebApplication;
import com.eds.Converter_Solution.model.output.Diagram.Elements.WebServer;
import com.eds.Converter_Solution.model.output.Diagram.Elements.WebService;
import com.eds.Converter_Solution.model.output.Diagram.Flows.Binary;
import com.eds.Converter_Solution.model.output.Diagram.Flows.Http;
import com.eds.Converter_Solution.model.output.Diagram.Flows.Https;
import com.eds.Converter_Solution.model.output.Diagram.Sections.CompanyTrustSection;
import com.eds.Converter_Solution.model.output.Diagram.Sections.SandboxTrustSection;
import com.eds.Converter_Solution.model.output.ExternalEntity;
import com.eds.Converter_Solution.model.output.Flow;
import com.eds.Converter_Solution.model.output.Process;
import com.eds.Converter_Solution.model.output.Shape;

public class Converter {
	
	private List<AKeyValueOfguidanyType> nodesSectionsAnnotations;
	private List<com.eds.Converter_Solution.model.input.ThreatModel.Lines.AKeyValueOfguidanyType> flowsBorders;
	private List<AKeyValueOfstringThreatpcP0PhOB> flowEndpoints;
	

	private List<JAXBElement<? extends BlockElement>> convertedElements;
	private List<JAXBElement<? extends BlockShape>> convertedSections;
	private List<JAXBElement<? extends Flow>> convertedFlows;
	private List<JAXBElement<? extends Shape>> convertedBoundaries;
	
	
	public Converter(ThreatModel inputDiagram) {
		nodesSectionsAnnotations = inputDiagram.getBorders().getAKeyValueOfguidanyType();
		flowsBorders = inputDiagram.getLines().getAKeyValueOfguidanyType();
		flowEndpoints = inputDiagram.getThreatInstances().getAKeyValueOfstringThreatpcP0PhOB();
		
		convertedElements = new ArrayList<>();
		convertedSections = new ArrayList<>();
		convertedFlows = new ArrayList<>();
		convertedBoundaries = new ArrayList<>();
	}
	
	public boolean convert() {
		
		convertNodesAndSections();
		
		convertFlowsAndBorders();
		
		return true;
	}
	
	private void convertNodesAndSections() {
		
		for (AKeyValueOfguidanyType element : nodesSectionsAnnotations) {
			
			//Process: SE.P.TMCore.OSProcess,SE.P.TMCore.Thread,SE.P.TMCore.KernelThread,SE.P.TMCore.WinApp,SE.P.TMCore.NetApp,SE.P.TMCore.BrowserClient,SE.P.TMCore.PlugIn,SE.P.TMCore.Modern,SE.P.TMCore.Win32Service,SE.P.TMCore.VM,SE.P.TMCore.NonMS
			//ExternalEntities: SE.EI.TMCore.AuthProvider,SE.EI.TMCore.User,SE.EI.TMCore.Megasevrice,SE.EI.TMCore.CRT,SE.EI.TMCore.NFX,SE.EI.TMCore.WinRT
			//DataStore: SE.DS.TMCore.Registry,SE.DS.TMCore.ConfigFile,SE.DS.TMCore.Cache,SE.DS.TMCore.HTML5LS,SE.DS.TMCore.Cookie,SE.DS.TMCore.Device
			//TextField: GE.A
			//Sections: SE.TB.B.TMCore.IEB,SE.TB.B.TMCore.NonIEB,SE.TB.B.TMCore.Sandbox
			
			String type = element.getAValue().getTypeId().getValue();
			
			String id = "m" + element.getAValue().getGuid().getValue();
			
			//PROCESI
			if (type.equals("SE.P.TMCore.WebApp")) {
				WebApplication instance = new WebApplication();
				instance.setId(id);
				fillProcesAttributes(instance, element);
				
				JAXBElement<WebApplication> jaxbElement =  new JAXBElement(new QName("webApplication"), WebApplication.class, null);
		        jaxbElement.setValue(instance); 				
				convertedElements.add(jaxbElement);
			} else if (type.equals("SE.P.TMCore.WebServer")) {
				WebServer instance = new WebServer();
				instance.setId(id);
				fillProcesAttributes(instance, element);
				
				JAXBElement<WebServer> jaxbElement =  new JAXBElement(new QName("webServer"), WebServer.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);
			} else if (type.equals("SE.P.TMCore.WebSvc")) {
				WebService instance = new WebService();
				instance.setId(id);
				fillProcesAttributes(instance, element);
				
				JAXBElement<WebService> jaxbElement =  new JAXBElement(new QName("webService"), WebService.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);
			} else if (type.equals("SE.P.TMCore.ThickClient")) {
				ThickClient instance = new ThickClient();
				instance.setId(id);
				fillProcesAttributes(instance, element);
				
				JAXBElement<ThickClient> jaxbElement =  new JAXBElement(new QName("thickClient"), ThickClient.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);
			} else if (type.contains("SE.P") || type.contains("GE.P")) {		// make web server if something else
				WebServer instance = new WebServer();
				instance.setId(id);
				fillProcesAttributes(instance, element);
				
				JAXBElement<WebServer> jaxbElement =  new JAXBElement(new QName("webServer"), WebServer.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);
				
			//SPOLJASNJI
			} else if (type.equals("SE.EI.TMCore.Browser")) {
				Browser instance = new Browser();
				instance.setId(id);
				fillExternalEntityAttributes(instance, element);
				
				JAXBElement<Browser> jaxbElement =  new JAXBElement(new QName("browser"), Browser.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);				
			} else if (type.equals("SE.EI.TMCore.WebApp")) {
				ExternalWebApplication instance = new ExternalWebApplication();
				instance.setId(id);
				fillExternalEntityAttributes(instance, element);
				
				JAXBElement<ExternalWebApplication> jaxbElement =  new JAXBElement(new QName("externalWebApplication"), ExternalWebApplication.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);				
			} else if (type.equals("SE.EI.TMCore.WebSvc")) {
				ExternalWebService instance = new ExternalWebService();
				instance.setId(id);
				fillExternalEntityAttributes(instance, element);
				
				JAXBElement<ExternalWebService> jaxbElement =  new JAXBElement(new QName("externalWebService"), ExternalWebService.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);				
			} else if (type.contains("SE.EI") || type.contains("GE.EI")) {		// make browser if something else
				Browser instance = new Browser();
				instance.setId(id);
				fillExternalEntityAttributes(instance, element);
				
				JAXBElement<Browser> jaxbElement =  new JAXBElement(new QName("browser"), Browser.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);	
				
			//SKLADISTA
			} else if (type.equals("SE.DS.TMCore.CloudStorage")) {
				CloudStorage instance = new CloudStorage();
				instance.setId(id);
				fillDataStoreAttributes(instance, element);
				
				JAXBElement<CloudStorage> jaxbElement =  new JAXBElement(new QName("cloudStorage"), CloudStorage.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);		
			} else if (type.equals("SE.DS.TMCore.SQL")) {
				SqlDatabase instance = new SqlDatabase();
				instance.setId(id);
				fillDataStoreAttributes(instance, element);
				
				JAXBElement<SqlDatabase> jaxbElement =  new JAXBElement(new QName("sqlDatabase"), SqlDatabase.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);		
			} else if (type.equals("SE.DS.TMCore.NoSQL")) {
				NoSqlDatabase instance = new NoSqlDatabase();
				instance.setId(id);
				fillDataStoreAttributes(instance, element);
				
				JAXBElement<NoSqlDatabase> jaxbElement =  new JAXBElement(new QName("noSqlDatabase"), NoSqlDatabase.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);		
			} else if (type.equals("SE.DS.TMCore.FS")) {
				FileSystem instance = new FileSystem();
				instance.setId(id);
				fillDataStoreAttributes(instance, element);
				
				JAXBElement<FileSystem> jaxbElement =  new JAXBElement(new QName("fileSystem"), FileSystem.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);		
			} else if (type.contains("SE.DS") || type.contains("GE.DS")) {		// make sql database if something else
				SqlDatabase instance = new SqlDatabase();
				instance.setId(id);
				fillDataStoreAttributes(instance, element);
				
				JAXBElement<SqlDatabase> jaxbElement =  new JAXBElement(new QName("sqlDatabase"), SqlDatabase.class, null);
		        jaxbElement.setValue(instance); 	
				convertedElements.add(jaxbElement);		
				 
			//SEKCIJE
			} else if (type.equals("SE.TB.B.TMCore.CorpNet")) {
				CompanyTrustSection instance = new CompanyTrustSection();
				instance.setId(id);
				fillSectionAttributes(instance, element);
				
				JAXBElement<CompanyTrustSection> jaxbElement =  new JAXBElement(new QName("companyTrustSection"), CompanyTrustSection.class, null);
		        jaxbElement.setValue(instance); 	
				convertedSections.add(jaxbElement);		
						
			} else if (type.equals("SE.TB.B.TMCore.Sandbox")) {
				SandboxTrustSection instance = new SandboxTrustSection();
				instance.setId(id);
				fillSectionAttributes(instance, element);
				
				JAXBElement<SandboxTrustSection> jaxbElement =  new JAXBElement(new QName("sandboxTrustSection"), SandboxTrustSection.class, null);
		        jaxbElement.setValue(instance); 	
				convertedSections.add(jaxbElement);					
			} else if (type.contains("SE.TB.B")) {		// make CompanyTrustSection if something else
				CompanyTrustSection instance = new CompanyTrustSection();
				instance.setId(id);
				fillSectionAttributes(instance, element);
				
				JAXBElement<CompanyTrustSection> jaxbElement =  new JAXBElement(new QName("companyTrustSection"), CompanyTrustSection.class, null);
		        jaxbElement.setValue(instance); 	
				convertedSections.add(jaxbElement);		
						
			}
		}		
	}
	
	private void convertFlowsAndBorders() {
				
		for (com.eds.Converter_Solution.model.input.ThreatModel.Lines.AKeyValueOfguidanyType element : flowsBorders) {
			
			//Boundaries: SE.TB.L.TMCore.Kernel,SE.TB.L.TMCore.AppContainer
			//Flows: SE.DF.TMCore.IPsec,SE.DF.TMCore.NamedPipe,SE.DF.TMCore.SMB,SE.DF.TMCore.RPC,SE.DF.TMCore.ALPC,SE.DF.TMCore.UDP,SE.DF.TMCore.IOCTL

			String type = element.getAValue().getTypeId().getValue();
			
			String id = "m" + element.getAValue().getGuid().getValue();

			//TOKOVI
			if (type.equals("SE.DF.TMCore.HTTP")) {
				Http instance = new Http();
				instance.setId(id);
				fillFlowAttributes(instance, element);	

				JAXBElement<Http> jaxbElement =  new JAXBElement(new QName("http"), Http.class, null);
	        	jaxbElement.setValue(instance); 
				convertedFlows.add(jaxbElement);
			} else if (type.equals("SE.DF.TMCore.HTTPS")) {
				Https instance = new Https();
				instance.setId(id);
				fillFlowAttributes(instance, element);

				JAXBElement<Https> jaxbElement =  new JAXBElement(new QName("https"), Https.class, null);
	        	jaxbElement.setValue(instance); 
				convertedFlows.add(jaxbElement);
			} else if (type.equals("SE.DF.TMCore.Binary")) {
				Binary instance = new Binary();
				instance.setId(id);
				fillFlowAttributes(instance, element);

				JAXBElement<Binary> jaxbElement =  new JAXBElement(new QName("binary"), Binary.class, null);
	        	jaxbElement.setValue(instance); 
				convertedFlows.add(jaxbElement);
			} else if (type.contains("SE.DF")) {		// make http if something else
				Http instance = new Http();
				instance.setId(id);
				fillFlowAttributes(instance, element);

				JAXBElement<Http> jaxbElement =  new JAXBElement(new QName("http"), Http.class, null);
	        	jaxbElement.setValue(instance); 
				convertedFlows.add(jaxbElement);
				
			//GRANICE
			} else if (type.equals("SE.TB.L.TMCore.Machine")) {
				MachineBoundary instance = new MachineBoundary();
				instance.setId(id);
				fillBoundaryAttributes(instance, element);
				
				JAXBElement<MachineBoundary> jaxbElement =  new JAXBElement(new QName("machineBoundary"), MachineBoundary.class, null);
		        jaxbElement.setValue(instance); 	
				convertedBoundaries.add(jaxbElement);
			} else if (type.equals("SE.TB.L.TMCore.Internet")) {
				InternetBoundary instance = new InternetBoundary();
				instance.setId(id);
				fillBoundaryAttributes(instance, element);
				
				JAXBElement<InternetBoundary> jaxbElement =  new JAXBElement(new QName("internetBoundary"), InternetBoundary.class, null);
		        jaxbElement.setValue(instance); 	
				convertedBoundaries.add(jaxbElement);
			} else if (type.contains("SE.TB.L")) {		// make internet boundary if something else
				InternetBoundary instance = new InternetBoundary();
				instance.setId(id);
				fillBoundaryAttributes(instance, element);
				
				JAXBElement<InternetBoundary> jaxbElement =  new JAXBElement(new QName("internetBoundary"), InternetBoundary.class, null);
		        jaxbElement.setValue(instance); 	
				convertedBoundaries.add(jaxbElement);
			}
		}		
	}
			

	private void fillProcesAttributes(Process instance, AKeyValueOfguidanyType source) {
		//Atributi: Predefined Static Attributes,Code Type,Configurable Attributes,As Generic Process,Running As,Accepts Input From,Implements or Uses a Communication Protocol
		
		for (AanyType property : source.getAValue().getProperties().getAanyType()) {
			
			String propertyName = property.getBDisplayName();
			
			if (propertyName.equals("Name")) {				 
				instance.setName(property.getBValue().getContent().get(0).toString());
			} else if (propertyName.equals("Out Of Scope")) {
				instance.setOutOfScope(property.getBValue().getContent().get(0).equals("true"));
			} else if (propertyName.equals("Reason For Out Of Scope")) {
				if (property.getBValue().getContent().size() > 0) {
					instance.setOutOfScopeReason(property.getBValue().getContent().get(0).toString());
				} else {
					instance.setOutOfScopeReason("");
				}
			} else if (propertyName.equals("Sanitizes Input")) {
				instance.setSanitizeInput(property.getBSelectedIndex() == 1);
			} else if (propertyName.equals("Sanitizes Output")) {
				instance.setSanitizeOutput(property.getBSelectedIndex() == 1);				
			} else if (propertyName.equals("Isolation Level")) {
				switch (property.getBSelectedIndex()) {
					case 0:
						instance.setRunLevel("HIGH_PRIVILEGE");
						break;
					case 3:
						instance.setRunLevel("SANDBOX");
						break;
					case 4:
						instance.setRunLevel("SANDBOX");
						break;
					default:
						instance.setRunLevel("LOW_PRIVILEGE");
				}
			} else if (propertyName.equals("Implements or Uses an Authentication Mechanism")) {
				instance.setRequiresAuthentication(property.getBSelectedIndex() == 1);				
			} else if (propertyName.equals("Implements or Uses an Authorization Mechanism")) {
				instance.setRequiresAuthorization(property.getBSelectedIndex() == 1);							
			}
			
		}
	}
	
	private void fillExternalEntityAttributes(ExternalEntity instance, AKeyValueOfguidanyType source) {		
		//Atributi: Predefined Static Attributes,Type,Configurable Attributes,As Generic External Interactor,Authenticates Itself,Microsoft
		
		for (AanyType property : source.getAValue().getProperties().getAanyType()) {
			
			String propertyName = property.getBDisplayName();

			if (propertyName.equals("Name")) {				 
				instance.setName(property.getBValue().getContent().get(0).toString());
			} else if (propertyName.equals("Out Of Scope")) {
				instance.setOutOfScope(property.getBValue().getContent().get(0).equals("true"));
			} else if (propertyName.equals("Reason For Out Of Scope")) {
				if (property.getBValue().getContent().size() > 0) {
					instance.setOutOfScopeReason(property.getBValue().getContent().get(0).toString());
				} else {
					instance.setOutOfScopeReason("");
				}	
			}
			
		}
		
		instance.setRunLevel("HIGH_PRIVILEGE");
		instance.setSanitizeInput(false);
		instance.setSanitizeOutput(false);	
	}

	private void fillDataStoreAttributes(DataStore instance, AKeyValueOfguidanyType source) {	
		//Atributi: Predefined Static Attributes,Store Type,Configurable Attributes,As Generic Data Store,Stores Log Data,Write Access,Removable Storage,Shared
	
		for (AanyType property : source.getAValue().getProperties().getAanyType()) {	
			String propertyName = property.getBDisplayName();

			if (propertyName.equals("Name")) {				 
				instance.setName(property.getBValue().getContent().get(0).toString());
			} else if (propertyName.equals("Out Of Scope")) {
				instance.setOutOfScope(property.getBValue().getContent().get(0).equals("true"));
			} else if (propertyName.equals("Reason For Out Of Scope")) {
				if (property.getBValue().getContent().size() > 0) {
					instance.setOutOfScopeReason(property.getBValue().getContent().get(0).toString());
				} else {
					instance.setOutOfScopeReason("");
				}				

			} else if (propertyName.equals("Stores Credentials")) {
				instance.setStoreCredentials(property.getBSelectedIndex() == 1);
			} else if (propertyName.equals("Encrypted")) {
				instance.setDataIsEncrypted(property.getBSelectedIndex() == 1);
			} else if (propertyName.equals("Signed")) {
				instance.setDataIsSigned(property.getBSelectedIndex() == 1);
			} else if (propertyName.equals("Backup")) {
				instance.setHasBackup(property.getBSelectedIndex() == 1);
			}
		
		}
		instance.setRunLevel("HIGH_PRIVILEGE");
	}

	private void fillSectionAttributes(BlockShape instance, AKeyValueOfguidanyType source) {

		for (AanyType property : source.getAValue().getProperties().getAanyType()) {	
			String propertyName = property.getBDisplayName();

			if (propertyName.equals("Name")) {				 
				instance.setName(property.getBValue().getContent().get(0).toString());
			}
			
		}		
	}
	

	
	private void fillFlowAttributes(Flow instance, com.eds.Converter_Solution.model.input.ThreatModel.Lines.AKeyValueOfguidanyType source) {
		//Atributi: Predefined Static Attributes,Source Authenticated,Destination Authenticated,Provides Confidentiality,Provides Integrity,Configurable Attributes,As Generic Data Flow,Physical Network,SOAP Payload,REST Payload,RSS Payload,JSON Payload,Forgery Protection 
		
		for (com.eds.Converter_Solution.model.input.ThreatModel.Lines.AKeyValueOfguidanyType.AValue.Properties.AanyType property : source.getAValue().getProperties().getAanyType()) {	
			String propertyName = property.getBDisplayName();

			if (propertyName.equals("Name")) {				 
				instance.setName(property.getBValue().getContent().get(0).toString());
			} else if (propertyName.equals("Out Of Scope")) {
				instance.setOutOfScope(property.getBValue().getContent().get(0).equals("true"));
			} else if (propertyName.equals("Reason For Out Of Scope")) {
				if (property.getBValue().getContent().size() > 0) {
					instance.setOutOfScopeReason(property.getBValue().getContent().get(0).toString());
				} else {
					instance.setOutOfScopeReason("");
				}
			} else if (propertyName.equals("Transmits XML")) {
				instance.setContainsXML(property.getBSelectedIndex() == 1);
			} else if (propertyName.equals("Contains Cookies")) {
				instance.setContainsCookies(property.getBSelectedIndex() == 0);				
			}
				
		}
			
		String flowId = instance.getId().substring(1);
		
		List<AKeyValueOfstringThreatpcP0PhOB> endpoints = flowEndpoints.stream().filter(f -> ((AKeyValueOfstringThreatpcP0PhOB)f).getAValue().getBFlowGuid().equals(flowId)).collect(Collectors.toList());		

		if (endpoints.size() == 0) {
			System.out.println("WARNING: Some of flows can not connect elements! Flow ID: " + flowId + " Flow Name: " + instance.getName());
		}
		
		for (AKeyValueOfstringThreatpcP0PhOB endpoint : endpoints) {
			
			String sourceId = "m" + endpoint.getAValue().getBSourceGuid();
			String targetId = "m" + endpoint.getAValue().getBTargetGuid();
			
			if (sourceId != null && !sourceId.equals("") && targetId != null && !targetId.equals("")) {
				List<JAXBElement<? extends BlockElement>> sourceElements = convertedElements.stream().filter(e -> ((BlockElement)e.getValue()).getId().equals(sourceId)).collect(Collectors.toList());	
				List<JAXBElement<? extends BlockElement>> destinationElements = convertedElements.stream().filter(e -> ((BlockElement)e.getValue()).getId().equals(targetId)).collect(Collectors.toList());	
				
				if (sourceElements.size() > 0 && destinationElements.size() > 0) {
				
					BlockElement sourceElement = sourceElements.get(0).getValue();
					BlockElement destinationElement = destinationElements.get(0).getValue();
					
					if (sourceElement != null && destinationElement != null) {
						if (instance.getDestination() == null || instance.getDestination().equals("")) {				
							instance.setSource(sourceElement);
							instance.setDestination(destinationElement);
							break;
						}
					}
				}
			}
		}
		
	}
	
	private void fillBoundaryAttributes(Shape instance, com.eds.Converter_Solution.model.input.ThreatModel.Lines.AKeyValueOfguidanyType source) {

		for (com.eds.Converter_Solution.model.input.ThreatModel.Lines.AKeyValueOfguidanyType.AValue.Properties.AanyType property : source.getAValue().getProperties().getAanyType()) {	
			String propertyName = property.getBDisplayName();

			if (propertyName.equals("Name")) {				 
				instance.setName(property.getBValue().getContent().get(0).toString());
			}
			
		}		
	}

	
	
	public List<JAXBElement<? extends BlockElement>> getConvertedElements() {
		return convertedElements;
	}

	public List<JAXBElement<? extends BlockShape>> getConvertedSections() {
		return convertedSections;
	}

	public List<JAXBElement<? extends Flow>> getConvertedFlows() {
		return convertedFlows;
	}

	public List<JAXBElement<? extends Shape>> getConvertedBoundaries() {
		return convertedBoundaries;
	}

	
	
	
	
}
