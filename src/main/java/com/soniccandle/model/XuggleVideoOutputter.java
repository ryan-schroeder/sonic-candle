package com.soniccandle.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import co.uk.labbookpages.WavFile;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.demos.MediaConcatenator;
import com.xuggle.xuggler.ICodec;

public class XuggleVideoOutputter extends VideoOutputter {

	private static final int VIDEO_STREAM_INDEX = 0;
	private static final int AUDIO_STREAM_INDEX = 1;
	private IMediaWriter writer;
	public long frameNumber = 0;
	public long nsecsPerFrame;

	public XuggleVideoOutputter(File audioFile, File outputTo) {
		super(audioFile, outputTo);
	}

	public static BufferedImage convertImageType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		} else {
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}

	@Override
	public void start() throws Exception {
		System.out.println("CAA");
		frameNumber = 0;
		nsecsPerFrame = (long) (((double) 1) / ((double) frameRate) * ((double) 1000000000));

		WavFile wavFile = WavFile.openWavFile(audioFile);
		System.out.println("CAB");
		int audioStreamId = 0;
		int channelCount = wavFile.getNumChannels();
		int sampleRate = (int) wavFile.getSampleRate(); // Hz
		wavFile.close();
		System.out.println("CAC");
                System.out.println(audioFile.getPath());
                System.out.println("CAC2");
		IMediaReader audioReader;
		System.out.println("CAC3");
		try {
			audioReader = ToolFactory.makeReader(audioFile.getPath());
		} catch (Exception e) {
			System.out.println("Error");
			System.out.println(e.getMessage());
		}
		System.out.println("CAD");
		MediaConcatenator concatenator = new MediaConcatenator(AUDIO_STREAM_INDEX, VIDEO_STREAM_INDEX);
		audioReader.addListener(concatenator);
		System.out.println("CAE");
		writer = ToolFactory.makeWriter(outputTo.getPath());
		System.out.println("CAF");
		concatenator.addListener(writer);
		System.out.println("CAG");
		writer.addVideoStream(VIDEO_STREAM_INDEX, 0, ICodec.ID.CODEC_ID_MPEG4, width, height);
		System.out.println("CAH");
		writer.addAudioStream(AUDIO_STREAM_INDEX, audioStreamId, channelCount, sampleRate);
		System.out.println("CAI");
		while (audioReader.readPacket() == null) {
		} // Read in the full audio
		System.out.println("CAJ");
	}

	@Override
	public void addFrame(BufferedImage frame) throws Exception {
		BufferedImage convertedFrame = convertImageType(frame, BufferedImage.TYPE_3BYTE_BGR);
		writer.encodeVideo(0, convertedFrame, (frameNumber * nsecsPerFrame), TimeUnit.NANOSECONDS);
		frameNumber++;
	}

	@Override
	public void finish() {
		writer.close();
	}

}
