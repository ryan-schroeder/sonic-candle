package com.soniccandle.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.soniccandle.Main;
import com.soniccandle.model.ImageSeqVideoOutputter;
import com.soniccandle.model.MainModel;
import com.soniccandle.model.VideoOutputter;
import com.soniccandle.model.XuggleVideoOutputter;
import com.soniccandle.view.MainView;

public class MainController implements ActionListener {
	
	public static final String SET_INPUT_WAV = "SET_INPUT_WAV";
	public static final String SET_OUTPUT_MP4 = "SET_OUTPUT_MP4";
	public static final String SET_BG_OTHER_IMAGE = "SET_BG_OTHER_IMAGE";
	public static final String RENDER = "RENDER";
	
	public MainModel m;
	public MainView v;
	
	public void actionPerformed(ActionEvent e) {
		if (SET_INPUT_WAV.equals(e.getActionCommand())) {
			int returnVal = m.fc.showOpenDialog(m.pane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				m.audioFile = m.fc.getSelectedFile();
				m.audioFileNameLabel.setText(m.audioFile.getName());
			}
		}
		if (SET_OUTPUT_MP4.equals(e.getActionCommand())) {
			int returnVal = m.fc.showDialog(m.pane, "set output");

			if (returnVal != JFileChooser.APPROVE_OPTION) { // they hit cancel
				return;
			}

			if (!m.fc.getSelectedFile().getName().endsWith(".mp4") && (m.outputMethod.getSelectedItem().equals(MainView.OUTPUT_MP4_TITLE))) {
				File addedMp4 = new File(m.fc.getSelectedFile().getParent(), m.fc.getSelectedFile().getName() + ".mp4");
				m.fc.setSelectedFile(addedMp4);
			}

			if (m.fc.getSelectedFile().exists() && !m.fc.getSelectedFile().isDirectory()) {
				JOptionPane.showMessageDialog(m.pane, "That file exists already: if you render it will be overwritten.");
			}
			m.outputTo = m.fc.getSelectedFile();
			m.outputToNameLabel.setText(m.outputTo.getName());

		}

		if (m.audioFile != null && m.outputTo != null) {
			m.renderButton.setEnabled(true);
		} else {
			m.renderButton.setEnabled(false);
		}

		if (MainView.BG_FLAT_COLOR.equals(e.getActionCommand())) {
			m.bgColorPanel.setVisible(true);
			m.bgBuiltInPanel.setVisible(false);
			m.bgOtherImagePanel.setVisible(false);
		}
		else if (MainView.BG_BUILT_IN_IMAGE.equals(e.getActionCommand())) {
			m.bgColorPanel.setVisible(false);
			m.bgBuiltInPanel.setVisible(true);
			m.bgOtherImagePanel.setVisible(false);
		}
		else if (MainView.BG_OTHER_IMAGE.equals(e.getActionCommand())) {
			m.bgColorPanel.setVisible(false);
			m.bgBuiltInPanel.setVisible(false);
			m.bgOtherImagePanel.setVisible(true);
		}

		if (SET_BG_OTHER_IMAGE.equals(e.getActionCommand())) {
			int returnVal = m.fc.showDialog(m.pane, "set background image");

			if (returnVal != JFileChooser.APPROVE_OPTION) { // they hit cancel
				return;
			}
			m.backgroundImageFile = m.fc.getSelectedFile();
			m.bgImageNamelabel.setText(" "+m.backgroundImageFile.getName());
		}

		if (RENDER.equals(e.getActionCommand())) {
			if (m.outputTo.exists() && !m.outputTo.isDirectory()) {
				int result = JOptionPane.showConfirmDialog(m.pane, "Are you sure you want to over-write "+m.outputTo.getName()+"?");
				if (result != JOptionPane.OK_OPTION) {
					return;
				}
			}
			if (new File(m.outputTo, "frame_0.png").exists()) {
				int result = JOptionPane.showConfirmDialog(m.pane, "Your output folder has frames in it already: overwrite?");
				if (result != JOptionPane.OK_OPTION) {
					return;
				}
			}
			VideoOutputter outputter = null;
			if (m.outputMethod.getSelectedItem().equals(MainView.OUTPUT_MP4_TITLE)) {
				outputter = new XuggleVideoOutputter(m.audioFile, m.outputTo);
			} else {
				if (!m.outputTo.isDirectory()) {
					JOptionPane.showMessageDialog(m.pane, "You must choose an output folder if you want to render an image sequence.");
					return;
				}
				outputter = new ImageSeqVideoOutputter(m.audioFile, m.outputTo);
			}

			RenderSwingWorker renderSwingWorker = new RenderSwingWorker();
			renderSwingWorker.audioFile = m.audioFile;
			renderSwingWorker.outputTo = m.outputTo;
			renderSwingWorker.outputter = outputter;
			renderSwingWorker.videoFrameRate = Main.VIDEO_FRAME_RATE;
			renderSwingWorker.width = Main.WIDTH;
			renderSwingWorker.height = Main.HEIGHT;

			renderSwingWorker.outputter = outputter;
			renderSwingWorker.outputter.width = renderSwingWorker.width;
			renderSwingWorker.outputter.height = renderSwingWorker.height;
			renderSwingWorker.outputter.frameRate = renderSwingWorker.videoFrameRate;
			renderSwingWorker.progressBar = m.progressBar;

			if (m.flatColorRb.isSelected()) {
				BufferedImage backgroundImage = new BufferedImage(renderSwingWorker.width, renderSwingWorker.height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D backgroundImageG = backgroundImage.createGraphics();
				backgroundImageG.setColor(new Color(Integer.parseInt(m.bgColorRed.getText()), 
						Integer.parseInt(m.bgColorGreen.getText()), 
						Integer.parseInt(m.bgColorBlue.getText())));
				backgroundImageG.fillRect(0, 0, renderSwingWorker.width, renderSwingWorker.height);
				renderSwingWorker.backgroundImage = backgroundImage;
			}
			else if (m.builtInIimageRb.isSelected()) {
	    		ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    		BufferedImage backgroundImage = null;
	    		try {
					backgroundImage = ImageIO.read(loader.getResourceAsStream((String)m.bgBuiltIn.getSelectedItem()));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(m.pane, "Aah!  Could not read that built-in image.  Our fault!!  Sorry!");
					return;
				}
	    		renderSwingWorker.backgroundImage = backgroundImage;
			}
			else if (m.otherImageRb.isSelected()) {
				if (m.backgroundImageFile == null) {
					JOptionPane.showMessageDialog(m.pane, "You didn't select an image, silly!");
					return;
				}
				if (!m.backgroundImageFile.exists()) {
					JOptionPane.showMessageDialog(m.pane, "That image file does not exist, goofball!  =)");
					return;
				}
				BufferedImage backgroundImage = null;
	    		try {
					backgroundImage = ImageIO.read(m.backgroundImageFile);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(m.pane, "The background image given could not be read");
					return;
				}
	    		renderSwingWorker.backgroundImage = backgroundImage;
			}

			m.progressBar.setEnabled(true);
			renderSwingWorker.execute();
			JOptionPane.showMessageDialog(m.pane, "Takes a sec for progress bar to show: give it a moment.");
		}
	} 
}
