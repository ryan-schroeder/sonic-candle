package com.soniccandle.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.soniccandle.model.MainModel;
import com.soniccandle.model.SimpleRenderer;
import com.soniccandle.model.VideoOutputter;
import com.soniccandle.model.XuggleVideoOutputter;

public class RenderSwingWorker extends SwingWorker<Boolean, Integer> {

	public int videoFrameRate;
	public int width;
	public int height;
	public File audioFile;
	public File outputTo;
	public VideoOutputter outputter;
	public BufferedImage backgroundImage;
	public MainController c;
	public MainModel m;

	@Override
	public Boolean doInBackground() {
		SimpleRenderer renderer;
		try {
			renderer = new SimpleRenderer(audioFile, videoFrameRate, width, height, outputTo);
			renderer.backgroundImage = backgroundImage;
			renderer.outputter = outputter;
			renderer.start();
			while (!renderer.isDone && !Thread.currentThread().isInterrupted()) {
				renderer.renderNextFrame();
				this.publish(renderer.progress);
			}
			renderer.finish();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
    @Override
    protected void process(List<Integer> progresses) {
    	Integer progress = progresses.get(progresses.size() - 1);
    	m.progressBar.setValue(progress);
    }
	
	@Override
    public void done() {
		c.unlockAfterRender();
		if (m.progressBar.getValue() < 99) {
			JOptionPane.showMessageDialog(null, "Canceled - may not have created entire video =\\");
			return;
		}
		if (outputter instanceof XuggleVideoOutputter && outputTo.length() < 100) {
			JOptionPane.showMessageDialog(null, "Ooof - looks like there was a problem, sorry.  Please check that your adio file is 16-bit wav, not 24 or 32, thanks!  Other bitrates coming soon, hopefully.");
			return;
		}
		
		JOptionPane.showMessageDialog(null, "Done!");
	}
	
	
	
}
