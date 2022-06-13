package com.example.demo3;

import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.io.File;

public class VideoReader {

    public static FFmpegFrameGrabber read(String path) {
        File file = new File(path);
        return new FFmpegFrameGrabber(file);
    }
}
