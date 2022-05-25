package com.github.loyada.jdollarx.visual;

import java.awt.*;

import static java.lang.Math.abs;


public class ImageUtils {

    static boolean pixelValueIsSignificantlyDifferent(int rgb1, int rgb2) {
        if (rgb1==rgb2 || (rgb1 & 0xfefefe) == (rgb2 & 0xfefefe))
            return false;

        Color c1 = new Color(rgb1, false);
        YUV yuv1 = YUV.fromRGB(c1.getRed(), c1.getGreen(), c1.getBlue());
        Color c2 = new Color(rgb2, false);
        YUV yuv2 = YUV.fromRGB(c2.getRed(), c2.getGreen(), c2.getBlue());
        return yuv1.isSignificantlyDifferentFrom(yuv2) || colorSignificantlyDifferent(c1, c2);
    }

    private static boolean colorSignificantlyDifferent(Color c1, Color c2) {
       int bDiff = c1.getBlue() - c2.getBlue();
       int rDiff = c1.getRed() - c2.getRed();
       int gDiff = c1.getGreen() - c2.getGreen();
       return bDiff*bDiff + rDiff*rDiff + gDiff*gDiff > 100*100;
    }
}