package com.soniccandle;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class Main implements ActionListener {

	private static final String SAMPLE_SOUNDFILE = "sample.wav";
	private static final int VIDEO_FRAME_RATE = 30;
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String SET_INPUT_WAV = "SET_INPUT_WAV";
	private static final String SET_OUTPUT_MP4 = "SET_OUTPUT_MP4";
	private static final String RENDER = "RENDER";

	public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Main main = new Main();
            	ClassLoader loader = Thread.currentThread().getContextClassLoader();
            	try {
					main.audioFile  = new File(loader.getResource(SAMPLE_SOUNDFILE).toURI());
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	main.createAndShowGUI();
            }
        });
	}

	private JFileChooser fc;
	private Container pane;
	private JLabel audioFileNameLabel;
	private File audioFile;
	private File outputTo;
	private JLabel outputToNameLabel;
	private JButton renderButton;
	private JProgressBar progressBar;
	
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("sonic candle");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane = frame.getContentPane();
        pane.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        Box horizontalBox;
        JButton button;
        
        JLabel headerLabel = getHeaderLabel();
        c.gridwidth = 2;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(headerLabel, c);
        
        button = new JButton("set input wav");
        button.setActionCommand(SET_INPUT_WAV);
        button.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button, c);
        
        audioFileNameLabel = new JLabel(audioFile.getName());  // sample file
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.8;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(audioFileNameLabel);
        horizontalBox.add(Box.createHorizontalGlue());
        pane.add(horizontalBox, c);
    
        button = new JButton("set output mp4");
        button.setActionCommand(SET_OUTPUT_MP4);
        button.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(button, c);
        
        outputToNameLabel = new JLabel("(no file chosen)");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.8;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(outputToNameLabel);
        horizontalBox.add(Box.createHorizontalGlue());
        pane.add(horizontalBox, c);
        
        renderButton = new JButton("render");
        renderButton.setEnabled(false);
        renderButton.setActionCommand(RENDER);
        renderButton.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        pane.add(renderButton, c);
        
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
        progressBar.setValue(1);
        progressBar.setVisible(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        pane.add(progressBar, c);
        
        fc = new JFileChooser();
        
     
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    private JLabel getHeaderLabel() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        BufferedImage header;
		try {
			header = ImageIO.read(new File(loader.getResource("header.png").toURI()));
		} catch (Exception e) {
			throw new RuntimeException("header image not found");
		} 
        JLabel headerLabel = new JLabel(new ImageIcon(header));
        return headerLabel;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (SET_INPUT_WAV.equals(e.getActionCommand())) {
        	int returnVal = fc.showOpenDialog(pane);
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
        		audioFile = fc.getSelectedFile();
        		audioFileNameLabel.setText(audioFile.getName());
        	}
        }
        if (SET_OUTPUT_MP4.equals(e.getActionCommand())) {
        	int returnVal = fc.showDialog(pane, "set output");
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
        		if (fc.getSelectedFile().exists()) {
        			JOptionPane.showMessageDialog(pane, "That file exists already: if you render it will be overwritten.");
        		}
        		outputTo = fc.getSelectedFile();
        		outputToNameLabel.setText(outputTo.getName());
        	}
        }
        
        if (audioFile != null && outputTo != null) {
        	renderButton.setEnabled(true);
        } else {
        	renderButton.setEnabled(false);
        }
        
        if (RENDER.equals(e.getActionCommand())) {
        	if (outputTo.exists()) {
        		int result = JOptionPane.showConfirmDialog(pane, "Are you sure you want to over-write "+outputTo.getName()+"?");
        		if (result != JOptionPane.OK_OPTION) {
        			return;
        		}
        	}
        	RenderRunnable renderRunnable = new RenderRunnable();
        	renderRunnable.audioFile = audioFile;
        	renderRunnable.outputTo = outputTo;
        	renderRunnable.videoFrameRate = VIDEO_FRAME_RATE;
        	renderRunnable.width = WIDTH;
        	renderRunnable.height = HEIGHT;
        	renderRunnable.progressBar = progressBar;
        	Thread t = new Thread(renderRunnable);
        	progressBar.setVisible(true); 
        	t.start();
        	JOptionPane.showMessageDialog(pane, "The audio is written first: the progress bar will start once the audio is complete");
        }
    } 
    
    
}
