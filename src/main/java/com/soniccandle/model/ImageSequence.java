package com.soniccandle.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageSequence {

	public File firstFrame;
	public int currentFrame;

	public ImageSequence(File firstFrame) throws Exception {
                this.firstFrame = firstFrame;
		// check to make sure the name ends in a number
		this.currentFrame = this.getEndNumber(firstFrame);
	}

	private int getEndNumber(File toParse) throws Exception {
		String fileName = toParse.getName();
		int lastPeriodPos = fileName.lastIndexOf('.');
		if (lastPeriodPos > 0) {
			fileName = fileName.substring(0, lastPeriodPos);
			String pattern = "(\\d+)$";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(fileName);
			if (m.find( )) {
				return Integer.parseInt(m.group(1));
			} else {
				throw new Exception("Could not find a number on the end of this file: " + fileName);
			}
		} else {
			throw new Exception("Could not find a number on the end of this file because it has no extention: " + fileName);
		}
        }

	public BufferedImage getNextFrame() {
		return null;
	}

}
