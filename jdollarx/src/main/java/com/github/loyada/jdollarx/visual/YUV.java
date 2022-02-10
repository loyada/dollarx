package com.github.loyada.jdollarx.visual;

import static java.lang.Math.abs;

class YUV {
    private final float y, u, v;

    private YUV(float y, float u, float v) {
        this.y = y;
        this.u = u;
        this.v = v;
    }

    // normalize y,u,v to have a range of ~1
    public static YUV fromRGB(int r, int g, int b) {
        float rf = (float) r / 255;
        float gf = (float) g / 255;
        float bf = (float) b / 255;

        return new YUV(
                (float) (0.299 * rf + 0.587 * gf + 0.114 * bf),
                (float) (-0.14713 * rf - 0.28886 * gf + 0.436 * bf),
                (float) (0.615 * rf - 0.51499 * gf - 0.10001 * bf));
    }

    public boolean isSignificantlyDifferentFrom(YUV other) {
        float ydiff = abs(y - other.y);
        float udiff = abs(u - other.u);
        float vdiff = abs(v - other.v);
        return (ydiff > 0.1 || udiff > 0.25 || vdiff > 0.25);
    }

    public boolean isSignificantlyHigher(YUV other) {
        float ydiff = (y - other.y);
        float udiff = (u - other.u);
        float vdiff = (v - other.v);
     //   return (ydiff > 0.1 && (ydiff/y>2 || ydiff/other.y>2) ||  udiff>0.25 || vdiff > 0.25);
        return (ydiff > 0.05 ||  udiff>0.15 || vdiff > 0.15);
    }
}