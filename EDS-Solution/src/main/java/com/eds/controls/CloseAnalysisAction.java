package com.eds.controls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

public class CloseAnalysisAction extends AbstractAction {

	private static final long serialVersionUID = -217930600575499666L;
	
	private JDialog dialog;

	public CloseAnalysisAction(String title, JDialog dialog) {
        putValue(NAME, title);
        this.dialog = dialog;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(false);
		dialog.dispose();
	}

}
