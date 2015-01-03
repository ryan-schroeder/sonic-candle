package com.soniccandle;

import java.awt.image.BufferedImage;
import java.io.File;

public abstract class VideoOutputter {
	
	public File wavFile;
	public File outputTo;
	
	public VideoOutputter(File wavFile, File outputTo) {
		this.wavFile = wavFile;
		this.outputTo = outputTo;
	}
	
	public abstract void start();
	public abstract void addFrame(BufferedImage frame) throws Exception;
	public abstract void finish();
	
}
