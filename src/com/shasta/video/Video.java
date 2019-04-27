package com.shasta.video;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.shasta.img.Img2Ascii;
import com.shasta.threaded.MultiThreadedServer;

import static java.lang.Thread.sleep;


/**
 * Example of how to take single picture.
 *
 * @author Bartosz Firyn (SarXos)
 */
public class Video implements Runnable{
    private Webcam webcam;
    private Img2Ascii converter;

    public Video (){

        // get default webcam and open it
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));

        converter = new Img2Ascii();

        // get image

        // save image to PNG file
        //ImageIO.write(image, "PNG", new File("test.png"));

    }

    @Override
    public void run() {
        webcam.open();
        while(true) {
            BufferedImage image = webcam.getImage();
            try {
                String asciiimg = converter.convertToAscii(image, 640);
                MultiThreadedServer.getClients().forEach(client -> {
                    try {
                        client.sendMessage(asciiimg);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                });
                sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
