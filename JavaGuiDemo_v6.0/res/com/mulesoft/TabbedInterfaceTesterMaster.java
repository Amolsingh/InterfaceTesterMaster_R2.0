package com.mulesoft;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Desktop;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.rmi.CORBA.Util;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.text.html.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;

public class TabbedInterfaceTesterMaster extends JPanel {

	private JFrame frame;
	final static int interval = 100;
	int time;
	int timeGW;
	int timeVndr;
	Timer timer;
	Timer timerGW;
	Timer timerVndr;
	JLabel label;
	JLabel lblStatus; 
	JLabel lblStatusGW;
	JLabel lblStatusVndr;
	Graphics g;
	private String stProgressMsg;
	JTextArea textArea;
	JTextArea textAreaGW;
	JTextArea textAreaVndr;
	String ReportFilePath = "";
	String ReportFileName = "";
	String Env="";
	String EnvGW="";
	String EnvVndr="";
	Properties prop = new Properties();
	Properties propGW = new Properties();
	Properties propVndr = new Properties();
	InputStream  input  = null;
	InputStream inputGW =null;
	InputStream inputVndr=null;
	//private LongTask task;
	public String getStProgressMsg() {
		return stProgressMsg;
	}

	public void setStProgressMsg(String stProgressMsg) {
		this.stProgressMsg = stProgressMsg;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TabbedInterfaceTesterMaster window = new TabbedInterfaceTesterMaster();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		// draw the eye here
		Font myFont3 = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		textArea.setText("");
		// g.setFont(myFont3);
		// g.drawString("I", 308, 273);

	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public TabbedInterfaceTesterMaster() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {

		frame = new JFrame(Utility.ToolName.concat(Utility.ToolRelease));
		frame.setBounds(100, 100, 780, 474);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFont(new Font("sansserif", Font.BOLD, 18));
		frame.getContentPane().setLayout(null);

		//Creation of the Pane
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 764, 436);
		frame.getContentPane().add(tabbedPane);
		Image img = new ImageIcon(this.getClass().getResource(Utility.PekinLogoImageName)).getImage();
		tabbedPane.setFont(new Font("sansserif", Font.BOLD, 15));
		tabbedPane.setBackground(new Color(143, 188, 143));
		tabbedPane.setFont(new Font("sansserif", Font.BOLD, 15));
		tabbedPane.setBackground(new Color(173, 216, 230));
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		//Starting of the Panel 1(Mulesoft TAB)
		JPanel panel = new JPanel();
		panel.setForeground(new Color(128, 0, 0));
		panel.setBackground(new Color(143, 188, 143));
		tabbedPane.addTab(Utility.MuleSoftTabName, null, panel, null);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel(""); //For the Pekin Logo
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(468, 10, 152, 45);
		panel.add(lblNewLabel);

		JLabel lblInterfaceTesterMaster = new JLabel("  "+Utility.ToolName+"  ");//For the title
		lblInterfaceTesterMaster.setForeground(new Color(128, 0, 0));
		lblInterfaceTesterMaster.setBounds(172, 22, 306, 32);
		lblInterfaceTesterMaster.setFont(new Font("sansserif", Font.BOLD, 24));
		panel.add(lblInterfaceTesterMaster);

		/*Check Boxes Starts*/
		new MuleTabComponents(panel);
		/*Check Boxes ends*/

		JButton btnGenerateMuleReport = new JButton("Generate Mule Report");//For creating the button
		btnGenerateMuleReport.setBounds(94, 289, 174, 32);
		panel.add(btnGenerateMuleReport);

		lblStatus = new JLabel("Status");
		lblStatus.setForeground(new Color(128, 0, 128));
		lblStatus.setBounds(304, 283, 41, 14);
		panel.add(lblStatus);

		textArea = new JTextArea(); //For setting the Report Generation status
		textArea.setForeground(new Color(128, 0, 0));
		textArea.setBackground(new Color(143, 188, 143));
		textArea.setBounds(346, 283, 319, 14);
		panel.add(textArea);

		JProgressBar progressBar = new JProgressBar(); //For showing the Progress of an operation
		progressBar.setBackground(new Color(128, 0, 0));
		progressBar.setForeground(new Color(143, 188, 143));
		progressBar.setStringPainted(true);
		progressBar.setBounds(304, 298, 198, 17);
		panel.add(progressBar);

		JLabel lblMulepath = new JLabel(""); //For showing the Path of Report Generation 
		lblMulepath.setForeground(new Color(128, 0, 128));
		lblMulepath.setBounds(304, 318, 437, 14);
		panel.add(lblMulepath);

		JRadioButton rdbtnGetDetailedReport = new JRadioButton("Get Detailed Report");
		rdbtnGetDetailedReport.setBackground(new Color(143, 188, 143));
		rdbtnGetDetailedReport.setBounds(437, 152, 152, 23);
		panel.add(rdbtnGetDetailedReport);

		JRadioButton rdbtnGetQuickReport = new JRadioButton("Get Quick Report");
		rdbtnGetQuickReport.setBackground(new Color(143, 188, 143));
		rdbtnGetQuickReport.setBounds(437, 126, 152, 23);
		rdbtnGetQuickReport.setSelected(true);;

		ButtonGroup rprtGrp = new ButtonGroup();
		rprtGrp.add(rdbtnGetQuickReport);
		rprtGrp.add(rdbtnGetDetailedReport);
		panel.add(rdbtnGetQuickReport);

		JButton btnSelectAll = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource(Utility.selectAllImageName)).getImage()));
		btnSelectAll.setOpaque(false);
		btnSelectAll.setFocusPainted(false);
		btnSelectAll.setBorderPainted(false);
		btnSelectAll.setContentAreaFilled(false);
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MuleTabComponents.selectAll();
			}
		});
		btnSelectAll.setBounds(437, 181, 41, 36);
		panel.add(btnSelectAll);

		JButton btnDeselectAll = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource(Utility.deselectAllImageName)).getImage()));
		btnDeselectAll.setBackground(new Color(143, 188, 143));
		btnDeselectAll.setOpaque(false);
		btnDeselectAll.setFocusPainted(false);
		btnDeselectAll.setBorderPainted(false);
		btnDeselectAll.setContentAreaFilled(false);
		btnDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MuleTabComponents.deSelectAll();
			}
		});
		btnDeselectAll.setBounds(512, 185, 41, 32);
		panel.add(btnDeselectAll);

		JLabel lblSelectAll = new JLabel("Select All");
		lblSelectAll.setBounds(429, 228, 65, 14);
		panel.add(lblSelectAll);

		JLabel lblDeSelectAll = new JLabel("De Select All");
		lblDeSelectAll.setBounds(505, 228, 76, 14);
		panel.add(lblDeSelectAll);

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setBackground(new Color(0, 0, 0));
		separator.setForeground(new Color(0, 0, 0));
		separator.setBounds(408, 111, 11, 141);
		panel.add(separator);
		//End of Panel 1

		//############################################################ PANEL GUIDEWIRE ###############################################################
		//Panel 2 starts (Guidewire)
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(new Color(128, 0, 0));
		panel_1.setBackground(new Color(173, 216, 230));
		tabbedPane.addTab(Utility.GuidewireTabName, null, panel_1, null); //Guidewire Tab
		panel_1.setLayout(null);

		JLabel label_1 = new JLabel("  "+Utility.ToolName+"  "); //Title of the Guidewire Tab
		label_1.setForeground(new Color(128, 0, 0));
		label_1.setBounds(172, 22, 306, 32);
		label_1.setFont(new Font("SansSerif", Font.BOLD, 24));
		panel_1.add(label_1);

		lblStatusGW = new JLabel("Status"); //Status of the GW Report Generation
		lblStatusGW.setForeground(new Color(128, 0, 128));
		lblStatusGW.setBounds(304, 283, 41, 14);
		panel_1.add(lblStatusGW);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(img));
		lblNewLabel_1.setBounds(468, 10, 152, 45);
		panel_1.add(lblNewLabel_1);

		JCheckBox chckbxGwTDev = new JCheckBox(Utility.TDev);
		chckbxGwTDev.setBackground(new Color(173, 216, 230));
		chckbxGwTDev.setBounds(74, 104, 76, 23);
		panel_1.add(chckbxGwTDev);

		JCheckBox chckbxNewCheckBox_6 = new JCheckBox(Utility.development);
		chckbxNewCheckBox_6.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_6.setBounds(74, 156, 76, 23);
		panel_1.add(chckbxNewCheckBox_6);

		JCheckBox chckbxNewCheckBox_7 = new JCheckBox(Utility.QualityAssurance);
		chckbxNewCheckBox_7.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_7.setBounds(74, 212, 76, 23);
		panel_1.add(chckbxNewCheckBox_7);

		JCheckBox chckbxNewCheckBox_8 = new JCheckBox(Utility.TQA1);
		chckbxNewCheckBox_8.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_8.setBounds(152, 104, 65, 23);
		panel_1.add(chckbxNewCheckBox_8);

		JCheckBox chckbxNewCheckBox_9 = new JCheckBox(Utility.TQA2);
		chckbxNewCheckBox_9.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_9.setBounds(152, 156, 65, 23);
		panel_1.add(chckbxNewCheckBox_9);

		JCheckBox chckbxNewCheckBox_10 = new JCheckBox(Utility.TQA3);
		chckbxNewCheckBox_10.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_10.setBounds(152, 212, 65, 23);
		panel_1.add(chckbxNewCheckBox_10);

		JCheckBox chckbxNewCheckBox_11 = new JCheckBox(Utility.UserAcceptanceTesting);
		chckbxNewCheckBox_11.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_11.setBounds(227, 104, 76, 23);
		panel_1.add(chckbxNewCheckBox_11);

		JCheckBox chckbxNewCheckBox_12 = new JCheckBox(Utility.Training);
		chckbxNewCheckBox_12.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_12.setBounds(227, 156, 65, 23);
		//panel_1.add(chckbxNewCheckBox_12);

		JCheckBox chckbxNewCheckBox_13 = new JCheckBox(Utility.preProduction);
		chckbxNewCheckBox_13.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_13.setBounds(228, 212, 75, 23);
		//panel_1.add(chckbxNewCheckBox_13);

		JCheckBox chckbxNewCheckBox_14 = new JCheckBox(Utility.production);
		chckbxNewCheckBox_14.setBackground(new Color(173, 216, 230));
		chckbxNewCheckBox_14.setBounds(304, 104, 89, 23);
		//panel_1.add(chckbxNewCheckBox_14);

		JLabel lblGWPath = new JLabel(""); //For showing the Path of Report Generation 
		lblGWPath.setForeground(new Color(128, 0, 128));
		lblGWPath.setBounds(304, 318, 437, 14);
		panel_1.add(lblGWPath);

		textAreaGW = new JTextArea(); //For setting the Report Generation status
		textAreaGW.setForeground(new Color(128, 0, 0));
		textAreaGW.setBackground(new Color(173, 216, 230));
		textAreaGW.setBounds(346, 283, 319, 14);
		panel_1.add(textAreaGW);

		JButton gWReportButton = new JButton("Generate GW Report");
		gWReportButton.setBounds(94, 289, 174, 32);
		panel_1.add(gWReportButton);

		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setBounds(299, 301, 210, 23);
		progressBar_1.setBackground(new Color(128, 0, 0));
		progressBar_1.setForeground(new Color(173, 216, 230));
		progressBar_1.setStringPainted(true);
		progressBar_1.setBounds(304, 298, 198, 17);
		panel_1.add(progressBar_1);

		JButton btnSelectAllGW = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource(Utility.selectAllImageName)).getImage()));
		btnSelectAllGW.setOpaque(false);
		btnSelectAllGW.setFocusPainted(false);
		btnSelectAllGW.setBorderPainted(false);
		btnSelectAllGW.setContentAreaFilled(false);
		btnSelectAllGW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxGwTDev.setSelected(true);
				chckbxNewCheckBox_6.setSelected(true);
				chckbxNewCheckBox_7.setSelected(true);
				chckbxNewCheckBox_8.setSelected(true);
				chckbxNewCheckBox_9.setSelected(true);
				chckbxNewCheckBox_10.setSelected(true);
				chckbxNewCheckBox_11.setSelected(true);
				//chckbxNewCheckBox_12.setSelected(true);
				//chckbxNewCheckBox_13.setSelected(true);
				//chckbxNewCheckBox_14.setSelected(true);
			}
		});
		btnSelectAllGW.setBounds(437, 181, 41, 36);
		panel_1.add(btnSelectAllGW);

		JButton btnDeselectAllGW = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource(Utility.deselectAllImageName)).getImage()));
		btnDeselectAllGW.setBackground(new Color(143, 188, 143));
		btnDeselectAllGW.setOpaque(false);
		btnDeselectAllGW.setFocusPainted(false);
		btnDeselectAllGW.setBorderPainted(false);
		btnDeselectAllGW.setContentAreaFilled(false);
		btnDeselectAllGW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxGwTDev.setSelected(false);
				chckbxNewCheckBox_6.setSelected(false);
				chckbxNewCheckBox_7.setSelected(false);
				chckbxNewCheckBox_8.setSelected(false);
				chckbxNewCheckBox_9.setSelected(false);
				chckbxNewCheckBox_10.setSelected(false);
				chckbxNewCheckBox_11.setSelected(false);
				chckbxNewCheckBox_12.setSelected(false);
				chckbxNewCheckBox_13.setSelected(false);
				chckbxNewCheckBox_14.setSelected(false);
			}
		});
		btnDeselectAllGW.setBounds(512, 185, 41, 32);
		panel_1.add(btnDeselectAllGW);

		JLabel lblSelectAllGW = new JLabel("Select All");
		lblSelectAllGW.setBounds(429, 228, 65, 14);
		panel_1.add(lblSelectAllGW);

		JLabel lblDeSelectAllGW = new JLabel("De Select All");
		lblDeSelectAllGW.setBounds(505, 228, 76, 14);
		panel_1.add(lblDeSelectAllGW);

		JSeparator separatorGW = new JSeparator(SwingConstants.VERTICAL);
		separatorGW.setBackground(new Color(0, 0, 0));
		separatorGW.setForeground(new Color(0, 0, 0));
		separatorGW.setBounds(408, 111, 11, 141);
		panel_1.add(separatorGW);

		JLabel lblCopyrightGW = new JLabel(Utility.CopyrightDeloitte);
		lblCopyrightGW.setBounds(0, 393, 452, 9);
		lblCopyrightGW.setForeground(new Color(128, 0, 128));
		lblCopyrightGW.setFont(new Font("sansserif", Font.PLAIN, 8));
		panel_1.add(lblCopyrightGW);

		//Guidewire Flow of Execution

		//############################################################# VENDOR #################################################################################

		JPanel panel_2 = new JPanel();
		panel_2.setForeground(new Color(128, 0, 0));
		panel_2.setBackground(new Color(119, 136, 153));
		tabbedPane.addTab("   Vendor   ", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblNewLabelVndr = new JLabel(""); //For the Pekin Logo
		lblNewLabelVndr.setIcon(new ImageIcon(img));
		lblNewLabelVndr.setBounds(468, 10, 152, 45);
		panel_2.add(lblNewLabelVndr);

		JLabel lblInterfaceTesterMasterVndr = new JLabel("  "+Utility.ToolName+"  ");//For the title
		lblInterfaceTesterMasterVndr.setForeground(new Color(128, 0, 0));
		lblInterfaceTesterMasterVndr.setBounds(172, 22, 306, 32);
		lblInterfaceTesterMasterVndr.setFont(new Font("sansserif", Font.BOLD, 24));
		panel_2.add(lblInterfaceTesterMasterVndr);

		//Check Boxes for Vendor
		//All the Components required for the Mule Tab Pane
		JCheckBox chckbxNewCheckBox_15 = new JCheckBox(Utility.development);
		chckbxNewCheckBox_15.setBackground(new Color(119, 136, 153));
		chckbxNewCheckBox_15.setBounds(90, 111, 97, 23);
		panel_2.add(chckbxNewCheckBox_15);

		JCheckBox chckbxNewCheckBox_16 = new JCheckBox(Utility.QualityAssurance);
		chckbxNewCheckBox_16.setBackground(new Color(119, 136, 153));
		chckbxNewCheckBox_16.setBounds(90, 152, 97, 23);
		panel_2.add(chckbxNewCheckBox_16);

		JCheckBox chckbxNewCheckBox_17 = new JCheckBox(Utility.UserAcceptanceTesting);
		chckbxNewCheckBox_17.setBackground(new Color(119, 136, 153));
		chckbxNewCheckBox_17.setBounds(90, 194, 97, 23);
		panel_2.add(chckbxNewCheckBox_17);

		JCheckBox chckbxNewCheckBox_18 = new JCheckBox(Utility.Training);
		chckbxNewCheckBox_18.setBackground(new Color(119, 136, 153));
		chckbxNewCheckBox_18.setBounds(276, 111, 97, 23);
		//panel_2.add(chckbxNewCheckBox_18);

		JCheckBox chckbxNewCheckBox_19 = new JCheckBox(Utility.preProduction);
		chckbxNewCheckBox_19.setBackground(new Color(119, 136, 153));
		chckbxNewCheckBox_19.setBounds(276, 152, 97, 23);
		//panel_2.add(chckbxNewCheckBox_19);

		JCheckBox chckbxNewCheckBox_20 = new JCheckBox(Utility.production);
		chckbxNewCheckBox_20.setBackground(new Color(119, 136, 153));
		chckbxNewCheckBox_20.setBounds(276, 194, 97, 23);
		//panel_2.add(chckbxNewCheckBox_20);
		//Check Boxes End

		JButton btnSelectAllVndr = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource(Utility.selectAllImageName)).getImage()));
		btnSelectAllVndr.setOpaque(false);
		btnSelectAllVndr.setFocusPainted(false);
		btnSelectAllVndr.setBorderPainted(false);
		btnSelectAllVndr.setContentAreaFilled(false);
		btnSelectAllVndr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxNewCheckBox_15.setSelected(true);
				chckbxNewCheckBox_16.setSelected(true);
				chckbxNewCheckBox_17.setSelected(true);
				//chckbxNewCheckBox_18.setSelected(true);
				//chckbxNewCheckBox_19.setSelected(true);
				//chckbxNewCheckBox_20.setSelected(true);
			}
		});
		btnSelectAllVndr.setBounds(437, 181, 41, 36);
		panel_2.add(btnSelectAllVndr);

		JButton btnDeselectAllVndr = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource(Utility.deselectAllImageName)).getImage()));
		btnDeselectAllVndr.setBackground(new Color(143, 188, 143));
		btnDeselectAllVndr.setOpaque(false);
		btnDeselectAllVndr.setFocusPainted(false);
		btnDeselectAllVndr.setBorderPainted(false);
		btnDeselectAllVndr.setContentAreaFilled(false);
		btnDeselectAllVndr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxNewCheckBox_15.setSelected(false);
				chckbxNewCheckBox_16.setSelected(false);
				chckbxNewCheckBox_17.setSelected(false);
				chckbxNewCheckBox_18.setSelected(false);
				chckbxNewCheckBox_19.setSelected(false);
				chckbxNewCheckBox_20.setSelected(false);
			}
		});
		btnDeselectAllVndr.setBounds(512, 185, 41, 32);
		panel_2.add(btnDeselectAllVndr);
		
		lblStatusVndr = new JLabel("Status");
		lblStatusVndr.setForeground(new Color(128, 0, 128));
		lblStatusVndr.setBounds(304, 283, 41, 14);
		panel_2.add(lblStatusVndr);
		
		JLabel lblVndrPath = new JLabel(""); //For showing the Path of Report Generation 
		lblVndrPath.setForeground(new Color(128, 0, 0));
		lblVndrPath.setBounds(304, 318, 437, 14);
		panel_2.add(lblVndrPath);
		
		textAreaVndr = new JTextArea(); //For setting the Report Generation status
		textAreaVndr.setForeground(new Color(128, 0, 0));
		textAreaVndr.setBackground(new Color(119, 136, 153));
		textAreaVndr.setBounds(346, 283, 319, 14);
		panel_2.add(textAreaVndr);

		JButton VndrReportButton = new JButton("Generate Vendor Report");
		VndrReportButton.setBounds(94, 289, 174, 32);
		panel_2.add(VndrReportButton);
		
		JProgressBar progressBar_2 = new JProgressBar();
		progressBar_2.setBounds(299, 301, 210, 23);
		progressBar_2.setBackground(new Color(128, 0, 0));
		progressBar_2.setForeground(new Color(119, 136, 153));
		progressBar_2.setStringPainted(true);
		progressBar_2.setBounds(304, 298, 198, 17);
		panel_2.add(progressBar_2);
		
		JLabel lblSelectAllVndr = new JLabel("Select All");
		lblSelectAllVndr.setBounds(429, 228, 65, 14);
		panel_2.add(lblSelectAllVndr);

		JLabel lblDeSelectAllVndr = new JLabel("De Select All");
		lblDeSelectAllVndr.setBounds(505, 228, 76, 14);
		panel_2.add(lblDeSelectAllVndr);

		JSeparator separatorVndr = new JSeparator(SwingConstants.VERTICAL);
		separatorVndr.setBackground(new Color(0, 0, 0));
		separatorVndr.setForeground(new Color(0, 0, 0));
		separatorVndr.setBounds(408, 111, 11, 141);
		panel_2.add(separatorVndr);


		JLabel lblCopyrightVndr = new JLabel(Utility.CopyrightDeloitte);
		lblCopyrightVndr.setBounds(0, 393, 452, 9);
		lblCopyrightVndr.setForeground(new Color(128, 0, 128));
		lblCopyrightVndr.setFont(new Font("sansserif", Font.PLAIN, 8));
		panel_2.add(lblCopyrightVndr);
		//####################################################################################################################################################

		
		
		
		//#################################################### MULESOFT  #####################################################################################
		
		//Creating a timer For Mule Interfaces
		timer = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// TODO Auto-generated method stub
				
					try {

					if(time==99) {
						time=100;
						progressBar.setValue(time);
						Toolkit.getDefaultToolkit().beep();
						timer.stop();
						progressBar.setValue(0);
						btnGenerateMuleReport.setEnabled(true);
						btnSelectAll.setEnabled(true);
						btnDeselectAll.setEnabled(true);
						textArea.setText(Utility.ReportGenEndMsg);
					}
					while(time==0) 
					{
						input  = new FileInputStream(Utility.PropertyFilePath);
						prop.load(input);
						ReportFileName=prop.getProperty("ReportFileNamePattern").concat(checkURL.GetCurrentTimeStamp());
						ReportFilePath=prop.getProperty("ReportFilePath");
						Env= MuleTabComponents.getEnvironment(Env);
						if(Env.equals("")) 
						{
							Env=Utility.Development;
						}
						else if(!Env.equals(""))
						{
							Env = checkURL.removeLastComma(Env);
							checkURL objCheckURL= new checkURL(Env,ReportFilePath,ReportFileName);
							time=time+1;
						}
					}
					if(!(time==100)) {
						//Main logic goes over here
						Properties prop = new Properties();
						InputStream  input  = null;
						String interfaces = "";
						String currIntrfcNme = "";
						String URLIntExt = "";
						String [] arrURLIntExt;
						String url;
						String intExt;
						String envs = "";
						String internalURL = "";
						String externalURL = "";
						String urlTestedHTTPS = "";
						String urlTestedHTTP ="";
						List<String> arrEnvs = new ArrayList<String>();
						List<String> arrInterfaces = new ArrayList<String>();
						Map<String, List<String>> hm = null;
						Map<String, List<String>> nodeHM = new LinkedHashMap<String, List<String>>();
						int codeHTTPS = 0;
						int codeHTTP =0;
						String HTTPS_Status ="";
						String HTTP_Status="";
						String HTTPSuccessCodes = "";
						String HTTPFailureCodes = "";
						List<String> valuesForExcel = new ArrayList<String>();
						TrustManager[] trustAllCerts = new ConnectionFactory().implementTrstMngr();
						checkURL objCheckURL = new checkURL();
						Date date = new Date();
						String filePath = "";
						String fileName = "";
						boolean boServerDown = false;
						try 
						{
							input  = new FileInputStream(Utility.PropertyFilePath);
							//input  = new FileInputStream("./muleConfig.properties");
							prop.load(input);
							interfaces=prop.getProperty(Utility.Interfaces);
							//envs= prop.getProperty("Environments"); //To be taken by request URL
							envs=Env;
							HTTPSuccessCodes=prop.getProperty(Utility.HTTPSuccessCodes);
							HTTPFailureCodes=prop.getProperty(Utility.HTTPFailureCodes);
							arrInterfaces = Arrays.asList(interfaces.split(","));
							arrEnvs= Arrays.asList(envs.split(","));
							URLIntExt = new String();
							filePath= ReportFilePath;
							File filePth = new File (filePath);
							if(!filePth.exists())
							{
								filePth.mkdirs();
							}
							fileName=filePath+ReportFileName;
							File file = new File(fileName+Utility.ExcelExtension);
							if(!file.exists())
							{
								file.createNewFile();
							}
							XSSFWorkbook  workbook = new XSSFWorkbook ();
							XSSFSheet  sheet ;

							//Iterate over the Env
							for (int i = 0; i < arrEnvs.size(); i++) 
							{
								hm = new LinkedHashMap<String, List<String>>();
								sheet = workbook.createSheet(arrEnvs.get(i));
								sheet.setColumnWidth(0, 2000);
								sheet.setColumnWidth(1, 7000);
								sheet.setColumnWidth(2, 20000);
								List nodeDtls = new ArrayList();
								String stnodeDtls ="";
								nodeHM = checkURL.loadNodeDetails(arrEnvs.get(i));
								
								if(Utility.onPremFlag) //on Prem
									boServerDown = checkURL.hostAvailabilityCheck(checkURL.getIPParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(3)), checkURL.getPortParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(3)));
								else //Cloud
									//boServerDown = checkURL.hostAvailabilityCheck(checkURL.getIPParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(3)), checkURL.getPortParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(3))) && checkURL.hostAvailabilityCheck(checkURL.getIPParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(4)), checkURL.getPortParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(4)));
									boServerDown = checkURL.hostAvailabilityCheck(checkURL.getIPParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(3)), checkURL.getPortParseURL(nodeHM.get(arrEnvs.get(i).concat("1")).get(3)));
								if(boServerDown)
								{
								
								//Proceed to interface logic
								for(int j=0; j<arrInterfaces.size(); j++)
								{  
									//Get Current Interface Name
									currIntrfcNme=	arrInterfaces.get(j);
									//Find internal or external URL
									URLIntExt = prop.getProperty(currIntrfcNme);
									arrURLIntExt = URLIntExt.split(",");
									url   = arrURLIntExt[0];
									intExt= arrURLIntExt[1];
									try 
									{
										//Forming the URL to be tested
										urlTestedHTTPS = checkURL.formURLTested(arrEnvs.get(i),url,intExt,prop,internalURL,externalURL).trim();
										URL urlHTTPS = new URL(urlTestedHTTPS);
										codeHTTPS = ConnectionFactory.retrnConectnFrURL(urlHTTPS).getResponseCode(); //Just for the load Balancer
									}
									catch(ConnectException e)
									{
										e.printStackTrace();
									}
									catch(Exception e)
									{
										e.printStackTrace();	
									}

									//Get Pass Fail status
									HTTPS_Status = checkURL.getPassFailStatus(codeHTTPS, HTTPSuccessCodes, HTTPFailureCodes);
									//End Getting the status code of the URL for LB
									try {
										valuesForExcel.add(urlTestedHTTPS); //index0
										valuesForExcel.add(Integer.toString(Integer.valueOf(codeHTTPS)));//Integer.valueOf(code) //index1
										valuesForExcel.add(HTTPS_Status); //index2
										//If Detailed Flag is true
										//load detailed ; check with the flag
										if(rdbtnGetDetailedReport.isSelected())
										{
											URL urlIntrnlHTTP = null;
											int codeIntrnlHTTP =0;
											URL urlIntrnlHTTPS = null;
											int codeIntrnlHTTPS = 0;
											URL urlExtrnlHTTPS = null;
											int codeExtrnlHTTPS = 0;
											for( Map.Entry<String, List<String>> entry : nodeHM.entrySet()) 
											{
												String key = entry.getKey(); //PRD1,PRD2
												List<String> nodeValues = entry.getValue(); 
												//nodeValues --> "env" 		    //index 0
												//nodeValues --> "name"  	    //index 1
												//nodeValues --> "internalHTTP" //index 2
												//nodeValues --> "internalHTTPS"//index 3
												//nodeValues --> "externalHTTPS"//index 4
												urlIntrnlHTTP= new URL (checkURL.formURLTestedNode(nodeValues.get(2),arrEnvs.get(i),url));
												codeIntrnlHTTP = ConnectionFactory.retrnConectnFrURL(urlIntrnlHTTP).getResponseCode();
												valuesForExcel.add(checkURL.getPassFailStatus(codeIntrnlHTTP, HTTPSuccessCodes, HTTPFailureCodes)); //index 3

												if(intExt.equals(Utility.Internal)) {
													urlIntrnlHTTPS = new URL(checkURL.formURLTestedNode(nodeValues.get(3),arrEnvs.get(i),url)); 
													codeIntrnlHTTPS = ConnectionFactory.retrnConectnFrURL(urlIntrnlHTTPS).getResponseCode();
													//valuesForExcel.add(key.concat("|").concat(checkURL.getPassFailStatus(codeIntrnlHTTPS, HTTPSuccessCodes, HTTPFailureCodes))); //index 4
													valuesForExcel.add(checkURL.getPassFailStatus(codeIntrnlHTTPS, HTTPSuccessCodes, HTTPFailureCodes)) ; //index 4
												}
												else if(intExt.equals(Utility.External))
												{
													urlExtrnlHTTPS = new URL(checkURL.formURLTestedNode(nodeValues.get(4),arrEnvs.get(i),url)); 
													if(Utility.onPremFlag)
														valuesForExcel.add(Utility.NotImplemented);
													else
													{
														try{
															codeExtrnlHTTPS = ConnectionFactory.retrnConectnFrURL(urlExtrnlHTTPS).getResponseCode(); 
														}
														catch (Exception e)
														{
															e.printStackTrace();
														}
														//valuesForExcel.add(key.concat("|").concat(checkURL.getPassFailStatus(codeExtrnlHTTPS, HTTPSuccessCodes, HTTPFailureCodes))); //index 4
														valuesForExcel.add(checkURL.getPassFailStatus(codeExtrnlHTTPS, HTTPSuccessCodes, HTTPFailureCodes)) ; //index 4
													}
												}
											}
											//End load detailed 
										}
										//End of Detailed Flag is true

										//Storing URLs for specific env with key as name, value as Arraylist of URL,Code,Status
										hm.put(currIntrfcNme, valuesForExcel) ;		
										valuesForExcel = new ArrayList<String>(); 
									}
									catch(Exception e)
									{
										e.printStackTrace();
										JOptionPane.showMessageDialog(null, e);
									}
									finally {
										//if(!(arrInterfaces.size()==arrInterfaces.size()-1))
										codeHTTPS=0;
										HTTPS_Status="";
										urlTestedHTTPS="";

										codeHTTP=0;
										HTTP_Status="";
										urlTestedHTTP="";
									}
								}// End of Interface iteration				

								//Write the data in the excel sheet with 4 columns name, url, code and status
								objCheckURL.writeExcel(hm, arrEnvs.get(i),file,workbook,sheet,fileName,rdbtnGetDetailedReport.isSelected());
								valuesForExcel.clear();
								}
								else 
								{
									valuesForExcel.add(arrEnvs.get(i).concat(" "+Utility.ServerDownMessage));
									valuesForExcel.add(Utility.ServerDownStatus);
									valuesForExcel.add(Utility.ServerDownStatus);
									valuesForExcel.add(Utility.ServerDownStatus);
									hm.put(Utility.ServerDownStatus, valuesForExcel);
									objCheckURL.writeExcel(hm, arrEnvs.get(i),file,workbook,sheet,fileName,rdbtnGetDetailedReport.isSelected());
									//hm = null;
									valuesForExcel.clear();
								}
								//End of Server Down Check
								time = MuleTabComponents.getTimeMule(time, arrEnvs);
								progressBar.setValue(time);
								progressBar.update(progressBar.getGraphics());
							}// End of Environment Iteration	
							workbook.close();
							System.out.println("File Processed!!");
							time=99;
							progressBar.setValue(time);
							
							Desktop dt = Desktop.getDesktop();
							dt.open(file);
							//File Download Option
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally 
						{

						}
						//End of main logic
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e);
				}
				finally {
					Env="";
					ReportFilePath="";
					ReportFileName="";
				}

			}
		});

		btnGenerateMuleReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				//The main logic for mule goes here, Flow After Button Press
				try {
					time=0;
					timer.start();
					input  = new FileInputStream(Utility.PropertyFilePath);
					prop.load(input);
					ReportFileName=prop.getProperty("ReportFileNamePattern").concat(checkURL.GetCurrentTimeStamp());
					ReportFilePath=prop.getProperty("ReportFilePath");
					lblMulepath.setText("Report Location - "+ReportFilePath+ReportFileName);
					Font font = new Font("Verdana", Font.BOLD, 12);
					textArea.setFont(font);
					textArea.setText(Utility.ReportGenStrtMsg);
					textArea.setEditable(false);
					btnGenerateMuleReport.setEnabled(false);
					btnSelectAll.setEnabled(false);
					btnDeselectAll.setEnabled(false);
				}
				catch(Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
		

		//############################################################ GUIDEWIRE CODE ##########################################################################
		

		//Timer for Guidewire Integration
		timerGW = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// TODO Auto-generated method stub
				try {

					if(timeGW==99) {
						timeGW=100;
						progressBar_1.setValue(timeGW);
						Toolkit.getDefaultToolkit().beep();
						timerGW.stop();
						progressBar_1.setValue(0);
						gWReportButton.setEnabled(true);
						textAreaGW.setText(Utility.ReportGenEndMsg);
					}

					while(timeGW==0) {

						inputGW  = new FileInputStream(Utility.PropertyFilePathGW);
						propGW.load(inputGW);
						ReportFileName=propGW.getProperty("ReportFileNamePattern").concat(checkURL.GetCurrentTimeStamp());
						ReportFilePath=propGW.getProperty("ReportFilePath");
						if(chckbxGwTDev.isSelected())
							EnvGW=EnvGW.concat(Utility.T_DEV.concat(","));
						if(chckbxNewCheckBox_6.isSelected())
							EnvGW=EnvGW.concat(Utility.Development.concat(","));
						if(chckbxNewCheckBox_7.isSelected())
							EnvGW=EnvGW.concat(Utility.QualityAssurance.concat(","));
						if(chckbxNewCheckBox_8.isSelected())
							EnvGW=EnvGW.concat(Utility.TQA1.concat(","));
						if(chckbxNewCheckBox_9.isSelected())
							EnvGW=EnvGW.concat(Utility.TQA2.concat(","));
						if(chckbxNewCheckBox_10.isSelected())
							EnvGW=EnvGW.concat(Utility.TQA3.concat(","));
						if(chckbxNewCheckBox_11.isSelected())
							EnvGW=EnvGW.concat(Utility.UserAcceptanceTesting.concat(","));
						if(chckbxNewCheckBox_12.isSelected())
							EnvGW=EnvGW.concat(Utility.Training.concat(","));
						if(chckbxNewCheckBox_13.isSelected())
							EnvGW=EnvGW.concat(Utility.PreProduction.concat(","));
						if(chckbxNewCheckBox_14.isSelected())
							EnvGW=EnvGW.concat(Utility.Production.concat(","));

						if(EnvGW.equals("")) 
						{
							EnvGW=Utility.Development;
						}
						else if(!EnvGW.equals(""))
						{
							EnvGW = checkURL.removeLastComma(EnvGW);
							timeGW=timeGW+1;
						}

					}

					if(!(timeGW==100)) {
						//Main Logic goes here
						propGW = new Properties();
						InputStream  inputGW  = null;
						String readyStateResponse="";
						Map<String, List<String>> hmGW = new LinkedHashMap<String, List<String>>();
						List envListGW = new ArrayList<>();

						try {
							//Main logic goes over here
							inputGW  = new FileInputStream(Utility.PropertyFilePathGW);
							propGW.load(inputGW);
							//EnvGW=propGW.getProperty("Environments");
							readyStateResponse=propGW.getProperty("HTTP_ReadyState_Response");
							checkCenter objCheckCenter = new checkCenter();
							envListGW = Arrays.asList(EnvGW.split(","));

							//Load Properties
							List<String> valuesForExcel = new ArrayList<String>();
							String[] pingDetls= new String[3];
							String stReportFilePath = "";
							String stReportFileNamePattern ="";
							String stFileName ="";
							//Map<String, List<String>> hm = new LinkedHashMap<String, List<String>>();
							try {
								//needed for excel generation
								stReportFilePath = propGW.getProperty("ReportFilePath");
								stReportFileNamePattern = propGW.getProperty("ReportFileNamePattern");
								stFileName = stReportFilePath+stReportFileNamePattern+objCheckCenter.GetCurrentTimeStamp().replace(":"," ").replace("."," ").replace("-", " ");

								File filePth = new File (stReportFilePath);
								if(!filePth.exists())
								{
									filePth.mkdirs();
								}

								File file = new File(stFileName+Utility.ExcelExtension);
								if(!file.exists())
								{
									file.createNewFile();
								}
								XSSFWorkbook  workbook = new XSSFWorkbook ();
								XSSFSheet  sheet ;

								File fXmlFile = new File(Utility.URLxmlFilePathGW);
								DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
								DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
								Document doc = dBuilder.parse(fXmlFile);
								doc.getDocumentElement().normalize();

								for(int env =0; env < envListGW.size(); env ++) // Starting the environment iteration
								{
									sheet = workbook.createSheet(envListGW.get(env).toString());
									sheet.setColumnWidth(0, 2000);
									sheet.setColumnWidth(1, 7000);
									sheet.setColumnWidth(2, 20000);
									sheet.setColumnWidth(4, 7000);

									NodeList nList = doc.getElementsByTagName(envListGW.get(env).toString());//This to be iterated by loop
									for (int temp = 0; temp < nList.getLength(); temp++) //Iterations for Centers in Each Environment
									{
										Node nNode = nList.item(temp);
										if (nNode.getNodeType() == Node.ELEMENT_NODE) {
											Element eElement = (Element) nNode;
											NodeList appList = eElement.getElementsByTagName("app");
											for(int i=0;i< appList.getLength();i++)
											{
												if (nNode.getNodeType() == Node.ELEMENT_NODE) 
												{
													Node appNode = appList.item(i);
													Element element = (Element) appNode;
													valuesForExcel.add(element.getAttribute("url")); //index 0
													valuesForExcel.add(element.getAttribute("id"));  //index 1
													valuesForExcel.add(element.getAttribute("env")); //index 2
													pingDetls=objCheckCenter.getResponse(element.getAttribute("url")); 
													valuesForExcel.add(pingDetls[1]); //code  //index 3
													valuesForExcel.add(pingDetls[0]); //Status //index4
													hmGW.put(element.getAttribute("name"), valuesForExcel);
													valuesForExcel = new ArrayList<String>(); 
												}
											}
										}
									}
									//Write the data in the excel sheet with 4 columns name, url, code and status
									checkCenter.writeExcel(hmGW, envListGW.get(env).toString(),file,workbook,sheet,stFileName);
									//workbook.close();
									//valuesForExcel.clear();
									hmGW.clear();
									hmGW = new LinkedHashMap<String, List<String>>();
									timeGW = checkCenter.getGWTimeCompletionStatus(timeGW,envListGW.size());
									progressBar_1.setValue(timeGW);
									progressBar_1.update(progressBar_1.getGraphics());
								}
								
								workbook.close();
								System.out.println("File Processed!!");
								timeGW=99;
								progressBar_1.setValue(timeGW);
								progressBar_1.update(progressBar_1.getGraphics());
								Desktop dt = Desktop.getDesktop();
								dt.open(file);

							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, e);
							}
						}
						catch(Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e);
						}
						finally {

						}	
						//End of Main Logic
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally {
					EnvGW="";
					ReportFilePath="";
					ReportFileName="";
				}
			}
		});
		
		
		gWReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					//First flow of GW Flow sequence, Flow After GW Report Button Press
					timeGW=0;
					timerGW.start();
					inputGW  = new FileInputStream(Utility.PropertyFilePathGW);
					propGW.load(inputGW);
					ReportFileName=propGW.getProperty("ReportFileNamePattern").concat(checkURL.GetCurrentTimeStamp());
					ReportFilePath=propGW.getProperty("ReportFilePath");

					lblGWPath.setText("Report Location - "+ReportFilePath+ReportFileName);
					Font font = new Font("Verdana", Font.BOLD, 12);
					textAreaGW.setFont(font);
					textAreaGW.setText(Utility.ReportGenStrtMsg);
					textAreaGW.setEditable(false);

					gWReportButton.setEnabled(false);
					//End of first flow of GW Flow sequence
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		//End Guidewire Flow of Execution	
		
//############################################################################# VENDOR ####################################################################
		
		//Timer for Guidewire Integration
			timerVndr = new Timer(interval, new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						// TODO Auto-generated method stub
						try {
							if(timeVndr==99) {
								timeVndr=100;
								progressBar_2.setValue(timeVndr);
								Toolkit.getDefaultToolkit().beep();
								timerVndr.stop();
								progressBar_2.setValue(0);
								progressBar_2.update(progressBar_2.getGraphics());
								VndrReportButton.setEnabled(true);
								textAreaVndr.setText(Utility.ReportGenEndMsg);
							}

							while(timeVndr==0) {

								inputVndr  = new FileInputStream(Utility.PropertyFilePathVndr);
								propVndr.load(inputVndr);
								ReportFileName=propVndr.getProperty("ReportFileNamePattern").concat(checkURL.GetCurrentTimeStamp());
								ReportFilePath=propVndr.getProperty("ReportFilePath");
								if(chckbxNewCheckBox_15.isSelected())
									EnvVndr=EnvVndr.concat(Utility.Development.concat(","));
								if(chckbxNewCheckBox_16.isSelected())
									EnvVndr=EnvVndr.concat(Utility.QualityAssurance.concat(","));
								if(chckbxNewCheckBox_17.isSelected())
									EnvVndr=EnvVndr.concat(Utility.UserAcceptanceTesting.concat(","));
								if(chckbxNewCheckBox_18.isSelected())
									EnvVndr=EnvVndr.concat(Utility.Training.concat(","));
								if(chckbxNewCheckBox_19.isSelected())
									EnvVndr=EnvVndr.concat(Utility.PreProduction.concat(","));
								if(chckbxNewCheckBox_20.isSelected())
									EnvVndr=EnvVndr.concat(Utility.Production.concat(","));

								if(EnvVndr.equals("")) 
								{
									EnvVndr=Utility.Development;
								}
								else if(!EnvVndr.equals(""))
								{
									EnvVndr = checkURL.removeLastComma(EnvVndr);
									timeVndr=timeVndr+1;
								}

							}

							if(!(timeVndr==100)) {
								//Main Logic goes here
								propVndr = new Properties();
								InputStream  inputVndr  = null;
								//String EnvGW="";
								String readyStateResponse="";
								Map<String, List<String>> hmVndr = new LinkedHashMap<String, List<String>>();
								List envListVndr = new ArrayList<>();

								try {
									//Main logic goes over here
									inputVndr  = new FileInputStream(Utility.PropertyFilePathVndr);
									propVndr.load(inputVndr);
									//EnvGW=propGW.getProperty("Environments");
									checkCenter objCheckCenter = new checkCenter();
									envListVndr = Arrays.asList(EnvVndr.split(","));
									//objCheckCenter.loadProperties2(envListGW,propGW);


									//Load Properties
									List<String> valuesForExcel = new ArrayList<String>();
									String[] pingDetls= new String[3];
									String stReportFilePath = "";
									String stReportFileNamePattern ="";
									String stFileName ="";
									File file ;
									XSSFWorkbook  workbook;
									XSSFSheet  sheet ;
									//Map<String, List<String>> hm = new LinkedHashMap<String, List<String>>();
									try {
										//needed for excel generation
										stReportFilePath = propVndr.getProperty("ReportFilePath");
										stReportFileNamePattern = propVndr.getProperty("ReportFileNamePattern");
										stFileName = stReportFilePath+stReportFileNamePattern+objCheckCenter.GetCurrentTimeStamp().replace(":"," ").replace("."," ").replace("-", " ");

										File filePth = new File (stReportFilePath);
										if(!filePth.exists())
										{
											filePth.mkdirs();
										}

										file = new File(stFileName+Utility.ExcelExtension);
										if(!file.exists())
										{
											file.createNewFile();
										}
										workbook = new XSSFWorkbook ();
										File fXmlFile = new File(Utility.URLxmlFilePathVndr);
										DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
										DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
										Document doc = dBuilder.parse(fXmlFile);
										doc.getDocumentElement().normalize();
										System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

										for(int env =0; env < envListVndr.size(); env ++) // Starting the environment iteration
										{
											sheet = workbook.createSheet(envListVndr.get(env).toString());
											sheet.setColumnWidth(0, 2000);
											sheet.setColumnWidth(1, 7000);
											sheet.setColumnWidth(2, 20000);
											sheet.setColumnWidth(4, 7000);

											NodeList nList = doc.getElementsByTagName(envListVndr.get(env).toString());//This to be iterated by loop
											for (int temp = 0; temp < nList.getLength(); temp++) //Iterations for Centers in Each Environment
											{
												Node nNode = nList.item(temp);
												System.out.println("\nCurrent Element :" + nNode.getNodeName());
												if (nNode.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) nNode;
													System.out.println("Server id : " + eElement.getAttribute("id"));
													NodeList appList = eElement.getElementsByTagName("app");
													for(int i=0;i< appList.getLength();i++)
													{
														if (nNode.getNodeType() == Node.ELEMENT_NODE) 
														{
															Node appNode = appList.item(i);
															Element element = (Element) appNode;
															valuesForExcel.add(element.getAttribute("url")); //index 0
															valuesForExcel.add(element.getAttribute("name"));//index 1
															valuesForExcel.add(element.getAttribute("env")); //index 2
															valuesForExcel.add(Integer.toString(checkVendor.getResponse(element.getAttribute("url")))); //code  //index 3
															valuesForExcel.add(checkVendor.getVndrStatus(checkVendor.getResponse(element.getAttribute("url")),propVndr)); //index 4
															hmVndr.put(element.getAttribute("name"), valuesForExcel);
															valuesForExcel = new ArrayList<String>(); 
															//logger.info(hm);
														}
													}
												}

												timeVndr = MuleTabComponents.getTimeMule(timeVndr, envListVndr);
												progressBar_2.setValue(timeVndr);
												progressBar_2.update(progressBar_2.getGraphics());
											}

											//Write the data in the excel sheet with 4 columns name, url, code and status
											checkVendor.writeExcel(hmVndr, envListVndr.get(env).toString(),file,workbook,sheet,stFileName);
											//workbook.close();
											//TimeUnit.SECONDS.sleep(1);
											//valuesForExcel.clear();
											hmVndr.clear();
											hmVndr= new LinkedHashMap<String, List<String>>();

										}
										
										workbook.close();
										System.out.println("File Processed!!");
										timeVndr=99;
										progressBar_2.setValue(timeVndr);
										progressBar_2.update(progressBar_2.getGraphics());
										Desktop dt = Desktop.getDesktop();
										dt.open(file);
										
									} catch (Exception e) {
										e.printStackTrace();
										JOptionPane.showMessageDialog(null, e);
									}
									
									finally
									{
										file =null;
										workbook = null;
										sheet = null;
									}
								}
								catch(Exception e) {
									e.printStackTrace();
									JOptionPane.showMessageDialog(null, e);
								}
								finally {

								}
								//End of Main Logic
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e);
						}
						finally {
							EnvVndr="";
							ReportFilePath="";
							ReportFileName="";
							
						}
					}
				});

					
		VndrReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Vendor Logic Goes here
				try {
					timeVndr = 0;
					timerVndr.start();
					inputVndr = new FileInputStream(Utility.PropertyFilePathVndr);
					propVndr.load(inputVndr);
					ReportFileName=propVndr.getProperty("ReportFileNamePattern").concat(checkURL.GetCurrentTimeStamp());
					ReportFilePath=propVndr.getProperty("ReportFilePath");
					lblVndrPath.setText("Report Location - "+ReportFilePath+ReportFileName);
					Font font = new Font("Verdana", Font.BOLD, 12);
					textAreaVndr.setFont(font);
					textAreaVndr.setText(Utility.ReportGenStrtMsg);
					textAreaVndr.setEditable(false);
					VndrReportButton.setEnabled(false);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				//End of Vendor Logic
			}
		});
	}
}
