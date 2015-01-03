package com.soniccandle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import co.uk.labbookpages.WavFile;
import co.uk.labbookpages.WavFileException;

public abstract class SpectrumRenderer {
	
	public WavFile wavFile;
	public int frameRate;
	public int width;
	public int height;
	public int barCount = 100;
	public String outputDir;
	public long sampleRate;
	public long totalFrames;
	public int framesPerVFrame;
	public int numChannels;
	
	public SpectrumRenderer(WavFile wavFile, int frameRate, int width, int height, String outputDir) {
		this.wavFile = wavFile;
		this.frameRate = frameRate;
		this.width = width;
		this.height = height;
		this.outputDir = outputDir;
		
		sampleRate = wavFile.getSampleRate();
		totalFrames = wavFile.getNumFrames();
		System.out.println("sample rate (frames [one frame = several samples, one for each channel] per second): "+sampleRate+", total frames: "+totalFrames);
		framesPerVFrame =  (int) (sampleRate / frameRate); 
		System.out.println("this means, at "+frameRate+" frames per second, we will have " + framesPerVFrame + " audio frames per each video frame.");
		numChannels = wavFile.getNumChannels();
	}
	
	public void render() throws IOException, WavFileException {
		long currentVFrame = 0;
		double lengthInSeconds = (((double) totalFrames) / ((double) sampleRate));
		long totalVFrames = (long) (lengthInSeconds * frameRate);
		System.out.println("audio is "+lengthInSeconds+" seconds long, which means we have " + totalVFrames+" total video frames ("+frameRate+" fps).");
		while (currentVFrame < totalVFrames) {
			System.out.println("on frame " + currentVFrame + " of " + totalVFrames);
			File outputfile = new File(outputDir+"/frame_"+currentVFrame+".png");
			ImageIO.write(renderVFrame(currentVFrame), "png", outputfile);
			currentVFrame ++;
		}
	}
	public abstract BufferedImage renderVFrame(long vFrameNum) throws IOException, WavFileException;
}
