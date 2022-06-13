package com.example.demo3;

import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class VideoMover extends VideoFramer {
    VideoMover(File file) throws IOException {
        super(file);
    }

    public File perform(int start, int end, int goal) throws IOException {
        this.frameRecorder.start();
        int i = 0;
        int x;
        Java2DFrameConverter c = new Java2DFrameConverter();
        List<Frame> cutFrames = new LinkedList<>();
        int frameRate = (int) this.frameGrabber.getFrameRate();
        boolean ok = false;

        while (true) {
            Frame frame = this.frameGrabber.grab();
            if (frame == null) break;

            BufferedImage bi = c.getBufferedImage(frame);
            if (bi == null) continue;
            if ((i / frameRate) >= start && !ok) {
                x = i;
                while ((x / frameRate) <= end) {

                    frame = this.frameGrabber.grab();
                    bi = c.getBufferedImage(frame);
                    if (bi != null) {
                        cutFrames.add(frame.clone());
                        x++;
                    }
                }
                i++;
                ok = true;
                continue;
            }
            i++;

            this.frameRecorder.record(frame);
            if ((i / frameRate) == goal) {
                for (Frame frame1 : cutFrames) {
                    this.frameRecorder.record(frame1);
                }
                cutFrames.clear();
            }
        }
        if (!cutFrames.isEmpty()) {
            for (Frame frame1 : cutFrames) {
                this.frameRecorder.record(frame1);
            }
        }
        this.frameGrabber.stop();
        this.frameRecorder.stop();
        return this.processedFile;
    }

}
