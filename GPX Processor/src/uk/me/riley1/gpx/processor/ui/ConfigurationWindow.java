package uk.me.riley1.gpx.processor.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.me.riley1.gpx.processor.core.*;


import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JCheckBox;

public class ConfigurationWindow implements ActionListener, PopupMenuListener, ItemListener {

	private JFrame frmGpxFileAdd;
	private JTextField tfDistance;
	private JButton btnCancel;
	private JButton btnAddMarkers;
	private JComboBox<String> cbFileName;
	private JComboBox<String> cbDirection;
	private JComboBox<UnitConverter> cbDistanceUnit;
	private JLabel statusBar;
	private JCheckBox chckbxSFM;
	private GPXFile file = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				ConfigurationWindow window = null;
				try {
					
					window = new ConfigurationWindow();
					window.initialize();
					window.frmGpxFileAdd.setVisible(true);
				} catch (Exception e) {
					
					window.displayException(e);;
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConfigurationWindow() {
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGpxFileAdd = new JFrame();
		frmGpxFileAdd.setTitle("GPX File Add Markers");
		frmGpxFileAdd.getContentPane().setFont(new Font("Arial", Font.BOLD, 16));
		frmGpxFileAdd.setBounds(100, 100, 525, 293);
		frmGpxFileAdd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(31, 100));
		frmGpxFileAdd.getContentPane().add(panel);
		panel.setLayout(null);
		
		cbFileName = new JComboBox<String>();
		cbFileName.setBounds(176, 28, 255, 20);
		cbFileName.addItemListener(this);
		cbFileName.addPopupMenuListener(this);
		cbFileName.setEditable(true);
		panel.add(cbFileName);
		
		JLabel lblFileToProcess = new JLabel("File to Process");
		lblFileToProcess.setLabelFor(cbFileName);
		lblFileToProcess.setFont(new Font("Arial", Font.PLAIN, 12));
		lblFileToProcess.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileToProcess.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFileToProcess.setBounds(57, 30, 109, 14);
		panel.add(lblFileToProcess);
		
		cbDirection = new JComboBox<String>();
		cbDirection.setEditable(false);
		addItems(cbDirection, new String[] {"Backwards", "Forwards"});
		cbDirection.setSelectedIndex(0);
		cbDirection.setBounds(176, 68, 109, 20);
		cbDirection.addItemListener(this);
		panel.add(cbDirection);
		
		JLabel lblDirection = new JLabel("Direction");
		lblDirection.setLabelFor(cbDirection);
		lblDirection.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDirection.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDirection.setAlignmentX(0.5f);
		lblDirection.setBounds(57, 74, 109, 14);
		panel.add(lblDirection);
		
		cbDistanceUnit = new JComboBox<UnitConverter>();
		cbDistanceUnit.setEditable(false);
		addItems(cbDistanceUnit, UnitConverter.values());
		cbDistanceUnit.setSelectedItem(UnitConverter.MILES);
		cbDistanceUnit.setBounds(288, 114, 109, 20);
		panel.add(cbDistanceUnit);
		
		JLabel lblDsitanceBetweenMarkers = new JLabel("Distance Between Markers");
		lblDsitanceBetweenMarkers.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDsitanceBetweenMarkers.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDsitanceBetweenMarkers.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblDsitanceBetweenMarkers.setBounds(0, 116, 166, 14);
		panel.add(lblDsitanceBetweenMarkers);
		
		tfDistance = new JTextField("1");
		tfDistance.setBounds(176, 114, 86, 20);
		panel.add(tfDistance);
		tfDistance.setColumns(8);
		
		
		btnAddMarkers = new JButton("Add Markers");
		btnAddMarkers.setToolTipText("<html>Starts adding markers to the selected file at the specified intervals and<br/>"
				+ "starting at the beginning of a route or the end depending on whether<br/>"
				+ "direction is Forwards or Backwards</html>");
		btnAddMarkers.setEnabled(false);
		btnAddMarkers.addActionListener(this);
		btnAddMarkers.setFont(new Font("Arial", Font.BOLD, 12));
		btnAddMarkers.setForeground(SystemColor.textText);
		btnAddMarkers.setBackground(UIManager.getColor("Button.background"));
		btnAddMarkers.setBounds(267, 181, 113, 23);
		panel.add(btnAddMarkers);
		
		btnCancel = new JButton("Close");
		btnCancel.setToolTipText("Close this dialogue and exit the application");
		btnCancel.addActionListener(this);
		btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
		btnCancel.setForeground(SystemColor.textText);
		btnCancel.setBackground(UIManager.getColor("Button.background"));
		btnCancel.setEnabled(true);
		btnCancel.setBounds(132, 181, 89, 23);
		panel.add(btnCancel);
		
		statusBar = new JLabel("");
		statusBar.setBounds(70, 229, 379, 14);
		panel.add(statusBar);
		
		chckbxSFM = new JCheckBox("Add Start/finish Markers");
		chckbxSFM.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxSFM.setBounds(308, 67, 166, 23);
		panel.add(chckbxSFM);
		
	}

	private <E> void addItems(JComboBox<E> cB, E[] values) {
		
		for (E item : values) {
			cB.addItem(item);
		}
		cB.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent aE) {
		
		if (aE.getSource().equals(btnCancel)) {
			frmGpxFileAdd.dispose();
			System.exit(0);
		}
		else if (aE.getSource().equals(btnAddMarkers)) {
			doAddMarkers();
		}
	}

	private void doAddMarkers() {

		file = new GPXFile((String) cbFileName.getSelectedItem());
		Configuration config = new Configuration(this);
		frmGpxFileAdd.setEnabled(false);
		statusBar.setToolTipText("");
		try {
			
			GPXProcessor proc = new GPXProcessor(configuration);
			proc.setMarkerInterval(Double.parseDouble(tfDistance.getText()));
			proc.setIntervalUnit((UnitConverter) cbDistanceUnit.getSelectedItem());
			statusBar.setText("Removing any existing markers ...");
			proc.removeMarkers();
			statusBar.setText("Adding new markers ...");
			proc.addMarkers();
			statusBar.setText("Renaming input file and creating new output file ...");
			proc.generateOutput();
			statusBar.setText("Completed processing - " + file.getName());
			frmGpxFileAdd.setEnabled(true);
		}
		catch (Exception e) {
			displayException(e);
		}
	}

	protected void displayException(Exception e) {
		
		frmGpxFileAdd.setEnabled(true);
		statusBar.setToolTipText("");
		Object[] options = {"OK", "Show Exception"};
		int n = JOptionPane.showOptionDialog(frmGpxFileAdd,
		"An Error has Occurred while processing your file:\n\n" + 
				e.getLocalizedMessage(),
		"Error",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.ERROR_MESSAGE,
		null,     //do not use a custom Icon
		options,  //the titles of buttons
		options[0]); //default button title
		
		if (n == 1) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    PrintStream ps = new PrintStream(baos);
		    e.printStackTrace(ps);
			JOptionPane.showMessageDialog(frmGpxFileAdd, baos.toString(), 
					"Full Error Stack Trace",JOptionPane.ERROR_MESSAGE);
		}
	}

	
	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// Does Nothing. Here to honour implementation	
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// Does Nothing. Here to honour implementation	
		
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

		int retVal = 0;
		if (e.getSource().equals(cbFileName)) {
			JFileChooser fC = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "GPX Files", "gpx");
			fC.setFileFilter(filter);
			fC.addActionListener(this);
			retVal = fC.showDialog(cbFileName, "Select File");
			if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = fC.getSelectedFile();
                cbFileName.removeAllItems();
                cbFileName.setSelectedItem(file.getAbsolutePath());
			}
		}
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getSource();
		String item = (String) e.getItem();
		if (source.equals(cbFileName)) {
			File file = new File(item);
			btnAddMarkers.setEnabled(file.isFile());
		} 			
	}
	
	public static class Configuration extends HashMap<Integer, Object> {
		
		public static int DIRECTION = 1;
		public static int START_FINISH = 2;
		public static int GPX_FILE = 3;
		
		private Configuration(ConfigurationWindow window) {
			
			put(GPX_FILE, window.file);
			put(DIRECTION, (String) window.cbFileName.getSelectedItem());
			put(START_FINISH, window.chckbxSFM.isSelected());

		}

	}
}
