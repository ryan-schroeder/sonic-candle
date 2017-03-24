package com.dakkra.wav;

public class LittleEndianInt {
    private int value;

    public LittleEndianInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Converts little endian value to big endian
     * @return Big Endian int
     */
    public int convert() {
        //Flip all bits via shifting and masking
        return value << 24 | value >> 8 & 0xff00 | value << 8 & 0xff0000 | value >>> 24;
    }

    public static int getBigEndianFromInt(int integer) {
        return integer << 24 | integer >> 8 & 0xff00 | integer << 8 & 0xff0000 | integer >>> 24;
    }
}
