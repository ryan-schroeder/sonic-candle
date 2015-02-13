package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class ThickBlockBarDrawer extends BarDrawer {

	public ThickBlockBarDrawer(Graphics2D g, int half) { super(g, half); }

	@Override
	public void drawBar(int height, int x) {
		Stroke str = new BasicStroke(7);
		g.setStroke(str);
		g.drawLine(x, half, x, half+height); // draws down
		g.drawLine(x, half, x, half-height); // draws up
	}

}
