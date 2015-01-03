package com.soniccandle;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageSeqVideoOutputter extends VideoOutputter {
	
	public long frameNumber = 0;

	public ImageSeqVideoOutputter(File wavFile, File outputTo) {
		super(wavFile, outputTo);
	}

	@Override
	public void start() { }

	@Override
	public void addFrame(BufferedImage frame) throws Exception {
		File outputfile = new File(outputTo, "frame_"+frameNumber+".png");
		ImageIO.write(frame, "png", outputfile);
		frameNumber ++;
	}

	@Override
	public void finish() { }

}
