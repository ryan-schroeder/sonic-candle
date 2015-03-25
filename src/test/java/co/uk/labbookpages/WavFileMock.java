package co.uk.labbookpages;

public class WavFileMock extends WavFile {

	public static long SAMPLE_RATE = 300;
	public static long NUM_FRAMES = 100;

	public WavFileMock() {
		super();
		return;
	}

	@Override
	public long getSampleRate() {
		return SAMPLE_RATE;
	}

	@Override
	public long getNumFrames() {
		return NUM_FRAMES;
	}
}
