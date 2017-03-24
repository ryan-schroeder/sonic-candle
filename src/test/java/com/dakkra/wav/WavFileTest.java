package com.dakkra.wav;

import org.junit.*;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class WavFileTest {

    @Test
    public void test_byte_to_int() {
        int testOne = 10;
        int testTwo = 50;
        int testThree = 0xffffff;
        int testFour = 0x000fff;
        int testFive = 0xff00ff00;

        byte[] t1bytes = ByteBuffer.allocate(4).putInt(testOne).array();
        byte[] t2bytes = ByteBuffer.allocate(4).putInt(testTwo).array();
        byte[] t3bytes = ByteBuffer.allocate(4).putInt(testThree).array();
        byte[] t4bytes = ByteBuffer.allocate(4).putInt(testFour).array();
        byte[] t5Bytes = {(byte) 0xff, (byte) 0x00, (byte) 0xff, (byte) 0x00};

        InputStream is = null;
        WavFile wf = new WavFile(is);

        int testOneResult = wf.bytesToInt(t1bytes);
        int testTwoResult = wf.bytesToInt(t2bytes);
        int testThreeResult = wf.bytesToInt(t3bytes);
        int testFourResult = wf.bytesToInt(t4bytes);
        int testFiveResult = wf.bytesToInt(t5Bytes);

        Assert.assertEquals(testOne, testOneResult);
        Assert.assertEquals(testTwo, testTwoResult);
        Assert.assertEquals(testThree, testThreeResult);
        Assert.assertEquals(testFour, testFourResult);
        Assert.assertEquals(testFive, testFiveResult);
    }

}
