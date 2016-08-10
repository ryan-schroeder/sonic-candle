package com.soniccandle.model;

import java.awt.image.BufferedImage;
import com.soniccandle.model.BackgroundConjuror;
import com.soniccandle.model.ImageSequence;

public class ImageSequenceBgConjuror implements BackgroundConjuror {

	public ImageSequence imageSequence;

	public ImageSequenceBgConjuror(ImageSequence imageSequence) {
		this.imageSequence = imageSequence;
	}

	public BufferedImage conjure() {
		return imageSequence.getNextFrame();
	}

}
