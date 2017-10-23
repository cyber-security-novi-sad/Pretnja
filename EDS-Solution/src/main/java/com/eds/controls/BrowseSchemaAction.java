package com.eds.controls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.eds.gui.MainWindow;
import com.eds.utils.ResourcesLocation;

public class BrowseSchemaAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5609881124089381726L;

	public BrowseSchemaAction(String title) {
        putValue(NAME, title);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String folderLocation = ResourcesLocation.getInstance().getSchemaLocationFolder();
		
		JFileChooser fileChooser = new JFileChooser(folderLocation);
		FileFilter filter = new FileNameExtensionFilter("XML Schema files (*.xsd)", "xsd");
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Choose Diagram XML Schema File");
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int result = fileChooser.showOpenDialog(MainWindow.getInstance());		
		
		if (result == JFileChooser.APPROVE_OPTION) {
			String fileLocation = fileChooser.getSelectedFile().getAbsolutePath();
			MainWindow.getInstance().setSchemaLocation(fileLocation);

		    int index = fileLocation.lastIndexOf('\\');		
		    if (index == -1) {
			    index = fileLocation.lastIndexOf('/');	
		    }
			ResourcesLocation.getInstance().setSchemaLocationFolder(fileLocation.substring(0,index));
		}
	}
}

