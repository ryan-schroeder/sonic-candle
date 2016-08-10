package com.soniccandle.model;

import java.awt.image.BufferedImage;
import com.soniccandle.model.BackgroundConjuror;

public class StillBgConjuror implements BackgroundConjuror {

	public BufferedImage stillImage;

	public StillBgConjuror(BufferedImage stillImage) {
		this.stillImage = stillImage;
	}

	public BufferedImage conjure() {
		return stillImage;
	}

}
