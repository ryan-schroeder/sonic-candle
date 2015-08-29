package net.sf.soniccandle.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DisplayPane extends JPanel {
	private static final long serialVersionUID = -4889428737358196704L;

	private DisplayContext context = new DisplayContext();

	public DisplayPane() {

	}

	@Override
	protected void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.drawImage(context.getImage(), getWidth() - context.getImage().getWidth(), 0, this);
	}

}
