package com.eds.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "report")
public class ReportClass {
	
	private String dateAndTimeOfGeneration;
	private String diagramFileName;
	private ReportPatterns patterns;

	public ReportClass() {
		
	}

	public ReportClass(List<ReportPattern> patternList, String diagramFileName) {
		this.patterns = new ReportPatterns(patternList);
		this.diagramFileName = diagramFileName;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
		this.dateAndTimeOfGeneration = sdf.format(new Date());
	}

	public ReportPatterns getPatterns() {
		return patterns;
	}

	public void setPatterns(ReportPatterns patterns) {
		this.patterns = patterns;
	}

	public String getDateAndTimeOfGeneration() {
		return dateAndTimeOfGeneration;
	}

	public void setDateAndTimeOfGeneration(String dateAndTimeOfGeneration) {
		this.dateAndTimeOfGeneration = dateAndTimeOfGeneration;
	}

	public String getDiagramFileName() {
		return diagramFileName;
	}

	public void setDiagramFileName(String diagramFileName) {
		this.diagramFileName = diagramFileName;
	}	
	
}
