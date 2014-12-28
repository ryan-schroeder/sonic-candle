package com.soniccandle;

public class DoubleAddZeros {
	
	public double[] output;
	
	public DoubleAddZeros(double[] input) {
		this.output = new double[input.length * 2];
		int i = 0;
		int j = 0;
		while (i < input.length) {
			output[j] = input[i];
			j ++;
			output[j] = 0; // complex component.
			j ++;
			i ++;
		}
	}
}
