package com.eds.controls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.eds.gui.WorkingDialog;

public class ShowErrorDetailsAction extends AbstractAction {
	
	private static final long serialVersionUID = 6862848854663455969L;
	
	private WorkingDialog dialog;
	
	private boolean isPressed;

	public ShowErrorDetailsAction(String title, WorkingDialog dialog) {
		super(title);
        putValue(NAME, title);
        this.dialog = dialog;
        this.isPressed = false;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		isPressed = !isPressed;
		dialog.setErrorVisibility(isPressed);
	}

}
