package com.soniccandle.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import co.uk.labbookpages.WavFile;
import co.uk.labbookpages.WavFileException;

public abstract class SpectrumRenderer {
	
	public WavFile wavFile;
	public int frameRate;
	public int width;
	public int height;
	public int barCount = 100;
	public long sampleRate;
	public long totalFrames;
	public int framesPerVFrame;
	public int numChannels;
	
	public double lengthInSeconds;
	public long totalVFrames;
	long currentVFrame;
	public boolean isDone;
	
	public VideoOutputter outputter;
	private JProgressBar progressBar;
	private int progress;
	
	public SpectrumRenderer(File audioFile, int frameRate, int width, int height, File outputTo, JProgressBar progressBar) throws IOException, WavFileException {
		this.wavFile = WavFile.openWavFile(audioFile);
		this.frameRate = frameRate;
		this.width = width;
		this.height = height;
		this.progressBar = progressBar;
		
		sampleRate = wavFile.getSampleRate();
		totalFrames = wavFile.getNumFrames();
		System.out.println("sample rate (frames [one frame = several samples, one for each channel] per second): "+sampleRate+", total frames: "+totalFrames);
		framesPerVFrame =  (int) (sampleRate / frameRate); 
		System.out.println("this means, at "+frameRate+" frames per second, we will have " + framesPerVFrame + " audio frames per each video frame.");
		numChannels = wavFile.getNumChannels();
	}
	
	public void start() throws Exception {
		outputter.start();
		lengthInSeconds = (((double) totalFrames) / ((double) sampleRate));
		currentVFrame = 0;
		totalVFrames = (long) (lengthInSeconds * frameRate);
		System.out.println("audio is "+lengthInSeconds+" seconds long, which means we have " + totalVFrames+" total video frames ("+frameRate+" fps).");
	}
	
	public void checkDone() {
		if (currentVFrame >= totalVFrames) { 
			isDone = true;
		} else {
			isDone = false;
		}
	}
	
	public void renderNextFrame() throws Exception {
		checkDone();
		if(isDone) { return; }
		System.out.println("rendering frame " + currentVFrame + " of " + totalVFrames);
		outputter.addFrame(renderVFrame(currentVFrame));
		currentVFrame ++;
		progress = (int) (((double) currentVFrame) / ((double) totalVFrames) * 100);
	}
	
	public void finish() {
		outputter.finish();
	}
	
	public void renderAll() throws Exception {
		this.start();
		while (!isDone) {
			renderNextFrame();
			progressBar.setValue(progress);
			progressBar.repaint();
		}
		outputter.finish();
		JOptionPane.showMessageDialog(null, "Done!");
	}


	public abstract BufferedImage renderVFrame(long vFrameNum) throws IOException, WavFileException;
}
