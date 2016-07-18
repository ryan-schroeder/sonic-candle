package com.soniccandle.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.*;

public class ImageSequence {

	public File firstFrame;
        public int firstFrameNumber;
	public int currentFrameNumber;
        public int digitCount; // number of digits used in the file name for the frame number
        public String extention; // includes period
        public String baseName;

	public ImageSequence(File firstFrame) throws Exception {
                this.firstFrame = firstFrame;
		// check to make sure the name ends in a number
		this.parseFirstFrameName(firstFrame);
                System.out.println("the number for the first frame is: " + this.firstFrameNumber + " and the extention is: " + this.extention + " and the digitCount is: " + this.digitCount + " and baseName: " + this.baseName);
		this.getNextFrame();
		this.getNextFrame();
		this.getNextFrame();
	}

	private void parseFirstFrameName(File toParse) throws Exception {
		String fileName = toParse.getName();
		String noEx;
		int lastPeriodPos = fileName.lastIndexOf('.');
		if (lastPeriodPos > 0) {
			noEx = fileName.substring(0, lastPeriodPos);
                        this.extention = fileName.substring(lastPeriodPos);
			String pattern = "(\\d+)$";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(noEx);
			if (m.find( )) {
                                this.digitCount = m.group(1).length();
                                this.baseName = noEx.substring(0, noEx.length() - this.digitCount);
				this.firstFrameNumber = Integer.parseInt(m.group(1));
				this.currentFrameNumber = this.firstFrameNumber;
			} else {
				throw new Exception("Could not find a number on the end of this file: " + fileName);
			}
		} else {
			throw new Exception("Could not find a number on the end of this file because it has no extention: " + fileName);
		}
        }

	public BufferedImage getNextFrame() {
		BufferedImage nextFrame = null;
		while (nextFrame == null) {
			try {
				String paddedFrameNumber = String.format("%0" + this.digitCount +"d", this.currentFrameNumber);
				String fullPath = Paths.get(this.firstFrame.getParent(), this.baseName + paddedFrameNumber + this.extention).toString();
				System.out.println("reading background image sequence frame: " + fullPath);
				nextFrame = ImageIO.read(new File(fullPath));
			} catch (Exception e) {
				// if the current frame is past the end of the sequence, loop back to the first frame
				this.currentFrameNumber = this.firstFrameNumber;
			}
		}
		this.currentFrameNumber = currentFrameNumber + 1;
		return nextFrame;
	}

}
