package com.soniccandle.controller;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JButton;

import org.junit.Test;

import com.soniccandle.model.MainModel;

public class MainControllerTest {

    @Test
    public void test_giving_valid_inputs_enables_render_button() {
        MainController c = new MainController();
        c.m = new MainModel();
        c.m.renderButton = new JButton();
        c.m.renderButton.setEnabled(false);
        c.m.audioFile = new File("/dummy");
        c.m.outputTo = new File("/dummy");
        c.allowRenderIfReady();
        assertTrue(c.m.renderButton.isEnabled());
    }
}
