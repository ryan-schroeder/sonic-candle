package net.sf.soniccandle.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.sf.soniccandle.SonicCandle;
import net.sf.soniccandle.util.SonicIO;

public class MainWindow {

	private static int mFrameDefaultWidth = (int) Math.round(SonicCandle.SCREEN_WIDTH / 1.5);
	private static int mFrameDefaultHeight = (int) Math.round(SonicCandle.SCREEN_HEIGHT / 1.5);
	private static Dimension mFrameDefaultDimension = new Dimension(mFrameDefaultWidth, mFrameDefaultHeight);
	private static SonicFrame mFrame;

	public MainWindow() {

		// Use Nimbus
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Nimbus look and feel not found (MainView.java)");
			e.printStackTrace();
		}

	}

	public void show() {
		mFrame = new SonicFrame();

		mFrame.setVisible(true);
	}

	private class SonicFrame extends JFrame {
		private static final long serialVersionUID = 3519931960287250842L;

		public SonicFrame() {
			this.setTitle("Sonic Candle");
			this.setIconImage(SonicIO.loadResourceAsImage("/sonic-candle-icon.png"));
			this.setSize(mFrameDefaultDimension);
			this.setResizable(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		}

	}

}
