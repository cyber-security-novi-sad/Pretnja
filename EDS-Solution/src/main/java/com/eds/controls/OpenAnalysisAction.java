package com.eds.controls;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class OpenAnalysisAction extends AbstractAction {
	
	private static final long serialVersionUID = 416977068250458096L;
	
	private JDialog dialog;
	private String filePath;

	public OpenAnalysisAction(String title, JDialog dialog, String filePath) {
        putValue(NAME, title);
        this.dialog = dialog;
        this.filePath = filePath;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Runtime.getRuntime().exec("explorer.exe /select," + filePath);
		} catch (IOException e1) {
			String message = "Report file can not be found!\nPlease try to reach report file manualy at:\n" + filePath;
			JOptionPane.showMessageDialog(dialog, message, "Explorer Error", JOptionPane.ERROR_MESSAGE);
		}
		dialog.setVisible(false);
		dialog.dispose();
	}

}
