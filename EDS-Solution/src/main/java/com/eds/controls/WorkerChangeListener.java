package com.eds.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import com.eds.gui.WorkingDialog;
import com.eds.utils.ProgressCode;

public class WorkerChangeListener implements PropertyChangeListener {
	
	private WorkingDialog dialog;
	
	public WorkerChangeListener(WorkingDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		switch (event.getPropertyName()) {
			case "progress":
				dialog.setWorkerProgress((Integer) event.getNewValue());
				break;
			case "state":
				switch ((StateValue) event.getNewValue()) {
					case PENDING:
						dialog.setVisible(true);
						break;
					case STARTED:
						dialog.setVisible(true);
						break;
					case DONE:
						dialog.setWorkerProgress(ProgressCode.DONE);
						try {
							@SuppressWarnings("rawtypes")
							boolean result = (boolean) ((SwingWorker)event.getSource()).get();
							dialog.setResultLabel(result);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						} catch (CancellationException e) {
							dialog.setResultLabel(false);
						}
						break;
				}
				break;
		}		
	}

}
