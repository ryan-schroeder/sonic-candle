package com.soniccandle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.jtransforms.fft.DoubleFFT_1D;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import co.uk.labbookpages.WavFile;

public class Main {

	private static final String SOUND_FILENAME = "twotones_mono.wav";
	private static final int BUFFER_FRAMES = 512;
	private static final int BAR_COUNT = 100;
	private static final String VIDEO_OUTPUT_FILENAME = "output.mp4";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	public static void main(String[] args) throws Exception {
		
		// load a sound file
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		File f = new File(loader.getResource(SOUND_FILENAME).toURI());
		if (!f.exists()) {
			throw new RuntimeException("no sound file found");
		}
        WavFile wavFile = WavFile.openWavFile(f);
        // Display information about the wav file
        wavFile.display();
        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();
        // Create a buffer
        double[] buffer = new double[BUFFER_FRAMES * numChannels];

        int framesRead;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        do
        {
           // Read frames into buffer
           framesRead = wavFile.readFrames(buffer, BUFFER_FRAMES);  // not multiplied by number of channels; that's correct.

           // Loop through frames and look for minimum and maximum value
           for (int s=0 ; s<framesRead * numChannels ; s++)
           {
              if (buffer[s] > max) max = buffer[s];
              if (buffer[s] < min) min = buffer[s];
           }
        }
        while (framesRead != 0);
        // Close the wavFile
        wavFile.close();

        // Output the minimum and maximum value
        System.out.printf("Min: %f, Max: %f\n", min, max);
        DoubleFFT_1D fft = new DoubleFFT_1D(BUFFER_FRAMES);
        DoubleAddZeros addZeros;
        if (numChannels == 2) {
        	ChannelSplitter splitter = new ChannelSplitter(buffer);
        	addZeros = new DoubleAddZeros(splitter.leftBuffer);
        } else if (numChannels == 1) {
        	addZeros = new DoubleAddZeros(buffer);
        } else {
        	throw new Exception("only supporting 1 or 2 channels");
        }
        double[] fftResult = addZeros.output;
        fft.complexForward(fftResult);
        System.out.println("array fftResult has this many entries: " + fftResult.length);
        int i = 0;
        while (i < (BUFFER_FRAMES*2)) {
        	//System.out.println(fftResult[i]);
        	i = i + 1;
        }
        
        int[] bars = new int[BAR_COUNT];
        int pointsPerBar = Math.round( (BUFFER_FRAMES*2) / BAR_COUNT );
        i = 0;
        while (i < BAR_COUNT) {
        	bars[i] = 0;
        	// find the absolute-value peek in this region.
        	int j = pointsPerBar * i;
        	while (j < (pointsPerBar*(i+1))) {
        		if (Math.abs(fftResult[j]) > bars[i]) {
        			bars[i] = (int) Math.round(Math.abs(fftResult[j]));
        		}
        		j ++;
        	}
        	i ++;
        }
        
        
		// draw a spectrum
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		Color white = new Color(255,255,255);
		g.setColor(white);
		Stroke str = new BasicStroke(7);
		g.setStroke(str);
		i = 0;
		int x;
		int half = HEIGHT/2;
		while (i < BAR_COUNT) {
			x = 40+(i*12);
			g.drawLine(x, half, x, half+bars[i]); // draws down
			g.drawLine(x, half, x, half-bars[i]); // draws up
			i ++;
		}
		File outputfile = new File("saved.png");
		ImageIO.write(img, "png", outputfile);
		

	}
}
