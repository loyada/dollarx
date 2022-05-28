package com.github.loyada.jdollarx.visual;

import java.awt.*;

import static java.lang.Math.abs;


public class ImageUtils {

    static boolean avgIsSignificantlyDifferent(int x1, int x2, int x3, int y1, int y2, int y3) {
        Color cx1 = new Color(x1, false);
        Color cx2 = new Color(x2, false);
        Color cx3 = new Color(x3, false);
        Color cy1 = new Color(y1, false);
        Color cy2 = new Color(y2, false);
        Color cy3 = new Color(y3, false);
        Color cx = new Color(
                (cx1.getRed()+cx2.getRed()+cx3.getRed())/3,
                (cx1.getGreen()+cx2.getGreen()+cx3.getGreen())/3,
                (cx1.getBlue()+cx2.getBlue()+cx3.getBlue())/3
        );
        Color cy = new Color(
                (cy1.getRed()+cy2.getRed()+cy3.getRed())/3,
                (cy1.getGreen()+cy2.getGreen()+cy3.getGreen())/3,
                (cx1.getBlue()+cy2.getBlue()+cy3.getBlue())/3
        );
        YUV yuvX = YUV.fromRGB(cx.getRed(), cx.getGreen(), cx.getBlue());
        YUV yuvY = YUV.fromRGB(cy.getRed(), cy.getGreen(), cy.getBlue());
        return yuvX.isSignificantlyDifferentFrom(yuvY) || colorSignificantlyDifferent(cx, cy);

    }

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