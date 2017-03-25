package com.soniccandle.model;

import com.dakkra.wav.WavFile;

import java.io.IOException;
import java.io.InputStream;

public class WavFileMock extends WavFile {

    public static int SAMPLE_RATE = 300;
    public static int NUM_FRAMES = 100;

    public WavFileMock() {
        super(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
        return;
    }

    @Override
    public int getNumChannels() {
        return 2;
    }

    @Override
    public int getSampleRate() {
        return SAMPLE_RATE;
    }

    @Override
    public int getNumFrames() {
        return NUM_FRAMES;
    }
}
