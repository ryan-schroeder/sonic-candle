package com.soniccandle.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import co.uk.labbookpages.WavFileException;
import co.uk.labbookpages.WavFileMock;

public class SpectrumRendererTest {
	
	public static int VIDEO_FRAME_RATE = 30;
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	
	private SpectrumRenderer getSpectrumRendererShunt() throws IOException, WavFileException {
		SpectrumRenderer s = new SpectrumRendererShunt(null, VIDEO_FRAME_RATE, WIDTH, HEIGHT, null);
		s.outputter = new VideoOutputterShunt(null, null);
		return s;
		
	}
	
	@Test
	public void test_frames_per_video_frame_is_calculated_by_constructor() throws IOException, WavFileException {
		SpectrumRenderer s = getSpectrumRendererShunt();
		assertEquals(WavFileMock.SAMPLE_RATE/VIDEO_FRAME_RATE, s.framesPerVFrame);
	}
	
	@Test
	public void test_total_video_frames_is_calculated_on_start() throws Exception {
		SpectrumRenderer s = getSpectrumRendererShunt();
		s.start();
		assertEquals(WavFileMock.NUM_FRAMES/s.framesPerVFrame, s.totalVFrames);
	}
	
	@Test
	public void test_check_done_sets_isdone_to_true_when_done() throws IOException, WavFileException {
		SpectrumRenderer s = getSpectrumRendererShunt();
		s.currentVFrame = 100;
		s.totalVFrames = 100;
		s.checkDone();
		assertEquals(true, s.isDone);
	}
	
	@Test
	public void test_check_done_sets_isdone_to_false_when_not_done() throws IOException, WavFileException {
		SpectrumRenderer s = getSpectrumRendererShunt();
		s.currentVFrame = 42;
		s.totalVFrames = 100;
		s.checkDone();
		assertEquals(false, s.isDone);
	}
	
	@Test
	public void test_rendr_next_frame_increases_current_frame() throws Exception {
		SpectrumRenderer s = getSpectrumRendererShunt();
		s.totalVFrames = 100;
		s.renderNextFrame();
		assertEquals(1, s.currentVFrame);
	}
	
	@Test
	public void test_render_all_goes_until_done() throws Exception {
		SpectrumRenderer s = getSpectrumRendererShunt();
		s.totalVFrames = 100;
		s.renderAll();
		assertEquals(true, s.isDone);
		
	}
	
}
