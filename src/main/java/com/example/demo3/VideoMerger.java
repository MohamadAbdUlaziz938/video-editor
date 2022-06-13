package com.example.demo3;

import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VideoMerger extends VideoFramer {
    VideoMerger(File file) throws IOException {
        super(file);
    }

    public File perform(File file) throws FrameRecorder.Exception, FrameGrabber.Exception {
        FFmpegFrameGrabber newGrabber = new FFmpegFrameGrabber(file);
        this.frameRecorder.start();
        this.process(this.frameGrabber);
        newGrabber.start();
        this.process(newGrabber);
        this.frameGrabber.stop();
        this.frameRecorder.stop();
        newGrabber.stop();
        return this.processedFile;
    }

    private void process(FFmpegFrameGrabber frameGrabber) throws FrameGrabber.Exception, FrameRecorder.Exception {
        Java2DFrameConverter c = new Java2DFrameConverter();
        while (true) {
            Frame frame = frameGrabber.grab();
            if (frame == null) break;
            BufferedImage bi = c.getBufferedImage(frame);
            if (bi != null) {
                this.frameRecorder.record(frame);
            }

        }

    }
}
