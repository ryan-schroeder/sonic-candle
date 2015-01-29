package com.soniccandle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.soniccandle.Main;
import com.soniccandle.controller.MainController;
import com.soniccandle.model.MainModel;

public class MainView {
	
	public static final String BG_OTHER_IMAGE = "other image";
	public static final String BG_BUILT_IN_IMAGE = "built-in image";
	public static final String BG_FLAT_COLOR = "flat color";
	public static final String OUTPUT_MP4_TITLE = "mp4 file";
	public static final String OUTPUT_SEQUENCE_TITLE = "image sequence (select a folder for output)";
	
	public MainModel m;
	public MainController cr;
	
	public void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("sonic candle");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.pane = frame.getContentPane();
		m.pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton button;
		JLabel label;

		JLabel headerLabel = getHeaderLabel();
		c.gridwidth = 1;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		m.pane.add(headerLabel, c);

		c.insets = new Insets(5, 5, 5, 5);

		JPanel inOutPanel = new JPanel();
		inOutPanel.setLayout(new GridBagLayout());
		inOutPanel.setBorder(BorderFactory.createTitledBorder("input and output files"));
		GridBagConstraints inOutC = new GridBagConstraints();
		inOutC.insets = new Insets(5, 5, 5, 5);
		inOutC.fill = GridBagConstraints.BOTH;

		button = new JButton("set input wav");
		button.setActionCommand(MainController.SET_INPUT_WAV);
		button.addActionListener(cr);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 0;
		inOutPanel.add(button, inOutC);

		m.audioFileNameLabel = new JLabel("  (no file chosen)");
		m.audioFileNameLabel.setOpaque(true);
		m.audioFileNameLabel.setBackground(new Color(255, 255, 255));
		inOutC.gridwidth = 1;
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 0;
		inOutPanel.add(m.audioFileNameLabel, inOutC);

		label = new JLabel("  output format:");
		label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 1;
		inOutPanel.add(label, inOutC);

		String[] outputMethods = {OUTPUT_MP4_TITLE, OUTPUT_SEQUENCE_TITLE};
		m.outputMethod = new JComboBox<String>(outputMethods);
		inOutC.weightx = 0.2;
		inOutC.gridwidth = 1;
		inOutC.gridx = 1;
		inOutC.gridy = 1;
		inOutPanel.add(m.outputMethod, inOutC);

		button = new JButton("set output location");
		button.setActionCommand(MainController.SET_OUTPUT_MP4);
		button.addActionListener(cr);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 2;
		inOutPanel.add(button, inOutC);

		m.outputToNameLabel = new JLabel("  (no file or folder chosen)");
		m.outputToNameLabel.setOpaque(true);
		m.outputToNameLabel.setBackground(new Color(255, 255, 255));
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 2;
		inOutPanel.add(m.outputToNameLabel, inOutC);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		m.pane.add(inOutPanel, c);


		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(new GridBagLayout());
		bgPanel.setBorder(BorderFactory.createTitledBorder("background"));
		GridBagConstraints bgC = new GridBagConstraints();
		bgC.insets = new Insets(5, 5, 5, 5);
		bgC.fill = GridBagConstraints.BOTH;


		m.bgTypeGroup = new ButtonGroup();
		m.flatColorRb = new JRadioButton(BG_FLAT_COLOR);
		m.flatColorRb.setActionCommand(BG_FLAT_COLOR);
		m.flatColorRb.addActionListener(cr);
		m.flatColorRb.setSelected(true);
		m.bgTypeGroup.add(m.flatColorRb);
		bgC.gridx = 0;
		bgC.gridy = 0;
		bgPanel.add(m.flatColorRb, bgC);

		m.builtInIimageRb = new JRadioButton(BG_BUILT_IN_IMAGE);
		m.builtInIimageRb.setActionCommand(BG_BUILT_IN_IMAGE);
		m.builtInIimageRb.addActionListener(cr);
		m.bgTypeGroup.add(m.builtInIimageRb);
		bgC.gridx = 1;
		bgC.gridy = 0;
		bgPanel.add(m.builtInIimageRb, bgC);

		m.otherImageRb = new JRadioButton(BG_OTHER_IMAGE);
		m.otherImageRb.setActionCommand(BG_OTHER_IMAGE);
		m.otherImageRb.addActionListener(cr);
		m.bgTypeGroup.add(m.otherImageRb);
		bgC.gridx = 2;
		bgC.gridy = 0;
		bgPanel.add(m.otherImageRb, bgC);

		m.bgColorPanel = new JPanel();
		label = new JLabel("RGB values (0-255 for each color): ");
		m.bgColorPanel.add(label);

		m.bgColorRed = new JTextField("0");
		m.bgColorRed.setColumns(3);
		m.bgColorPanel.add(m.bgColorRed);

		m.bgColorGreen = new JTextField("0");
		m.bgColorGreen.setColumns(3);
		m.bgColorPanel.add(m.bgColorGreen);

		m.bgColorBlue = new JTextField("0");
		m.bgColorBlue.setColumns(3);
		m.bgColorPanel.add(m.bgColorBlue);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 1;
		bgPanel.add(m.bgColorPanel, bgC);

		m.bgBuiltInPanel = new JPanel();
		m.bgBuiltInPanel.setVisible(false);
		label = new JLabel("built-in image: ");
		m.bgBuiltInPanel.add(label);

		String[] builtInImages = {"blue_grid.png", "blue_soft_grid.png", "blue_soft.png", "blue.png", "dark_gray.png", "purple.png",
				"rainbow_bright.png", "rainbow_faint.png", "rainbow_mesh.png", "rainbow.png", "red_grid.png", "red.png"};
		m.bgBuiltIn = new JComboBox<String>(builtInImages);
		m.bgBuiltInPanel.add(m.bgBuiltIn);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 2;
		bgPanel.add(m.bgBuiltInPanel, bgC);

		m.bgOtherImagePanel = new JPanel();
		m.bgOtherImagePanel.setVisible(false);
		label = new JLabel("background image, png or jpg, "+Main.WIDTH+"x"+Main.HEIGHT);
		m.bgOtherImagePanel.add(label);

		button = new JButton("set");
		button.setActionCommand(MainController.SET_BG_OTHER_IMAGE);
		button.addActionListener(cr);
		m.bgOtherImagePanel.add(button);

		m.bgImageNamelabel = new JLabel("  (no file chosen)  ");
		m.bgImageNamelabel.setOpaque(true);
		m.bgImageNamelabel.setBackground(new Color(255, 255, 255));
		m.bgOtherImagePanel.add(m.bgImageNamelabel);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 3;
		bgPanel.add(m.bgOtherImagePanel, bgC);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 2;
		m.pane.add(bgPanel, c);

		JPanel renderPanel = new JPanel();
		renderPanel.setLayout(new GridBagLayout());
		renderPanel.setBorder(BorderFactory.createTitledBorder("render"));
		GridBagConstraints renderC = new GridBagConstraints();
		renderC.insets = new Insets(5, 5, 5, 5);
		renderC.fill = GridBagConstraints.BOTH;

		m.renderButton = new JButton("render");
		m.renderButton.setEnabled(false);
		m.renderButton.setActionCommand(MainController.RENDER);
		m.renderButton.addActionListener(cr);
		c.gridwidth = 2;
		renderC.weightx = 1;
		renderC.gridx = 0;
		renderC.gridy = 0;
		renderPanel.add(m.renderButton, renderC);


		m.progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		m.progressBar.setValue(0);
		m.progressBar.setEnabled(false);
		m.progressBar.setMinimumSize(new Dimension(30, 30));
		renderC.weightx = 1;
		renderC.gridx = 0;
		renderC.gridy = 1;
		renderPanel.add(m.progressBar, renderC);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 3;
		m.pane.add(renderPanel, c);

		m.fc = new JFileChooser();
		m.fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	private JLabel getHeaderLabel() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		BufferedImage header;
		try {
			header = ImageIO.read(loader.getResourceAsStream("header.png"));
		} catch (Exception e) {
			throw new RuntimeException("header image not found");
		} 
		JLabel headerLabel = new JLabel(new ImageIcon(header));
		return headerLabel;
	}

}
