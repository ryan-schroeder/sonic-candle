package com.soniccandle.model;

import java.awt.Graphics2D;

public abstract class BarDrawer {

    public Graphics2D g;
    public int half;
    public int barWidth;

    public BarDrawer(Graphics2D g, int half, int barWidth) {
        this.g = g;
        this.half = half;
        this.barWidth = barWidth;
    }

    public abstract void drawBar(int height, int x);
}
