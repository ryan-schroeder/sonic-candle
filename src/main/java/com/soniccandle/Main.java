package com.soniccandle;

import java.io.File;

public class Main {

	private static final String SOUND_FILENAME = "beepboop.wav";
	private static final int VIDEO_FRAME_RATE = 30;
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	public static void main(String[] args) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		File audioFile = new File(loader.getResource(SOUND_FILENAME).toURI());
		File outputTo = new File ("c:/sonic-candle/frames.mp4");
		if (!audioFile.exists()) {
			throw new RuntimeException("no audio file found");
		}
		SpectrumRenderer renderer = new SimpleRenderer(audioFile, VIDEO_FRAME_RATE, WIDTH, HEIGHT, outputTo);
		renderer.render();

	}
}
