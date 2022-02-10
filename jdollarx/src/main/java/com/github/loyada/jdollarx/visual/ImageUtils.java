package com.github.loyada.jdollarx.visual;

import java.awt.*;


public class ImageUtils {

    static boolean pixelValueIsSignificantlyDifferent(int rgb1, int rgb2) {
        if (rgb1==rgb2 || (rgb1 & 0xfefefe) == (rgb2 & 0xfefefe))
            return false;

        Color c1 = new Color(rgb1, false);
        YUV yuv1 = YUV.fromRGB(c1.getRed(), c1.getGreen(), c1.getBlue());
        Color c2 = new Color(rgb2, false);
        YUV yuv2 = YUV.fromRGB(c2.getRed(), c2.getGreen(), c2.getBlue());
        return yuv1.isSignificantlyDifferentFrom(yuv2);
    }
}