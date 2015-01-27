package com.soniccandle.model;

import java.awt.image.BufferedImage;
import java.io.File;

public abstract class VideoOutputter {
	
	public File audioFile;
	public File outputTo;
	public int frameRate = 30;
	public int width = 1080;
	public int height = 720;
	
	public VideoOutputter(File audioFile, File outputTo) {
		this.audioFile = audioFile;
		this.outputTo = outputTo;
	}
	
	public abstract void start() throws Exception;
	public abstract void addFrame(BufferedImage frame) throws Exception;
	public abstract void finish();
	
}
