package com.github.loyada.jdollarx.visual;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

import static com.github.loyada.jdollarx.visual.ImageUtils.pixelValueIsSignificantlyDifferent;
import static java.lang.String.format;

public class SimilarityComparatorWithFilter implements BiConsumer<BufferedImage, BufferedImage> {
    private final BufferedImage filterImage;
    private final int maxBadPixelsRatio;

    public SimilarityComparatorWithFilter(BufferedImage filterImage, int maxBadPixelsRatio) {
        this.filterImage = filterImage;
        this.maxBadPixelsRatio = maxBadPixelsRatio;
    }

    @Override
    public void accept(BufferedImage refImage, BufferedImage actualImage) {
        int totalPixels = 0;
        int countOfErrors = 0;
        for (int y=0; y<refImage.getHeight(); y++) {
            for (int x = 0; x < refImage.getWidth(); x++) {
                if ((filterImage.getRGB(x, y) & 0xffffff) == 0)
                    continue;

                totalPixels += 1;
                if (pixelValueIsSignificantlyDifferent(
                        refImage.getRGB(x, y) & 0xffffff,
                        actualImage.getRGB(x, y) & 0xffffff)) {
                    countOfErrors++;
                }
            }
        }
        int threshold = totalPixels / maxBadPixelsRatio;
        String errMessage = format(
                        "found %d significant differences. Allowed %d",
                countOfErrors, threshold);
        Images.logger.info(errMessage);
        if (countOfErrors > threshold) {
            Images.logger.info("images have significant differences");
            throw new AssertionError("images have significant differences");
        }
    }
}
