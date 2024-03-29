package com.github.loyada.jdollarx.visual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import static com.github.loyada.jdollarx.visual.ImageUtils.pixelValueIsSignificantlyDifferent;
import static java.lang.String.format;

public class SimilarityComparator  implements BiConsumer<BufferedImage, BufferedImage> {
    private final int maxBadPixelsRatio;

    public SimilarityComparator(int maxBadPixelsRatio) {
        this.maxBadPixelsRatio = maxBadPixelsRatio;
    }

    private static boolean noTransitionFound(
            int actualPixOneSide, int actual, int actualPixOtherSide,
            int refPixOneSide, int ref, int refPixOtherSize
    ) {
        boolean foundTransition  = (pixelValueIsSignificantlyDifferent(actualPixOneSide, actualPixOtherSide) &&
                pixelValueIsSignificantlyDifferent(refPixOneSide, refPixOtherSize)) ||
                (pixelValueIsSignificantlyDifferent(actualPixOneSide, actual) &&
                pixelValueIsSignificantlyDifferent(refPixOneSide, ref)) ||
                (pixelValueIsSignificantlyDifferent(actualPixOtherSide, actual) &&
                        pixelValueIsSignificantlyDifferent(refPixOtherSize, ref));

        return !foundTransition || ImageUtils.avgIsSignificantlyDifferent(actualPixOneSide, actual, actualPixOtherSide,
                refPixOneSide, ref, refPixOtherSize);
    }

    static boolean pixelMismatch(BufferedImage refImage, BufferedImage actualImage, int x, int y) {
        int actual = actualImage.getRGB(x, y);
        int ref = refImage.getRGB(x, y);
        if  (!pixelValueIsSignificantlyDifferent(actual, ref)) {
            return false;
        }

        return (noTransitionFound(
                actualImage.getRGB(x - 1, y - 1),
                actual,
                actualImage.getRGB(x + 1, y + 1),
                refImage.getRGB(x - 1, y - 1),
                ref,
                refImage.getRGB(x + 1, y + 1)
        ) && noTransitionFound(
                actualImage.getRGB(x - 1, y),
                actual,
                actualImage.getRGB(x + 1, y),
                refImage.getRGB(x - 1, y),
                ref,
                refImage.getRGB(x + 1, y)
        ) && noTransitionFound(
                actualImage.getRGB(x, y - 1),
                actual,
                actualImage.getRGB(x, y + 1),
                refImage.getRGB(x, y - 1),
                ref,
                refImage.getRGB(x, y + 1)
        ) && noTransitionFound(
                actualImage.getRGB(x - 1, y + 1),
                actual,
                actualImage.getRGB(x + 1, y - 1),
                refImage.getRGB(x - 1, y + 1),
                ref,
                refImage.getRGB(x + 1, y - 1)
        ));
    }

    @Override
    public void accept(BufferedImage refImage, BufferedImage actualImage) {
        int threshold = actualImage.getWidth() * actualImage.getHeight() / maxBadPixelsRatio;
        int countOfErrors = 0;
        for (int y = 1; y < actualImage.getHeight()-1; y++) {
            for (int x = 1; x < actualImage.getWidth() - 1; x++) {
                if (pixelMismatch(actualImage, refImage, x, y)) {
                    countOfErrors++;
                }
                if (countOfErrors > threshold) {
                    String errMessage = format("images have significant differences. \n" +
                                    "found %d significant differences in %d pixels",
                            threshold, y * actualImage.getWidth() + x);
                    Images.logger.info(errMessage);
                    throw new AssertionError(errMessage);
                }
            }
        }
        Images.logger.info(format(
                        "found %d significant differences. This is under the %d threshold",
                countOfErrors, threshold));
    }

    public static Optional<BufferedImage> getErrorImage(BufferedImage refImage, BufferedImage actualImage) {
        AtomicReference<Boolean> foundDiff = new AtomicReference<>(false);
        BufferedImage errImage = new BufferedImage(refImage.getWidth(), refImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int y = 1; y < actualImage.getHeight()-1; y++) {
            for (int x = 1; x < actualImage.getWidth() - 1; x++) {
                if (pixelMismatch(actualImage, refImage, x, y)) {
                    foundDiff.set(true);
                    errImage.setRGB(x, y, 0xff0000);
                } else {
                    Color oldColor = new Color(refImage.getRGB(x, y), refImage.isAlphaPremultiplied());
                    Color newColor = new Color(oldColor.getRed() / 4, oldColor.getGreen() / 4, oldColor.getBlue() / 4);
                    errImage.setRGB(x, y, newColor.getRGB());
                }
            }
        }

        return foundDiff.get() ? Optional.of(errImage) : Optional.empty();
    }

}