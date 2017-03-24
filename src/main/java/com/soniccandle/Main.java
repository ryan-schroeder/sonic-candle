package com.soniccandle;

// -------------------------------------------------------------------------------
// see this: http://stackoverflow.com/questions/5217611/the-mvc-pattern-and-swing 
// -------------------------------------------------------------------------------

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.soniccandle.controller.MainController;
import com.soniccandle.model.MainModel;
import com.soniccandle.view.MainView;

public class Main {

	private static GraphicsDevice gd = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static int screenWidth = gd.getDisplayMode().getWidth();
	private static int screenHeight = gd.getDisplayMode().getHeight();

	// set default video property constants
	public static final int VIDEO_FRAME_RATE = 30;
	public static final int WIDTH = screenWidth;
	public static final int HEIGHT = screenHeight;

	// set variables for changing video properties
	private static int mainVideoFrameRate = VIDEO_FRAME_RATE;
	private static int mainVideoWidth = WIDTH;
	private static int mainVideoHeight = HEIGHT;

	// set video properties methods
	public static void setVideoFrameRate(int mainVideoFrameRateNew) {
		mainVideoFrameRate = mainVideoFrameRateNew;
	}

	public static void setVideoWidth(int mainVideoWidthNew) {
		mainVideoWidth = mainVideoWidthNew;
	}

	public static void setVideoHeight(int mainVideoHeightNew) {
		mainVideoHeight = mainVideoHeightNew;
	}

	// get Video Properties Methods
	public static int getVideoFrameRate() {
		return (mainVideoFrameRate);
	}

	public static int getVideoWidth() {
		return (mainVideoWidth);
	}

	public static int getVideoHeight() {
		return (mainVideoHeight);
	}

	public static void main(String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				MainModel m = new MainModel();
				MainView v = new MainView();
				MainController c = new MainController();
				v.m = m;
				v.c = c;
				c.m = m;
				c.v = v;
				v.createAndShowGUI();
			}
		});
	}
}
