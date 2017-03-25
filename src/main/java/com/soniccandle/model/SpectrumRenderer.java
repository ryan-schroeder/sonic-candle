package com.soniccandle.model;

import com.dakkra.wav.WavFile;
import com.dakkra.wav.WavFileException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class SpectrumRenderer {

    public WavFile wavFile;
    public int frameRate;
    public int width;
    public int height;
    public int barCount = 100;
    public long sampleRate;
    public long totalFrames;
    public int framesPerVFrame;
    public int numChannels;

    public VideoOutputter outputter;

    public double lengthInSeconds;
    public long totalVFrames;
    long currentVFrame;
    public boolean isDone = false;
    public int progress;

    public SpectrumRenderer(File audioFile, int frameRate, int width, int height, File outputTo) throws IOException, WavFileException {
        this.wavFile = getWavFile(audioFile);
        this.frameRate = frameRate;
        this.width = width;
        this.height = height;

        sampleRate = wavFile.getSampleRate();
        totalFrames = wavFile.getNumFrames();
        System.out.println("sample rate (frames [one frame = several samples, one for each channel] per second): " + sampleRate + ", total frames: " + totalFrames);
        framesPerVFrame = (int) (sampleRate / frameRate);
        System.out.println("this means, at " + frameRate + " frames per second, we will have " + framesPerVFrame + " audio frames per each video frame.");
        numChannels = wavFile.getNumChannels();
    }

    // exposed for unit tests only!
    public WavFile getWavFile(File audioFile) throws IOException, WavFileException {
        WavFile wf = new WavFile(audioFile);
        try {
            wf.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wf;
    }

    public void start() throws Exception {
        outputter.start();
        lengthInSeconds = (((double) totalFrames) / ((double) sampleRate));
        currentVFrame = 0;
        totalVFrames = (long) (lengthInSeconds * frameRate);
        System.out.println("audio is " + lengthInSeconds + " seconds long, which means we have " + totalVFrames + " total video frames (" + frameRate + " fps).");
    }

    public void checkDone() {
        if (currentVFrame >= totalVFrames) {
            isDone = true;
        } else {
            isDone = false;
        }
    }

    public void renderNextFrame() throws Exception {
        checkDone();
        if (isDone) {
            return;
        }
        System.out.println("rendering frame " + currentVFrame + " of " + totalVFrames);
        outputter.addFrame(renderVFrame(currentVFrame));
        currentVFrame++;
        progress = (int) (((double) currentVFrame) / ((double) totalVFrames) * 100);
    }

    public void finish() {
        outputter.finish();
        try {
            wavFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderAll() throws Exception {
        this.start();
        while (!isDone) {
            renderNextFrame();
        }
        finish();
    }

    public abstract BufferedImage renderVFrame(long vFrameNum)
            throws IOException, WavFileException;
}
