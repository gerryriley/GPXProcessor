package uk.me.riley1.gpx.processor.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.UIManager;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigurationWindow {

	private JFrame frmGpxFileAdd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigurationWindow window = new ConfigurationWindow();
					window.frmGpxFileAdd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConfigurationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGpxFileAdd = new JFrame();
		frmGpxFileAdd.setTitle("GPX File Add Markers");
		frmGpxFileAdd.getContentPane().setFont(new Font("Arial", Font.BOLD, 16));
		frmGpxFileAdd.setBounds(100, 100, 525, 376);
		frmGpxFileAdd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(31, 100));
		frmGpxFileAdd.getContentPane().add(panel);
		panel.setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(176, 28, 255, 20);
		panel.add(comboBox);
		
		JLabel lblFileToProcess = new JLabel("File to Process");
		lblFileToProcess.setLabelFor(comboBox);
		lblFileToProcess.setFont(new Font("Arial", Font.PLAIN, 12));
		lblFileToProcess.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileToProcess.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFileToProcess.setBounds(57, 30, 109, 14);
		panel.add(lblFileToProcess);
		
		JLabel lblDirection = new JLabel("Direction");
		lblDirection.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDirection.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDirection.setAlignmentX(0.5f);
		lblDirection.setBounds(57, 74, 109, 14);
		panel.add(lblDirection);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(176, 68, 109, 20);
		panel.add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(176, 122, 109, 20);
		panel.add(comboBox_2);
		
				
		JLabel lblDistanceUnit = new JLabel("Distance Unit");
		lblDistanceUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDistanceUnit.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDistanceUnit.setAlignmentX(0.5f);
		lblDistanceUnit.setBounds(57, 124, 109, 14);
		panel.add(lblDistanceUnit);
		
		JLabel lblDsitanceBetweenMarkers = new JLabel("Distance Between Markers");
		lblDsitanceBetweenMarkers.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDsitanceBetweenMarkers.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDsitanceBetweenMarkers.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblDsitanceBetweenMarkers.setBounds(0, 171, 166, 14);
		panel.add(lblDsitanceBetweenMarkers);
		
		JButton btnAddMarkers = new JButton("Add Markers");
		btnAddMarkers.setFont(new Font("Arial", Font.BOLD, 12));
		btnAddMarkers.setForeground(UIManager.getColor("Button.focus"));
		btnAddMarkers.setBackground(UIManager.getColor("Button.background"));
		btnAddMarkers.setBounds(265, 233, 113, 23);
		panel.add(btnAddMarkers);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmGpxFileAdd.dispose();
				System.exit(0);
			}
		});
		btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
		btnCancel.setForeground(UIManager.getColor("Button.focus"));
		btnCancel.setBackground(UIManager.getColor("Button.background"));
		btnCancel.setBounds(123, 233, 89, 23);
		panel.add(btnCancel);
		
	}
}
