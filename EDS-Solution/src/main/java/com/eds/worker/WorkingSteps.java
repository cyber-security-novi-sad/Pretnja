package com.eds.worker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.kie.api.runtime.StatelessKieSession;
import org.xml.sax.SAXException;

import com.eds.gui.WorkingDialog;
import com.eds.model.report.ReportClass;
import com.eds.model.report.ReportPattern;
import com.eds.model.risk_pattern.DiagramPattern;
import com.eds.model.risk_pattern.ExploitOfAsset;
import com.eds.utils.Decomposer;
import com.eds.utils.KieRulesBase;
import com.eds.utils.DiagramMerge;
import com.eds.utils.ProgressCode;
import com.eds.utils.Validator;
import com.eds.utils.XMLLinker;
import com.eds.EDS_Library.diagram.AssetDefinitions;
import com.eds.EDS_Library.diagram.Diagram;
import com.eds.EDS_Library.diagram.Element;
import com.eds.EDS_Library.diagram.ExploitDefinition;
import com.eds.EDS_Library.diagram.ExploitDefinitions;

public class WorkingSteps extends SwingWorker<Boolean, Object> {
	
	private WorkingDialog dialog;

	private File diagramFile;
	private File assetsFile;
	private File exploitsFile;
	private File reportFile;
	private String schemaPath;;
	private boolean analyseComponents;
	
	private Diagram diagram;
	private AssetDefinitions assetDefinitions;
	private ExploitDefinitions exploitDefinitions;
	
	private List<DiagramPattern> patterns;
	private ReportClass report;
	
	
	
	public WorkingSteps(File diagram, File assets, File exploits, File report, String schemaPath, boolean analyseComponents) {
		this.diagramFile = diagram;
		this.assetsFile = assets;
		this.exploitsFile = exploits;
		this.reportFile = report;
		this.schemaPath = schemaPath;
		this.analyseComponents = analyseComponents;
	}

	@Override
	protected Boolean doInBackground() throws Exception {		
		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}		
		
		/* 1 ********	VALIDATING XML DIAGRAM			**********	- DONE	*/
		setProgress(ProgressCode.STARTED);
		setProgress(ProgressCode.VALIDATING_DIAGRAM);
		
		if (!validateDiagram()) {
			return false;
		}		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}		

		/* 1 ********	VALIDATING XML ASSETS			**********	- DONE	*/
		setProgress(ProgressCode.VALIDATING_ASSETS);
		
		if (!validateAssetDefinitions()) {
			return false;
		}		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}		

		/* 1 ********	VALIDATING XML EXPLOITS			**********	- DONE	*/
		setProgress(ProgressCode.VALIDATING_EXPLOITS);
		
		if (!validateExploitDefinitions()) {
			return false;
		}		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}		

		/* 1 ********	READING XML DIAGRAM TO MEMORY	**********	- DONE	*/
		setProgress(ProgressCode.READING_DIAGRAM);
		
		if (!readDiagram()) {
			return false;
		}
		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}

		/* 1 ********	READING XML ASSETS TO MEMORY	**********	- DONE	*/
		setProgress(ProgressCode.READING_ASSETS);
		
		if (!readAssetDefinitions()) {
			return false;
		}
		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}

		/* 2 ********	MERGING DIAGRAM	AND ASSETS		**********	- DONE	*/
		setProgress(ProgressCode.MERGING_DIAGRAM_ASSETS);

		DiagramMerge mergeAssets = new DiagramMerge(diagram, assetDefinitions);
		diagram = mergeAssets.mergeAssetsToDiagram();
		
		if (Thread.currentThread().isInterrupted()) {
			 return false;
		}

		/* 3 ********	DECOMPOSING XML DIAGRAM			**********	- DONE	*/
		setProgress(ProgressCode.DECOMPOSING_DIAGRAM);
		
		Decomposer decomposer = new Decomposer(diagram);
		patterns = decomposer.decomposeAllPatterns();
		
		if (patterns.size() == 0) {
			return false;
		}
		
		//printDiagramPatterns(patterns);
		//if (patterns.size() > 0) return true;
		
		if (Thread.currentThread().isInterrupted()) {
			 return false; 
		}

		/* 4 ********	ANALYZING DIAGRAM COMPONENTS	**********	- DONE	*/
		setProgress(ProgressCode.RULES_ANALYZING);
				
		if (!createAndFireRules()) {
			return false;
		}

		//printRawExploitsForDiagramPatterns(patterns);
		//if (patterns.size() > 0) return true;
		
		if (Thread.currentThread().isInterrupted()) {
			 return false; 
		}
		
		/* 4 ********	REQUEST TO NVD FOR VULNERABILITIES*********	- DONE  */	
		if (analyseComponents) {
			analyzeVulnerabilitiesForComponentTechnologies();
		}		
		
		if (Thread.currentThread().isInterrupted()) {
			 return false; 
		}

		/* 5 ********	READING EXPLOITS		 		**********	- DONE  */	
		setProgress(ProgressCode.READING_EXPLOITS);	
		
		if (!readExploitDefinitions()) {
			return false;
		}				
		
		if (Thread.currentThread().isInterrupted()) {
			 return false; 
		}
		
		/* 5 ********	MERGING EXPLOITS AND DIAGRAMS	**********	- DONE	*/
		setProgress(ProgressCode.MERGING_DIAGRAM_EXPLOITS);	
			
		DiagramMerge mergeExploits = new DiagramMerge(patterns, exploitDefinitions, assetDefinitions);
		patterns = mergeExploits.mergeExploitsAndAssetsToDiagramPieces();
		
		if (Thread.currentThread().isInterrupted()) {
			 return false; 
		}

		/* 6 ********	CREATING REPORT	PATTERN			**********	- DONE */	
		setProgress(ProgressCode.GENERATING_REPORT_PATTERNS);
		
		report = createReportPatternsFromDiagramPatterns();				
		
		if (Thread.currentThread().isInterrupted()) {
			return false;
		}

		/* 6 ********	CREATING XML REPORT				**********	- DONE */	
		setProgress(ProgressCode.GENERATING_REPORT);

		if (!createXMLReportFile()) {
			return false;
		}				
		
		if (Thread.currentThread().isInterrupted()) {
			return false;
		}
		
		/* ******************************************************* */	
		
		setProgress(ProgressCode.DONE);

		//printExploitsForDiagramPatterns(patterns);		
		
		return true;
	}

	private boolean validateDiagram() {
		String message;
		
		try {
			
			Validator.checkWellFormness(diagramFile);
			
		} catch (ParserConfigurationException e) {
			message = "Parser could not be initialized!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		} catch (SAXException e) {
			message = "XML Diagram is not well formed!\nXML Diagram contains syntax errors. Please choose another diagram or fix current.";
			dialog.setErrorMessage(message);
			return false;
		} catch (IOException e) {
			message = "File could not be read!\nPlease load file again and try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		
		try {
			
			Validator.checkDiagramValidity(diagramFile, schemaPath);
			
		} catch (SAXException e) {
			message = "XML Diagram is not valid!\nXML Diagram contains semantic errors. Please choose another diagram or fix current.";
			dialog.setErrorMessage(message);
			return false;
		} catch (IOException e) {
			message = "File could not be read!\nPlease load file again and try again.";
			dialog.setErrorMessage(message);
			return false;
		} 
		
		return true;
	}
	
	private boolean validateAssetDefinitions() {
		String message;
		
		try {
			
			Validator.checkWellFormness(assetsFile);
			
		} catch (ParserConfigurationException e) {
			message = "Parser could not be initialized!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		} catch (SAXException e) {
			message = "Asset Definitions XML file is not well formed!\nXML File contains syntax errors. Please choose another file or fix current.";
			dialog.setErrorMessage(message);
			return false;
		} catch (IOException e) {
			message = "File could not be read!\nPlease load file again and try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		
		try {
			
			Validator.checkAssetDefinitionsValidity(assetsFile, schemaPath);
			
		} catch (SAXException e) {
			message = "Asset Definitions XML file is not valid!\nXML File contains semantic errors. Please choose another file or fix current.";
			dialog.setErrorMessage(message);
			return false;
		} catch (IOException e) {
			message = "File could not be read!\nPlease load file again and try again.";
			dialog.setErrorMessage(message);
			return false;
		} 
		
		return true;
	}
	
	private boolean validateExploitDefinitions() {
		String message;
		
		try {
			
			Validator.checkWellFormness(assetsFile);
			
		} catch (ParserConfigurationException e) {
			message = "Parser could not be initialized!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		} catch (SAXException e) {
			message = "Exploit Definitions XML file is not well formed!\nXML File has been unauthorizedly modified and contains syntax errors. Please contact the support.";
			dialog.setErrorMessage(message);
			return false;
		} catch (IOException e) {
			message = "File could not be read!\nPlease load file again and try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		
		try {
			
			Validator.checkExploitDefinitionsValidity(exploitsFile, schemaPath);
			
		} catch (SAXException e) {
			message = "Exploit Definitions XML file is not valid!\nXML File has been unauthorizedly modified and contains semantic errors. Please contact the support.";
			dialog.setErrorMessage(message);
			return false;
		} catch (IOException e) {
			message = "File could not be read!\nPlease load file again and try again.";
			dialog.setErrorMessage(message);
			return false;
		} 
		
		return true;
	}
	
	private boolean readDiagram() {

		try {
			
			diagram = XMLLinker.readXMLDiagram(diagramFile);
			
		} catch (JAXBException e) {
			String message = "Parser could not be initialized!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		return true;
	}
	
	private boolean readAssetDefinitions() {

		try {
			
			assetDefinitions = XMLLinker.readXMLAssets(assetsFile);
			
		} catch (JAXBException e) {
			String message = "Parser could not be initialized!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		return true;
	}
	
	private boolean readExploitDefinitions() {

		try {
			
			exploitDefinitions = XMLLinker.readXMLExploits(exploitsFile);
			
		} catch (JAXBException e) {
			String message = "Parser could not be initialized!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		return true;
	}
	
	private boolean createAndFireRules() {
		
		StatelessKieSession session = null;
		
		try {
			session = KieRulesBase.createStatelessSession();
		} catch (RuntimeException e) {
			String message = "Rule base could not be built!\nThere is an error in rule definition files.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		if (session == null) {
			String message = "There are no rules files locations defined!\nPlease check the 'resources/locations.txt' file.";
			dialog.setErrorMessage(message);			
			return false;
		}
		
		for (DiagramPattern diagramPattern : patterns) {
			session.execute(diagramPattern);				
		}
		return true;
	}
	
	private void analyzeVulnerabilitiesForComponentTechnologies() {
		
	}
	
	private ReportClass createReportPatternsFromDiagramPatterns() {
		List<ReportPattern> reports = new ArrayList<>();
		
		for (DiagramPattern diagramPattern : patterns) {
			if (diagramPattern.getExploitValues().size() > 0) {
				reports.add(new ReportPattern(diagramPattern));
			}
		}
		
		ReportClass report = new ReportClass(reports, diagramFile.getName());
		
		return report;
	}
	
	private boolean createXMLReportFile() {
		
		try {
			XMLLinker.writeXMLReportFile(report, reportFile);
		} catch (JAXBException e) {
			String message = "Report File could not be written!\nPlease try again.";
			dialog.setErrorMessage(message);
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unused")
	private void printDiagramPatterns(List<DiagramPattern> diagramPatterns) {
		for (DiagramPattern pattern : diagramPatterns) {
			if (pattern.getTraceStart() != null) {
				System.out.println("\n********* Pattern for " + pattern.getElement().getName() + "\n- Trace for " + pattern.getTraceStart().getName() + ":");
				for (Element element : pattern.getTrace()) {
					System.out.println("\t " + element.getName());
				}
			} else {
				System.out.println("\n********* Pattern " + pattern.getElement().getName() + " with no trace");
			}
		}	
	}

	@SuppressWarnings("unused")
	private void printRawExploitsForDiagramPatterns(List<DiagramPattern> diagramPatterns) {
		for (DiagramPattern pattern : diagramPatterns) {	
			if (pattern.getTraceStart() != null) {		
				System.out.println("*******************\nExploits on element " + pattern.getElement().getName() + "\n- Trace for "  + pattern.getTraceStart().getName() + ":");
				for (ExploitOfAsset exploit : pattern.getExploitAsset()) {
					if (exploit != null) {
						System.out.println("\t\t- " + exploit.getExploit() + " \t asset: " + exploit.getAsset());
					}
				}
			} else {
				System.out.println("*******************\nExploits on element " + pattern.getElement().getName() + " with no trace:");
				for (ExploitOfAsset exploit : pattern.getExploitAsset()) {
					if (exploit != null) {
						System.out.println("\t\t- " + exploit.getExploit() + " \t asset: " + exploit.getAsset());
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void printExploitsForDiagramPatterns(List<DiagramPattern> diagramPatterns) {
		System.out.println("\n*************************************************************************");
		System.out.println("*************************************************************************\n");
		for (DiagramPattern pattern : diagramPatterns) {	
			if (pattern.getTraceStart() != null) {		
				System.out.println("*******************\n" + pattern.getElement().getName() + " from "  + pattern.getTraceStart().getName() + ":");
				for (ExploitDefinition exploit : pattern.getExploitValues()) {
					if (exploit != null) {
						System.out.println("\t\t- " + exploit.getExploitTitle());
					}
				}
			} else {
				System.out.println("*******************\n" + pattern.getElement().getName() + " with no trace:");
				for (ExploitDefinition exploit : pattern.getExploitValues()) {
					if (exploit != null) {
						System.out.println("\t\t- " + exploit.getExploitTitle());
					}
				}
			}
		}
	}

	public void setDialog(WorkingDialog dialog) {
		this.dialog = dialog;
	}

	public File getReportFile() {
		return reportFile;
	}
	
	

}
