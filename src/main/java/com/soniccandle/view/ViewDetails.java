package com.soniccandle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

public class ViewDetails {
	
	// color constants
	public static final Color BGCOLOR = new Color(17, 17, 17);
	public static final Color PANELCOLOR = new Color(45, 45, 45);
	public static final Color SCBLUE = new Color(95, 115, 133);
	
	
	JFrame frame = new JFrame("Details");
	
	public void ViewwDetails(){
		
		//use Nimbus
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		        	customizeNimbus();
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    System.out.println("Nimbus look and feel not found (ViewDetails.java)");
		    e.printStackTrace();
		}
		
	
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Icon code
		Image icon;
		InputStream input = getClass().getResourceAsStream(
				"/sonic-candle-icon.png");
		try {
			icon = ImageIO.read(input);
			frame.setIconImage(icon);
		} catch (IOException e) {
			// Intentionally ignore exception (because it should never happen)
		}
			
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Details"));
		
		JTextArea outputField = new JTextArea();
		outputField.setEditable(false);
		
		panel.add(outputField);
		frame.add(panel);
		
		frame.pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		
		frame.setLocation(width/2, height/2);
		
		frame.setVisible(true);
	}
	
	public void setVisible(boolean tf){
		if (tf == true){
			frame.setVisible(true);
		}else{
			frame.setVisible(false);
		}
	}
	
	private void customizeNimbus(){
		//General Changes
		UIManager.put("control", PANELCOLOR);
		UIManager.put("text", Color.WHITE);
		UIManager.put("nimbusLightBackground", BGCOLOR);
		UIManager.put("nimbusFocus", Color.DARK_GRAY);
		UIManager.put("nimbusBase", Color.BLACK);
		UIManager.put("nimbusOrange", Color.GRAY);
		UIManager.put("nimbusBorder", Color.BLACK);
		UIManager.put("background", BGCOLOR);
		UIManager.put("nimbusSelection", Color.GRAY);
		UIManager.put("textHighlight", Color.GRAY);
		UIManager.put("nimbusSelectionBackground", Color.DARK_GRAY);
		
	}
}
