package org.example;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    private static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double rectangle2D) {
        rectangle2D.x = -2;
        rectangle2D.y = -1.5;
        rectangle2D.height = 3;
        rectangle2D.width = 3;
    }
    public int numIterations(double x, double y) {
        int i = 0;
        for (double real = 0, complex = 0, temp = 0; i < MAX_ITERATIONS && real * real + complex * complex < 4;
             temp = real, real = temp * temp - complex * complex + x, complex = 2 * temp * complex + y, i++);
        return i < MAX_ITERATIONS ? i : -1;
    }
    @Override
    public String toString() {
        return "Mandelbrot";
    }
}
