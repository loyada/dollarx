package com.github.loyada.jdollarx.visual;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

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
        for (int y=1; y<refImage.getHeight()-1; y++) {
            for (int x = 1; x < refImage.getWidth()-1; x++) {
                if ((filterImage.getRGB(x, y) & 0xffffff) == 0)
                    continue;

                totalPixels += 1;
                if (SimilarityComparator.pixelMismatch(
                        refImage,
                        actualImage,
                        x,
                        y)) {
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
