package com.soniccandle;

import java.io.File;

import javax.swing.JProgressBar;

public class RenderRunnable implements Runnable {

	public int videoFrameRate;
	public int width;
	public int height;
	public File audioFile;
	public File outputTo;
	public JProgressBar progressBar;

	public void run() {
		SpectrumRenderer renderer;
		try {
			renderer = new SimpleRenderer(audioFile, videoFrameRate, width, height, outputTo, progressBar);
			renderer.render();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
