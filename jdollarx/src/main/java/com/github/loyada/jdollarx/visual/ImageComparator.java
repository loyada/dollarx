package com.github.loyada.jdollarx.visual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class ImageComparator {
    private ImageComparator(){}

    public static void verifyImagesAreSimilar(BufferedImage img1, BufferedImage img2, int maxBadPixelsRatio) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparator(
                maxBadPixelsRatio
        );
        verifyImagesAreSimilarInternal(verifier, img1, img2, 1);
    }

    public static void verifyImagesAreSimilarWithShift(
            BufferedImage img1,
            BufferedImage img2,
            int maxBadPixelsRatio,
            int maxShift
    ) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparator(
                maxBadPixelsRatio
        );
        verifyImagesAreSimilarInternal(verifier, img1, img2, maxShift);
    }

    public static void verifyImagesAreSimilarFilteringInterestingAreas(
            BufferedImage filterImage,
            BufferedImage refImage,
            BufferedImage img,
            int maxBadPixelsRatio) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparatorWithFilter(
                filterImage,
                maxBadPixelsRatio
        );
        verifyImagesAreSimilarInternal(verifier, refImage, img, 2);
    }

    public static void verifyImagesAreEqualWithShift(BufferedImage img1, BufferedImage img2, int maxShift) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new IdentityComparator();
        verifyImagesAreSimilarInternal(verifier, img1, img2, maxShift);
    }


    public static void verifyImagesAreSimilarInternal(
            BiConsumer<BufferedImage,BufferedImage> verifier,
            BufferedImage img1,
            BufferedImage img2,
            int maxShift
    ) {
        assertThat("width", abs(img1.getWidth() - img2.getWidth()), lessThan(maxShift +1));
        assertThat("height", abs(img1.getHeight() - img2.getHeight()), lessThan(maxShift+1));
        for (int yShift=0; yShift<=maxShift; yShift++) {
            for (int xShift=0; xShift<=maxShift; xShift++) {
                BufferedImage croppedImage1 = img1.getSubimage(xShift, yShift,
                        min(img1.getWidth(), img2.getWidth()) - xShift,
                        min(img1.getHeight(), img2.getHeight()) - yShift);
                BufferedImage croppedImage2 = img2.getSubimage(0, 0,
                        min(img1.getWidth(), img2.getWidth()) - xShift,
                        min(img1.getHeight(), img2.getHeight()) - yShift);
                try {
                    Images.logger.info(format("Trying shift: %d, %d", xShift, yShift));
                    verifier.accept(croppedImage1, croppedImage2);
                    Images.logger.info(format("Found correct shift: %d, %d", xShift, yShift));
                    return;
                } catch (AssertionError e) {
                    if (maxShift==0)
                        throw e;
                    //     Images.logger.info(String.format("failed with shift: %d, %d", xShift, yShift));
                }
            }

        }

        for (int yShift=0; yShift<=maxShift; yShift++) {
            for (int xShift=0; xShift<=maxShift; xShift++) {
                BufferedImage croppedImage1 = img1.getSubimage(0, 0,
                        min(img1.getWidth(), img2.getWidth()) - xShift,
                        min(img1.getHeight(), img2.getHeight()) - yShift);
                BufferedImage croppedImage2 = img2.getSubimage(xShift, yShift,
                        min(img1.getWidth(), img2.getWidth()) - xShift,
                        min(img1.getHeight(), img2.getHeight()) - yShift);
                try {
                    Images.logger.info(format("Trying shift: %d, %d", xShift, yShift));
                    verifier.accept(croppedImage1, croppedImage2);
                    Images.logger.info(format("Found correct negative shift: %d, %d", xShift, yShift));
                    return;
                } catch (AssertionError e) {
                    //           Images.logger.info(String.format("failed with negative shift: %d, %d", xShift, yShift));
                }
            }
        }

        throw new AssertionError("could not find any shift");
    }


    public static Optional<BufferedImage> getErrorImage(BufferedImage img1, BufferedImage img2) {
        assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
        assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
        BufferedImage errImage = new BufferedImage(img1.getWidth(), img1.getHeight(),BufferedImage.TYPE_INT_RGB);
        AtomicReference<Boolean> foundDiff = new AtomicReference<>(false);
        range(0, img1.getHeight()).forEach(y ->
                range(0, img1.getWidth()).forEach(x -> {
                            if (img1.getRGB(x, y) == img2.getRGB(x, y)) {
                                Color oldColor = new Color(img1.getRGB(x, y), img1.isAlphaPremultiplied());
                                Color newColor = new Color(oldColor.getRed() / 4, oldColor.getGreen() / 4, oldColor.getBlue() / 4);
                                errImage.setRGB(x, y, newColor.getRGB());
                            } else {
                                foundDiff.set(true);
                                errImage.setRGB(x, y, 0xff0000);
                            }
                        }
                ));
        return foundDiff.get() ? Optional.of(errImage) : Optional.empty();
    }


    public static void verifyImagesAreEqual(BufferedImage img1, BufferedImage img2) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new IdentityComparator();
        verifyImagesAreSimilarInternal(verifier, img1, img2, 0);
    }

    public static void verifyImagesAreEqualFilteringInterestingAreas(BufferedImage filterImg, BufferedImage img1, BufferedImage img2) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparatorWithFilter(
                filterImg,
                1000000
        );
        verifyImagesAreSimilarInternal(verifier, img1, img2, 1);
    }
}
