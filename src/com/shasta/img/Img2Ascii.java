package com.shasta.img;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.Buffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

public class Img2Ascii {

    private BufferedImage img;
    private double pixval;
    private PrintWriter prntwrt;
    private FileWriter filewrt;

    public static Map<String, Integer> rgbToAnscii;

    public int getHeight() {
        return height;
    }

    private int height;

    public Img2Ascii() {
        rgbToAnscii = new HashMap<String, Integer>();
        //rgbToAnscii.put()
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }


    public String convertToAscii(URL imgUrl, int desiredWidth) throws Exception{
        try {
            BufferedImage tmp = ImageIO.read(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertToAscii(img, desiredWidth);
    }

    public String convertToAscii(BufferedImage tmp, int desiredWidth) throws Exception{
        try {
            double change = desiredWidth / (double) tmp.getWidth();
            height = (int) (tmp.getHeight() * change * .33);
            img = resize(tmp, height, desiredWidth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color pixcol = new Color(img.getRGB(j, i));
                pixval = (((pixcol.getRed() * 0.30) + (pixcol.getBlue() * 0.59) + (pixcol
                        .getGreen() * 0.11)));
                //if((i+j) % 10 == 0) {
                //    sb.append(String.format("\u001B[38;5;%dm", (int) pixval));
                //}
                sb.append(strChar(255-pixval));
                //sb.append("\e[0m");

            }
            try {
                sb.append("\n");
            } catch (Exception ex) {
            }
        }

        return sb.toString();
    }

    public String strChar(double g) {
        String str = " ";

        if (g >= 240) {
            str = " ";
        } else if (g >= 210) {
            str = ".";
        } else if (g >= 190) {
            str = "*";
        } else if (g >= 170) {
            str = "+";
        } else if (g >= 120) {
            str = "^";
        } else if (g >= 110) {
            str = "&";
        } else if (g >= 80) {
            str = "8";
        } else if (g >= 60) {
            str = "#";
        } else {
            str = "@";
        }
        return str;
    }



//    public void print(String str) {
//        try {
//            prntwrt.print(str);
//            prntwrt.flush();
//            filewrt.flush();
//        } catch (Exception ex) {
//        }
//    }

//    public static void main(String[] args) {
//        Img2Ascii obj = new Img2Ascii();
//        obj.convertToAscii("/Users/chandler/Desktop/test1.jpg");
//    }
}
