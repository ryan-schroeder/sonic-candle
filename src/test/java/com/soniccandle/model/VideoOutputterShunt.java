package com.soniccandle.model;

import java.awt.image.BufferedImage;
import java.io.File;

public class VideoOutputterShunt extends VideoOutputter {

	public VideoOutputterShunt(File audioFile, File outputTo) {
		super(audioFile, outputTo);
	}

	@Override
	public void start() throws Exception {
	}

	@Override
	public void addFrame(BufferedImage frame) throws Exception {
	}

	@Override
	public void finish() {
	}

}
