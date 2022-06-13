package com.example.demo3;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageToVideo {
    private FFmpegFrameRecorder fFmpegFrameRecorder;
    private File processedFile;
    ImageToVideo(){
        this.processedFile = new File("x.mp4");
        this.fFmpegFrameRecorder = new FFmpegFrameRecorder(this.processedFile,1024,1024);
        this.fFmpegFrameRecorder.setFrameRate(1);
        this.fFmpegFrameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        this.fFmpegFrameRecorder.setFormat("mp4");
    }
    public File perform(List<File> files) throws IOException {
        this.fFmpegFrameRecorder.start();
        for(File image : files){
            this.process(image);
        }
        this.fFmpegFrameRecorder.stop();
        return this.processedFile;
    }
    private void process(File file) throws IOException {
        Java2DFrameConverter c = new Java2DFrameConverter();
        this.fFmpegFrameRecorder.record(c.getFrame(ImageIO.read(file)));
    }
}
