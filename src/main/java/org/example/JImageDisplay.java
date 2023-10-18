package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{
    public BufferedImage bufferedImage;
    public JImageDisplay(int height, int width) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        super.setPreferredSize(new Dimension(width,height));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
    }
    public void clearImage() {
        for (int width = 0, height, stopByWidth = bufferedImage.getWidth(), stopByHeight = bufferedImage.getHeight();
             width < stopByWidth; width++) {
            for (height = 0; height < stopByHeight; height++) {
                bufferedImage.setRGB(width, height, 0);
            }
        }
    }
    public void drawPixel(int x, int y, int rgbColor) {
        bufferedImage.setRGB(x, y, rgbColor);
    }
}
