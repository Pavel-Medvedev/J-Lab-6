package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class FractalExplorer {
    private static int SIZE = 800;
    private int jImageDisplaySize;
    private JImageDisplay jImageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double rectangle2D;
    private JFrame jFrame;
    
    public FractalExplorer(int size) {
        jImageDisplaySize = size;
        fractalGenerator = new Mandelbrot();
        rectangle2D = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(rectangle2D);
        jImageDisplay = new JImageDisplay(jImageDisplaySize, jImageDisplaySize);
        jFrame = new JFrame("Fractal Explorer.");
    }
    public void createAndShowGUI() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

/*        JTextArea jTextArea = new JTextArea("Mandelbrot fractal is default.\n" +
                "Switch fractal or press Reset to draw it.");
        Font font = new Font("Arial", Font.BOLD, 32);
        jTextArea.setFont(font);
        jTextArea.setForeground(Color.GRAY);
        jFrame.add(jTextArea, BorderLayout.CENTER); */

        jFrame.add(jImageDisplay, BorderLayout.CENTER);
        drawFractal();
        JButton resetButton = new JButton("Reset");
        JButton saveButton = new JButton("Save Image");
        JPanel toolkitPanel = new JPanel();
        toolkitPanel.add(saveButton);
        toolkitPanel.add(resetButton);
        jFrame.add(toolkitPanel, BorderLayout.SOUTH);

        JLabel jLabel = new JLabel("Fractals");

        JComboBox jComboBox = new JComboBox();
        Mandelbrot mandelbrot = new Mandelbrot();
        Tricorn tricorn = new Tricorn();
        BurningShip burningShip = new BurningShip();
        jComboBox.addItem(mandelbrot);
        jComboBox.addItem(tricorn);
        jComboBox.addItem(burningShip);

        JPanel jPanel = new JPanel();
        jPanel.add(jLabel);
        jPanel.add(jComboBox);
        jFrame.add(jPanel, BorderLayout.NORTH);

        jImageDisplay.addMouseListener(new MouseHandler());
        Handler handler = new Handler();
        jComboBox.addActionListener(handler);
        resetButton.addActionListener(handler);
        saveButton.addActionListener(handler);


        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setBounds((toolkit.getScreenSize().width/2-SIZE/2),
                (int) (toolkit.getScreenSize().height/2-SIZE/2*1.1), SIZE, SIZE);
        jFrame.setResizable(false);
    }

    private class Handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() instanceof JComboBox) {
                JComboBox sourceBox = (JComboBox) event.getSource();
                fractalGenerator = (FractalGenerator) sourceBox.getSelectedItem();
                fractalGenerator.getInitialRange(rectangle2D);
                drawFractal();
            } else {
                if (event.getActionCommand().equals("Reset")) {
                    fractalGenerator.getInitialRange(rectangle2D);
                    drawFractal();
                } else {
                    try {
                        JFileChooser chooser = new JFileChooser();
                        FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                        chooser.setFileFilter(filter);
                        chooser.setAcceptAllFileFilterUsed(false);
                        if (chooser.showSaveDialog(jImageDisplay) == JFileChooser.APPROVE_OPTION) {
                            ImageIO.write(jImageDisplay.bufferedImage, "png", chooser.getSelectedFile());
                            JOptionPane.showMessageDialog(jImageDisplay, "Successfully Saved");
                        }
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(jImageDisplay, exception.getMessage(),
                                "Saving image raise exception", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void drawFractal() {
        for (int x = 0, y = 0; x < jImageDisplaySize; x++, y = 0)
            for (float i = (float) fractalGenerator.numIterations(FractalGenerator.getCoord(rectangle2D.x, rectangle2D.x +
                rectangle2D.width, jImageDisplaySize, x), FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y +
                rectangle2D.height, jImageDisplaySize, y)); y < jImageDisplaySize; y++, i = (float) fractalGenerator.numIterations(FractalGenerator.getCoord(rectangle2D.x, rectangle2D.x +
                rectangle2D.width, jImageDisplaySize, x), FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y +
                rectangle2D.height, jImageDisplaySize, y)))
                    jImageDisplay.drawPixel(x, y, i < 0 ?256:Color.HSBtoRGB(0.7f + i / 200f, 1f, 1f));
        jImageDisplay.repaint();
    }

    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent mouseEvent) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            fractalGenerator.recenterAndZoomRange(rectangle2D, FractalGenerator.getCoord(rectangle2D.x,rectangle2D.x + rectangle2D.width, jImageDisplaySize, x),
                    FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y + rectangle2D.height, jImageDisplaySize, y), 0.5);
            drawFractal();
        }
    }
    public static void main(String[] args) {
        FractalExplorer fractalGenerator = new FractalExplorer(SIZE);
        fractalGenerator.createAndShowGUI();
    }
}