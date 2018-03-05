package com.github.loyada.jdollarx;


import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public interface Path {
    Optional<WebElement> getUnderlyingSource();

    Optional<String> getXpathExplanation();

    Optional<String> getDescribedBy();

    /**
     *
     * @return an optional containing an xpath this Path is mapped to. The optional is empty only in case
     * it is used as a wrapper of a WebElement (not the typical case).
     */
    Optional<String> getXPath();

    Optional<String> getAlternateXPath();

    List<ElementProperty> getElementProperties();

    /**
     * A useful method to give a readable description to the path, for example:
     * Suppose that instead of describing it's DOM positions and attributes, you prefer to describe it as "search result".
     * Then you'd call: searchResult = myElement.describedBy("search result");
     * Now, calling System.out.println(firstOccurrenceOf(searchResult)), will print:
     * "first occurrence of search result"
     * This will replace its toString() result.
     * @param description a readable description to that expresses the functionality of the path
     * @return a new Path similar to the old one but that is described by the given description
     */
    Path describedBy(String description);

    /**
     * Returns an element that is explicitly inside the document.
     * This is usually not needed - it will be added implicitly when needed.
     * @return a new Path
     */
    Path insideTopLevel();

    /**
     * match more than a single path.
     * Example:
     * div.or(span) - matches both div and span
     *
     * @param path the alternative path to match
     * @return returns a new path that matches both the original one and the given parameter
     */
    Path or(Path path);

    /**
     * returns a path with the provided properties.
     * For example:
     * div.that(hasText("abc"), hasClass("foo"));
     *
     * @param prop - one or more properties. See ElementProperties documentation for details
     * @return a new path with the added constraints
     */
    Path that(ElementProperty... prop);

    /**
     * Alias equivalent to that(). Added for readability.
     * Example:
     * div.that(hasClass("a")).and(hasText("foo"));
     * @param prop a list of element properties (constraints)
     * @return a new Path
     */
    Path and(ElementProperty... prop);

    /**
     * Element with text equals (ignoring case) to txt.
     * Equivalent to path.that(hasText(txt)).
     *
     * @param txt - the text to equal to, ignoring case
     * @return a new Path with the added constraint
     */
    Path withText(String txt);

    /**
     * Element that is inside another element
     * @param path - the containing element
     * @return a new Path with the added constraint
     */
    Path inside(Path path);

    /**
     * The element is a sibling of the given path, and appears after it
     * @param path - the sibling element that appears before
     * @return a new Path with the added constraint
     */
    Path afterSibling(Path path);

    /**
     * The element appears after the given path
     * @param path - the element that appear before
     * @return a new Path with the added constraint
     */
    Path after(Path path);

    /**
     * The element is a sibling of the given path, and appears before it
     * @param path - the sibling element that appears after
     * @return a new Path with the added constraint
     */
    Path beforeSibling(Path path);

    /**
     * The element appears before the given path
     * @param path - the element that appear after
     * @return a new Path with the added constraint
     */
    Path before(Path path);

    /**
     * The element is a direct child of the given path
     * @param path - the parent element
     * @return a new Path with the added constraint
     */
    Path childOf(Path path);

    /**
     * The element is a parent of the given path
     * @param path - the child element
     * @return a new Path with the added constraint
     */
    Path parentOf(Path path);

    /**
     * The element contains the given path, i.e. the given path parameter is inside the element
     * @param path - the element that is inside our element
     * @return a new Path with the added constraint
     */
    Path containing(Path path);

    /**
     * The element contains the given path, i.e. the given path parameter is inside the element
     * @param path - the element that is inside our element
     * @return a new Path with the added constraint
     */
    Path contains(Path path);

    /**
     * The element contains the given path, i.e. the given path parameter is inside the element
     * @param path - the element that is inside our element
     * @return a new Path with the added constraint
     */
    Path ancestorOf(Path path);

    /**
     * The element is contained in the given path element, i.e. the given path parameter is wrapping it
     * @param path - the element that is wrapping our element
     * @return a new Path with the added constraint
     */
    Path descendantOf(Path path);

    /**
     * Return the nth occurrence of the element in the entire document. Count starts at 1.
     * The following expressions are equivalent:
     * occurrenceNumber(4).of(myElement));
     * myElement.withGlobalIndex(4);
     * Return the nth occurrence of the element in the entire document. Count starts at 1.
     * For example:
     * occurrenceNumber(3).of(listItem)
     * @param index the number of occurrence
     * @return a new Path with the added constraint
     */
    Path withGlobalIndex(Integer index);

    /**
     * The element has the given class name
     * @param cssClass the class name
     * @return a new Path with the added constraint
     */
    Path withClass(String cssClass);

    /**
     * The element has the given class names
     * @param cssClasses the class names
     * @return a new Path with the added constraint
     */
    Path withClasses(String... cssClasses);

    /**
     * The element has text, containing the given txt parameter. The match is case insensitive.
     * @param txt the text to match to
     * @return a new Path with the added constraint
     */
    Path withTextContaining(String txt);
}
