package com.soniccandle.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;//http://docs.oracle.com/javase/tutorial/2d/advanced/quality.html -- set in the specific bar drawing class
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

import com.soniccandle.Main;
import com.soniccandle.model.FastSimpleRenderer;
import com.soniccandle.model.ImageSeqVideoOutputter;
import com.soniccandle.model.MainModel;
import com.soniccandle.model.RenderSettings;
import com.soniccandle.model.VideoOutputter;
import com.soniccandle.model.XuggleVideoOutputter;
import com.soniccandle.util.ImageFilter;
import com.soniccandle.util.InputFilter;
import com.soniccandle.util.Utils;
import com.soniccandle.view.MainView;

public class MainController implements ActionListener {

	public static final String SET_INPUT_WAV = "SET_INPUT_WAV";
	public static final String SET_OUTPUT_MP4 = "SET_OUTPUT_MP4";
	public static final String SET_BG_OTHER_IMAGE = "SET_BG_OTHER_IMAGE";
	public static final String RENDER = "RENDER";
	public static final String CANCEL_RENDER = "CANCEL_RENDER";
	public static final String BAR_STYLE_THICK_BLOCK = "01 Thick Block";
	public static final String BAR_STYLE_OUTLINE_BLOCK = "02 Outline Block";
	public static final String BAR_STYLE_THIN = "03 Thin";
	public static final String BAR_STYLE_ROUND_BLOCK = "04 Round Filled";
	public static final String BAR_STYLE_ROUND_OUTLINE = "05 Round Outline";
	public static final String BAR_STYLE_DEPTH_BLOCK = "06 Pop Up Block";
	public static final String BAR_STYLE_DEPTH_BLOCK2 = "07 Etched Block";
	public static final String BAR_STYLE_OVAL_FILLED = "08 Oval Filled";
	public static final String BAR_STYLE_OVAL_OUTLINE = "09 Oval Outline";
	public static final String PREVIEW = "PREVIEW";
	public static final String DETAILS = "DETAILS";

	public static String audioType;

	File generatedWav;

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
			int returnVal = m.fcIn.showOpenDialog(m.pane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File inputFile = m.fcIn.getSelectedFile();
				if (InputFilter.supportedType(inputFile)) {
					audioType = (Utils.getExtension(inputFile));
					System.out.println("File type is: " + audioType);
					// I wanted to move this into the render event but the
					// preview requires a wav file already loaded (broke when I
					// moved this to render)
					// So I'll have the wav file deleted after the render
					// finishes but it will be created when the user loads in
					// the mp3 (hooray awkward API's)
					if ("wav".equals(audioType)) {
						m.audioFile = inputFile;
						m.audioFileNameLabel.setText(m.audioFile.getName());
					} else if ("mp3".equals(audioType)) {
						m.audioFile = mp3ToWav(inputFile);
						generatedWav = m.audioFile;
						m.audioFileNameLabel.setText(inputFile.getName());
					}
					System.out.println("Wavfile: " + m.audioFile.getName());
					m.setOutputButton.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(m.pane,
							"Please use a supported format");
				}
			}
		}

		if (SET_OUTPUT_MP4.equals(e.getActionCommand())) {
			m.fcOut.setSelectedFile(m.fcIn.getSelectedFile());
			int returnVal = m.fcOut.showDialog(m.pane, "Set Output");

			if (returnVal != JFileChooser.APPROVE_OPTION) { // they hit cancel
				return;
			}

			if (!m.fcOut.getSelectedFile().getName().endsWith(".mp4")
					&& (m.outputMethod.getSelectedItem()
							.equals(MainView.OUTPUT_MP4_TITLE))) {
				File addedMp4 = new File(m.fcOut.getSelectedFile().getParent(),
						m.fcOut.getSelectedFile().getName() + ".mp4");
				m.fcOut.setSelectedFile(addedMp4);
			}

			if (m.fcOut.getSelectedFile().exists()
					&& !m.fcOut.getSelectedFile().isDirectory()) {
				JOptionPane
						.showMessageDialog(m.pane,
								"That file exists already: if you render it will be overwritten.");
			}
			m.outputTo = m.fcOut.getSelectedFile();
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
			int returnVal = m.fcBG.showDialog(m.pane, "Set Background Image");

			if (returnVal != JFileChooser.APPROVE_OPTION) { // they hit cancel
				return;
			}
			if (ImageFilter.supportedType(m.fcBG.getSelectedFile())) {
				m.backgroundImageFile = m.fcBG.getSelectedFile();
				m.bgImageNamelabel.setText(" "
						+ m.backgroundImageFile.getName());
			} else {
				JOptionPane.showMessageDialog(m.pane,
						"Please use a supported format");
			}
		}

		if (PREVIEW.equals(e.getActionCommand())) {
			RenderSettings rs;
			try {
				rs = extractRenderSettings(false);
				FastSimpleRenderer renderer;
				renderer = rs.bakeSimpleRenderer(m);
				int previewFrameInt = Integer
						.parseInt(m.previewFrame.getText());
				if (previewFrameInt < 1) {
					JOptionPane.showMessageDialog(m.pane,
							"Please enter a real frame to preview");
					return;
				}
				System.out.println("previewing frame: " + previewFrameInt);
				// this is a temp fix. Eventually, we should fast-forward inside
				// the file to save time.
				Integer currentFrame = 0;
				while (currentFrame < previewFrameInt) {
					renderer.renderVFrame(currentFrame);
					currentFrame++;
				}

				BufferedImage preview = renderer.renderVFrame(currentFrame);
				JLabel previewLabel = new JLabel(new ImageIcon(preview));
				JFrame previewFrame = new JFrame("Preview");
				previewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// Added
																				// this
																				// to
																				// make
																				// sure
																				// it
																				// behaves
																				// as
																				// it
																				// should
				// Icon code
				Image icon;
				InputStream input = getClass().getResourceAsStream(
						"/sonic-candle-icon.png");
				try {
					icon = ImageIO.read(input);
					previewFrame.setIconImage(icon);
				} catch (IOException iconE) {
					// Intentionally ignore exception (because it should never
					// happen)
				}
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

	private RenderSettings extractRenderSettings(boolean getOutputter)
			throws Exception {
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
			BufferedImage backgroundImage = new BufferedImage(rs.width,
					rs.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D backgroundImageG = backgroundImage.createGraphics();
			backgroundImageG.setColor(new Color(Integer.parseInt(m.bgColorRed
					.getText()), Integer.parseInt(m.bgColorGreen.getText()),
					Integer.parseInt(m.bgColorBlue.getText())));
			backgroundImageG.fillRect(0, 0, rs.width, rs.height);
			rs.backgroundImage = backgroundImage;
		} else if (m.builtInImageRb.isSelected()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
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

	private File mp3ToWav(File audioFile) {
		// Check to make sure that if a generated wav file was created, it is
		// deleted
		if (generatedWav != null) {
			String oldName = generatedWav.getName();
			if (generatedWav.delete() == true) {
				System.out.println("Deleted unused wav file: " + oldName);
			} else {
				System.out
						.println("There was an issue deleting the old wavfile");
			}
			generatedWav = null;
		}
		System.out.println("Converting mp3 to wav...");
		Converter converter = new Converter();
		String fileURI = audioFile.getAbsolutePath();
		File tempWavFile = null;
		try {
			tempWavFile = File.createTempFile("soniccandle", ".wav");
			tempWavFile.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			converter.convert(fileURI, tempWavFile.getAbsolutePath());
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		System.out.println("Conversion Complete");
		return tempWavFile;

	}

	// TODO add oggToWav converter

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
		m.renderButton.setText("Cancel Render");
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
		m.bgColorButton.setEnabled(false);
		m.barColorButton.setEnabled(false);

	}

	public void unlockAfterRender() {
		m.setAudioButton.setEnabled(true);
		m.setOutputButton.setEnabled(true);
		m.renderButton.setText("Render");
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
		m.bgColorButton.setEnabled(true);
		m.barColorButton.setEnabled(true);
	}
}
