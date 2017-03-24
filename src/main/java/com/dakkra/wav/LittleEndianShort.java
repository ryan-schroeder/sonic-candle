package com.dakkra.wav;

public class LittleEndianShort {
    private short value;

    public LittleEndianShort(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    /**
     * Converts little endian to big endian
     * @return Big Endian short
     */
    public short convert() {
        return (short) (((value & 0xff) << 8) | (((value >> 8) & 0xff)));
    }

    public static short getBigEndianFromShort(short s) {
        return (short) (((s & 0xff) << 8) | (((s >> 8) & 0xff)));
    }
}
