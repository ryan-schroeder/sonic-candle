package com.soniccandle.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.soniccandle.Main;

import co.uk.labbookpages.WavFileException;

public class Utils {
	// Audio file types
	public final static String wav = "wav";
	public final static String mp3 = "mp3";
	// Video file types
	public final static String mp4 = "mp4";
	// Image file types
	public final static String png = "png";
	public final static String jpg = "jpg";

	// Get file extension
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public static double[] doubleAddZeros(double[] input) {
		double[] output = new double[input.length * 2];
		int i = 0;
		int j = 0;
		while (i < input.length) {
			output[j] = input[i];
			j++;
			output[j] = 0; // complex component.
			j++;
			i++;
		}
		return output;
	}

	public static StereoData splitChannels(double[] input) {
		StereoData stereo = new StereoData();
		int halfLength = input.length / 2;
		stereo.left = new double[halfLength];
		stereo.right = new double[halfLength];
		int leftI = 0;
		int rightI = 0;
		int i = 0;
		boolean isOdd = false;
		while (i < input.length) {
			if (isOdd) {
				stereo.left[leftI] = input[i];
				leftI++;
			} else {
				stereo.right[rightI] = input[i];
				rightI++;
			}
			isOdd = !isOdd;
			i++;
		}
		return stereo;
	}

	public static void printMinMax(double[] buffer) throws IOException,
			WavFileException {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		int i = 0;
		while (i < buffer.length) {
			if (buffer[i] > max)
				max = buffer[i];
			if (buffer[i] < min)
				min = buffer[i];
			i++;
		}
		System.out.printf("Min: %f, Max: %f\n", min, max);
	}

	public static Font scFont(int size, int style) {
		InputStream in = Main.class
				.getResourceAsStream("/SourceSansPro-Regular.ttf");
		Font myFont = null;
		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(
					style, size);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (myFont == null) {
			myFont = new Font("Plain", style, 12);
		}
		return myFont;
	}
}
