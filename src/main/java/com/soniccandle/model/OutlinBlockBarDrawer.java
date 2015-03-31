package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class OutlinBlockBarDrawer extends BarDrawer {

	public OutlinBlockBarDrawer(Graphics2D g, int half, int barWidth) {
		super(g, half, barWidth);
	}

	@Override
	public void drawBar(int height, int x) {
		Stroke str = new BasicStroke(1);
		g.setStroke(str);
		int k = barWidth / 6;
		g.drawLine(x + k, half, x + k, half + height); // draws down
		g.drawLine(x - k, half, x - k, half + height); // draws down
		g.drawLine(x - k, half + height, x + k, half + height); // draws across
		g.drawLine(x + k, half, x + k, half - height); // draws up
		g.drawLine(x - k, half, x - k, half - height); // draws up
		g.drawLine(x - k, half - height, x + k, half - height); // draws across
	}

}
