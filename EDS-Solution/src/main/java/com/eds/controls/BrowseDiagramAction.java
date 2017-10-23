package com.eds.controls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.eds.gui.MainWindow;
import com.eds.utils.ResourcesLocation;

public class BrowseDiagramAction extends AbstractAction {

	private static final long serialVersionUID = -7013513164077524833L;

	public BrowseDiagramAction(String title) {
        putValue(NAME, title);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String folderLocation = ResourcesLocation.getInstance().getDiagramLocationFolder();
		
		JFileChooser fileChooser = new JFileChooser(folderLocation);		
		FileFilter filter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Choose Diagram File");
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int result = fileChooser.showOpenDialog(MainWindow.getInstance());		
		
		if (result == JFileChooser.APPROVE_OPTION) {
			String fileLocation = fileChooser.getSelectedFile().getAbsolutePath();
			MainWindow.getInstance().setDiagramLocation(fileLocation);

		    int index = fileLocation.lastIndexOf('\\');		
		    if (index == -1) {
			    index = fileLocation.lastIndexOf('/');	
		    }	
			ResourcesLocation.getInstance().setDiagramLocationFolder(fileLocation.substring(0,index));
		}		
	}
}
