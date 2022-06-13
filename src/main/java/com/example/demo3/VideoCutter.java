package com.example.demo3;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VideoCutter extends VideoFramer {
    VideoCutter(File file) throws IOException {
        super(file);
    }

    public File perform(int start, int end) throws FrameGrabber.Exception, FrameRecorder.Exception {
        this.frameRecorder.start();
        int i = 0;
        Java2DFrameConverter c = new Java2DFrameConverter();
        int frameRate = (int)this.frameGrabber.getFrameRate();
        while(true){
            Frame frame = this.frameGrabber.grab();
            if (frame == null) break;


            BufferedImage bi = c.getBufferedImage(frame);

            if(bi != null){
                i++;
                if((i / frameRate) >= start && (i / frameRate) <= end)continue;
                this.frameRecorder.record(frame);
            }

        }
        this.frameGrabber.stop();
        this.frameRecorder.stop();
        return this.processedFile;
    }
}
