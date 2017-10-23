package com.eds.model.report;

import com.eds.model.risk_pattern.DiagramPattern;

public class ReportPattern {

	private static int counter = 0;
	private int id;
	private ReportPatternElement destinationElement;
	private ReportPatternElement sourceElement;
	private ReportPatternExploits exploits;
	
	public ReportPattern() {		
		
	}
	
	public ReportPattern(DiagramPattern diagramPattern) {
		counter++;
		this.id = counter;
		this.destinationElement = new ReportPatternElement(diagramPattern.getElement());
		this.sourceElement = diagramPattern.getTraceStart() == null ? null : new ReportPatternElement(diagramPattern.getTraceStart());
		this.exploits = new ReportPatternExploits(diagramPattern.getExploitValues(), diagramPattern.getAssetValues());
	}
	
		
	public static int getCounter() {
		return counter;
	}

	public int getId() {
		return id;
	}

	public ReportPatternElement getDestinationElement() {
		return destinationElement;
	}

	public ReportPatternElement getSourceElement() {
		return sourceElement;
	}

	public ReportPatternExploits getExploits() {
		return exploits;
	}

	public static void setCounter(int counter) {
		ReportPattern.counter = counter;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDestinationElement(ReportPatternElement destinationElement) {
		this.destinationElement = destinationElement;
	}

	public void setSourceElement(ReportPatternElement sourceElement) {
		this.sourceElement = sourceElement;
	}

	public void setExploits(ReportPatternExploits exploits) {
		this.exploits = exploits;
	}

}
