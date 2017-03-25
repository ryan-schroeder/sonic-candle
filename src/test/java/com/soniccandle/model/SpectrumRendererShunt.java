package com.soniccandle.model;

import com.dakkra.wav.WavFile;
import com.dakkra.wav.WavFileException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpectrumRendererShunt extends SpectrumRenderer {

    public SpectrumRendererShunt(File audioFile, int frameRate, int width, int height, File outputTo) throws IOException, WavFileException {
        super(audioFile, frameRate, width, height, outputTo);
    }

    @Override
    public WavFile getWavFile(File audioFile) {
        return new WavFileMock();
    }

    @Override
    public BufferedImage renderVFrame(long vFrameNum) throws IOException,
            WavFileException {
        return null;
    }

}
