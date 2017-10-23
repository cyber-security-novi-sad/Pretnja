package com.eds.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.eds.controls.CancelAnalysisAction;
import com.eds.controls.CloseAnalysisAction;
import com.eds.controls.OpenAnalysisAction;
import com.eds.controls.ShowErrorDetailsAction;
import com.eds.utils.ProgressCode;
import com.eds.worker.WorkingSteps;

public class WorkingDialog extends JDialog {

	private static final long serialVersionUID = -483201866634973300L;
	
	private static int DIALOG_WIDTH = 330;
	
	private WorkingSteps backgroundWork;
	private String diagramFile;
	private String reportFile;
	private String reportPath;
	private JLabel labelPhase;
	private JLabel labelNumber;
	private JLabel labelReport;
	private JProgressBar progressBar;
	private JTextArea textError;
	private JLabel labelError;
	private JScrollPane scrollError;
	private JButton buttonCancel;
	private JButton buttonClose;
	private JButton buttonOpen;
	private JToggleButton buttonError;
	private JSeparator separator;
	
	public WorkingDialog(WorkingSteps backgroundWork, String diagram, String report, String reportPath) {
		super();
		this.backgroundWork = backgroundWork;
		this.diagramFile = diagram;
		this.reportFile = report;
		this.reportPath = reportPath;
		initializeDialog();
		initializeComponents();
	}

	private void initializeDialog() {
		setSize(DIALOG_WIDTH,210);
		setTitle("Analyzing Diagram...");
		setResizable(false);
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.out.println("Windows look and feel unsupported.");
		}
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	CloseAnalysisAction closeAction = new CloseAnalysisAction("", WorkingDialog.this);
				if (!WorkingDialog.this.backgroundWork.isCancelled() && !WorkingDialog.this.backgroundWork.isDone()) {
					int dialogResult = JOptionPane.showConfirmDialog (null, "Are you shre you want to cancel diagram analysis?", "Canceling Analysis", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION){
						CancelAnalysisAction cancelAction = new CancelAnalysisAction("", backgroundWork, WorkingDialog.this);
						cancelAction.actionPerformed(null);
			    		closeAction.actionPerformed(null);
					}					
				} else {
		    		closeAction.actionPerformed(null);
				}
		    }
		});	
		setLocationRelativeTo(MainWindow.getInstance());		
	}

	private void initializeComponents() {		
		setLayout(new GridBagLayout());
		JLabel labelDiagram = new JLabel("Diagram file: " + diagramFile);
		labelReport = new JLabel("Report file: " + reportFile);
		labelNumber = new JLabel("Phase: 1 of 6");
		labelPhase = new JLabel("Validating and Importing resourcesm");
		progressBar = new JProgressBar();
		labelError = new JLabel("Show error details");
		textError = new JTextArea();
		buttonCancel = new JButton(new CancelAnalysisAction("Cancel", backgroundWork, this));
		buttonClose = new JButton(new CloseAnalysisAction("Close", this));
		buttonOpen = new JButton(new OpenAnalysisAction("Locate Report", this, reportPath));
		scrollError = new JScrollPane (textError, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		buttonError = new JToggleButton(new ShowErrorDetailsAction("", this));
		separator = new JSeparator(JSeparator.HORIZONTAL); 
				   
		try {
			buttonError.setIcon(new ImageIcon(((new ImageIcon(ImageIO.read(getClass().getResource("toggle_up.png"))).getImage().getScaledInstance(8, 8, java.awt.Image.SCALE_SMOOTH)))));
			buttonError.setSelectedIcon(new ImageIcon(((new ImageIcon(ImageIO.read(getClass().getResource("toggle_down.png"))).getImage().getScaledInstance(8, 8, java.awt.Image.SCALE_SMOOTH)))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		UIManager.put("ProgressBar.repaintInterval", new Integer(15));
		UIManager.put("ProgressBar.cycleTime", new Integer(4000));
        progressBar.setBorderPainted(true); 
        textError.setForeground(Color.red);
		buttonCancel.setPreferredSize(new Dimension(100, 25));
		buttonClose.setPreferredSize(new Dimension(100, 25));
		progressBar.setPreferredSize(new Dimension(300, 25));
		buttonOpen.setPreferredSize(new Dimension(100, 25));
		buttonError.setPreferredSize(new Dimension(25,10));
		
		Font font = textError.getFont();
		Font smallFont = new Font(font.getFontName(),font.getStyle(), 11);
		textError.setFont(smallFont);
		textError.setWrapStyleWord(true);
		textError.setLineWrap(true);
		textError.setEditable(false);
		scrollError.setPreferredSize(new Dimension(300, 70));
		//separator.setPreferredSize(new Dimension(300, 3));
		
		labelError.setVisible(false);
		scrollError.setVisible(false);
		buttonError.setVisible(false);
		separator.setVisible(false);

		buttonOpen.setVisible(false);
		buttonClose.setVisible(false);
		setPhaseAndProgressBar(0, 0);
		
		add(labelNumber, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,0,0,0), 5, 5));		
		add(labelPhase, new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 5, 5));		
		add(progressBar, new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,0,0,0), 5, 5));
		
		add(labelDiagram, new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(10,0,0,0), 5, 5));		
		add(labelReport, new GridBagConstraints(0, 4, 2, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 5, 5));
		
		add(buttonCancel, new GridBagConstraints(0, 5, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,0,10,0), 5, 5));
		add(buttonClose, new GridBagConstraints(0, 5, 2, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(10,40,10,20), 5, 5));
		add(buttonOpen, new GridBagConstraints(0, 5, 2, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10,20,10,40), 5, 5));
		
		add(separator, new GridBagConstraints(0, 6, 2, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 5, 5));
		add(buttonError, new GridBagConstraints(0, 7, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,5,0), 5, 5));
		add(labelError, new GridBagConstraints(1, 7, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,2,5,0), 5, 5));
		add(scrollError, new GridBagConstraints(0, 8, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,10,0), 5, 5));
	}

	private void setPhaseAndProgressBar(int progressPhase, int progressValue) {

		if (backgroundWork.isCancelled() || backgroundWork.isDone()) {
			switch (progressPhase) {
				case 10: 
					progressBar.setIndeterminate(false);
					buttonCancel.setVisible(false);
					break;
				case 11:
					progressBar.setIndeterminate(true);
					buttonCancel.setEnabled(false);
					labelNumber.setText("Canceling");
					labelPhase.setText("Waiting for process to stop");
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					break;
			}
			progressBar.setValue(progressValue);
			return;
		}

		progressBar.setIndeterminate(false);
		
		switch (progressPhase) {
			case 0: 
				progressBar.setIndeterminate(true);
				labelNumber.setText("Initializing");
				labelPhase.setText("Waiting for process to start");
				break;
			case 1: 
				progressBar.setIndeterminate(false);
				labelNumber.setText("Phase: 1 of 6");
				labelPhase.setText("Validating and Importing resources");
				break;
			case 2: 
				labelNumber.setText("Phase: 2 of 6");
				labelPhase.setText("Applying assets");
				break;
			case 3: 
				labelNumber.setText("Phase: 6 of 6");
				labelPhase.setText("Decomposing diagram");
				break;
			case 4: 
				labelNumber.setText("Phase: 4 of 6");
				labelPhase.setText("Analyzing diagram patterns");
				break;
			case 5: 
				labelNumber.setText("Phase: 5 of 6");
				labelPhase.setText("Applying exploits");
				break;
			case 6: 
				labelNumber.setText("Phase: 6 of 6");
				labelPhase.setText("Creating report");
				break;
		}	
		progressBar.setValue(progressValue);	
	}
	
	public void setErrorVisibility(boolean visibility) {
		if (visibility) {
			setSize(DIALOG_WIDTH,335);
		} else {
			setSize(DIALOG_WIDTH,250);
		}
		scrollError.setVisible(visibility);
	}
	
	public void setWorkerProgress(int value) {
		int phase = 0;
		if (value == ProgressCode.INITIALIZED) {
			phase = 0;
		} else if (ProgressCode.STARTED <= value && value < ProgressCode.READING_DIAGRAM) {
			phase = 1;
		} else if (ProgressCode.READING_DIAGRAM <= value && value < ProgressCode.MERGING_DIAGRAM_ASSETS) {
			phase = 2;
		} else if (ProgressCode.MERGING_DIAGRAM_ASSETS <= value && value < ProgressCode.DECOMPOSING_DIAGRAM) {
			phase = 2;
		} else if (ProgressCode.DECOMPOSING_DIAGRAM <= value && value < ProgressCode.RULES_ANALYZING) {
			phase = 3;
		} else if (ProgressCode.RULES_ANALYZING <= value && value < ProgressCode.READING_EXPLOITS) {
			phase = 4;
		} else if (ProgressCode.READING_EXPLOITS <= value && value < ProgressCode.GENERATING_REPORT_PATTERNS) {
			phase = 5;
		} else if (ProgressCode.GENERATING_REPORT_PATTERNS <= value && value < ProgressCode.DONE) {
			phase = 6;
		} else if (value == ProgressCode.DONE) {
			phase = 10;
		} else if (value == ProgressCode.CANCELED) {
			phase = 11;
		}
		setPhaseAndProgressBar(phase, value);
	}
	
	public void setErrorMessage(String error) {
		if (backgroundWork.isCancelled() || backgroundWork.isDone()) {
			return;
		}
		textError.setText("An error occured while working:\n" + error);
		separator.setVisible(true);
		labelError.setVisible(true);
		buttonError.setVisible(true);
		setSize(DIALOG_WIDTH,250);
	}
	
	public void setResultLabel(boolean successful) {
		Font font = labelNumber.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		buttonOpen.setVisible(true);
		buttonClose.setVisible(true);
		setCursor(Cursor.getDefaultCursor());
		if (successful) {
			labelNumber.setText("DONE");
			labelPhase.setText("Threat analysis is finished");
			labelNumber.setFont(boldFont);
			buttonOpen.setEnabled(true);
		} else {
			labelNumber.setText("NOT COMPLETED");
			labelPhase.setText("Threat analysis was unable to finish");
			labelNumber.setForeground(Color.RED);
			labelNumber.setFont(boldFont);
			labelReport.setText("Report file: Report file is not created");
			buttonOpen.setEnabled(false);
		}
	}
}
