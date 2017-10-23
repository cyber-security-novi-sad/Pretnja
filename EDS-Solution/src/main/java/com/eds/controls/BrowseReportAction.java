package com.eds.controls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.eds.gui.MainWindow;
import com.eds.utils.ResourcesLocation;

public class BrowseReportAction extends AbstractAction {

	private static final long serialVersionUID = 8106916567683754946L;

	public BrowseReportAction(String title) {
        putValue(NAME, title);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {

		String folderLocation = ResourcesLocation.getInstance().getReportLocationFolder();
		
		JFileChooser fileChooser = new JFileChooser(folderLocation);
		FileFilter filter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Save Report File");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		int result = fileChooser.showSaveDialog(MainWindow.getInstance());
		
		if (result == JFileChooser.APPROVE_OPTION) {
			String fileLocation = fileChooser.getSelectedFile().getAbsolutePath();
			if (!fileLocation.endsWith(".xml")) {
				if (fileLocation.contains(".")) {
					String message = "File name can not contain dots!\nPlease remove dots and try again.";
					JOptionPane.showMessageDialog(MainWindow.getInstance(), message, "Wrong File Name", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					fileLocation = fileLocation.concat(".xml");
				}
			}
			MainWindow.getInstance().setReportLocation(fileLocation);

		    int index = fileLocation.lastIndexOf('\\');		
		    if (index == -1) {
			    index = fileLocation.lastIndexOf('/');	
		    }
			ResourcesLocation.getInstance().setReportLocationFolder(fileLocation.substring(0,index));
		}
	}

}
