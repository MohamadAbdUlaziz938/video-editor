package com.example.demo3;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VideoWaterMarker extends VideoFramer {


    VideoWaterMarker(File file) throws IOException {
        super(file);
    }

    public File perform(File image) throws IOException {
        this.frameRecorder.start();
        Java2DFrameConverter c = new Java2DFrameConverter();
        BufferedImage bufferedImage = (ImageIO.read(image));
        while(true){
            Frame frame = this.frameGrabber.grab();
            if (frame == null) break;
            BufferedImage bi = c.getBufferedImage(frame);
            if(bi != null){
                this.frameRecorder.record(this.addPhoto(frame,bufferedImage));
            }

        }
        this.frameGrabber.stop();
        this.frameRecorder.stop();
        return this.processedFile;
    }
    private Frame addPhoto(Frame frame , BufferedImage watermarkImage) throws IOException {
        Java2DFrameConverter c = new Java2DFrameConverter();
        BufferedImage bf = c.getBufferedImage(frame);
        Graphics2D g2d = (Graphics2D) bf.getGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        g2d.setComposite(alphaComposite);
        int topLeftX = (bf.getWidth() - watermarkImage.getWidth()) / 2;
        int topLeftY = (bf.getHeight() - watermarkImage.getHeight()) / 2;

        g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);
        //   ImageIO.write(bf, "png",new File(i + ".png"));
        g2d.dispose();

        //    System.out.println("The tex watermark is added to the image.");
        //  ImageIO.write(c.getBufferedImage(frame),"png",new File("y.png"));
        return c.getFrame(bf);
        // g.drawImage(image,1,1,1);
    }
}
