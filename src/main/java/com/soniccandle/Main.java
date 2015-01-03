package com.soniccandle;

import java.io.File;

import co.uk.labbookpages.WavFile;

public class Main {

	private static final String SOUND_FILENAME = "Derek_Clegg_-_03_-_If_Only_We_Had_More_Time.wav";
	private static final int VIDEO_FRAME_RATE = 30;
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	public static void main(String[] args) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		File f = new File(loader.getResource(SOUND_FILENAME).toURI());
		if (!f.exists()) {
			throw new RuntimeException("no sound file found");
		}
		WavFile wavFile = WavFile.openWavFile(f);
		SpectrumRenderer renderer = new SimpleRenderer(wavFile, VIDEO_FRAME_RATE, WIDTH, HEIGHT, "frames");
		renderer.render();

	}
}
