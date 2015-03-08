package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class ThickBlockBarDrawer extends BarDrawer {

	public ThickBlockBarDrawer(Graphics2D g, int half, int barWidth) { super(g, half, barWidth); }

	@Override
	public void drawBar(int height, int x) {
		int space = barWidth / 2;
		Stroke str = new BasicStroke(barWidth - space);
		g.setStroke(str);
		g.drawLine(x, half, x, half+height); // draws down
		g.drawLine(x, half, x, half-height); // draws up
	}

}
