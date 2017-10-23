package com.eds.controls;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.eds.gui.MainWindow;
import com.eds.gui.WorkingDialog;
import com.eds.worker.WorkingSteps;

public class StartAnalysisAction extends AbstractAction {

	private static final long serialVersionUID = 3351176738051001159L;

	public StartAnalysisAction(String title) {
        putValue(NAME, title);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String diagramPath = MainWindow.getInstance().getDiagramLocation();
		String assetsPath = MainWindow.getInstance().getAssetLocation();
		String exploitsPath = MainWindow.getInstance().getExploitLocation();
		String reportPath = MainWindow.getInstance().getReportLocation();
		String schemaPath = MainWindow.getInstance().getSchemaLocation();
		boolean analyseComponents = MainWindow.getInstance().getComponentsThreatsSelected();
		
		if (diagramPath.equals("")) {
			String message = "Diagram file name can not be empty!\nPlease locate the diagram file.";
			JOptionPane.showMessageDialog(MainWindow.getInstance(), message, "Wrong Diagram File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (schemaPath.equals("")) {
			String message = "Diagram XML schema file name can not be empty!\nPlease locate the XML schema file.";
			JOptionPane.showMessageDialog(MainWindow.getInstance(), message, "Wrong XML Schema File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (assetsPath.equals("")) {
			String message = "Asset definitions file name can not be empty!\nPlease locate the asset definitions file.";
			JOptionPane.showMessageDialog(MainWindow.getInstance(), message, "Wrong Asset Definitios File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (exploitsPath.equals("")) {
			String message = "Exploit definitions file name can not be empty!\nPlease locate the exploit definitions file.";
			JOptionPane.showMessageDialog(MainWindow.getInstance(), message, "Wrong Exploit Definitios File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (reportPath.equals("")) {
			//MainWindow.getInstance().setReportDefaultLocation();
			//reportPath = MainWindow.getInstance().getReportLocation();
			String message = "Save report location can not be empty!\nPlease set location for the report file.";
			JOptionPane.showMessageDialog(MainWindow.getInstance(), message, "Wrong Report Location", JOptionPane.ERROR_MESSAGE);
			return;
		}

		File diagram = new File(diagramPath);	
		File assets = new File(assetsPath);	
		File exploits = new File(exploitsPath);
		File report = new File(reportPath);
				
		WorkingSteps analyzer = new WorkingSteps(diagram, assets, exploits, report, schemaPath, analyseComponents);		
		WorkingDialog dialog = new WorkingDialog(analyzer, diagram.getName(), report.getName(), reportPath);
		analyzer.setDialog(dialog);
		analyzer.addPropertyChangeListener(new WorkerChangeListener(dialog));
		dialog.setVisible(true);
		
		analyzer.execute();
	}

}
