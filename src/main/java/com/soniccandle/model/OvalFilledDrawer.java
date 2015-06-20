package com.soniccandle.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

public class OvalFilledDrawer extends BarDrawer {

	public OvalFilledDrawer(Graphics2D g, int half, int barWidth) {
		super(g, half, barWidth);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void drawBar(int height, int x) {
		int center = barWidth / 2;
		int drawBarWidth = barWidth - 2;
		int drawHeight = height + 1; // make it so we never see a value of 0
		Stroke str = new BasicStroke(1);
		g.setStroke(str);

		// new way
		// System.out.println(radius); --system runs on truncation for non ints
		g.fillOval(x - center, half - height, drawBarWidth, drawHeight * 2);// draw
																			// up
																			// and
																			// down

	}

}
