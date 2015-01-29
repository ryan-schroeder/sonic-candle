package com.soniccandle.model;

import java.awt.Container;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MainModel {
	public JFileChooser fc;
	public Container pane;
	public JLabel audioFileNameLabel;
	public File audioFile;
	public File outputTo;
	public JLabel outputToNameLabel;
	public JButton renderButton;
	public JProgressBar progressBar;
	public JComboBox<String> outputMethod;
	public JTextField bgColorGreen;
	public JTextField bgColorBlue;
	public JTextField bgColorRed;
	public JPanel bgColorPanel;
	public JPanel bgBuiltInPanel;
	public JPanel bgOtherImagePanel;
	public File backgroundImageFile;
	public JLabel bgImageNamelabel;
	public ButtonGroup bgTypeGroup;
	public JComboBox<String> bgBuiltIn;
	public JRadioButton flatColorRb;
	public JRadioButton builtInIimageRb;
	public JRadioButton otherImageRb;
}
