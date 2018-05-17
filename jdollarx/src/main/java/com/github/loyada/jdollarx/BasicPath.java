package com.github.loyada.jdollarx;


import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.loyada.jdollarx.ElementProperties.isInside;
import static com.github.loyada.jdollarx.PathUtils.hasHeirarchy;
import static com.github.loyada.jdollarx.PathUtils.oppositeRelation;
import static com.github.loyada.jdollarx.PathUtils.transformXpathToCorrectAxis;
import static java.lang.String.format;

/**
 * The standard implementation of Path in DollarX
 */
public final class BasicPath implements Path {
    private Optional<String> insideXpath = Optional.empty();
    private final Optional<String> xpath;
    private final Optional<String> alternateXpath;
    private final Optional<String> xpathExplanation;
    private final Optional<String> describedBy;
    private final Optional<WebElement> underlying;
    private final ImmutableList<ElementProperty> elementProperties;

    public static PathBuilder builder() {
        return new PathBuilder();
    }

    /**
     * A builder for BasicPath. Usually {@link #customElement(String)} is simpler and sufficient.
     */
    public static final class PathBuilder {
        private final Optional<String> insideXpath;
        private final Optional<String> xpath;
        private final Optional<String> xpathExplanation;
        private final Optional<String> describedBy;
        private final Optional<WebElement> underlying;
        private final List<ElementProperty> elementProperties;
        private final Optional<String> alternateXpath;

        public PathBuilder() {
            insideXpath = Optional.empty();
            xpath = Optional.empty();
            alternateXpath = Optional.empty();
            xpathExplanation = Optional.empty();
            underlying = Optional.empty();
            describedBy = Optional.empty();
            elementProperties = Collections.emptyList();
        }

        public PathBuilder(Optional<String> insideXpath,
                           Optional<String> xpath,
                           Optional<String> xpathExplanation,
                           Optional<String> describedBy,
                           Optional<WebElement> underlying,
                           List<ElementProperty> elementProperties,
                           Optional<String> alternateXpath
        ) {
            this.insideXpath = insideXpath;
            this.xpath = xpath;
            this.xpathExplanation = xpathExplanation;
            this.describedBy = describedBy;
            this.underlying = underlying;
            this.elementProperties = elementProperties;
            this.alternateXpath = alternateXpath;
        }

        public PathBuilder withXpath(String xpath) {
            return new PathBuilder(insideXpath, Optional.of(xpath), xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withAlternateXpath(String alternateXpath) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, Optional.of(alternateXpath));
        }

        public PathBuilder withInsideXpath(String insideXpath) {
            return new PathBuilder(Optional.of(insideXpath), xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withXpathExplanation(String xpathExplanation) {
            return new PathBuilder(insideXpath, xpath, Optional.of(xpathExplanation), describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withDescribedBy(String describedBy) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, Optional.of(describedBy), underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withUnderlying(WebElement underlying) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, Optional.of(underlying), elementProperties, alternateXpath);

        }

        public PathBuilder withInsideXpathOptional(Optional<String> insideXpath) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withXpathOptional(Optional<String> xpath) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withAlternateXpathOptional(Optional<String> alternateXpath) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withXpathExplanationOptional(Optional<String> xpathExplanation) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withDescribedByOptional(Optional<String> describedBy) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withUnderlyingOptional(Optional<WebElement> underlying) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public PathBuilder withElementProperties(List<ElementProperty> elementProperties) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties, alternateXpath);
        }

        public BasicPath build() {
            return new BasicPath(underlying, xpath, elementProperties, xpathExplanation, describedBy, insideXpath, alternateXpath);
        }
    }

    private BasicPath(Optional<WebElement> underlying,
                      Optional<String> xpath,
                      List<ElementProperty> elementProperties,
                      Optional<String> xpathExplanation,
                      Optional<String> describedBy,
                      Optional<String> insideXpath,
                      Optional<String> alternateXpath
    ) {
        this.xpath = xpath;
        this.xpathExplanation = xpathExplanation;
        this.describedBy = describedBy;
        this.underlying = underlying;
        this.elementProperties = ImmutableList.copyOf(elementProperties);
        this.insideXpath = insideXpath;
        this.alternateXpath = alternateXpath;
    }

    //elements
    /**
     * Any element
     */
    public static final BasicPath element = builder().withXpath("*").withXpathExplanation("any element").build();
    public static final BasicPath div = builder().withXpath("div").withXpathExplanation("div").build();
    public static final BasicPath span = builder().withXpath("span").withXpathExplanation("span").build();
    public static final BasicPath image = builder().withXpath("img").withXpathExplanation("image").build();
    /**
     * An "li" element
     */
    public static final BasicPath listItem = builder().withXpath("li").withXpathExplanation("list item").build();
    public static final BasicPath button = builder().withXpath("button").withXpathExplanation("button").build();
    /**
     * An "ul" element
     */
    public static final BasicPath unorderedList = builder().withXpath("ul").withXpathExplanation("unordered list").build();
    public static final BasicPath input = builder().withXpath("input").withXpathExplanation("input").build();
    /**
     * An anchor(or "a") element
     */
    public static final BasicPath anchor = builder().withXpath("a").withXpathExplanation("anchor").build();
    public static final BasicPath form = builder().withXpath("form").withXpathExplanation("form").build();
    public static final BasicPath iframe = builder().withXpath("iframe").withXpathExplanation("iframe").build();
    public static final BasicPath html = builder().withXpath("html").withXpathExplanation("document").build();
    public static final BasicPath body = builder().withXpath("body").withXpathExplanation("document body").build();
    public static final BasicPath header1 = builder().withXpath("h1").withXpathExplanation("header-1").build();
    public static final BasicPath header2 = builder().withXpath("h2").withXpathExplanation("header-2").build();
    public static final BasicPath header3 = builder().withXpath("h3").withXpathExplanation("header-3").build();
    public static final BasicPath header4 = builder().withXpath("h4").withXpathExplanation("header-4").build();
    public static final BasicPath header5 = builder().withXpath("h5").withXpathExplanation("header-5").build();
    public static final BasicPath header6 = builder().withXpath("h6").withXpathExplanation("header-6").build();
    /**
     * Any header element
     */
    public static final BasicPath header = (BasicPath) header1.or(header2).or(header3).or(header4).or(header5).or(header6);
    public static final BasicPath title = builder().withXpath("title").withXpathExplanation("title").build();
    public static final BasicPath tr = builder().withXpath("tr").withXpathExplanation("table row").build();
    public static final BasicPath td = builder().withXpath("td").withXpathExplanation("table cell").build();
    public static final BasicPath th = builder().withXpath("th").withXpathExplanation("table header cell").build();
    public static final BasicPath table = customElement("table");
    public static final BasicPath select = builder().withXpath("select").withXpathExplanation("selection menu").build();
    public static final BasicPath option = customElement("option");
    public static final BasicPath label = customElement("label");


    /**
     * Create a custom element Path using a simple API instead of the builder pattern.
     * Example:
     * <pre>
     *   Path myDiv = customElement("div");
     * </pre>
     *
     * @param el - the element type in W3C. will be used for the toString as well.
     * @return a Path representing the element
     */
    public static BasicPath customElement(String el) {
        return builder().withXpath(el).withXpathExplanation(el).build();
    }

    /**
     * Allows to define an element that has a predefined number of similar preceding siblings.
     * Count starts at 1 (same as you would use in English).
     * Should be used through the method {@link #childNumber(Integer)} .
     * Example:
     * <pre>
     *     {@code childNumber(5).ofType(div); }
     * </pre>
     */
    public static final class ChildNumber {
        private final Integer n;

        /**
         * Does not return any usable Path by itself. Must be used like:
         *  {@code ChildNumber(5).ofType(div) }
         * @param n the number of child. Count starts at 1.
         */
        public ChildNumber(Integer n) {
            this.n = n;
        }

        /**
         * an element that has n similar preceding siblings. For example:
         * {@code ChildNumber(5).ofType(element.withText("john")) } will correspond to the fifth child that has text "john"
         * @param path the element to find
         * @return a new Path instance
         */
        public Path ofType(Path path) {
            String newXPath = path.getXPath().get() + format("[%d]", n);
            String alternateXpath = path.getAlternateXPath().get() + format("[%d]", n);

            return builder().withUnderlyingOptional(path.getUnderlyingSource()).
                    withXpath(newXPath).
                    withAlternateXpath(alternateXpath).
                    withXpathExplanation(format("child number %d of type(%s)", n, path)).build();
        }
    }

    /**
     *  Not to be used directly, but through the utility functions:
     *  {@link #firstOccurrenceOf(Path)}, {@link #lastOccurrenceOf(Path)}, {@link #occurrenceNumber(Integer)}
     */
    public static final class GlobalOccurrenceNumber {
        private final Integer n;

         GlobalOccurrenceNumber(final Integer n) {
            this.n = n;
        }

        /**
         * return the nth global occurrence (in the entire document) of the given path.
         * @param path the element to find
         * @return a new Path instance, that adds the global occurrence constraint to it
         */
        public Path of(final Path path) {
            final String prefix = (n == 1) ? "the first occurrence of " :
                    (n == 0) ? "the last occurrence of " : format("occurrence number %d of ", n);
            final String pathString = path.toString();
            final String wrapped = (pathString.contains(" ")) ? format("(%s)", pathString) : pathString;
            final String index = (n == 0) ? "last()" : format("%d", n);
            final String xpathPrefix =  (path.getXPath().get().startsWith("(")) ? "(" : "(//";
            final String newXPath = format("%s%s)[%s]", xpathPrefix, path.getXPath().get(), index);
            final String newAlternatePath = format("%s%s)[%s]", xpathPrefix, path.getAlternateXPath().get(), index);
            return builder().withUnderlyingOptional(path.getUnderlyingSource()).
                    withXpath(newXPath).
                    withAlternateXpath(newAlternatePath).
                    withXpathExplanation(prefix + wrapped).build();
        }
    }


    /**
     * the element is the nth child of its parent. Count starts at 1.
     * For example:
     * <pre>
     *     childNumber(4).ofType(div.withClass("foo"))
     * </pre>
     *
     *
     * @param n the index of the child - starting at 1
     * @return a ChildNumber instance, which is used with as in the example.
     */
    public static ChildNumber childNumber(Integer n) {
        return new ChildNumber(n);
    }

    /**
     * used in the form : occurrenceNumber(4).of(myElement)).
     * Return the nth occurrence of the element in the entire document. Count starts at 1.
     * For example:
     * <pre>
     *     occurrenceNumber(3).of(listItem)
     * </pre>
     *
     * @param n the number of occurrence
     * @return GlobalOccurrenceNumber instance, which is used as in the example.
     */
    public static GlobalOccurrenceNumber occurrenceNumber(Integer n) {
        return new GlobalOccurrenceNumber(n);
    }

    /**
     * First global occurrence of an element in the document.
     * @param path the element to find
     * @return a new path with the added constraint
     */
    public static Path firstOccurrenceOf(Path path) {
        return path.withGlobalIndex(0);
    }

    /**
     * Last global occurrence of an element in the document
     * @param path the element to find
     * @return a new path with the added constraint
     */
    public static Path lastOccurrenceOf(Path path) {
        return path.withGlobalIndex(-1);
    }

    @Override
    public Optional<String> getXPath() {
        if (!xpath.isPresent() && elementProperties.isEmpty() && !insideXpath.isPresent()) {
            return Optional.empty();
        } else {
            String processedXpath = (insideXpath.isPresent() ? (insideXpath.get() + "//") : "") + xpath.orElse("*");

            String props = elementProperties.stream().map(e -> format("[%s]", e.toXpath())).
                    collect(Collectors.joining());
            return Optional.of(processedXpath + props);
        }
    }

    @Override
    public Optional<String> getAlternateXPath() {
        if (!xpath.isPresent() && elementProperties.isEmpty() && !insideXpath.isPresent()) {
            return Optional.empty();
        } else {
            String props = elementProperties.stream().map(e -> format("[%s]", e.toXpath())).
                    collect(Collectors.joining());
            return Optional.of(alternateXpath.orElse(xpath.orElse("*")) + props);
        }
    }

    private Optional<String> getXPathWithoutInsideClause() {
        if (!xpath.isPresent() && elementProperties.isEmpty()) {
            return Optional.empty();
        } else {
            String props = elementProperties.stream().map(e -> format("[%s]", e.toXpath())).
                    collect(Collectors.joining());
            return Optional.of(xpath.orElse("*") + props);
        }
    }

    @Override
    public Optional<WebElement> getUnderlyingSource() {
        return this.underlying;
    }

    @Override
    public Optional<String> getXpathExplanation() {
        return xpathExplanation;
    }

    @Override
    public Optional<String> getDescribedBy() {
        return describedBy;
    }

    @Override
    public List<ElementProperty> getElementProperties() {
        return elementProperties;
    }

    @Override
    public Path describedBy(String description) {
        return new BasicPath(underlying, xpath, elementProperties, xpathExplanation, Optional.of(description),
                insideXpath, alternateXpath);
    }

    private void verifyRelationBetweenElements(Path path) {
        if (path.getUnderlyingSource().isPresent() || !getXPath().isPresent() || !path.getXPath().isPresent())
            throw new IllegalArgumentException();
    }

    /**
     * match more than a single path.
     * Example:
     * div.or(span) - matches both div and span
     *
     * @param path the alternative path to match
     * @return returns a new path that matches both the original one and the given parameter
     */
    @Override
    public Path or(Path path) {
        verifyRelationBetweenElements(path);
        return builder().
                withUnderlyingOptional(underlying).
                withXpath(format("*[(self::%s) | (self::%s)]", transformXpathToCorrectAxis(this).get(),
                        transformXpathToCorrectAxis(path).get())).
                withAlternateXpath(format("*[(self::%s) | (self::%s)]", getAlternateXPath().get(),
                        path.getAlternateXPath().get())).
                withXpathExplanation(format("%s or %s", wrapIfNeeded(this), wrapIfNeeded(path))).
                build();
    }

    /**
     * returns a path with the provided properties.
     * For example:
     * div.that(hasText("abc"), hasClass("foo"));
     *
     * @param prop - one or more properties. See ElementProperties documentation for details
     * @return a new path with the added constraints
     */
    @Override
    public Path that(ElementProperty... prop) {
        if (describedBy.isPresent()) {
            return builder().withUnderlyingOptional(underlying).
                    withXpathOptional(getXPathWithoutInsideClause()).
                    withInsideXpathOptional(insideXpath).
                    withElementProperties(ImmutableList.copyOf(prop)).
                    withAlternateXpathOptional(alternateXpath).
                    withXpathExplanation(describedBy.get()).build();
        } else {
            ImmutableList<ElementProperty> newProps = ImmutableList.<ElementProperty>builder().
                    addAll(elementProperties).
                    addAll(Arrays.asList(prop)).
                    build();
            return builder().withUnderlyingOptional(underlying).
                    withXpathOptional(xpath).
                    withInsideXpathOptional(insideXpath).
                    withElementProperties(newProps).
                    withDescribedByOptional(describedBy).
                    withAlternateXpathOptional(alternateXpath).
                    withXpathExplanationOptional(xpathExplanation).build();
        }
    }

    /**
     * Alias equivalent to that(). Added for readability.
     * Example:
     * <pre>
     *     div.that(hasClass("a")).and(hasText("foo"));
     * </pre>
     *
     * @param prop a list of element properties (constraints)
     * @return a new Path
     */
    @Override
    public Path and(ElementProperty... prop) {
        return that(prop);
    }

    /**
     * Element with text equals (ignoring case) to txt.
     *
     * Equivalent to:
     * <pre>
     *     path.that(hasText(txt))
     * </pre>
     *
     * @param txt - the text to equal to, ignoring case
     * @return a new Path with the added constraint
     */
    @Override
    public Path withText(String txt) {
        return createNewWithAdditionalProperty(ElementProperties.hasText(txt));
    }

    /**
     * Element that is inside another element
     * @param path - the containing element
     * @return a new Path with the added constraint
     */
    @Override
    public Path inside(final Path path) {
        final String newXPath = getXPathWithoutInsideClause().orElse("");
        final String correctedXpathForIndex;
        final Optional<String> correctInsidePath;
        final String descriptionPrefix;
        if (newXPath.startsWith("(")) {
            correctedXpathForIndex = (newXPath + format("[%s]", isInside(path).toXpath()));
            correctInsidePath = Optional.empty();
            descriptionPrefix = ", and is inside ";
        } else {
            correctedXpathForIndex = newXPath;
            correctInsidePath = Optional.of(path.getXPath().get() + (insideXpath.isPresent() ? "//" + insideXpath.get() : ""));
            descriptionPrefix = ", inside ";
        }

        return builder().
                withUnderlyingOptional(path.getUnderlyingSource()).
                withXpath(correctedXpathForIndex).
                withInsideXpathOptional(correctInsidePath).
                withAlternateXpathOptional(this.that(ElementProperties.isDescendantOf(path)).getAlternateXPath()).
                withXpathExplanation(toString() + descriptionPrefix + wrapIfNeeded(path)).
                build();
    }

    /**
     * Returns an element that is explicitly inside the document.
     * This is usually not needed - it will be added implicitly when needed.
     * @return a new Path
     */
    @Override
    public Path insideTopLevel() {
        if (!getXPath().isPresent()) throw new IllegalArgumentException("must have a non-empty xpath");

        return new PathBuilder().
                withXpath(XpathUtils.insideTopLevel(getXPath().get())).
                withDescribedBy(toString()).
                build();
    }

    /**
     * The element has a preceding sibling that matches to the given Path parameter
     * @param path - the sibling element that appears before
     * @return a new path with the added constraint
     */
    @Override
    public Path afterSibling(Path path) {
        return createWithHumanReadableRelation(path, "following-sibling", "after the sibling");
    }

    /**
     * The sibling right before the current element matches to the given Path parameter
     * @param path - the sibling element that appears right before
     * @return a new path with the added constraint
     */
    @Override
    public Path immediatelyAfterSibling(Path path) {
        return createWithHumanReadableRelation(path, "following-sibling::*[1]/self", "immediately after the sibling");
    }


    /**
     * The element appears after the given path
     * @param path - the element that appear before
     * @return a new path with the added constraint
     */
    @Override
    public Path after(Path path) {
        return createWithHumanReadableRelation(path, "following", "after");
    }

    /**
     * The element is a sibling of the given path and appears before it
     * @param path - the sibling element that appears after
     * @return a new path with the added constraint
     */
    @Override
    public Path beforeSibling(Path path) {
        return createWithHumanReadableRelation(path, "preceding-sibling", "before the sibling");
    }

    /**
     * The sibling right after the element matches the given path parameter
     * @param path - the sibling element that appears after
     * @return a new path with the added constraint
     */
    @Override
    public Path immediatelyBeforeSibling(Path path) {
        return createWithHumanReadableRelation(path, "preceding-sibling::*[1]/self", "immediately before the sibling");
    }

    /**
     * The element is before the given path parameter
     * @param path - the element that appear after
     * @return a new path with the added constraint
     */
    @Override
    public Path before(Path path) {
        return createWithHumanReadableRelation(path, "preceding", "before");
    }


    /**
     *
     * @param path - the parent element
     * @return a new path with the added constraint
     */
    @Override
    public Path childOf(Path path) {
        return createWithSimpleRelation(path, "child");

    }

    /**
     *
     * @param path - the child element
     * @return a new path with the added constraint
     */
    @Override
    public Path parentOf(Path path) {
        return createWithSimpleRelation(path, "parent");
    }

    /**
     *
     * @param path - the element that is inside our element
     * @return a new path with the added constraint
     */
    @Override
    public Path containing(Path path) {
        return ancestorOf(path);
    }

    /**
     *
     * @param path - the element that is inside our element
     * @return a new path with the added constraint
     */
    @Override
    public Path contains(Path path) {
        return ancestorOf(path);
    }

    /**
     *
     * @param path - the element that is inside our element
     * @return a new path with the added constraint
     */
    @Override
    public Path ancestorOf(Path path) {
        return createWithSimpleRelation(path, "ancestor");
    }

    /**
     * The element is inside the given path parameter
     * @param path - the element that is wrapping our element
     * @return a new path with the added constraint
     */
    @Override
    public Path descendantOf(Path path) {
        return createWithSimpleRelation(path, "descendant");
    }

    /**
     * An alias of: {@code occurrenceNumber(n + 1).of(this) }
     * @param n - the global occurrence index of the path, starting from 0
     * @return a new path with the added constraint
     */
    @Override
    public Path withGlobalIndex(Integer n) {
        return occurrenceNumber(n + 1).of(this);
    }

    /**
     * Equivalent to {@code this.that(hasClass(cssClass)) }
     * @param cssClass the class name
     * @return a new path with the added constraint
     */
    @Override
    public Path withClass(String cssClass) {
        return createNewWithAdditionalProperty(ElementProperties.hasClass(cssClass));
    }

    /**
     *  Equivalent to {@code this.that(hasClasses(cssClasses)) }
     *
     * @param cssClasses the class names
     * @return a new path with the added constraint
     */
    @Override
    public Path withClasses(String... cssClasses) {
        return createNewWithAdditionalProperty(ElementProperties.hasClasses(cssClasses));
    }

    /**
     * Equivalent to {@code this.that(hasTextContaining(txt))}.
     * @param txt the text to match to. The match is case insensitive.
     * @return a new path with the added constraint
     */
    @Override
    public Path withTextContaining(String txt) {
        return createNewWithAdditionalProperty(ElementProperties.hasTextContaining(txt));
    }

    private Optional<String> getXpathExplanationForToString() {
        if (xpath.isPresent()) {
            return xpathExplanation.isPresent() ?
                    xpathExplanation :
                    Optional.of("xpath: \"" + xpath.get() + "\"");
        } else return Optional.empty();
    }

    private Optional<String> getPropertiesToStringForLength1() {
        String thatMaybe = (elementProperties.get(0).toString().startsWith("has") || elementProperties.get(0).toString().startsWith("is")) ? "that " : "";
        return Optional.of(thatMaybe + elementProperties.get(0));
    }

    private Optional<String> getPropertiesToStringForLengthLargerThan2() {
        String propsAsList = elementProperties.stream().map(Object::toString).collect(Collectors.joining(", "));
        if (xpathExplanation.isPresent() && xpathExplanation.get().contains("with properties") || elementProperties.size() == 1) {
            return Optional.of("and " + propsAsList);
        } else {
            return Optional.of("that [" + propsAsList + "]");
        }
    }

    @Override
    public String toString() {
        if (describedBy.isPresent() && !describedBy.equals(xpathExplanation)) {
            return describedBy.get();
        } else {
            Optional<String> underlyingOption = (underlying.isPresent()) ?
                    Optional.of("under reference element " + underlying.get()) :
                    Optional.empty();
            Optional<String> xpathOption = getXpathExplanationForToString();

            Optional<String> propsOption =
                    (elementProperties.size() == 1 && (!xpathOption.orElse("").contains(", ") || xpathOption.equals(describedBy))) ?
                            getPropertiesToStringForLength1() :
                            (elementProperties.size() == 2 && !xpathOption.orElse("").contains(" ")) ?
                                    Optional.of(format("that %s, and %s",
                                            elementProperties.get(0), elementProperties.get(elementProperties.size() - 1))) :
                                    (elementProperties.size() > 1 || (xpathOption.orElse("").contains(" ") && !elementProperties.isEmpty())) ?
                                            getPropertiesToStringForLengthLargerThan2() :
                                            Optional.empty();

            return (xpathExplanation.isPresent() && !underlyingOption.isPresent() && !propsOption.isPresent()) ?
                    xpathExplanation.get() :
                    Stream.of(underlyingOption, xpathOption, propsOption).filter(Optional::isPresent).map(Optional::get).
                            collect(Collectors.joining(", "));
        }

    }

    private BasicPath createWithSimpleRelation(Path path, String relation) {
        verifyRelationBetweenElements(path);
        String myXpath = getXPath().get();
        boolean isInside = insideXpath.isPresent();
        String processedXpath = isInside ? format("*[ancestor::%s and self::%s]", insideXpath.get(), xpath.orElse("*")) : myXpath;
        String newAlternateXpath = getAlternateXPath().get() + format("[%s::%s]", oppositeRelation(relation), path.getAlternateXPath().get());
        boolean useAlternateXpath = hasHeirarchy(processedXpath);
        String newXpath = useAlternateXpath ? newAlternateXpath : (path.getXPath().get() + "/" + relation + "::" + processedXpath);

        return builder().
                withUnderlyingOptional(underlying).
                withXpath(newXpath).
                withAlternateXpath(newAlternateXpath).
                withXpathExplanation(toString() + ", " + relation + " of " + path.toString()).
                build();
    }

    private String wrapIfNeeded(Path path) {
        return (path.toString().trim().contains(" ")) ? "(" + path + ")" : path.toString();
    }

    private BasicPath createWithHumanReadableRelation(Path path, String xpathRelation, String humanReadableRelation) {
        verifyRelationBetweenElements(path);
        String myXpath = getXPath().get();
        boolean isInside = insideXpath.isPresent();
        String processedXpath = isInside ? format("%s[ancestor::%s]", getXPathWithoutInsideClause().get(), insideXpath.get()) : myXpath;
        String newAlternateXpath = getAlternateXPath().get() + format("[%s::%s]", oppositeRelation(xpathRelation), path.getAlternateXPath().get());
        boolean useAlternateXpath = hasHeirarchy(processedXpath);
        String newXpath = useAlternateXpath ? newAlternateXpath : (path.getXPath().get() + "/" + xpathRelation + "::" + processedXpath);

        return builder().
                withUnderlyingOptional(underlying).
                withXpath(newXpath).
                withAlternateXpath(newAlternateXpath).
                withXpathExplanation(toString() + ", " + humanReadableRelation + " " + wrapIfNeeded(path)).
                build();
    }


    private BasicPath createNewWithAdditionalProperty(ElementProperty prop) {
        if (describedBy.isPresent()) {
            return builder().withUnderlyingOptional(underlying).
                    withXpathOptional(getXPath()).
                    withAlternateXpathOptional(getAlternateXPath()).
                    withInsideXpathOptional(insideXpath).
                    withElementProperties(ImmutableList.of(prop)).
                    withXpathExplanation(describedBy.get()).build();
        } else {
            ImmutableList<ElementProperty> newProps = ImmutableList.<ElementProperty>builder().
                    addAll(elementProperties).add(prop).
                    build();
            return builder().withUnderlyingOptional(underlying).
                    withXpathOptional(xpath).
                    withInsideXpathOptional(insideXpath).
                    withAlternateXpathOptional(getAlternateXPath()).
                    withElementProperties(newProps).
                    withDescribedByOptional(describedBy).
                    withXpathExplanationOptional(xpathExplanation).build();
        }
    }


}
