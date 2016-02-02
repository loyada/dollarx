package info.testtools.jdollarx;


import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebElement;

import javax.xml.xpath.XPathExpression;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static info.testtools.jdollarx.ElementProperties.isInside;
import static java.lang.String.format;

public final class BasicPath implements Path {
    private Optional<String> insideXpath = Optional.empty();
    private final Optional<String> xpath;
    private final Optional<String> xpathExplanation;
    private final Optional<String> describedBy;
    private final Optional<WebElement> underlying;
    private final ImmutableList<ElementProperty> elementProperties;

    public static PathBuilder builder() {
        return new PathBuilder();
    }

    public static final class PathBuilder {
        private final Optional<String> insideXpath;
        private final Optional<String> xpath;
        private final Optional<String> xpathExplanation;
        private final Optional<String> describedBy;
        private final Optional<WebElement> underlying;
        private final List<ElementProperty> elementProperties;

        public PathBuilder() {
            insideXpath = Optional.empty();
            xpath = Optional.empty();
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
                           List<ElementProperty> elementProperties) {
            this.insideXpath = insideXpath;
            this.xpath = xpath;
            this.xpathExplanation = xpathExplanation;
            this.describedBy = describedBy;
            this.underlying = underlying;
            this.elementProperties = elementProperties;
        }

        public PathBuilder withXpath(String xpath) {
            return new PathBuilder(insideXpath, Optional.of(xpath), xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withInsideXpath(String insideXpath) {
            return new PathBuilder(Optional.of(insideXpath), xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withXpathExplanation(String xpathExplanation) {
            return new PathBuilder(insideXpath, xpath, Optional.of(xpathExplanation), describedBy, underlying, elementProperties);
        }

        public PathBuilder withDescribedBy(String describedBy) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, Optional.of(describedBy), underlying, elementProperties);
        }

        public PathBuilder withUnderlying(WebElement underlying) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, Optional.of(underlying), elementProperties);

        }

        public PathBuilder withInsideXpathOptional(Optional<String> insideXpath) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withXpathOptional(Optional<String> xpath) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withXpathExplanationOptional(Optional<String> xpathExplanation) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withDescribedByOptional(Optional<String> describedBy) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withUnderlyingOptional(Optional<WebElement> underlying) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public PathBuilder withElementProperties(List<ElementProperty> elementProperties) {
            return new PathBuilder(insideXpath, xpath, xpathExplanation, describedBy, underlying, elementProperties);
        }

        public BasicPath build() {
            return new BasicPath(underlying, xpath, elementProperties, xpathExplanation, describedBy, insideXpath);
        }
    }

    private BasicPath(Optional<WebElement> underlying,
                      Optional<String> xpath,
                      List<ElementProperty> elementProperties,
                      Optional<String> xpathExplanation,
                      Optional<String> describedBy,
                      Optional<String> insideXpath
    ) {
        this.xpath = xpath;
        this.xpathExplanation = xpathExplanation;
        this.describedBy = describedBy;
        this.underlying = underlying;
        this.elementProperties = ImmutableList.copyOf(elementProperties);
        this.insideXpath = insideXpath;
    }

    //elements
    public static final BasicPath element = builder().withXpath("*").withXpathExplanation("any element").build();
    public static final BasicPath div = builder().withXpath("div").withXpathExplanation("div").build();
    public static final BasicPath span = builder().withXpath("span").withXpathExplanation("span").build();
    public static final BasicPath image = builder().withXpath("img").withXpathExplanation("image").build();
    public static final BasicPath listItem = builder().withXpath("li").withXpathExplanation("list item").build();
    public static final BasicPath button = builder().withXpath("button").withXpathExplanation("button").build();
    public static final BasicPath unorderedList = builder().withXpath("ul").withXpathExplanation("unordered list").build();
    public static final BasicPath input = builder().withXpath("input").withXpathExplanation("input").build();
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
    public static final BasicPath header = (BasicPath) header1.or(header2).or(header3).or(header4).or(header5).or(header6);
    public static final BasicPath title = builder().withXpath("title").withXpathExplanation("title").build();
    public static final BasicPath tr = builder().withXpath("tr").withXpathExplanation("table row").build();
    public static final BasicPath td = builder().withXpath("td").withXpathExplanation("table cell").build();
    public static final BasicPath th = builder().withXpath("th").withXpathExplanation("table header cell").build();
    public static final BasicPath table = customElement("table");
    public static final BasicPath select = builder().withXpath("select").withXpathExplanation("selection menu").build();
    public static final BasicPath option = customElement("option");

    public static BasicPath customElement(String el) {
        return builder().withXpath(el).withXpathExplanation(el).build();
    }

    public static final class ChildNumber {
        private final Integer n;

        public ChildNumber(Integer n) {
            this.n = n;
        }

        public Path ofType(Path path) {
            String newXPath = path.getXPath().get() + format("[%d]", n);
            return builder().withUnderlyingOptional(path.getUnderlyingSource()).
                    withXpath(newXPath).withXpathExplanation(format("child number %d of type(%s)", n, path)).build();
        }
    }

    public static final class GlobalOccurrenceNumber {
        private final Integer n;

        public GlobalOccurrenceNumber(final Integer n) {
            this.n = n;
        }

        public Path of(final Path path) {
            final String prefix = (n == 1) ? "the first occurrence of " : format("occurrence number %d of ", n);
            final String pathString = path.toString();
            final String wrapped = (pathString.contains(" ")) ? format("(%s)", pathString) : pathString;
            final String newXPath = format("(//%s)[%d]", path.getXPath().get(), n);
            return builder().withUnderlyingOptional(path.getUnderlyingSource()).
                    withXpath(newXPath).withXpathExplanation(prefix + wrapped).build();
        }
    }


    public static ChildNumber childNumber(Integer n) {
        return new ChildNumber(n);
    }

    public static GlobalOccurrenceNumber occurrenceNumber(Integer n) {
        return new GlobalOccurrenceNumber(n);
    }

    public static Path firstOccuranceOf(Path path) {
        return path.withGlobalIndex(0);
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
    public BasicPath describedBy(String description) {
        return new BasicPath(underlying, xpath, elementProperties, xpathExplanation, Optional.of(description), insideXpath);
    }

    private void verifyRelationBetweenElements(Path path) {
        if (path.getUnderlyingSource().isPresent() || !getXPath().isPresent() || !path.getXPath().isPresent())
            throw new IllegalArgumentException();
    }

    @Override
    public Path or(Path path) {
        verifyRelationBetweenElements(path);
        return builder().
                withUnderlyingOptional(underlying).
                withXpath(format("*[self::%s | self::%s]", getXPath().get(), path.getXPath().get())).
                withXpathExplanation(format("%s or %s", wrapIfNeeded(this), wrapIfNeeded(path))).
                build();
    }

    @Override
    public Path that(ElementProperty... prop) {
        if (describedBy.isPresent()) {
            return builder().withUnderlyingOptional(underlying).
                    withXpathOptional(getXPathWithoutInsideClause()).
                    withInsideXpathOptional(insideXpath).
                    withElementProperties(ImmutableList.copyOf(prop)).
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
                    withXpathExplanationOptional(xpathExplanation).build();
        }
    }

    @Override
    public Path and(ElementProperty... prop) {
        return that(prop);
    }

    @Override
    public Path withText(String txt) {
        return createNewWithAdditionalProperty(ElementProperties.hasText(txt));
    }

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
                withXpathExplanation(toString() + descriptionPrefix + wrapIfNeeded(path)).
                build();
    }

    public  Path insideTopLevel() {
        if (!getXPath().isPresent()) throw new IllegalArgumentException("must have a non-empty xpath");
        String extractedXpath = getXPath().get();
        final String prefix =  (extractedXpath.startsWith("/") || extractedXpath.startsWith("(/")) ? "" :
                (extractedXpath.startsWith("(")) ? "(//" :
                        "//";
        final int chopn = (extractedXpath.startsWith("(") && !extractedXpath.startsWith("(/")) ? 1 : 0;

        return new PathBuilder().
                withXpath(prefix + extractedXpath.substring(chopn)).
                withDescribedBy(toString()).
                build();
    }

    @Override
    public Path afterSibling(Path path) {
        return createWithHumanReadableRelation(path, "following-sibling", "after the sibling");
    }

    @Override
    public BasicPath after(Path path) {
        return createWithHumanReadableRelation(path, "following", "after");
    }

    @Override
    public BasicPath beforeSibling(Path path) {
        return createWithHumanReadableRelation(path, "preceding-sibling", "before the sibling");
    }

    @Override
    public BasicPath before(Path path) {
        return createWithHumanReadableRelation(path, "preceding", "before");
    }


    @Override
    public Path childOf(Path path) {
        return createWithSimpleRelation(path, "child");

    }

    @Override
    public Path parentOf(Path path) {
        return createWithSimpleRelation(path, "parent");
    }

    @Override
    public Path containing(Path path) {
        return ancestorOf(path);
    }

    @Override
    public Path contains(Path path) {
        return ancestorOf(path);
    }

    @Override
    public Path ancestorOf(Path path) {
        return createWithSimpleRelation(path, "ancestor");
    }

    @Override
    public Path descendantOf(Path path) {
        return createWithSimpleRelation(path, "descendant");
    }

    @Override
    public Path withGlobalIndex(Integer n) {
        return occurrenceNumber(n + 1).of(this);
    }

    @Override
    public Path withClass(String cssClass) {
        return createNewWithAdditionalProperty(ElementProperties.hasClass(cssClass));
    }

    @Override
    public Path withClasses(String... cssClasses) {
        return createNewWithAdditionalProperty(ElementProperties.hasClasses(cssClasses));
    }

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

            String detail = (xpathExplanation.isPresent() && !underlyingOption.isPresent() && !propsOption.isPresent()) ?
                    xpathExplanation.get() :
                    Stream.of(underlyingOption, xpathOption, propsOption).filter(Optional::isPresent).map(Optional::get).
                            collect(Collectors.joining(", "));
            return detail;
        }

    }

    private BasicPath createWithSimpleRelation(Path path, String relation) {
        verifyRelationBetweenElements(path);
        String myXpath = getXPath().get();
        boolean isInside = insideXpath.isPresent();
        String processedXpath = isInside ? format("*[ancestor::%s and self::%s]", insideXpath.get(), xpath.orElse("*")) : myXpath;
        return builder().
                withUnderlyingOptional(underlying).
                withXpath(path.getXPath().get() + "/" + relation + "::" + processedXpath).
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
        String processedXpath = isInside ? format("*[ancestor::%s]", insideXpath.get()) : myXpath;
        return builder().
                withUnderlyingOptional(underlying).
                withXpath(path.getXPath().get() + "/" + xpathRelation + "::" + processedXpath).
                withXpathExplanation(toString() + ", " + humanReadableRelation + " " + wrapIfNeeded(path)).

                build();
    }


    private BasicPath createNewWithAdditionalProperty(ElementProperty prop) {
        if (describedBy.isPresent()) {
            return builder().withUnderlyingOptional(underlying).
                    withXpathOptional(getXPath()).
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
                    withElementProperties(newProps).
                    withDescribedByOptional(describedBy).
                    withXpathExplanationOptional(xpathExplanation).build();
        }
    }


}
