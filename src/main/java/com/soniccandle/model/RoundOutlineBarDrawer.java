package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

public class RoundOutlineBarDrawer extends BarDrawer {

	public RoundOutlineBarDrawer(Graphics2D g, int half, int barWidth) {
		super(g, half, barWidth);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

	}

	@Override
	public void drawBar(int height, int x) {
		int center = barWidth / 2;
		int drawBarWidth = barWidth - 3;
		int drawHeight = height + 1; // make it so we never see a value of 0
		int radius = drawBarWidth;
		Stroke str = new BasicStroke(1);
		g.setStroke(str);

		// new way
		// System.out.println(radius); --system runs on truncation for non ints
		g.drawRoundRect(x - center, half - height, drawBarWidth,
				drawHeight * 2, radius, radius);// draw up and down

	}

}
