package com.soniccandle.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.soniccandle.model.SimpleRenderer;
import com.soniccandle.model.VideoOutputter;

public class RenderSwingWorker extends SwingWorker<Boolean, Integer> {

	public int videoFrameRate;
	public int width;
	public int height;
	public File audioFile;
	public File outputTo;
	public VideoOutputter outputter;
	public BufferedImage backgroundImage;
	public JProgressBar progressBar;

	@Override
	public Boolean doInBackground() {
		SimpleRenderer renderer;
		try {
			renderer = new SimpleRenderer(audioFile, videoFrameRate, width, height, outputTo);
			renderer.backgroundImage = backgroundImage;
			renderer.outputter = outputter;
			renderer.start();
			while (!renderer.isDone) {
				renderer.renderNextFrame();
				this.publish(renderer.progress);
			}
			renderer.finish();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
    @Override
    protected void process(List<Integer> progresses) {
    	Integer progress = progresses.get(progresses.size() - 1);
    	progressBar.setValue(progress);
    }
	
	@Override
    public void done() {
		JOptionPane.showMessageDialog(null, "Done!");
	}
	
	
	
}
