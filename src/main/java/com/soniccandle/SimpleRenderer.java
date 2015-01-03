package com.soniccandle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jtransforms.fft.DoubleFFT_1D;

import co.uk.labbookpages.WavFile;
import co.uk.labbookpages.WavFileException;

import com.soniccandle.util.StereoData;
import com.soniccandle.util.Utils;

public class SimpleRenderer extends SpectrumRenderer {

	public SimpleRenderer(WavFile wavFile, int frameRate, int width, int height, String outputDir) {
		super(wavFile, frameRate, width, height, outputDir);
	}

	@Override
	public BufferedImage renderVFrame(long vFrameNum) throws IOException, WavFileException {
		int framesRead;
		double[] buffer = new double[framesPerVFrame * numChannels];
		framesRead = wavFile.readFrames(buffer, framesPerVFrame);  // not multiplied by number of channels; that's correct.
		if (framesRead == 0) {
			throw new RuntimeException("no frames were read for vFrameNum "+vFrameNum);
		}

		DoubleFFT_1D fft = new DoubleFFT_1D(framesPerVFrame);
		double[] fftResult;
		if (numChannels == 2) {
			StereoData stereo = Utils.splitChannels(buffer);
			fftResult = Utils.doubleAddZeros(stereo.left);
		} else if (numChannels == 1) {
			fftResult = Utils.doubleAddZeros(buffer);
		} else {
			throw new RuntimeException("only supporting 1 or 2 channels");
		}
		fft.complexForward(fftResult);
		System.out.println("array fftResult has this many entries: " + fftResult.length);
		int i = 0;
		int[] bars = new int[barCount];
		int pointsPerBar = Math.round( ((framesPerVFrame*2) / barCount) / 16 ); // divide by 2 because size of fft result is N/2.  Rest of array is mirror of first half.  Divide by 4 because most of the frequencies we want are actually towards the far left, so this scales us over.
		System.out.println("total fftResult points: "+fftResult.length + "; pointsPerBar: " + pointsPerBar);
		while (i < barCount) {
			bars[i] = 0;
			// find the absolute-value peek in this region.
			int j = pointsPerBar * i;
			while (j < (pointsPerBar*(i+1))) {
				double real = fftResult[j];
				double imaginary = fftResult[j + 1];
				double freqencyMagnatude = Math.sqrt( (real*real) + (imaginary*imaginary));  // frequency magnatude = sqrt (real * real + imag * imag)   http://stackoverflow.com/questions/6740545/need-help-understanding-fft-output
				freqencyMagnatude = freqencyMagnatude * 1.2;  // scale up.
				if (Math.abs(fftResult[j]) > bars[i]) {
					bars[i] = (int) Math.round(freqencyMagnatude);
				}
				j = j + 2; // real and imaginary part
 			}
			i ++;
		}

		// draw a spectrum
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(0));
		g.fillRect(0, 0, width, height);
		Color white = new Color(255,255,255);
		g.setColor(white);
		Stroke str = new BasicStroke(7);
		g.setStroke(str);
		i = 0;
		int x;
		int half = height/2;
		while (i < barCount) {
			x = 40+(i*12);
			g.drawLine(x, half, x, half+bars[i]); // draws down
			g.drawLine(x, half, x, half-bars[i]); // draws up
			i ++;
		}
		return img;
	}
}
