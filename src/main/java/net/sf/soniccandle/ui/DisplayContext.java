package net.sf.soniccandle.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.sf.soniccandle.SonicCandle;

public class DisplayContext {

	private BufferedImage img = new BufferedImage(SonicCandle.SCREEN_WIDTH / 3, SonicCandle.SCREEN_HEIGHT / 3,
			BufferedImage.TYPE_INT_ARGB);
	private Graphics2D imgG = img.createGraphics();

	public DisplayContext() {
		imgG.setColor(Color.GREEN);
		imgG.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	public BufferedImage getImage(){
		return img;
	}
	
}
