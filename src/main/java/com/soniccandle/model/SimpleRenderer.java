package com.soniccandle.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jtransforms.fft.DoubleFFT_1D;

import co.uk.labbookpages.WavFileException;

import com.soniccandle.controller.MainController;
import com.soniccandle.util.StereoData;
import com.soniccandle.util.Utils;

public class SimpleRenderer extends SpectrumRenderer {

    public BufferedImage backgroundImage;
    public String barStyle;
    public Color barColor;

    public SimpleRenderer(File audioFile, int frameRate, int width, int height,
                          File outputTo) throws IOException, WavFileException {
        super(audioFile, frameRate, width, height, outputTo);
    }

    @Override
    public BufferedImage renderVFrame(long vFrameNum) throws IOException,
            WavFileException {
        int framesRead;
        double[] buffer = new double[framesPerVFrame * numChannels];
        framesRead = wavFile.readFrames(buffer, framesPerVFrame); // not
        // multiplied
        // by number
        // of
        // channels;
        // that's
        // correct.
        if (framesRead == 0) {
            throw new RuntimeException("no frames were read for vFrameNum "
                    + vFrameNum);
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
        System.out.println("array fftResult has this many entries: "
                + fftResult.length);

        int i = 0;
        int[] bars = new int[barCount];
        int pointsPerBar = Math.round(((framesPerVFrame * 2) / barCount) / 16); // divide
        // by
        // 2
        // because
        // size
        // of
        // fft
        // result
        // is
        // N/2.
        // Rest
        // of
        // array
        // is
        // mirror
        // of
        // first
        // half.
        // Divide
        // by
        // 4
        // because
        // most
        // of
        // the
        // frequencies
        // we
        // want
        // are
        // actually
        // towards
        // the
        // far
        // left,
        // so
        // this
        // scales
        // us
        // over.
        System.out.println("total fftResult points: " + fftResult.length
                + "; pointsPerBar: " + pointsPerBar);

        while (i < barCount) {
            bars[i] = 0;
            // find the absolute-value peek in this region.
            int j = pointsPerBar * i;
            while (j < (pointsPerBar * (i + 1))) {
                double real = fftResult[j];
                double imaginary = fftResult[j + 1];
                double freqencyMagnatude = Math.sqrt((real * real)
                        + (imaginary * imaginary)); // frequency magnatude =
                // sqrt (real * real + imag
                // * imag)
                // http://stackoverflow.com/questions/6740545/need-help-understanding-fft-output
                freqencyMagnatude = freqencyMagnatude * 1.2; // scale up.
                if (Math.abs(fftResult[j]) > bars[i]) {
                    bars[i] = (int) Math.round(freqencyMagnatude);
                }
                j = j + 2; // real and imaginary part
            }
            i++;
        }

        // draw a spectrum
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(backgroundImage, 0, 0, width, height, 0, 0, width, height,
                null);
        g.setColor(barColor);

        i = 0;
        int x;
        int half = height / 2;
        // int barWidth = 18;
        // Attempt to scale bars horizontally
        int barWidth = (width / barCount);
        int fftSize = (barCount * barWidth);
        int whiteSpace = width - fftSize;
        int offset = whiteSpace / 2;

        BarDrawer barDrawer = null;
        if (barStyle.equals(MainController.BAR_STYLE_THICK_BROCK)) {
            barDrawer = new ThickBlockBarDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_OUTLINE_BLOCK)) {
            barDrawer = new OutlinBlockBarDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_THIN)) {
            barDrawer = new ThinBarDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_ROUND_BLOCK)) {
            barDrawer = new RoundBlockBarDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_ROUND_OUTLINE)) {
            barDrawer = new RoundOutlineBarDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_DEPTH_BLOCK)) {
            barDrawer = new PopUpBlockDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_DEPTH_BLOCK2)) {
            barDrawer = new EtchedBlockDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_OVAL_FILLED)) {
            barDrawer = new OvalFilledDrawer(g, half, barWidth);
        } else if (barStyle.equals(MainController.BAR_STYLE_OVAL_OUTLINE)) {
            barDrawer = new OvalOutlineDrawer(g, half, barWidth);
        }

        while (i < barCount) {
            // x = 60+(i*barWidth);
            // Manage inset from left
            x = (barWidth / 2) + offset + (i * barWidth); // barwidth/2 keeps
            // bars from
            // clipping on left,
            // offset centers
            // the fft as best
            // it can (you using
            // integers made a
            // perfect scale
            // really hard)
            barDrawer.drawBar(bars[i], x);
            i++;
        }
        return img;
    }
}
