package com.dakkra.wav;

public class WavFileException extends Exception {
    public WavFileException() {
    }

    public WavFileException(String message) {
        super(message);
    }

    public WavFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public WavFileException(Throwable cause) {
        super(cause);
    }

    public WavFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
