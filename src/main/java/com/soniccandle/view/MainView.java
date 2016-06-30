package com.soniccandle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

import com.soniccandle.Main;
import com.soniccandle.controller.MainController;
import com.soniccandle.model.MainModel;
import com.soniccandle.util.ImageFilter;
import com.soniccandle.util.InputFilter;
import com.soniccandle.util.OutputFilter;
import com.soniccandle.util.Utils;
import com.soniccandle.view.components.ColorBox;

public class MainView {

	// color constants
	public static final Color BGCOLOR = new Color(17, 17, 17);
	public static final Color PANELCOLOR = new Color(45, 45, 45);
	public static final Color SCPURPLE = new Color(91, 18, 153);
	public static final Color SCPURPLE_LT = SCPURPLE.brighter();
	public static final Color SCPURPLE_BACK = new Color(61, 54, 64);

	public static final String BG_OTHER_IMAGE = "Other Image";
	public static final String BG_BUILT_IN_IMAGE = "Built-in Image";
	public static final String BG_FLAT_COLOR = "Flat Color";
	public static final String OUTPUT_MP4_TITLE = "Mp4 video file";
	public static final String OUTPUT_SEQUENCE_TITLE = "Image Sequence (png) (select a folder for output)";

	public MainModel m;
	public MainController c;

	private ColorBox colorBox;

	public void createAndShowGUI() {

		// use Nimbus
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					customizeNimbus();
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Nimbus look and feel not found (MainView.java)");
			e.printStackTrace();
		}

		// Get screen size
		int screenWidth = Main.WIDTH;
		int screenHeight = Main.HEIGHT;

		// Create and set up the window.
		JFrame frame = new JFrame("Sonic Candle");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Icon code
		Image icon;
		InputStream input = getClass().getResourceAsStream("/sonic-candle-icon.png");
		try {
			icon = ImageIO.read(input);
			frame.setIconImage(icon);
		} catch (IOException e) {
			// Intentionally ignore exception (because it should never happen)
		}

		m.pane = frame.getContentPane();
		m.pane.setLayout(new GridBagLayout());
		m.pane.setBackground(BGCOLOR);

		GridBagConstraints topC = new GridBagConstraints();
		topC.fill = GridBagConstraints.HORIZONTAL;
		JLabel label;

		// Header (Image)
		JLabel headerLabel = getHeaderLabel();
		headerLabel.setPreferredSize(new Dimension(930, 54));
		topC.gridwidth = 2;
		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 0;
		m.pane.add(headerLabel, topC);

		topC.insets = new Insets(5, 5, 5, 5);

		// Video Properties panel
		JPanel vpPanel = new JPanel();
		vpPanel.setLayout(new GridBagLayout());
		vpPanel.setBorder(newTitledLabel("Video Properties"));
		vpPanel.setPreferredSize(new Dimension(460, 115));
		GridBagConstraints vpC = new GridBagConstraints();
		vpC.insets = new Insets(5, 5, 5, 5);
		vpC.fill = GridBagConstraints.BOTH;

		JLabel fps = newSCLabel("Frame Rate (Minimum 1):");
		vpC.gridwidth = 1;
		vpC.gridx = 0;
		vpC.gridy = 0;
		vpPanel.add(fps, vpC);

		m.videoSetFrameRate = newSCTextField(Integer.toString(Main.VIDEO_FRAME_RATE), 120, 1, 10);
		m.videoSetFrameRate.setColumns(3);
		vpC.gridwidth = 1;
		vpC.gridx = 1;
		vpC.gridy = 0;
		vpPanel.add(m.videoSetFrameRate, vpC);

		JLabel frameWidth = newSCLabel("Video Width (Minumum 400):");
		vpC.gridwidth = 1;
		vpC.gridx = 0;
		vpC.gridy = 1;
		vpPanel.add(frameWidth, vpC);

		m.videoSetWidth = newSCTextField(Integer.toString(Main.WIDTH), 10000, 400, 100);
		m.videoSetWidth.setColumns(3);
		vpPanel.add(m.videoSetWidth);
		vpC.gridwidth = 1;
		vpC.gridx = 1;
		vpC.gridy = 1;
		vpPanel.add(m.videoSetWidth, vpC);

		JLabel frameHeight = newSCLabel("Video Height (Minimum 300):");
		vpC.gridwidth = 1;
		vpC.gridx = 0;
		vpC.gridy = 2;
		vpPanel.add(frameHeight, vpC);

		m.videoSetHeight = newSCTextField(Integer.toString(Main.HEIGHT), 5000, 300, 100);
		m.videoSetHeight.setColumns(3);
		vpPanel.add(m.videoSetHeight);
		vpC.gridwidth = 1;
		vpC.gridx = 1;
		vpC.gridy = 2;
		vpPanel.add(m.videoSetHeight, vpC);
		topC.gridwidth = 1;
		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 1;
		m.pane.add(vpPanel, topC);

		// IO panel
		JPanel inOutPanel = new JPanel();
		inOutPanel.setLayout(new GridBagLayout());
		inOutPanel.setBorder(newTitledLabel("Input and Output Files"));
		inOutPanel.setPreferredSize(new Dimension(460, 150));
		GridBagConstraints inOutC = new GridBagConstraints();
		inOutC.insets = new Insets(5, 5, 5, 5);
		inOutC.fill = GridBagConstraints.BOTH;

		m.setAudioButton = newSCButton("Set Input");
		m.setAudioButton.setActionCommand(MainController.SET_INPUT_WAV);
		m.setAudioButton.addActionListener(c);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 0;
		inOutPanel.add(m.setAudioButton, inOutC);

		m.audioFileNameLabel = newSCLabel("  (no file chosen)");
		m.audioFileNameLabel.setOpaque(true);
		inOutC.gridwidth = 1;
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 0;
		inOutPanel.add(m.audioFileNameLabel, inOutC);

		label = new JLabel("  Output Format:");
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 1;
		inOutPanel.add(label, inOutC);

		String[] outputMethods = { OUTPUT_MP4_TITLE, OUTPUT_SEQUENCE_TITLE };
		m.outputMethod = newSCComboBoxString(outputMethods);
		inOutC.weightx = 0.2;
		inOutC.gridwidth = 1;
		inOutC.gridx = 1;
		inOutC.gridy = 1;
		inOutPanel.add(m.outputMethod, inOutC);

		m.setOutputButton = newSCButton("Set Output");
		m.setOutputButton.setActionCommand(MainController.SET_OUTPUT_MP4);
		m.setOutputButton.addActionListener(c);
		m.setOutputButton.setEnabled(false);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 2;
		inOutPanel.add(m.setOutputButton, inOutC);

		m.outputToNameLabel = newSCLabel("  (no file or folder chosen)");
		m.outputToNameLabel.setOpaque(true);
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
		bgPanel.setBorder(newTitledLabel("Background Options"));
		bgPanel.setPreferredSize(new Dimension(460, 115));
		GridBagConstraints bgC = new GridBagConstraints();
		bgC.insets = new Insets(5, 5, 5, 5);
		bgC.fill = GridBagConstraints.BOTH;

		m.bgTypeGroup = new ButtonGroup();
		m.flatColorRb = newSCRadioButton(BG_FLAT_COLOR);
		m.flatColorRb.setActionCommand(BG_FLAT_COLOR);
		m.flatColorRb.addActionListener(c);
		m.flatColorRb.setSelected(true);
		m.bgTypeGroup.add(m.flatColorRb);
		bgC.gridx = 0;
		bgC.gridy = 0;
		bgPanel.add(m.flatColorRb, bgC);

		m.builtInImageRb = newSCRadioButton(BG_BUILT_IN_IMAGE);
		m.builtInImageRb.setActionCommand(BG_BUILT_IN_IMAGE);
		m.builtInImageRb.addActionListener(c);
		m.bgTypeGroup.add(m.builtInImageRb);
		bgC.gridx = 1;
		bgC.gridy = 0;
		bgPanel.add(m.builtInImageRb, bgC);

		m.otherImageRb = newSCRadioButton(BG_OTHER_IMAGE);
		m.otherImageRb.setActionCommand(BG_OTHER_IMAGE);
		m.otherImageRb.addActionListener(c);
		m.bgTypeGroup.add(m.otherImageRb);
		bgC.gridx = 2;
		bgC.gridy = 0;
		bgPanel.add(m.otherImageRb, bgC);

		m.imageSequenceRb = newSCRadioButton(BG_OTHER_IMAGE);
		m.imageSequenceRb.setActionCommand(BG_OTHER_IMAGE);
		m.imageSequenceRb.addActionListener(c);
		m.bgTypeGroup.add(m.imageSequenceRb);
		bgC.gridx = 3;
		bgC.gridy = 0;
		bgPanel.add(m.imageSequenceRb, bgC);

		m.bgColorPanel = new JPanel();
		label = newSCLabel("RGB values (0-255 for each color): ");
		m.bgColorPanel.add(label);

		m.bgColorRed = newSCTextField("" + BGCOLOR.getRed(), 255, 0, 5);
		m.bgColorRed.addKeyListener(new ColorFieldEar(m.bgColorRed));
		m.bgColorRed.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.bgColorRed.setColumns(3);
		m.bgColorPanel.add(m.bgColorRed);

		m.bgColorGreen = newSCTextField("" + BGCOLOR.getGreen(), 255, 0, 5);
		m.bgColorGreen.addKeyListener(new ColorFieldEar(m.bgColorGreen));
		m.bgColorGreen.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.bgColorGreen.setColumns(3);
		m.bgColorPanel.add(m.bgColorGreen);

		m.bgColorBlue = newSCTextField("" + BGCOLOR.getBlue(), 255, 0, 5);
		m.bgColorBlue.addKeyListener(new ColorFieldEar(m.bgColorBlue));
		m.bgColorBlue.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.bgColorBlue.setColumns(3);
		m.bgColorPanel.add(m.bgColorBlue);

		m.bgColorButton = new JButton("Color");

		m.bgColorButton.addActionListener(new BGColorPickerEar());

		m.bgColorPanel.add(m.bgColorButton);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 1;
		bgPanel.add(m.bgColorPanel, bgC);

		m.bgBuiltInPanel = new JPanel();
		m.bgBuiltInPanel.setVisible(false);
		label = newSCLabel("Built-in Image(1920x1080): ");
		m.bgBuiltInPanel.add(label);

		String[] builtInImages = { "blue.png", "deep.png", "golden.png", "maroon.png", "red.png", "sea.png",
				"silver.png", "violet.png" };
		m.bgBuiltIn = newSCComboBoxString(builtInImages);
		m.bgBuiltInPanel.add(m.bgBuiltIn);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 2;
		bgPanel.add(m.bgBuiltInPanel, bgC);

		m.bgOtherImagePanel = new JPanel();
		m.bgOtherImagePanel.setVisible(false);
		label = newSCLabel("Background Image, png or jpg");
		m.bgOtherImagePanel.add(label);

		m.setBgOtherImageButton = newSCButton("Set Image");
		m.setBgOtherImageButton.setActionCommand(MainController.SET_BG_OTHER_IMAGE);
		m.setBgOtherImageButton.addActionListener(c);
		m.bgOtherImagePanel.add(m.setBgOtherImageButton);

		m.bgImageNamelabel = new JLabel("  (no file chosen)  ");
		m.bgImageNamelabel.setOpaque(true);
		m.bgOtherImagePanel.add(m.bgImageNamelabel);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 3;
		bgPanel.add(m.bgOtherImagePanel, bgC);

		topC.weightx = 1;
		topC.gridx = 1;
		topC.gridy = 1;
		m.pane.add(bgPanel, topC);

		// Bars panel - search makeBarsPanel
		JPanel barsPanel = makeBarsPanel();
		topC.weightx = 1;
		topC.gridx = 1;
		topC.gridy = 2;
		m.pane.add(barsPanel, topC);

		// Render panel
		JPanel renderPanel = new JPanel();
		renderPanel.setLayout(new GridBagLayout());
		renderPanel.setBorder(newTitledLabel("Render"));
		GridBagConstraints renderC = new GridBagConstraints();
		renderC.insets = new Insets(5, 5, 5, 5);
		renderC.fill = GridBagConstraints.BOTH;

		m.renderButton = newSCButton("Render");
		m.renderButton.setEnabled(false);
		m.renderButton.setActionCommand(MainController.RENDER);
		m.renderButton.addActionListener(c);
		renderC.gridwidth = 3;
		renderC.weightx = 1;
		renderC.gridx = 0;
		renderC.gridy = 0;
		renderPanel.add(m.renderButton, renderC);

		label = newSCLabel("Preview at Frame: ");
		renderC.gridwidth = 1;
		renderC.weightx = 0;
		renderC.gridx = 3;
		renderC.gridy = 0;
		renderPanel.add(label, renderC);

		m.previewFrame = newSCTextField("1", 100, 1, 1);
		m.previewFrame.setColumns(4);
		renderC.gridx = 4;
		renderC.gridy = 0;
		renderPanel.add(m.previewFrame, renderC);

		m.previewButton = newSCButton(" Preview ");
		m.previewButton.setActionCommand(MainController.PREVIEW);
		m.previewButton.addActionListener(c);
		renderC.gridx = 5;
		renderC.gridy = 0;
		renderPanel.add(m.previewButton, renderC);

		m.progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		m.progressBar.setValue(0);
		m.progressBar.setEnabled(false);
		m.progressBar.setMinimumSize(new Dimension(30, 30));
		renderC.weightx = 1;
		renderC.gridwidth = 6;
		renderC.gridx = 0;
		renderC.gridy = 1;
		renderPanel.add(m.progressBar, renderC);

		topC.gridwidth = 2;
		topC.weightx = 1;
		topC.gridx = 0;
		topC.gridy = 3;
		m.pane.add(renderPanel, topC);

		m.fcBG = new JFileChooser();
		m.fcBG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		m.fcBG.setFileFilter(new ImageFilter());
		m.fcIn = new JFileChooser();
		m.fcIn.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		m.fcIn.setFileFilter(new InputFilter());
		m.fcOut = new JFileChooser();
		// TODO Make the file filters for output the object that decides what
		// format to output
		m.fcOut.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		m.fcOut.setFileFilter(new OutputFilter());

		// Display the window.
		frame.pack();
		// Center frame on screen
		frame.setLocation((screenWidth / 2) - (frame.getWidth() / 2), (screenHeight / 2) - (frame.getHeight() / 2));

		frame.setVisible(true);
	}

	private JLabel getHeaderLabel() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		BufferedImage header;
		try {
			header = ImageIO.read(loader.getResourceAsStream("longheader.png"));
		} catch (Exception e) {
			throw new RuntimeException("header image not found");
		}
		JLabel headerLabel = new JLabel(new ImageIcon(header));
		return headerLabel;
	}

	private JPanel makeBarsPanel() {
		JPanel barsPanel = new JPanel();
		barsPanel.setLayout(new GridBagLayout());
		barsPanel.setBorder(newTitledLabel("Bar Customization (More options coming soon!)"));
		barsPanel.setPreferredSize(new Dimension(460, 150));
		GridBagConstraints barsC = new GridBagConstraints();
		barsC.insets = new Insets(5, 5, 5, 5);
		barsC.fill = GridBagConstraints.BOTH;

		JLabel label = newSCLabel("Bar Style:");
		barsC.gridwidth = 1;
		barsC.gridx = 0;
		barsC.gridy = 0;
		barsPanel.add(label, barsC);

		label = newSCLabel("Bar Color (RGBA 0-255) ");
		barsC.gridwidth = 1;
		barsC.gridx = 0;
		barsC.gridy = 1;
		barsPanel.add(label, barsC);

		JPanel panel = new JPanel();

		m.barColorRed = newSCTextField("" + SCPURPLE.getRed(), 255, 0, 5);
		m.barColorRed.addKeyListener(new ColorFieldEar(m.barColorRed));
		m.barColorRed.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.barColorRed.setColumns(3);
		panel.add(m.barColorRed);

		m.barColorGreen = newSCTextField("" + SCPURPLE.getGreen(), 255, 0, 5);
		m.barColorGreen.addKeyListener(new ColorFieldEar(m.barColorGreen));
		m.barColorGreen.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.barColorGreen.setColumns(3);
		panel.add(m.barColorGreen);

		m.barColorBlue = newSCTextField("" + SCPURPLE.getBlue(), 255, 0, 5);
		m.barColorBlue.addKeyListener(new ColorFieldEar(m.barColorBlue));
		m.barColorBlue.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.barColorBlue.setColumns(3);
		panel.add(m.barColorBlue);

		m.barAlpha = newSCTextField("" + SCPURPLE.getAlpha(), 255, 0, 5);
		m.barAlpha.addKeyListener(new ColorFieldEar(m.barAlpha));
		m.barAlpha.addMouseWheelListener(new ColorFieldMouseWheelEar());
		m.barAlpha.setColumns(3);
		panel.add(m.barAlpha);

		barsC.gridx = 1;
		barsC.gridy = 1;
		barsPanel.add(panel, barsC);

		label = newSCLabel("Color Picker: ");
		barsC.gridwidth = 1;
		barsC.gridx = 0;
		barsC.gridy = 2;
		barsPanel.add(label, barsC);

		JPanel panelColorPicker = new JPanel();

		m.barColorButton = new JButton("Color Picker");
		m.barColorButton.addActionListener(new BarColorPickerEar());

		panelColorPicker.add(m.barColorButton);

		colorBox = new ColorBox(m);

		panelColorPicker.add(colorBox);

		barsC.gridwidth = 1;
		barsC.gridx = 1;
		barsC.gridy = 2;
		barsPanel.add(panelColorPicker, barsC);

		String[] barStyles = { MainController.BAR_STYLE_THICK_BLOCK, MainController.BAR_STYLE_OUTLINE_BLOCK,
				MainController.BAR_STYLE_THIN, MainController.BAR_STYLE_ROUND_BLOCK,
				MainController.BAR_STYLE_ROUND_OUTLINE, MainController.BAR_STYLE_DEPTH_BLOCK,
				MainController.BAR_STYLE_DEPTH_BLOCK2, MainController.BAR_STYLE_OVAL_FILLED,
				MainController.BAR_STYLE_OVAL_OUTLINE };
		m.barStyle = newSCComboBoxString(barStyles);
		barsC.gridx = 1;
		barsC.gridy = 0;
		barsPanel.add(m.barStyle, barsC);

		return barsPanel;
	}

	private TitledBorder newTitledLabel(String title) {

		javax.swing.border.Border normalBorder = (BorderFactory.createSoftBevelBorder(2));
		Font myFont = Utils.scFont(14, Font.BOLD);
		TitledBorder SCBorder = (BorderFactory.createTitledBorder(normalBorder, title, TitledBorder.LEFT,
				TitledBorder.ABOVE_TOP, myFont, Color.WHITE));

		return (SCBorder);
	}

	private JLabel newSCLabel(String text) {
		JLabel newSCLabel = new JLabel(text);
		return newSCLabel;

	}

	private JTextField newSCTextField(String text, int max, int min, int step) {
		JTextField newSCTextField = new JTextField(text);
		newSCTextField.addKeyListener(new TextFieldKeyEar(newSCTextField, max, min, step));
		newSCTextField.addMouseWheelListener(new TextFieldMouseWheelEar(newSCTextField, max, min, step));
		newSCTextField.setMinimumSize(new Dimension(46, 24));
		return (newSCTextField);
	}

	private JRadioButton newSCRadioButton(String text) {
		JRadioButton newSCRadioButton = new JRadioButton(text);
		return newSCRadioButton;
	}

	private JComboBox<String> newSCComboBoxString(String[] outputMethods) {
		JComboBox<String> newSCComboBox = new JComboBox<String>(outputMethods);
		return newSCComboBox;
	}

	private JButton newSCButton(String text) {
		JButton newSCButton = new JButton(text);
		return newSCButton;
	}

	private void customizeNimbus() {
		// General Changes
		// UIManager.put("defaultFont", Utils.scFont());
		UIManager.put("control", PANELCOLOR);
		UIManager.put("text", Color.WHITE);
		UIManager.put("nimbusLightBackground", Color.BLACK);
		UIManager.put("nimbusFocus", SCPURPLE_LT);
		UIManager.put("nimbusBase", Color.BLACK);
		UIManager.put("nimbusOrange", SCPURPLE_LT);
		UIManager.put("nimbusBorder", Color.DARK_GRAY);
		UIManager.put("background", SCPURPLE_BACK);
		UIManager.put("nimbusSelection", SCPURPLE);
		UIManager.put("textHighlight", SCPURPLE);
		UIManager.put("nimbusSelectionBackground", SCPURPLE.darker());

	}

	// Action listeners (ears... hehe)

	private class BarColorPickerEar implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Color color = new Color(Integer.parseInt(m.barColorRed.getText()),
					Integer.parseInt(m.barColorGreen.getText()), Integer.parseInt(m.barColorBlue.getText()),
					Integer.parseInt(m.barAlpha.getText()));
			Color newColor = null;
			try {
				newColor = JColorChooser.showDialog(null, "Color Picker", color);
			} catch (Exception e1) {
				System.out.println("User didn't select a color");
				return;
			}
			if (newColor == null) {
				return;
			}
			m.barColorRed.setText("" + newColor.getRed());
			m.barColorBlue.setText("" + newColor.getBlue());
			m.barColorGreen.setText("" + newColor.getGreen());
			m.barAlpha.setText("" + newColor.getAlpha());

			colorBox.updateBox();
		}
	}

	private class BGColorPickerEar implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Color color = new Color(Integer.parseInt(m.bgColorRed.getText()),
					Integer.parseInt(m.bgColorGreen.getText()), Integer.parseInt(m.bgColorBlue.getText()));
			Color newColor = null;
			try {
				newColor = JColorChooser.showDialog(null, "Color Picker", color);
			} catch (Exception e1) {
				System.out.println("User didn't select a color");
				return;
			}
			if (newColor == null) {
				return;
			}
			m.bgColorRed.setText("" + newColor.getRed());
			m.bgColorBlue.setText("" + newColor.getBlue());
			m.bgColorGreen.setText("" + newColor.getGreen());

			colorBox.updateBox();
		}
	}

	// MouseListeners

	private class TextFieldMouseWheelEar implements MouseWheelListener {

		private JTextField field;
		private int max;
		private int min;
		private int step;

		public TextFieldMouseWheelEar(JTextField field, int max, int min, int step) {
			this.field = field;
			this.max = max;
			this.min = min;
			this.step = step;
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) {
				int value = Integer.parseInt(field.getText());

				value += step;

				if (value <= max) {
					field.setText("" + value);
				}
			} else if (e.getWheelRotation() > 0) {
				int value = Integer.parseInt(field.getText());

				value -= step;

				if (value >= min) {
					field.setText("" + value);
				}
			}
		}

	}

	private class ColorFieldMouseWheelEar implements MouseWheelListener {

		public void mouseWheelMoved(MouseWheelEvent e) {
			colorBox.updateBox();
		}
	}

	// KeyListeners

	private class TextFieldKeyEar implements KeyListener {
		private JTextField field;
		private int max;
		private int min;
		private int step;

		public TextFieldKeyEar(JTextField field, int max, int min, int step) {
			this.field = field;
			this.max = max;
			this.min = min;
			this.step = step;
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				int value = Integer.parseInt(field.getText());

				value += step;

				if (value <= max) {
					field.setText("" + value);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				int value = Integer.parseInt(field.getText());

				value -= step;

				if (value >= min) {
					field.setText("" + value);
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	private class ColorFieldEar implements KeyListener {
		JTextField field;

		public ColorFieldEar(JTextField field) {
			this.field = field;
		}

		public void keyTyped(KeyEvent e) {

			if (!field.getText().equals("")) {

				int value;

				try {
					value = Integer.parseInt(field.getText());
				} catch (Exception e1) {
					value = 0;
				}

				if (value > 255) {
					field.setText("" + 255);
				} else if (value < 0) {
					field.setText("" + 0);
				}
			}
			colorBox.updateBox();
		}

		public void keyPressed(KeyEvent e) {
			colorBox.updateBox();
		}

		public void keyReleased(KeyEvent e) {
		}

	}
}
