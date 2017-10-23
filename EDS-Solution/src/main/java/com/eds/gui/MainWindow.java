package com.eds.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.eds.controls.BrowseDiagramAction;
import com.eds.controls.BrowseExploitAction;
import com.eds.controls.BrowseReportAction;
import com.eds.controls.BrowseSchemaAction;
import com.eds.controls.StartAnalysisAction;
import com.eds.utils.ResourcesLocation;
import com.eds.controls.BrowseAssetAction;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 6982195527732029083L;

	private static MainWindow instance = null;
	
	private JTextField contentDiagram;
	private JTextField contentSchema;
	private JTextField contentAssets;
	private JTextField contentExploits;
	private JTextField contentReport;
	private JCheckBox checkboxComponentsThreats;
	
	private MainWindow() {
		super();
	}
	
	private void initialize() {
		initializeWindow();
		initializeContent();
	}
	
	private void initializeWindow() {
		//setSize(560,350);
		setSize(560,320);
		setTitle("Exploits Detection System");
		try {
			setIconImage(new ImageIcon(ImageIO.read(getClass().getResource("icon.png"))).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
		} catch (IOException e1) {
			
		}
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
		    	ResourcesLocation.getInstance().saveAllParameters();
		    	System.exit(0);
		    }
		});	
		
		setLocationRelativeTo(null);		
	}
	
	private void initializeContent() {
		setLayout(new GridBagLayout());

		String assetsLocation = ResourcesLocation.getInstance().getAssetLocation();
		String exploitsLocation = ResourcesLocation.getInstance().getExploitLocation();
		String schemaLocation = ResourcesLocation.getInstance().getSchemaLocation();
		
		contentDiagram = new JTextField();
		contentSchema = new JTextField(schemaLocation);
		contentAssets = new JTextField(assetsLocation);
		contentExploits = new JTextField(exploitsLocation);

		contentReport = new JTextField();
		JLabel labelBrowse = new JLabel("Source diagram location: ");
		JLabel labelSchema = new JLabel("Diagram XML schema location: ");
		JLabel labelAssets = new JLabel("Assets definition location: ");
		JLabel labelExploits = new JLabel("Exploits definition location: ");
		JLabel labelReport = new JLabel("Save report location: ");
		checkboxComponentsThreats = new JCheckBox("Analyse components vulnerabilities");
		
		JButton buttonBrowse = new JButton(new BrowseDiagramAction("..."));
		JButton buttonSchema = new JButton(new BrowseSchemaAction("..."));
		JButton buttonAssets = new JButton(new BrowseAssetAction("..."));
		JButton buttonExploits = new JButton(new BrowseExploitAction("..."));
		JButton buttonReport = new JButton(new BrowseReportAction("..."));
		JButton buttonStart = new JButton(new StartAnalysisAction("Analyze diagram and Create report"));
		
		buttonBrowse.setPreferredSize(new Dimension(20, 25));	
		buttonSchema.setPreferredSize(new Dimension(20, 25));
		buttonAssets.setPreferredSize(new Dimension(20, 25));
		buttonExploits.setPreferredSize(new Dimension(20, 25));
		buttonReport.setPreferredSize(new Dimension(20, 25));
		contentDiagram.setPreferredSize(new Dimension(350, 25));
		contentSchema.setPreferredSize(new Dimension(350, 25));		
		contentAssets.setPreferredSize(new Dimension(350, 25));	
		contentExploits.setPreferredSize(new Dimension(350, 25));
		contentReport.setPreferredSize(new Dimension(350, 25));
		checkboxComponentsThreats.setPreferredSize(new Dimension(250, 25));
		buttonStart.setPreferredSize(new Dimension(200, 25));
				
		add(labelBrowse, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,0,0), 5, 5));
		add(contentDiagram, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(10,0,5,0), 5, 5));
		add(buttonBrowse, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(10,5,5,0), 5, 5));

		add(labelReport, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 5, 5));
		add(contentReport, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,5,0), 5, 5));
		add(buttonReport, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,5,5,0), 5, 5));

		add(new JSeparator(SwingConstants.HORIZONTAL), new GridBagConstraints(0, 2, 3, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets(5,0,0,0), 0, 0));

		add(labelSchema, new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,0,0), 5, 5));
		add(contentSchema, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(10,0,5,0), 5, 5));
		add(buttonSchema, new GridBagConstraints(2, 3, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(10,5,5,0), 5, 5));
		
		add(labelAssets, new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 5, 5));
		add(contentAssets, new GridBagConstraints(1, 4, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,5,0), 5, 5));
		add(buttonAssets, new GridBagConstraints(2, 4, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,5,5,0), 5, 5));
		
		add(labelExploits, new GridBagConstraints(0, 5, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 5, 5));
		add(contentExploits, new GridBagConstraints(1, 5, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,5,0), 5, 5));
		add(buttonExploits, new GridBagConstraints(2, 5, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,5,5,0), 5, 5));
		
		//add(checkboxComponentsThreats, new GridBagConstraints(1, 6, 2, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(5,0,5,0), 5, 5));
		
		add(buttonStart, new GridBagConstraints(1, 7, 2, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(20,0,0,0), 5, 5));
	}
	
	public void setReportLocation(String location) {
		contentReport.setText(location);
	}
	
	public void setAssetsLocation(String location) {
		contentAssets.setText(location);
	}
	
	public void setExploitsLocation(String location) {
		contentExploits.setText(location);
	}
	
	public void setDiagramLocation(String location) {
		contentDiagram.setText(location);		
	}
	
	public void setSchemaLocation(String location) {
		contentSchema.setText(location);		
	}
	
	public void setReportDefaultLocation() {
		DateFormat dateFormat = new SimpleDateFormat("MMdd_HHmmss");
		Date date = new Date();		
		contentReport.setText("D:\\Master\\temp\\ThreatReport_" + dateFormat.format(date) + ".xml");
		//contentReport.setText("E:\\Users\\NemanjaM\\Documents\\Practice\\Master\\temp\\ThreatReport_" + dateFormat.format(date) + ".xml");
	}	
	
	
	public String getReportLocation() {
		return contentReport.getText();
	}
	
	public String getAssetLocation() {
		return contentAssets.getText();
	}
	
	public String getExploitLocation() {
		return contentExploits.getText();
	}
	
	public String getDiagramLocation() {
		return contentDiagram.getText();		
	}
	
	public String getSchemaLocation() {
		return contentSchema.getText();		
	}
	
	public boolean getComponentsThreatsSelected() {
		return checkboxComponentsThreats.isSelected();	
	}
	
	public static MainWindow getInstance() {
		if (instance == null) {
			instance = new MainWindow();
			instance.initialize();
		}
		return instance;
	}
}
