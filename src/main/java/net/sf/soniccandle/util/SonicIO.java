package net.sf.soniccandle.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.sf.soniccandle.SonicCandle;

public class SonicIO {

	public static BufferedImage loadResourceAsImage(String uri) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(SonicCandle.class.getResourceAsStream(uri));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (img != null) {
			return img;
		} else {
			return null;
		}
	}

	public static ImageIcon loadResourceAsIcon(String uri) {
		BufferedImage img = loadResourceAsImage(uri);
		return new ImageIcon(img);
	}

}
