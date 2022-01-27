package com.github.loyada.jdollarx.visual;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

import static java.lang.String.format;

public class IdentityComparator implements BiConsumer<BufferedImage, BufferedImage> {

    public IdentityComparator() {
    }

    @Override
    public void accept(BufferedImage refImage, BufferedImage actualImage) {
        for (int y = 0; y < actualImage.getHeight(); y++) {
            for (int x = 0; x < actualImage.getWidth(); x++) {
                if (refImage.getRGB(x, y) != actualImage.getRGB(x, y))
                    throw new AssertionError(format("found a different pixel at %d, %d",x ,y));
            }
            }
        }
}
