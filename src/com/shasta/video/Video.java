package com.shasta.video;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    private boolean useColor;
    private int refreshTime;

    public Video (boolean useColor, int refreshTime){

        // get default webcam and open it
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));

        converter = new Img2Ascii();
        this.useColor = useColor;
        this.refreshTime = refreshTime;

        if(useColor){
            this.refreshTime += 200;
        }
    }

    @Override
    public void run() {
        webcam.open();
        while(true) {
            BufferedImage image = webcam.getImage();
            try {
                String asciiimg = converter.convertToAscii(image, 256, useColor);
                MultiThreadedServer.getClients().forEach(client -> {
                    try {
                        client.clearLines(converter.getHeight());
                        client.sendMessage(asciiimg);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                });
                sleep(refreshTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
