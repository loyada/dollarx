package com.github.loyada.jdollarx.visual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class ImageComparator {
    private ImageComparator(){}

    /**
     * Verify images are "similar", given a threshold for the error rate. It allows a shift(offset) of a single pixel.
     * @param refImage - reference
     * @param actualImage - actual
     * @param maxBadPixelsRatio - max error rate (the higher the error rate,
     *                          the more similar the images are required to be)
     */
    public static void verifyImagesAreSimilar(BufferedImage refImage, BufferedImage actualImage, int maxBadPixelsRatio) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparator(
                maxBadPixelsRatio
        );
        verifyImagesAreSimilarInternal(verifier, refImage, actualImage, 1);
    }

    /**
     * Verify images are "similar", allowing for some shift (offset) between the images.
     * "Similar" means that the ratio of total pixels to the pixels that are "significantly different"
     * is less than the given threshold.
     *
     * @param refImage first image
     * @param actualImage second image
     * @param maxBadPixelsRatio max allowed ratio between total pixels and pixels that are found to be "significantly different"
     * @param maxShift max allowed shift between the images, in pixels
     */
    public static void verifyImagesAreSimilarWithShift(
            BufferedImage refImage,
            BufferedImage actualImage,
            int maxBadPixelsRatio,
            int maxShift
    ) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparator(
                maxBadPixelsRatio
        );
        verifyImagesAreSimilarInternal(verifier, refImage, actualImage, maxShift);
    }


    /**
     * Similar to verifyImagesAreSimilarWithShift() but also provides a "filter image" that highlights the areas
     * we focus on. The filter image has white pixels in interesting areas and black pixels in areas that are not
     * interesting. This allows to filter out areas with no information.
     * T
     * "Similar" means that the ratio of total pixels of interest based on the filter image, and the pixels that are
     * "significantly different" is less than the given threshold.
     *
     * Unlike verifyImagesAreSimilarWithShift(), here max shift is set to 2 pixels.
     *
     * @param filterImage - "filter image" that highlights the areas of interest
     * @param refImage - the reference image
     * @param img - the image we are asserting
     * @param maxBadPixelsRatio - max allowed ration between total pixels "of interest" and pixels that are significantly
     *                            different.
     */
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

    /**
     * Verify images are equal, allowing for some shift(offset) between them
     * @param refImage
     * @param actualImage
     * @param maxShift - max allowed shift in pixels
     */
    public static void verifyImagesAreEqualWithShift(BufferedImage refImage, BufferedImage actualImage, int maxShift) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new IdentityComparator();
        verifyImagesAreSimilarInternal(verifier, refImage, actualImage, maxShift);
    }


    private static void verifyImagesAreSimilarInternal(
            BiConsumer<BufferedImage,BufferedImage> verifier,
            BufferedImage refImage,
            BufferedImage actualImage,
            int maxShift
    ) {
        Images.logger.info(format("ref image dimensions: %d, %d", refImage.getWidth(), refImage.getHeight()));
        Images.logger.info(format("actual image dimensions: %d, %d", actualImage.getWidth(), actualImage.getHeight()));

        assertThat("width", abs(refImage.getWidth() - actualImage.getWidth()), lessThan(maxShift +1));
        assertThat("height", abs(refImage.getHeight() - actualImage.getHeight()), lessThan(maxShift+1));
        for (int yShift=0; yShift<=maxShift; yShift++) {
            for (int xShift=0; xShift<=maxShift; xShift++) {
                BufferedImage croppedImage1 = refImage.getSubimage(xShift, yShift,
                        min(refImage.getWidth(), actualImage.getWidth()) - xShift,
                        min(refImage.getHeight(), actualImage.getHeight()) - yShift);
                BufferedImage croppedImage2 = actualImage.getSubimage(0, 0,
                        min(refImage.getWidth(), actualImage.getWidth()) - xShift,
                        min(refImage.getHeight(), actualImage.getHeight()) - yShift);
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
                BufferedImage croppedImage1 = refImage.getSubimage(0, 0,
                        min(refImage.getWidth(), actualImage.getWidth()) - xShift,
                        min(refImage.getHeight(), actualImage.getHeight()) - yShift);
                BufferedImage croppedImage2 = actualImage.getSubimage(xShift, yShift,
                        min(refImage.getWidth(), actualImage.getWidth()) - xShift,
                        min(refImage.getHeight(), actualImage.getHeight()) - yShift);
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


    /**
     * Capture an error image for an assertion of equality of
     * two images. If the images are equal it returns on empty Optional.
     * The error image displayed a faded out version of the original image, overlaid
     * with the bright red pixels in the location of errors.
     * @param img1 first image
     * @param img2 second image
     * @return and optional of the error image.
     */
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

    /**
     * Capture an error image which is the result of a failed assertion of similarity of
     * two images. If the images are similar it returns on empty Optional.
     * Capture an error image which is the result of a failed assertion of similarity of
     * two images. If the images are similar it returns on empty Optional.
     *
     * @param img1 - first image
     * @param img2 - second image
     * @return - an optional of the error image.
     */
    public static Optional<BufferedImage> getFuzzyErrorImage(BufferedImage img1, BufferedImage img2) {
        assertThat("width", img1.getWidth(), equalTo(img2.getWidth()));
        assertThat("height", img1.getHeight(), equalTo(img2.getHeight()));
        return SimilarityComparator.getErrorImage(img1, img2);
    }


    public static void verifyImagesAreEqual(BufferedImage img1, BufferedImage img2) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new IdentityComparator();
        verifyImagesAreSimilarInternal(verifier, img1, img2, 0);
    }


    /**
     * Verify equality to a reference image, ignoring areas that we are uninterested in.
     * This is done by providing an additional "filter image" that displays white pixels
     * in the interesting areas and black pixels in the rest.
     *
     * @param filterImg - "filter image" that tells the comparator the areas to focus on
     * @param img1 - captured image
     * @param img2 - reference image
     */
    public static void verifyImagesAreEqualFilteringInterestingAreas(BufferedImage filterImg, BufferedImage img1, BufferedImage img2) {
        BiConsumer<BufferedImage, BufferedImage> verifier = new SimilarityComparatorWithFilter(
                filterImg,
                1000000
        );
        verifyImagesAreSimilarInternal(verifier, img1, img2, 1);
    }
}
