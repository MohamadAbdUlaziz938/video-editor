package com.example.demo3;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class VideoImages extends VideoFramer {
    public VideoImages(File file) throws IOException {
        super(file);
    }

    public List<BufferedImage> perform() throws FrameRecorder.Exception, FrameGrabber.Exception {
        this.frameRecorder.start();
        int i = 0;
        Java2DFrameConverter c = new Java2DFrameConverter();
        List<BufferedImage> images = new LinkedList<>();
        int frameRate = (int) this.frameGrabber.getFrameRate();
        while (true) {
            Frame frame = this.frameGrabber.grab();
            if (frame == null) break;


            BufferedImage bi = c.getBufferedImage(frame);

            if (bi != null) {
                images.add(bi);
            }

        }
        this.frameGrabber.stop();
        this.frameRecorder.stop();
        return images;
    }
}
