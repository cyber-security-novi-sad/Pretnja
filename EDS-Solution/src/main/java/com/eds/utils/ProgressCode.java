package com.eds.utils;

public class ProgressCode {

	public static final int INITIALIZED = 0;
	
	// 1- Validating resources - 10
	public static final int STARTED = 1;
	public static final int VALIDATING_DIAGRAM = 2;
	public static final int VALIDATING_ASSETS = 6;
	public static final int VALIDATING_EXPLOITS = 7;	
	// 1- Importing resources - 15
	public static final int READING_DIAGRAM = 10;
	public static final int READING_ASSETS = 20;
	
	// 2- Applying assets - 10
	public static final int MERGING_DIAGRAM_ASSETS = 25;
	
	// 3- Decomposing diagram - 15
	public static final int DECOMPOSING_DIAGRAM = 35;
	
	// 4- Analyzing diagram patterns - 35
	public static final int RULES_ANALYZING = 50;
	
	// 5- Applying exploits - 10
	public static final int READING_EXPLOITS = 85;
	public static final int MERGING_DIAGRAM_EXPLOITS = 88;
	
	// 6- Creating report - 5
	public static final int GENERATING_REPORT_PATTERNS = 95;
	public static final int GENERATING_REPORT = 98;

	public static final int DONE = 100;
	public static final int CANCELED = 111;
	
}
