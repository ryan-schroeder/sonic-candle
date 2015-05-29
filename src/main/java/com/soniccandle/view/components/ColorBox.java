package com.soniccandle.view.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.soniccandle.model.MainModel;

public class ColorBox extends JPanel {
	private static final long serialVersionUID = -3870442884382904214L;
	MainModel m;

	public ColorBox(MainModel m) {
		this.m = m;
		this.add(new JLabel("    "));
		updateUI();
	}
	
	public void updateBox(){
		updateUI();
	}

	@Override
	public void paint(Graphics g) {
		Color bgColor = new Color(Integer.parseInt(m.bgColorRed.getText()),
				Integer.parseInt(m.bgColorGreen.getText()),
				Integer.parseInt(m.bgColorBlue.getText()));

		Color barColor = new Color(Integer.parseInt(m.barColorRed.getText()),
				Integer.parseInt(m.barColorGreen.getText()),
				Integer.parseInt(m.barColorBlue.getText()));

		g.setColor(bgColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(barColor);
		g.fillRect(15, 15, this.getWidth() - 15, this.getHeight() - 15);
	}

}
