package com.eds.controls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.eds.gui.WorkingDialog;
import com.eds.utils.ProgressCode;
import com.eds.worker.WorkingSteps;

public class CancelAnalysisAction extends AbstractAction {

	private static final long serialVersionUID = 8742157777461519490L;
	
	private WorkingSteps backgroundProcess;
	private WorkingDialog dialog;

	public CancelAnalysisAction(String title, WorkingSteps backgroundProcess, WorkingDialog dialog) {
        putValue(NAME, title);
        this.backgroundProcess = backgroundProcess;
        this.dialog = dialog;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setWorkerProgress(ProgressCode.CANCELED);
		backgroundProcess.cancel(true);
		if (backgroundProcess.getReportFile().exists()) {
			backgroundProcess.getReportFile().delete();
		}
	}

}
