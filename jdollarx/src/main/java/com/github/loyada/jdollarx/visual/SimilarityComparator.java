package com.github.loyada.jdollarx.visual;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

import static com.github.loyada.jdollarx.visual.ImageUtils.pixelValueIsSignificantlyDifferent;
import static java.lang.String.format;

public class SimilarityComparator  implements BiConsumer<BufferedImage, BufferedImage> {
    private final int maxBadPixelsRatio;

    public SimilarityComparator(int maxBadPixelsRatio) {
        this.maxBadPixelsRatio = maxBadPixelsRatio;
    }

    @Override
    public void accept(BufferedImage refImage, BufferedImage actualImage) {
        int threshold = actualImage.getWidth() * actualImage.getHeight() / maxBadPixelsRatio;
        int countOfErrors = 0;
        for (int y = 0; y < actualImage.getHeight(); y++) {
            for (int x = 0; x < actualImage.getWidth(); x++) {
                if (pixelValueIsSignificantlyDifferent(actualImage.getRGB(x, y), refImage.getRGB(x, y))) {
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
}