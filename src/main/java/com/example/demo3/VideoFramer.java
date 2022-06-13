package com.example.demo3;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class VideoFramer {
    protected final List<Frame> frames;
    protected final FFmpegFrameGrabber frameGrabber;
    protected FFmpegFrameRecorder frameRecorder;
    protected File processedFile;

    VideoFramer(File file) throws IOException {
        this.frames = new LinkedList<Frame>();
        this.frameGrabber = new FFmpegFrameGrabber(file);
        frameGrabber.start();
         this.processedFile =  new File(Math.random() + ".mp4");
        this.frameRecorder = new FFmpegFrameRecorder(this.processedFile, this.frameGrabber.getImageWidth(), this.frameGrabber.getImageHeight(),this.frameGrabber.getAudioChannels());
        this.setRecorderDependencies(this.frameRecorder);
    }


    protected void setRecorderDependencies(FFmpegFrameRecorder f) throws FrameGrabber.Exception {
       // this.frameGrabber.start();
//        f.setVideoCodec();
//        f.setAudioChannels(this.frameGrabber.getAudioChannels());
//        f.setAudioBitrate(this.frameGrabber.getAudioBitrate());
//        f.setAudioCodec(this.frameGrabber.getAudioCodec());
//        f.setAudioMetadata(this.frameGrabber.getAudioMetadata());
//        f.setAudioOptions(this.frameGrabber.getAudioOptions());
//        f.setFormat("mp4");
        f.setSampleRate(this.frameGrabber.getSampleRate());
        f.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        f.setFrameRate(this.getFrameGrabber().getFrameRate());
        f.setFormat("mp4");
//        f.setTimestamp(this.frameGrabber.getTimestamp());
//        f.setSampleRate(this.frameGrabber.getSampleRate());
       // f.setImageHeight(this.frameGrabber.getImageHeight());
      //  f.setImageWidth(this.frameGrabber.getImageWidth());
    }


    public List<Frame> getFrames() {
        return this.frames;
    }

    public FFmpegFrameGrabber getFrameGrabber() {
        return this.frameGrabber;
    }
}
