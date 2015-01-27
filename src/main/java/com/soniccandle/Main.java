package com.soniccandle;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.soniccandle.model.ImageSeqVideoOutputter;
import com.soniccandle.model.VideoOutputter;
import com.soniccandle.model.XuggleVideoOutputter;

public class Main implements ActionListener {

	private static final String BG_OTHER_IMAGE = "other image";
	private static final String BG_BUILT_IN_IMAGE = "built-in image";
	private static final String BG_FLAT_COLOR = "flat color";
	private static final int VIDEO_FRAME_RATE = 30;
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String SET_INPUT_WAV = "SET_INPUT_WAV";
	private static final String SET_OUTPUT_MP4 = "SET_OUTPUT_MP4";
	private static final String RENDER = "RENDER";
	private static final String OUTPUT_MP4_TITLE = "mp4 file";
	private static final String OUTPUT_SEQUENCE_TITLE = "image sequence (select a folder for output)";
	private static final String SET_BG_OTHER_IMAGE = "SET_BG_OTHER_IMAGE";

	public static void main(String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main main = new Main();
				main.createAndShowGUI();
			}
		});
	}

	private JFileChooser fc;
	private Container pane;
	private JLabel audioFileNameLabel;
	private File audioFile;
	private File outputTo;
	private JLabel outputToNameLabel;
	private JButton renderButton;
	private JProgressBar progressBar;
	private JComboBox<String> outputMethod;
	private JTextField bgColorGreen;
	private JTextField bgColorBlue;
	private JTextField bgColorRed;
	private JPanel bgColorPanel;
	private JPanel bgBuiltInPanel;
	private JPanel bgOtherImagePanel;
	private File backgroundImageFile;
	private JLabel bgImageNamelabel;
	private ButtonGroup bgTypeGroup;
	private JComboBox<String> bgBuiltIn;
	private JRadioButton flatColorRb;
	private JRadioButton builtInIimageRb;
	private JRadioButton otherImageRb;

	private void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("sonic candle");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton button;
		JLabel label;

		JLabel headerLabel = getHeaderLabel();
		c.gridwidth = 1;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(headerLabel, c);

		c.insets = new Insets(5, 5, 5, 5);

		JPanel inOutPanel = new JPanel();
		inOutPanel.setLayout(new GridBagLayout());
		inOutPanel.setBorder(BorderFactory.createTitledBorder("input and output files"));
		GridBagConstraints inOutC = new GridBagConstraints();
		inOutC.insets = new Insets(5, 5, 5, 5);
		inOutC.fill = GridBagConstraints.BOTH;

		button = new JButton("set input wav");
		button.setActionCommand(SET_INPUT_WAV);
		button.addActionListener(this);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 0;
		inOutPanel.add(button, inOutC);

		audioFileNameLabel = new JLabel("  (no file chosen)");
		audioFileNameLabel.setOpaque(true);
		audioFileNameLabel.setBackground(new Color(255, 255, 255));
		inOutC.gridwidth = 1;
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 0;
		inOutPanel.add(audioFileNameLabel, inOutC);

		label = new JLabel("  output format:");
		label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 1;
		inOutPanel.add(label, inOutC);

		String[] outputMethods = {OUTPUT_MP4_TITLE, OUTPUT_SEQUENCE_TITLE};
		outputMethod = new JComboBox<String>(outputMethods);
		inOutC.weightx = 0.2;
		inOutC.gridwidth = 1;
		inOutC.gridx = 1;
		inOutC.gridy = 1;
		inOutPanel.add(outputMethod, inOutC);

		button = new JButton("set output location");
		button.setActionCommand(SET_OUTPUT_MP4);
		button.addActionListener(this);
		inOutC.weightx = 0.2;
		inOutC.gridx = 0;
		inOutC.gridy = 2;
		inOutPanel.add(button, inOutC);

		outputToNameLabel = new JLabel("  (no file or folder chosen)");
		outputToNameLabel.setOpaque(true);
		outputToNameLabel.setBackground(new Color(255, 255, 255));
		inOutC.weightx = 0.8;
		inOutC.gridx = 1;
		inOutC.gridy = 2;
		inOutPanel.add(outputToNameLabel, inOutC);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(inOutPanel, c);


		JPanel bgPanel = new JPanel();
		bgPanel.setLayout(new GridBagLayout());
		bgPanel.setBorder(BorderFactory.createTitledBorder("background"));
		GridBagConstraints bgC = new GridBagConstraints();
		bgC.insets = new Insets(5, 5, 5, 5);
		bgC.fill = GridBagConstraints.BOTH;


		bgTypeGroup = new ButtonGroup();
		flatColorRb = new JRadioButton(BG_FLAT_COLOR);
		flatColorRb.setActionCommand(BG_FLAT_COLOR);
		flatColorRb.addActionListener(this);
		flatColorRb.setSelected(true);
		bgTypeGroup.add(flatColorRb);
		bgC.gridx = 0;
		bgC.gridy = 0;
		bgPanel.add(flatColorRb, bgC);

		builtInIimageRb = new JRadioButton(BG_BUILT_IN_IMAGE);
		builtInIimageRb.setActionCommand(BG_BUILT_IN_IMAGE);
		builtInIimageRb.addActionListener(this);
		bgTypeGroup.add(builtInIimageRb);
		bgC.gridx = 1;
		bgC.gridy = 0;
		bgPanel.add(builtInIimageRb, bgC);

		otherImageRb = new JRadioButton(BG_OTHER_IMAGE);
		otherImageRb.setActionCommand(BG_OTHER_IMAGE);
		otherImageRb.addActionListener(this);
		bgTypeGroup.add(otherImageRb);
		bgC.gridx = 2;
		bgC.gridy = 0;
		bgPanel.add(otherImageRb, bgC);

		bgColorPanel = new JPanel();
		label = new JLabel("RGB values (0-255 for each color): ");
		bgColorPanel.add(label);

		bgColorRed = new JTextField("0");
		bgColorRed.setColumns(3);
		bgColorPanel.add(bgColorRed);

		bgColorGreen = new JTextField("0");
		bgColorGreen.setColumns(3);
		bgColorPanel.add(bgColorGreen);

		bgColorBlue = new JTextField("0");
		bgColorBlue.setColumns(3);
		bgColorPanel.add(bgColorBlue);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 1;
		bgPanel.add(bgColorPanel, bgC);

		bgBuiltInPanel = new JPanel();
		bgBuiltInPanel.setVisible(false);
		label = new JLabel("built-in image: ");
		bgBuiltInPanel.add(label);

		String[] builtInImages = {"blue_grid.png", "blue_soft_grid.png", "blue_soft.png", "blue.png", "dark_gray.png", "purple.png",
				"rainbow_bright.png", "rainbow_faint.png", "rainbow_mesh.png", "rainbow.png", "red_grid.png", "red.png"};
		bgBuiltIn = new JComboBox<String>(builtInImages);
		bgBuiltInPanel.add(bgBuiltIn);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 2;
		bgPanel.add(bgBuiltInPanel, bgC);

		bgOtherImagePanel = new JPanel();
		bgOtherImagePanel.setVisible(false);
		label = new JLabel("background image, png or jpg, "+WIDTH+"x"+HEIGHT);
		bgOtherImagePanel.add(label);

		button = new JButton("set");
		button.setActionCommand(SET_BG_OTHER_IMAGE);
		button.addActionListener(this);
		bgOtherImagePanel.add(button);

		bgImageNamelabel = new JLabel("  (no file chosen)  ");
		bgImageNamelabel.setOpaque(true);
		bgImageNamelabel.setBackground(new Color(255, 255, 255));
		bgOtherImagePanel.add(bgImageNamelabel);

		bgC.gridwidth = 3;
		bgC.gridx = 0;
		bgC.gridy = 3;
		bgPanel.add(bgOtherImagePanel, bgC);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(bgPanel, c);

		JPanel renderPanel = new JPanel();
		renderPanel.setLayout(new GridBagLayout());
		renderPanel.setBorder(BorderFactory.createTitledBorder("render"));
		GridBagConstraints renderC = new GridBagConstraints();
		renderC.insets = new Insets(5, 5, 5, 5);
		renderC.fill = GridBagConstraints.BOTH;

		renderButton = new JButton("render");
		renderButton.setEnabled(false);
		renderButton.setActionCommand(RENDER);
		renderButton.addActionListener(this);
		c.gridwidth = 2;
		renderC.weightx = 1;
		renderC.gridx = 0;
		renderC.gridy = 0;
		renderPanel.add(renderButton, renderC);


		progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		progressBar.setValue(0);
		progressBar.setEnabled(false);
		progressBar.setMinimumSize(new Dimension(30, 30));
		renderC.weightx = 1;
		renderC.gridx = 0;
		renderC.gridy = 1;
		renderPanel.add(progressBar, renderC);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(renderPanel, c);

		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


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

	public void actionPerformed(ActionEvent e) {
		if (SET_INPUT_WAV.equals(e.getActionCommand())) {
			int returnVal = fc.showOpenDialog(pane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				audioFile = fc.getSelectedFile();
				audioFileNameLabel.setText(audioFile.getName());
			}
		}
		if (SET_OUTPUT_MP4.equals(e.getActionCommand())) {
			int returnVal = fc.showDialog(pane, "set output");

			if (returnVal != JFileChooser.APPROVE_OPTION) { // they hit cancel
				return;
			}

			if (!fc.getSelectedFile().getName().endsWith(".mp4") && (outputMethod.getSelectedItem().equals(OUTPUT_MP4_TITLE))) {
				File addedMp4 = new File(fc.getSelectedFile().getParent(), fc.getSelectedFile().getName() + ".mp4");
				fc.setSelectedFile(addedMp4);
			}

			if (fc.getSelectedFile().exists() && !fc.getSelectedFile().isDirectory()) {
				JOptionPane.showMessageDialog(pane, "That file exists already: if you render it will be overwritten.");
			}
			outputTo = fc.getSelectedFile();
			outputToNameLabel.setText(outputTo.getName());

		}

		if (audioFile != null && outputTo != null) {
			renderButton.setEnabled(true);
		} else {
			renderButton.setEnabled(false);
		}

		if (BG_FLAT_COLOR.equals(e.getActionCommand())) {
			bgColorPanel.setVisible(true);
			bgBuiltInPanel.setVisible(false);
			bgOtherImagePanel.setVisible(false);
		}
		else if (BG_BUILT_IN_IMAGE.equals(e.getActionCommand())) {
			bgColorPanel.setVisible(false);
			bgBuiltInPanel.setVisible(true);
			bgOtherImagePanel.setVisible(false);
		}
		else if (BG_OTHER_IMAGE.equals(e.getActionCommand())) {
			bgColorPanel.setVisible(false);
			bgBuiltInPanel.setVisible(false);
			bgOtherImagePanel.setVisible(true);
		}

		if (SET_BG_OTHER_IMAGE.equals(e.getActionCommand())) {
			int returnVal = fc.showDialog(pane, "set background image");

			if (returnVal != JFileChooser.APPROVE_OPTION) { // they hit cancel
				return;
			}
			backgroundImageFile = fc.getSelectedFile();
			bgImageNamelabel.setText(" "+backgroundImageFile.getName());
		}

		if (RENDER.equals(e.getActionCommand())) {
			if (outputTo.exists() && !outputTo.isDirectory()) {
				int result = JOptionPane.showConfirmDialog(pane, "Are you sure you want to over-write "+outputTo.getName()+"?");
				if (result != JOptionPane.OK_OPTION) {
					return;
				}
			}
			if (new File(outputTo, "frame_0.png").exists()) {
				int result = JOptionPane.showConfirmDialog(pane, "Your output folder has frames in it already: overwrite?");
				if (result != JOptionPane.OK_OPTION) {
					return;
				}
			}
			VideoOutputter outputter = null;
			if (outputMethod.getSelectedItem().equals(OUTPUT_MP4_TITLE)) {
				outputter = new XuggleVideoOutputter(audioFile, outputTo);
			} else {
				if (!outputTo.isDirectory()) {
					JOptionPane.showMessageDialog(pane, "You must choose an output folder if you want to render an image sequence.");
					return;
				}
				outputter = new ImageSeqVideoOutputter(audioFile, outputTo);
			}

			RenderRunnable renderRunnable = new RenderRunnable();
			renderRunnable.audioFile = audioFile;
			renderRunnable.outputTo = outputTo;
			renderRunnable.outputter = outputter;
			renderRunnable.videoFrameRate = VIDEO_FRAME_RATE;
			renderRunnable.width = WIDTH;
			renderRunnable.height = HEIGHT;

			renderRunnable.outputter = outputter;
			renderRunnable.outputter.width = renderRunnable.width;
			renderRunnable.outputter.height = renderRunnable.height;
			renderRunnable.outputter.frameRate = renderRunnable.videoFrameRate;

			if (flatColorRb.isSelected()) {
				BufferedImage backgroundImage = new BufferedImage(renderRunnable.width, renderRunnable.height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D backgroundImageG = backgroundImage.createGraphics();
				backgroundImageG.setColor(new Color(Integer.parseInt(bgColorRed.getText()), 
						Integer.parseInt(bgColorGreen.getText()), 
						Integer.parseInt(bgColorBlue.getText())));
				backgroundImageG.fillRect(0, 0, renderRunnable.width, renderRunnable.height);
				renderRunnable.backgroundImage = backgroundImage;
			}
			else if (builtInIimageRb.isSelected()) {
	    		ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    		BufferedImage backgroundImage = null;
	    		try {
					backgroundImage = ImageIO.read(loader.getResourceAsStream((String)bgBuiltIn.getSelectedItem()));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(pane, "Aah!  Could not read that built-in image.  Our fault!!  Sorry!");
					return;
				}
	    		renderRunnable.backgroundImage = backgroundImage;
			}
			else if (otherImageRb.isSelected()) {
				if (backgroundImageFile == null) {
					JOptionPane.showMessageDialog(pane, "You didn't select an image, silly!");
					return;
				}
				if (!backgroundImageFile.exists()) {
					JOptionPane.showMessageDialog(pane, "That image file does not exist, goofball!  =)");
					return;
				}
				BufferedImage backgroundImage = null;
	    		try {
					backgroundImage = ImageIO.read(backgroundImageFile);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(pane, "The background image given could not be read");
					return;
				}
	    		renderRunnable.backgroundImage = backgroundImage;
			}

			renderRunnable.progressBar = progressBar;
			Thread t = new Thread(renderRunnable);
			progressBar.setEnabled(true);
			JOptionPane.showMessageDialog(pane, "Takes a sec for progress bar to show: give it a moment.");
			t.start();
		}
	} 


}
