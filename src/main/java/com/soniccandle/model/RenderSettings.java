package com.soniccandle.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import co.uk.labbookpages.WavFileException;

public class RenderSettings {
	public File audioFile;
	public File outputTo;
	public VideoOutputter outputter;
	public int videoFrameRate;
	public int width;
	public int height;
	public BufferedImage backgroundImage;

	public FastSimpleRenderer bakeSimpleRenderer(MainModel m)
			throws IOException, WavFileException {
		FastSimpleRenderer renderer = new FastSimpleRenderer(audioFile,
				videoFrameRate, width, height, outputTo);
		renderer.backgroundImage = backgroundImage;
		renderer.barStyle = (String) m.barStyle.getSelectedItem();
		renderer.barColor = new Color(
				Integer.parseInt(m.barColorRed.getText()),
				Integer.parseInt(m.barColorGreen.getText()),
				Integer.parseInt(m.barColorBlue.getText()),
				Integer.parseInt(m.barAlpha.getText()));
		renderer.outputter = outputter;
		return renderer;
	}

}
