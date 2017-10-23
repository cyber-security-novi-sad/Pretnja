package com.eds.model.risk_pattern;

import java.util.ArrayList;
import java.util.List;

import com.eds.EDS_Library.diagram.AssetDefinition;
import com.eds.EDS_Library.diagram.BlockElement;
import com.eds.EDS_Library.diagram.Element;
import com.eds.EDS_Library.diagram.ExploitDefinition;

public class DiagramPattern {

	private BlockElement element;
	private List<AssetDefinition> assets;

	private BlockElement traceStart;
	private List<AssetDefinition> assetsOnTraceStart;
	
	private List<Element> trace;	
	
	private List<ExploitOfAsset> exploitAsset;
	
	private List<ExploitDefinition> exploitValues;
	private List<AssetDefinition> assetValues;
	
	
	public DiagramPattern(BlockElement element, BlockElement traceStart, List<Element> trace) {
		this.element = element;
		this.assets = element.getImportAssets().getImportAsset();
		this.traceStart = traceStart;
		this.trace = trace;
		this.assetsOnTraceStart = traceStart == null ? new ArrayList<>() : traceStart.getImportAssets().getImportAsset();
		
		exploitValues = new ArrayList<>();
		assetValues = new ArrayList<>();
		exploitAsset = new ArrayList<>();
	}


	public BlockElement getElement() {
		return element;
	}

	public List<AssetDefinition> getAssets() {
		return assets;
	}

	public List<AssetDefinition> getAssetsOnTraceStart() {
		return assetsOnTraceStart;
	}

	public BlockElement getTraceStart() {
		return traceStart;
	}

	public List<Element> getTrace() {
		return trace;
	}

	public List<ExploitDefinition> getExploitValues() {
		return exploitValues;
	}

	public void setElement(BlockElement element) {
		this.element = element;
	}

	public void setAssets(List<AssetDefinition> assets) {
		this.assets = assets;
	}

	public void setAssetsOnTraceStart(List<AssetDefinition> assetsOnTraceStart) {
		this.assetsOnTraceStart = assetsOnTraceStart;
	}

	public void setTraceStart(BlockElement traceStart) {
		this.traceStart = traceStart;
	}

	public void setTrace(List<Element> trace) {
		this.trace = trace;
	}

	public void setExploitValues(List<ExploitDefinition> exploitValues) {
		this.exploitValues = exploitValues;
	}	

	public List<AssetDefinition> getAssetValues() {
		return assetValues;
	}

	public List<ExploitOfAsset> getExploitAsset() {
		return exploitAsset;
	}

	public void setAssetValues(List<AssetDefinition> assetValues) {
		this.assetValues = assetValues;
	}

	public void addExploitValue(ExploitDefinition exploitDefinition) {
		this.exploitValues.add(exploitDefinition);		
	}

	public void addAssetValue(AssetDefinition assetDefinition) {
		this.assetValues.add(assetDefinition);		
	}

	public void addExploitAndAsset(ExploitOfAsset exploitOfAsset) {
		if (!exploitAsset.contains(exploitOfAsset)) {
			this.exploitAsset.add(exploitOfAsset);
		}
	}
	public void removeExploitAndAsset(ExploitOfAsset exploitOfAsset) {
		this.exploitAsset.remove(exploitOfAsset);
	}
	
}
