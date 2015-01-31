package com.soniccandle;

// -------------------------------------------------------------------------------
// see this: http://stackoverflow.com/questions/5217611/the-mvc-pattern-and-swing 
// -------------------------------------------------------------------------------

import com.soniccandle.controller.MainController;
import com.soniccandle.model.MainModel;
import com.soniccandle.view.MainView;

public class Main {

	public static final int VIDEO_FRAME_RATE = 30;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	public static void main(String[] args) throws Exception {
		System.out.println("running!");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("invoked!");
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
