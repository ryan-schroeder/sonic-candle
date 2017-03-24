package com.dakkra.wav;

import org.junit.*;

public class LittleEndianTest {

    @Test
    public void testLittleEndian() {
        int bigEndian15 = 0x0000000f;
        int littleEndian15 = 0x0f000000;

        short bigEndian7 = 0x0007;
        short littleEndian7 = 0x0700;

        Assert.assertEquals(bigEndian15, new LittleEndianInt(littleEndian15).convert());
        Assert.assertEquals(bigEndian7, new LittleEndianShort(littleEndian7).convert());
    }

}
