package com.eds.utils;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.eds.model.risk_pattern.DiagramPattern;
import com.eds.model.risk_pattern.ExploitOfAsset;
import com.eds.EDS_Library.diagram.AssetDefinition;
import com.eds.EDS_Library.diagram.AssetDefinitions;
import com.eds.EDS_Library.diagram.Assets.Asset;
import com.eds.EDS_Library.diagram.BlockElement;
import com.eds.EDS_Library.diagram.Diagram;
import com.eds.EDS_Library.diagram.ExploitDefinition;
import com.eds.EDS_Library.diagram.ExploitDefinitions;
import com.eds.EDS_Library.diagram.Exploits.Exploit;
import com.eds.EDS_Library.diagram.Flow;
import com.eds.EDS_Library.diagram.ImportAssets;
import com.eds.EDS_Library.diagram.ImportExploits;

public class DiagramMerge {
	
	private Diagram diagram;
	private AssetDefinitions assetDefinitions;
	private List<DiagramPattern> diagramPatterns;
	private ExploitDefinitions exploitDefinitions;

	public DiagramMerge(Diagram diagram, ExploitDefinitions exploitDefinitions) {
		this.diagram = diagram;
		this.exploitDefinitions = exploitDefinitions;
		this.assetDefinitions = null;
	}

	public DiagramMerge(Diagram diagram, AssetDefinitions assetDefinitions) {
		this.diagram = diagram;
		this.assetDefinitions = assetDefinitions;
		this.exploitDefinitions = null;
	}

	public DiagramMerge(List<DiagramPattern> diagramPatterns, ExploitDefinitions exploitDefinitions) {
		this.diagramPatterns = diagramPatterns;
		this.exploitDefinitions = exploitDefinitions;
		this.assetDefinitions = null;
		this.diagram = null;
	}

	public DiagramMerge(List<DiagramPattern> diagramPatterns, ExploitDefinitions exploitDefinitions, AssetDefinitions assetDefinitions) {
		this.diagramPatterns = diagramPatterns;
		this.exploitDefinitions = exploitDefinitions;
		this.assetDefinitions = assetDefinitions;
		this.diagram = null;
	}
	
	public Diagram mergeAssetsToDiagram() {
		HashMap<String, AssetDefinition> assetsMap = makeAssetsMap();
		
		List<JAXBElement<? extends BlockElement>> elements = diagram.getElements().getOrWebApplicationOrWebServiceOrWebServer();
		for (JAXBElement<? extends BlockElement> element : elements) {
			element.getValue().setImportAssets(new ImportAssets());
			if (element.getValue().getAssets() != null) {
				for (Asset asset : element.getValue().getAssets().getAsset()) {
					AssetDefinition add = assetsMap.get(asset.getAssetId());
					element.getValue().getImportAssets().getImportAsset().add(add);
				}
			}
		}
		
		List<JAXBElement<? extends Flow>> flows = diagram.getFlows().getOrBinaryOrHttpOrHttps();	
		for (JAXBElement<? extends Flow> flow : flows) {
			flow.getValue().setImportAssets(new ImportAssets());
			if (flow.getValue().getAssets() != null) {
				for (Asset asset : flow.getValue().getAssets().getAsset()) {
					AssetDefinition add = assetsMap.get(asset.getAssetId());
					flow.getValue().getImportAssets().getImportAsset().add(add);
				}
			}
		}
		
		return this.diagram;
	}
	
	public Diagram mergeExploitsToDiagram() {
		HashMap<String, ExploitDefinition> exploitsMap = makeExploitsMap();
		
		List<JAXBElement<? extends BlockElement>> elements = diagram.getElements().getOrWebApplicationOrWebServiceOrWebServer();
		for (JAXBElement<? extends BlockElement> element : elements) {
			element.getValue().setImportExploits(new ImportExploits());
			if (element.getValue().getExploits() != null) {
				for (Exploit exploit : element.getValue().getExploits().getExploit()) {
					ExploitDefinition add = exploitsMap.get(exploit.getExploitId());
					element.getValue().getImportExploits().getImportExploit().add(add);
				}
			}
		}
		
		List<JAXBElement<? extends Flow>> flows = diagram.getFlows().getOrBinaryOrHttpOrHttps();		
		for (JAXBElement<? extends Flow> flow : flows) {
			flow.getValue().setImportExploits(new ImportExploits());
			if (flow.getValue().getExploits() != null) {
				for (Exploit exploit : flow.getValue().getExploits().getExploit()) {
					ExploitDefinition add = exploitsMap.get(exploit.getExploitId());
					flow.getValue().getImportExploits().getImportExploit().add(add);
				}
			}
		}

		return this.diagram;
	}
	
	public List<DiagramPattern> mergeExploitsToDiagramPieces() {
		HashMap<String, ExploitDefinition> exploitsMap = makeExploitsMap();
		
		for (DiagramPattern pattern : diagramPatterns) {
			for (ExploitOfAsset exploit : pattern.getExploitAsset()) {
				pattern.addExploitValue(exploitsMap.get(exploit.getExploit()));
			}
		}
		
		return diagramPatterns;
	}
	
	public List<DiagramPattern> mergeExploitsAndAssetsToDiagramPieces() {
		HashMap<String, ExploitDefinition> exploitsMap = makeExploitsMap();
		HashMap<String, AssetDefinition> assetsMap = makeAssetsMap();
		
		for (DiagramPattern pattern : diagramPatterns) {
			for (ExploitOfAsset exploitAsset : pattern.getExploitAsset()) {
				pattern.addExploitValue(exploitsMap.get(exploitAsset.getExploit()));
				pattern.addAssetValue(assetsMap.get(exploitAsset.getAsset()));
			}
		}
		
		return diagramPatterns;
	}
	
	private HashMap<String, AssetDefinition> makeAssetsMap() {
		HashMap<String, AssetDefinition> assetsMap = new HashMap<>();

		for (AssetDefinition asset : assetDefinitions.getAssetDefinition()) {
			assetsMap.put(asset.getAssetId(), asset);
		}
		
		return assetsMap;
	}
	
	private HashMap<String, ExploitDefinition> makeExploitsMap() {
		HashMap<String, ExploitDefinition> exploitsMap = new HashMap<>();
		
		for (ExploitDefinition exploit : exploitDefinitions.getExploitDefinition()) {
			exploitsMap.put(exploit.getExploitId(), exploit);
		}
		
		return exploitsMap;
	}
	
}
