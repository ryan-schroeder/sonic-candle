package com.soniccandle.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.soniccandle.util.StereoData;
import com.soniccandle.util.Utils;

public class UtilsTest {

    public void assertDoubleArrayEquals(double[] expected, double[] actual) {
        assertEquals(expected.length, actual.length);
        int i = 0;
        while (i < expected.length) {
            assertEquals(expected[i], actual[i], 0);
            i++;
        }
    }

    @Test
    public void test_double_add_zeros() {
        double[] input = {1, 2, 3};
        double[] expected = {1, 0, 2, 0, 3, 0};
        double[] output = Utils.doubleAddZeros(input);
        assertDoubleArrayEquals(expected, output);
    }

    @Test
    public void test_split_channels() {
        double[] input = {0, 1, 0, 1, 0, 1};
        double[] expectedRight = {0, 0, 0};
        double[] expectedLeft = {1, 1, 1};
        StereoData result = Utils.splitChannels(input);
        assertDoubleArrayEquals(expectedRight, result.right);
        assertDoubleArrayEquals(expectedLeft, result.left);
    }
}
