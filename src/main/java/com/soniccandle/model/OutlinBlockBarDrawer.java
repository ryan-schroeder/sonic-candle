package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class OutlinBlockBarDrawer extends BarDrawer {

	public OutlinBlockBarDrawer(Graphics2D g, int half) { super(g, half); }

	@Override
	public void drawBar(int height, int x) {
		Stroke str = new BasicStroke(1);
		g.setStroke(str);
		g.drawLine(x+2, half, x+2, half+height); // draws down
		g.drawLine(x-2, half, x-2, half+height); // draws down
		g.drawLine(x-2, half+height, x+2, half+height); // draws across
		g.drawLine(x+2, half, x+2, half-height); // draws up
		g.drawLine(x-2, half, x-2, half-height); // draws up
		g.drawLine(x-2, half-height, x+2, half-height); // draws across
	}

}
