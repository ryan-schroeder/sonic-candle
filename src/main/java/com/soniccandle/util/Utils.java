package com.soniccandle.util;

import java.io.File;
import java.io.IOException;

import co.uk.labbookpages.WavFileException;

public class Utils {
	
	public final static String wav = "wav";
	public final static String mp4 = "mp4";
	
	//Get file extension
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
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
}
