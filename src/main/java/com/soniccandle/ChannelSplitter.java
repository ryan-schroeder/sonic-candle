package com.soniccandle;

public class ChannelSplitter {
	
	public double[] buffer;
	public double[] leftBuffer;
	public double[] rightBuffer;
	
	public ChannelSplitter(double[] buffer) {
		this.buffer = buffer;
		int halfLength = this.buffer.length / 2;
		this.leftBuffer = new double[halfLength];
		this.rightBuffer = new double[halfLength];
		int leftI = 0;
		int rightI = 0;
		int i = 0;
		boolean isOdd = false;
		while (i < this.buffer.length) {
			if (isOdd) {
				this.leftBuffer[leftI] = this.buffer[i];
				leftI ++;
			} else {
				if (rightI == 512) { System.out.println("rightI = 512. leftI is "+leftI+".  i = "+i); }
				this.rightBuffer[rightI] = this.buffer[i];
				rightI ++;
			}
			isOdd = !isOdd;
			i ++;
		}
	}
}
