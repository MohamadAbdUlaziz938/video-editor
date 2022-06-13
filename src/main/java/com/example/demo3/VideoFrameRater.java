package com.example.demo3;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VideoFrameRater extends VideoFramer {
    VideoFrameRater(File file) throws IOException {
        super(file);
    }

    public File perform(int frameRate) throws FrameRecorder.Exception, FrameGrabber.Exception {
        this.frameRecorder.setFrameRate(frameRate);

        this.frameRecorder.start();
        Java2DFrameConverter c = new Java2DFrameConverter();
        while (true) {
            Frame frame = this.frameGrabber.grab();
            if (frame == null) break;


            BufferedImage bi = c.getBufferedImage(frame);

            if (bi != null) {
                this.frameRecorder.record(frame);
            }

        }
        this.frameGrabber.stop();
        this.frameRecorder.stop();
        return this.processedFile;
    }
}
