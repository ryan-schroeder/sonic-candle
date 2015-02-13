package com.soniccandle.model;

import java.awt.Graphics2D;

public abstract class BarDrawer {
	
	public Graphics2D g;
	public int half;
	
	public BarDrawer(Graphics2D g, int half) {
		this.g = g;
		this.half = half;
	}
	
	public abstract void drawBar(int height, int x);
}
