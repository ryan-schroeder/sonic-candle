package com.soniccandle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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
import javax.swing.border.TitledBorder;

import com.soniccandle.Main;
import com.soniccandle.controller.MainController;
import com.soniccandle.model.MainModel;

public class MainView {

	// color constants
	public static final Color FRAMECOLOR = new Color(17, 17, 17);// dark grey
	public static final Color PANELCOLOR = new Color(45, 45, 45); // lighter
																	// grey

	public static final String BG_OTHER_IMAGE = "other image";
	public static final String BG_BUILT_IN_IMAGE = "built-in image";
	public static final String BG_FLAT_COLOR = "flat color";
	public static final String OUTPUT_MP4_TITLE = "mp4 file";
	public static final String OUTPUT_SEQUENCE_TITLE = "image sequence (select a folder for output)";

	public MainModel m;
	public MainController c;

	public void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Sonic Candle");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Icon code
		Image icon;
		InputStream input = getClass().getResourceAsStream(
				"/sonic-candle-icon.png");
		try {
			icon = ImageIO.read(input);
			frame.setIconImage(icon);
		} catch (IOException e) {
			// Intentionally ignore exception (because it should never happen)
		}

		m.pane = frame.getContentPane();
		m.pane.setLayout(new GridBagLayout());
		m.pane.setBackground(FRAMECOLOR);

		GridBagConstraints topC = new GridBagConstraints();
		topC.fill = GridBagConstraints.HORIZONTAL;
		JLabel label;

		// Header (Image)
		JLabel headerLabel = getHeaderLabel();
		topC.gridwidth = 1;
		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 0;
		m.pane.add(headerLabel, topC);

		topC.insets = new Insets(5, 5, 5, 5);

		// Video Properties panel
		JPanel vpPanel = new JPanel();
		vpPanel.setLayout(new GridBagLayout());
		vpPanel.setBackground(PANELCOLOR);
		vpPanel.setBorder(newTitledPanel("Video Properties: "));
		vpPanel.setForeground(Color.WHITE);
		GridBagConstraints vpC = new GridBagConstraints();
		vpC.insets = new Insets(5, 5, 5, 5);
		vpC.fill = GridBagConstraints.BOTH;

		JLabel fps = SCLabel("Frame Rate (Minimum 1):");
		vpC.gridwidth = 1;
		vpC.gridx = 0;
		vpC.gridy = 0;
		vpPanel.add(fps, vpC);

		m.videoSetFrameRate = new JTextField(
				Integer.toString(Main.VIDEO_FRAME_RATE)); // set default Frame
															// Rate value with
															// the constant
		m.videoSetFrameRate.setColumns(3);
		vpC.gridwidth = 1;
		vpC.gridx = 1;
		vpC.gridy = 0;
		vpPanel.add(m.videoSetFrameRate, vpC);

		JLabel frameWidth = SCLabel("Video Width (Minumum 400):");
		vpC.gridwidth = 1;
		vpC.gridx = 0;
		vpC.gridy = 1;
		vpPanel.add(frameWidth, vpC);

		m.videoSetWidth = new JTextField(Integer.toString(Main.WIDTH)); // set
																		// default
																		// width
																		// value
																		// with
																		// the
																		// constant
		m.videoSetWidth.setColumns(3);
		vpPanel.add(m.videoSetWidth);
		vpC.gridwidth = 1;
		vpC.gridx = 1;
		vpC.gridy = 1;
		vpPanel.add(m.videoSetWidth, vpC);

		JLabel frameHeight = SCLabel("Video Height (Minimum 300):");
		vpC.gridwidth = 1;
		vpC.gridx = 0;
		vpC.gridy = 2;
		vpPanel.add(frameHeight, vpC);

		m.videoSetHeight = new JTextField(Integer.toString(Main.HEIGHT)); // set
																			// default
																			// height
																			// value
																			// with
																			// the
																			// constant
		m.videoSetHeight.setColumns(3);
		vpPanel.add(m.videoSetHeight);
		vpC.gridwidth = 1;
		vpC.gridx = 1;
		vpC.gridy = 2;
		vpPanel.add(m.videoSetHeight, vpC);

		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 1;
		m.pane.add(vpPanel, topC);

		// IO panel
		JPanel inOutPanel = new JPanel();
		inOutPanel.setLayout(new GridBagLayout());
		inOutPanel.setBorder(newTitledPanel("Input and Output Files"));
		inOutPanel.setBackground(PANELCOLOR);
		GridBagConstraints inOutC = new GridBagConstraints();
		inOutC.insets = new Insets(5, 5, 5, 5);
		inOutC.fill = GridBagConstraints.BOTH;

		m.setAudioButton = new JButton("Set Input .wav");
		m.setAudioButton.setActionCommand(MainController.SET_INPUT_WAV);
		m.setAudioButton.addActionListener(c);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 0;
		inOutPanel.add(m.setAudioButton, inOutC);

		m.audioFileNameLabel = new JLabel("  (no file chosen)");
		m.audioFileNameLabel.setOpaque(true);
		m.audioFileNameLabel.setBackground(new Color(255, 255, 255));
		inOutC.gridwidth = 1;
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 0;
		inOutPanel.add(m.audioFileNameLabel, inOutC);

		label = new JLabel("  Output Format:");
		label.setBorder(BorderFactory.createLineBorder(FRAMECOLOR));
		label.setForeground(Color.WHITE);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 1;
		inOutPanel.add(label, inOutC);

		String[] outputMethods = { OUTPUT_MP4_TITLE, OUTPUT_SEQUENCE_TITLE };
		m.outputMethod = new JComboBox<String>(outputMethods);
		inOutC.weightx = 0.2;
		inOutC.gridwidth = 1;
		inOutC.gridx = 1;
		inOutC.gridy = 1;
		inOutPanel.add(m.outputMethod, inOutC);

		m.setOutputButton = new JButton("Set Output Location");
		m.setOutputButton.setActionCommand(MainController.SET_OUTPUT_MP4);
		m.setOutputButton.addActionListener(c);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 2;
		inOutPanel.add(m.setOutputButton, inOutC);

		m.outputToNameLabel = new JLabel("  (no file or folder chosen)");
		m.outputToNameLabel.setOpaque(true);
		m.outputToNameLabel.setBackground(new Color(255, 255, 255));
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 2;
		inOutPanel.add(m.outputToNameLabel, inOutC);

		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 2;
		m.pane.add(inOutPanel, topC);

		// Background Panel
		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(new GridBagLayout());
		bgPanel.setBorder(newTitledPanel("Background Options"));
		bgPanel.setBackground(PANELCOLOR);
		GridBagConstraints bgC = new GridBagConstraints();
		bgC.insets = new Insets(5, 5, 5, 5);
		bgC.fill = GridBagConstraints.BOTH;

		m.bgTypeGroup = new ButtonGroup();
		m.flatColorRb = new JRadioButton(BG_FLAT_COLOR);
		m.flatColorRb.setActionCommand(BG_FLAT_COLOR);
		m.flatColorRb.addActionListener(c);
		m.flatColorRb.setSelected(true);
		m.bgTypeGroup.add(m.flatColorRb);
		bgC.gridx = 0;
		bgC.gridy = 0;
		bgPanel.add(m.flatColorRb, bgC);

		m.builtInImageRb = new JRadioButton(BG_BUILT_IN_IMAGE);
		m.builtInImageRb.setActionCommand(BG_BUILT_IN_IMAGE);
		m.builtInImageRb.addActionListener(c);
		m.bgTypeGroup.add(m.builtInImageRb);
		bgC.gridx = 1;
		bgC.gridy = 0;
		bgPanel.add(m.builtInImageRb, bgC);

		m.otherImageRb = new JRadioButton(BG_OTHER_IMAGE);
		m.otherImageRb.setActionCommand(BG_OTHER_IMAGE);
		m.otherImageRb.addActionListener(c);
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
		label = new JLabel("built-in image(1920x1080): ");
		m.bgBuiltInPanel.add(label);

		String[] builtInImages = { "blue.png", "deep.png", "golden.png",
				"maroon.png", "red.png", "sea.png", "silver.png", "violet.png" };
		m.bgBuiltIn = new JComboBox<String>(builtInImages);
		m.bgBuiltInPanel.add(m.bgBuiltIn);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 2;
		bgPanel.add(m.bgBuiltInPanel, bgC);

		m.bgOtherImagePanel = new JPanel();
		m.bgOtherImagePanel.setVisible(false);
		label = new JLabel("background image, png or jpg (Keep aspect ratio)");
		m.bgOtherImagePanel.add(label);

		m.setBgOtherImageButton = new JButton("set");
		m.setBgOtherImageButton
				.setActionCommand(MainController.SET_BG_OTHER_IMAGE);
		m.setBgOtherImageButton.addActionListener(c);
		m.bgOtherImagePanel.add(m.setBgOtherImageButton);

		m.bgImageNamelabel = new JLabel("  (no file chosen)  ");
		m.bgImageNamelabel.setOpaque(true);
		m.bgImageNamelabel.setBackground(new Color(255, 255, 255));
		m.bgOtherImagePanel.add(m.bgImageNamelabel);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 3;
		bgPanel.add(m.bgOtherImagePanel, bgC);

		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 3;
		m.pane.add(bgPanel, topC);

		// Bars panel - search makeBarsPanel
		JPanel barsPanel = makeBarsPanel();
		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 4;
		m.pane.add(barsPanel, topC);

		// Render panel
		JPanel renderPanel = new JPanel();
		renderPanel.setLayout(new GridBagLayout());
		renderPanel.setBackground(PANELCOLOR);
		renderPanel.setBorder(newTitledPanel("Render"));
		GridBagConstraints renderC = new GridBagConstraints();
		renderC.insets = new Insets(5, 5, 5, 5);
		renderC.fill = GridBagConstraints.BOTH;

		m.renderButton = new JButton("render");
		m.renderButton.setEnabled(false);
		m.renderButton.setActionCommand(MainController.RENDER);
		m.renderButton.addActionListener(c);
		topC.gridwidth = 2;
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

		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 5;
		m.pane.add(renderPanel, topC);

		m.fc = new JFileChooser();
		m.fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// Display the window.
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

	private JPanel makeBarsPanel() {
		JPanel barsPanel = new JPanel();
		barsPanel.setLayout(new GridBagLayout());
		barsPanel.setBackground(PANELCOLOR);
		barsPanel.setBorder(newTitledPanel("Bar Customization"));
		GridBagConstraints barsC = new GridBagConstraints();
		barsC.insets = new Insets(5, 5, 5, 5);
		barsC.fill = GridBagConstraints.BOTH;

		JLabel label = SCLabel("Bar Style:");
		barsC.gridwidth = 1;
		barsC.gridx = 0;
		barsC.gridy = 0;
		barsPanel.add(label, barsC);

		label = SCLabel("Bar Color (RGB 0-255) ");
		barsC.gridwidth = 1;
		barsC.gridx = 0;
		barsC.gridy = 1;
		barsPanel.add(label, barsC);

		JPanel panel = new JPanel();

		m.barColorRed = new JTextField("255");
		m.barColorRed.setColumns(3);
		panel.add(m.barColorRed);

		m.barColorGreen = new JTextField("255");
		m.barColorGreen.setColumns(3);
		panel.add(m.barColorGreen);

		m.barColorBlue = new JTextField("255");
		m.barColorBlue.setColumns(3);
		panel.add(m.barColorBlue);

		barsC.gridx = 1;
		barsC.gridy = 1;
		barsPanel.add(panel, barsC);

		label = SCLabel("Alpha/Transparency (0-255): ");
		barsC.gridwidth = 1;
		barsC.gridx = 0;
		barsC.gridy = 2;
		barsPanel.add(label, barsC);

		JPanel panelAlpha = new JPanel();

		m.barAlpha = new JTextField("255");
		m.barAlpha.setColumns(3);
		panelAlpha.add(m.barAlpha);

		barsC.gridwidth = 1;
		barsC.gridx = 1;
		barsC.gridy = 2;
		barsPanel.add(panelAlpha, barsC);

		String[] barStyles = { MainController.BAR_STYLE_THICK_BROCK,
				MainController.BAR_STYLE_OUTLINE_BLOCK,
				MainController.BAR_STYLE_THIN,
				MainController.BAR_STYLE_ROUND_BLOCK,
				MainController.BAR_STYLE_ROUND_OUTLINE };
		m.barStyle = new JComboBox<String>(barStyles);
		barsC.gridx = 1;
		barsC.gridy = 0;
		barsPanel.add(m.barStyle, barsC);

		return barsPanel;
	}

	private TitledBorder newTitledPanel(String title) {

		javax.swing.border.Border normalBorder = (BorderFactory
				.createEtchedBorder());
		Font myFont = new Font("Plain", Font.PLAIN, 12);

		TitledBorder SCBorder = (BorderFactory
				.createTitledBorder(normalBorder, title, TitledBorder.LEFT,
						TitledBorder.TOP, myFont, Color.WHITE));

		return (SCBorder);
	}

	private JLabel SCLabel(String text) {
		JLabel SCLabel = new JLabel(text);

		SCLabel.setForeground(Color.WHITE);

		return SCLabel;

	}
}
