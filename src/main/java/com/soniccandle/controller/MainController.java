package com.soniccandle.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;//http://docs.oracle.com/javase/tutorial/2d/advanced/quality.html -- set in the specific bar drawing class
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.soniccandle.Main;
import com.soniccandle.model.ImageSeqVideoOutputter;
import com.soniccandle.model.MainModel;
import com.soniccandle.model.RenderSettings;
import com.soniccandle.model.SimpleRenderer;
import com.soniccandle.model.VideoOutputter;
import com.soniccandle.model.XuggleVideoOutputter;
import com.soniccandle.view.MainView;

public class MainController implements ActionListener {

	public static final String SET_INPUT_WAV = "SET_INPUT_WAV";
	public static final String SET_OUTPUT_MP4 = "SET_OUTPUT_MP4";
	public static final String SET_BG_OTHER_IMAGE = "SET_BG_OTHER_IMAGE";
	public static final String RENDER = "RENDER";
	public static final String CANCEL_RENDER = "CANCEL_RENDER";
	public static final String BAR_STYLE_THICK_BROCK = "01 thick block";
	public static final String BAR_STYLE_OUTLINE_BLOCK = "02 outline block";
	public static final String BAR_STYLE_THIN = "03 thin";
	public static final String BAR_STYLE_ROUND_BLOCK = "04 round filled";
	public static final String BAR_STYLE_ROUND_OUTLINE = "05 round ouline";
	public static final String PREVIEW = "PREVIEW";

	public MainModel m;
	public MainView v;
	private RenderSwingWorker renderSwingWorker;

	public void actionPerformed(ActionEvent e) {

		// set frame rate
		if (Integer.parseInt(m.videoSetFrameRate.getText()) > 0)// set minimum
																// to 1
		{
			Main.setVideoFrameRate(Integer.parseInt(m.videoSetFrameRate
					.getText()));
		} else {
			JOptionPane.showMessageDialog(m.pane,
					"Please enter a real Frame Rate!");
			return;
		}

		// set video width
		if (Integer.parseInt(m.videoSetWidth.getText()) > 399)// set minimum to
																// 400
		{
			Main.setVideoWidth(Integer.parseInt(m.videoSetWidth.getText()));
		} else {
			JOptionPane.showMessageDialog(m.pane, "Please enter a real Width!");
			return;
		}

		// set video height
		if (Integer.parseInt(m.videoSetHeight.getText()) > 299)// set minimum to
																// 300
		{
			Main.setVideoHeight(Integer.parseInt(m.videoSetHeight.getText()));
		} else {
			JOptionPane
					.showMessageDialog(m.pane, "Please enter a real Height!");
			return;
		}

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

			if (!m.fc.getSelectedFile().getName().endsWith(".mp4")
					&& (m.outputMethod.getSelectedItem()
							.equals(MainView.OUTPUT_MP4_TITLE))) {
				File addedMp4 = new File(m.fc.getSelectedFile().getParent(),
						m.fc.getSelectedFile().getName() + ".mp4");
				m.fc.setSelectedFile(addedMp4);
			}

			if (m.fc.getSelectedFile().exists()
					&& !m.fc.getSelectedFile().isDirectory()) {
				JOptionPane
						.showMessageDialog(m.pane,
								"That file exists already: if you render it will be overwritten.");
			}
			m.outputTo = m.fc.getSelectedFile();
			m.outputToNameLabel.setText(m.outputTo.getName());

		}

		allowRenderIfReady();

		if (MainView.BG_FLAT_COLOR.equals(e.getActionCommand())) {
			m.bgColorPanel.setVisible(true);
			m.bgBuiltInPanel.setVisible(false);
			m.bgOtherImagePanel.setVisible(false);
		} else if (MainView.BG_BUILT_IN_IMAGE.equals(e.getActionCommand())) {
			m.bgColorPanel.setVisible(false);
			m.bgBuiltInPanel.setVisible(true);
			m.bgOtherImagePanel.setVisible(false);
		} else if (MainView.BG_OTHER_IMAGE.equals(e.getActionCommand())) {
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
			m.bgImageNamelabel.setText(" " + m.backgroundImageFile.getName());
		}
		
		if (PREVIEW.equals(e.getActionCommand())) {
			RenderSettings rs;
			try {
				rs = extractRenderSettings(false);
				SimpleRenderer renderer;
				renderer = rs.bakeSimpleRenderer(m);
				int previewFrameInt = Integer.parseInt(m.previewFrame.getText());
				System.out.println("previewing frame: " + previewFrameInt);
				// this is a temp fix.  Eventually, we should fast-forward inside the file to save time.
				Integer currentFrame = 0; 
				while (currentFrame < previewFrameInt) {
					renderer.renderVFrame(currentFrame);
					currentFrame ++;
				}
				BufferedImage preview = renderer.renderVFrame(currentFrame);
				JLabel previewLabel = new JLabel(new ImageIcon(preview));
				JFrame previewFrame = new JFrame("Preview");
				previewFrame.setResizable(false);
				previewFrame.getContentPane().add(previewLabel);
				previewFrame.pack();
				previewFrame.setVisible(true);
			} catch (Exception ex) {
				if (ex.getMessage() != null) {
					JOptionPane.showMessageDialog(m.pane, ex.getMessage());
				}
			}
			return;
	    }

		if (RENDER.equals(e.getActionCommand())) {
			renderSwingWorker = new RenderSwingWorker();
			renderSwingWorker.c = this;
			renderSwingWorker.m = m;
			RenderSettings rs;
			try {
				rs = extractRenderSettings(true);
			} catch (Exception ex) {
				return;
			}
			renderSwingWorker.rs = rs;

			m.progressBar.setEnabled(true);
			lockWhileRendering();
			renderSwingWorker.execute();
			JOptionPane.showMessageDialog(m.pane,
					"Takes a sec for progress bar to show: give it a moment.");
		}

		if (CANCEL_RENDER.equals(e.getActionCommand())) {
			renderSwingWorker.cancel(true);
		}
	}

	private RenderSettings extractRenderSettings(boolean getOutputter) throws Exception {
		RenderSettings rs = new RenderSettings();
		if (m.audioFile == null) {
			JOptionPane.showMessageDialog(null,
					"That audio file does not exist.");
			throw new Exception();
		} 
		if (!m.audioFile.exists()) {
			JOptionPane.showMessageDialog(null,
					"That audio file does not exist.");
			throw new Exception();
		} 
		VideoOutputter outputter = null;
		if (getOutputter) {
			if (m.outputTo.exists() && !m.outputTo.isDirectory()) {
				int result = JOptionPane.showConfirmDialog(
						m.pane,
						"Are you sure you want to over-write "
								+ m.outputTo.getName() + "?");
				if (result != JOptionPane.OK_OPTION) {
					throw new Exception();
				}
			}
			if (new File(m.outputTo, "frame_0.png").exists()) {
				int result = JOptionPane
						.showConfirmDialog(m.pane,
								"Your output folder has frames in it already: overwrite?");
				if (result != JOptionPane.OK_OPTION) {
					throw new Exception();
				}
			}

			if (m.outputMethod.getSelectedItem().equals(
					MainView.OUTPUT_MP4_TITLE)) {
				outputter = new XuggleVideoOutputter(m.audioFile, m.outputTo);
			} else {
				if (!m.outputTo.isDirectory()) {
					JOptionPane
							.showMessageDialog(m.pane,
									"You must choose an output folder if you want to render an image sequence.");
					throw new Exception();
				}
				outputter = new ImageSeqVideoOutputter(m.audioFile, m.outputTo);
			}
		}
		
		rs.audioFile = m.audioFile;
		rs.outputTo = m.outputTo;
		rs.outputter = outputter;
		rs.videoFrameRate = Main.getVideoFrameRate();
		rs.width = Main.getVideoWidth();
		rs.height = Main.getVideoHeight();
		
		if (getOutputter) {
			rs.outputter = outputter;
			rs.outputter.width = rs.width;
			rs.outputter.height = rs.height;
			rs.outputter.frameRate = rs.videoFrameRate;
		}

		if (m.flatColorRb.isSelected()) {
			BufferedImage backgroundImage = new BufferedImage(
					rs.width, rs.height,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D backgroundImageG = backgroundImage.createGraphics();
			backgroundImageG.setColor(new Color(Integer
					.parseInt(m.bgColorRed.getText()), Integer
					.parseInt(m.bgColorGreen.getText()), Integer
					.parseInt(m.bgColorBlue.getText())));
			backgroundImageG.fillRect(0, 0, rs.width,
					rs.height);
			rs.backgroundImage = backgroundImage;
		} else if (m.builtInImageRb.isSelected()) {
			ClassLoader loader = Thread.currentThread()
					.getContextClassLoader();
			BufferedImage backgroundImage = null;
			BufferedImage resizedBackgroundImage = null;
			Graphics2D g = null;
			try {
				backgroundImage = ImageIO.read(loader
						.getResourceAsStream("teneighty/"
								+ (String) m.bgBuiltIn.getSelectedItem()));

				// scale image --
				// http://www.mkyong.com/java/how-to-resize-an-image-in-java/
				int type = backgroundImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
						: backgroundImage.getType();
				resizedBackgroundImage = new BufferedImage(
						Main.getVideoWidth(), Main.getVideoHeight(), type);
				g = resizedBackgroundImage.createGraphics();
				g.drawImage(backgroundImage, 0, 0, Main.getVideoWidth(),
						Main.getVideoHeight(), null);

				g.setComposite(AlphaComposite.Src);

				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

			} catch (IOException e1) {
				JOptionPane
						.showMessageDialog(m.pane,
								"Aah!  Could not read that built-in image.  Our fault!!  Sorry!");
				throw new Exception();
			}
			rs.backgroundImage = resizedBackgroundImage;
			g.dispose();
		} else if (m.otherImageRb.isSelected()) {
			if (m.backgroundImageFile == null) {
				JOptionPane.showMessageDialog(m.pane,
						"You didn't select an image, silly!");
				throw new Exception();
			}
			if (!m.backgroundImageFile.exists()) {
				JOptionPane.showMessageDialog(m.pane,
						"That image file does not exist, goofball!  =)");
				throw new Exception();
			}
			BufferedImage backgroundImage = null;
			BufferedImage resizedBackgroundImage = null;
			Graphics2D g = null;
			try {
				backgroundImage = ImageIO.read(m.backgroundImageFile);
				// scale the bg image
				int type = backgroundImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
						: backgroundImage.getType();
				resizedBackgroundImage = new BufferedImage(
						Main.getVideoWidth(), Main.getVideoHeight(), type);
				g = resizedBackgroundImage.createGraphics();
				g.drawImage(backgroundImage, 0, 0, Main.getVideoWidth(),
						Main.getVideoHeight(), null);

				g.setComposite(AlphaComposite.Src);

				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

			} catch (IOException e1) {
				JOptionPane.showMessageDialog(m.pane,
						"The background image given could not be read");
				throw new Exception();
			}
			rs.backgroundImage = resizedBackgroundImage;
			g.dispose();

		}
		return rs;
	}

	// exposed for unit tests only!
	public void allowRenderIfReady() {
		if (m.audioFile != null && m.outputTo != null) {
			m.renderButton.setEnabled(true);
		} else {
			m.renderButton.setEnabled(false);
		}
	}

	public void lockWhileRendering() {
		m.setAudioButton.setEnabled(false);
		m.setOutputButton.setEnabled(false);
		m.renderButton.setText("cancel render");
		m.renderButton.setActionCommand(CANCEL_RENDER);
		m.outputMethod.setEnabled(false);
		m.bgColorRed.setEnabled(false);
		m.bgColorGreen.setEnabled(false);
		m.bgColorBlue.setEnabled(false);
		m.flatColorRb.setEnabled(false);
		m.builtInImageRb.setEnabled(false);
		m.bgBuiltIn.setEnabled(false);
		m.otherImageRb.setEnabled(false);
		m.setBgOtherImageButton.setEnabled(false);
		m.barStyle.setEnabled(false);
		m.barColorRed.setEnabled(false);
		m.barColorGreen.setEnabled(false);
		m.barColorBlue.setEnabled(false);
		m.videoSetFrameRate.setEnabled(false);
		m.videoSetHeight.setEnabled(false);
		m.videoSetWidth.setEnabled(false);
		m.barAlpha.setEnabled(false);
		m.previewFrame.setEnabled(false);
		m.previewButton.setEnabled(false);

	}

	public void unlockAfterRender() {
		m.setAudioButton.setEnabled(true);
		m.setOutputButton.setEnabled(true);
		m.renderButton.setText("render");
		m.renderButton.setActionCommand(RENDER);
		m.outputMethod.setEnabled(true);
		m.bgColorRed.setEnabled(true);
		m.bgColorGreen.setEnabled(true);
		m.bgColorBlue.setEnabled(true);
		m.flatColorRb.setEnabled(true);
		m.builtInImageRb.setEnabled(true);
		m.bgBuiltIn.setEnabled(true);
		m.otherImageRb.setEnabled(true);
		m.setBgOtherImageButton.setEnabled(true);
		m.barStyle.setEnabled(true);
		m.barColorRed.setEnabled(true);
		m.barColorGreen.setEnabled(true);
		m.barColorBlue.setEnabled(true);
		m.videoSetFrameRate.setEnabled(true);
		m.videoSetHeight.setEnabled(true);
		m.videoSetWidth.setEnabled(true);
		m.barAlpha.setEnabled(true);
		m.previewFrame.setEnabled(true);
		m.previewButton.setEnabled(true);
	}
}
