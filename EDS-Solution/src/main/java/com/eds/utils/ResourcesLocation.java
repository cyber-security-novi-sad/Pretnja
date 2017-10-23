package com.eds.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eds.gui.MainWindow;

public class ResourcesLocation {
	
	private static final String LOCATIONS_FILE_PATH = "resources\\locations.txt";

	private static ResourcesLocation instance = null;
	
	private String schemaLocation;
	private String schemaLocationFolder;
	private List<String> rulesLocations;
	private String assetLocation;
	private String assetLocationFolder;
	private String exploitLocation;
	private String exploitLocationFolder;
	private String diagramLocationFolder;
	private String reportLocationFolder;
	
	private Map<String, String> parametersMap;
	
	private ResourcesLocation() {
		parametersMap = new HashMap<String, String>();
		readValues();
		setAllParameters();
	}
	
	private void readValues() {
		byte[] encoded = null;
		
		try {
			encoded = Files.readAllBytes(Paths.get(LOCATIONS_FILE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String content =  new String(encoded, StandardCharsets.UTF_8);
		content = content.replaceAll("\\s+","");
		String[] parameters = content.split(";");		
		
		for (String parameter : parameters) {
			String[] values = parameter.split("=");
			if (values.length > 1) {
				parametersMap.put(values[0], values[1]);
			}
		}
	}
	
	private void setAllParameters() {
		String schema = parametersMap.get("schemaLocation");
		String schemaFolder = parametersMap.get("schemaLocationFolder");
		String asset = parametersMap.get("assetLocation");
		String assetFolder = parametersMap.get("assetLocationFolder");
		String exploit = parametersMap.get("exploitLocation");
		String exploitFolder = parametersMap.get("exploitLocationFolder");
		String diagramFolder = parametersMap.get("diagramLocationFolder");	
		String reportFolder = parametersMap.get("reportLocationFolder");		
		
		String rules = parametersMap.get("rulesLocations");
		List<String> rulesLocationList;
		if (rules != null) {
			String[] rulesLocations = rules.split(",");
			rulesLocationList = Arrays.asList(rulesLocations);
		} else {
			rulesLocationList = new ArrayList<>();
		}
		
		setSchemaLocation(schema == null ? "" : schema);
		setSchemaLocationFolder(schemaFolder == null ? "" : schemaFolder);
		setAssetLocation(asset == null ? "" : asset);
		setAssetLocationFolder(assetFolder == null ? "" : assetFolder);
		setExploitLocation(exploit == null ? "" : exploit);
		setExploitLocationFolder(exploitFolder == null ? "" : exploitFolder);
		setDiagramLocationFolder(diagramFolder == null ? "" : diagramFolder);
		setReportLocationFolder(reportFolder == null ? "" : reportFolder);
		setRulesLocations(rulesLocationList);
	}
	
	public void saveAllParameters() {
		StringBuilder fileContentBuilder = new StringBuilder();

		fileContentBuilder.append("schemaLocation = " + MainWindow.getInstance().getSchemaLocation() + ";\n");
		fileContentBuilder.append("schemaLocationFolder = " + getSchemaLocationFolder() + ";\n");
		fileContentBuilder.append("assetLocation = " + MainWindow.getInstance().getAssetLocation() + ";\n");
		fileContentBuilder.append("assetLocationFolder = " + getAssetLocationFolder() + ";\n");
		fileContentBuilder.append("exploitLocation = " + MainWindow.getInstance().getExploitLocation() + ";\n");
		fileContentBuilder.append("exploitLocationFolder = " + getExploitLocationFolder() + ";\n");
		fileContentBuilder.append("diagramLocationFolder = " + getDiagramLocationFolder() + ";\n");
		fileContentBuilder.append("reportLocationFolder = " + getReportLocationFolder() + ";\n");

		String rulesLocationString = "";
		for (String ruleLocation : getRulesLocations()) {
			rulesLocationString = rulesLocationString + ruleLocation + ",\n\t\t";
		}
		if (rulesLocationString.length() > 1) {
			rulesLocationString = rulesLocationString.substring(0, rulesLocationString.length() - 4);
		}
		
		fileContentBuilder.append("rulesLocations = " + rulesLocationString + ";");
		
		
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(LOCATIONS_FILE_PATH));
			out.write(fileContentBuilder.toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ResourcesLocation getInstance() {
		if (instance == null) {
			instance = new ResourcesLocation();
		}
		return instance;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public List<String> getRulesLocations() {
		return rulesLocations;
	}

	public String getAssetLocation() {
		return assetLocation;
	}

	public String getAssetLocationFolder() {
		return assetLocationFolder;
	}

	public String getExploitLocation() {
		return exploitLocation;
	}

	public String getExploitLocationFolder() {
		return exploitLocationFolder;
	}

	public String getDiagramLocationFolder() {
		return diagramLocationFolder;
	}

	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	public void setRulesLocations(List<String> rulesLocations) {
		this.rulesLocations = rulesLocations;
	}

	public void setAssetLocation(String assetLocation) {
		this.assetLocation = assetLocation;
	}

	public void setAssetLocationFolder(String assetLocationFolder) {
		this.assetLocationFolder = assetLocationFolder;
	}

	public void setExploitLocation(String exploitLocation) {
		this.exploitLocation = exploitLocation;
	}

	public void setExploitLocationFolder(String exploitLocationFolder) {
		this.exploitLocationFolder = exploitLocationFolder;
	}

	public void setDiagramLocationFolder(String diagramLocationFolder) {
		this.diagramLocationFolder = diagramLocationFolder;
	}

	public String getSchemaLocationFolder() {
		return schemaLocationFolder;
	}

	public void setSchemaLocationFolder(String schemaLocationFolder) {
		this.schemaLocationFolder = schemaLocationFolder;
	}

	public String getReportLocationFolder() {
		return reportLocationFolder;
	}

	public void setReportLocationFolder(String reportLocationFolder) {
		this.reportLocationFolder = reportLocationFolder;
	}
	
}
