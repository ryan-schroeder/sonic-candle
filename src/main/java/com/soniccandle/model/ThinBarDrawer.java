package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class ThinBarDrawer extends BarDrawer {

	public ThinBarDrawer(Graphics2D g, int half, int barWidth) {
		super(g, half, barWidth);
	}

	@Override
	public void drawBar(int height, int x) {
		Stroke str = new BasicStroke(2);
		g.setStroke(str);
		g.drawLine(x, half, x, half + height); // draws down
		g.drawLine(x, half, x, half - height); // draws up
	}

}
