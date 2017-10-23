package com.eds.model.report;

import java.util.List;

public class ReportPatterns {

	private List<ReportPattern> pattern;
	
	public ReportPatterns(List<ReportPattern> patternList) {
		this.pattern = patternList;
	}

	public List<ReportPattern> getPattern() {
		return pattern;
	}

	public void setPattern(List<ReportPattern> pattern) {
		this.pattern = pattern;
	}	
	
}
