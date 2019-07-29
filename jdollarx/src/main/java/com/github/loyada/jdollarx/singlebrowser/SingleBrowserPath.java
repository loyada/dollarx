package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

/**
 * An implementation of {@link Path} that is tailored to a singleton browser, thus allows some additional API's
 * for actions (for those who favor object-oriented API style)
 */
public final class SingleBrowserPath implements Path {
    private final BasicPath path;

    //elements
    public static final SingleBrowserPath element = new SingleBrowserPath(BasicPath.element);
    public static final SingleBrowserPath div = new SingleBrowserPath(BasicPath.div);
    public static final SingleBrowserPath span = new SingleBrowserPath(BasicPath.span);
    public static final SingleBrowserPath listItem = new SingleBrowserPath(BasicPath.listItem);
    public static final SingleBrowserPath button = new SingleBrowserPath(BasicPath.button);
    public static final SingleBrowserPath unorderedList = new SingleBrowserPath(BasicPath.unorderedList);
    public static final SingleBrowserPath input = new SingleBrowserPath(BasicPath.input);
    public static final SingleBrowserPath anchor =new SingleBrowserPath(BasicPath.anchor);
    public static final SingleBrowserPath form =new SingleBrowserPath(BasicPath.form);
    public static final SingleBrowserPath html = new SingleBrowserPath(BasicPath.html);
    public static final SingleBrowserPath body = new SingleBrowserPath(BasicPath.body);
    public static final SingleBrowserPath header1 = new SingleBrowserPath(BasicPath.header1);
    public static final SingleBrowserPath header2 = new SingleBrowserPath(BasicPath.header2);
    public static final SingleBrowserPath header3 = new SingleBrowserPath(BasicPath.header3);
    public static final SingleBrowserPath header4 = new SingleBrowserPath(BasicPath.header4);
    public static final SingleBrowserPath header5 = new SingleBrowserPath(BasicPath.header5);
    public static final SingleBrowserPath header6 = new SingleBrowserPath(BasicPath.header6);
    public static final SingleBrowserPath header = new SingleBrowserPath(BasicPath.header);
    public static final SingleBrowserPath svg = new SingleBrowserPath(BasicPath.svg);
    public static final SingleBrowserPath section = new SingleBrowserPath(BasicPath.section);


    public SingleBrowserPath(BasicPath path){
        this.path = path;
    }

    @Override
    public Optional<WebElement> getUnderlyingSource() {
        return path.getUnderlyingSource();
    }

    @Override
    public Optional<String> getXpathExplanation() {
        return path.getXpathExplanation();
    }

    @Override
    public Optional<String> getDescribedBy() {
        return path.getXpathExplanation();
    }

    @Override
    public Optional<String> getAlternateXPath() {
        return path.getAlternateXPath();
    }

    @Override
    public Optional<String> getXPath() {
        return path.getXPath();
    }

    @Override
    public List<ElementProperty> getElementProperties() {
        return path.getElementProperties();
    }

    @Override
    public Path describedBy(String description) {
        return new SingleBrowserPath((BasicPath)path.describedBy(description));
    }

    @Override
    public Path insideTopLevel() {
        return path.insideTopLevel();
    }


    @Override
    public Path or(Path another) {
        return new SingleBrowserPath((BasicPath)path.or(another));
    }

    @Override
    public Path that(ElementProperty... prop) {
        return new SingleBrowserPath((BasicPath)path.that(prop));
    }

    @Override
    public Path and(ElementProperty... prop) {
        return new SingleBrowserPath((BasicPath)path.and(prop));
    }

    @Override
    public Path inside(Path another) {
        return new SingleBrowserPath((BasicPath)path.inside(another));
    }

    @Override
    public Path afterSibling(Path another) {
        return new SingleBrowserPath((BasicPath)path.afterSibling(another));
    }

    @Override
    public Path immediatelyAfterSibling(Path another) {
         return new SingleBrowserPath((BasicPath)path.immediatelyAfterSibling(another));
    }

    @Override
    public Path after(Path another) {
        return new SingleBrowserPath((BasicPath)path.after(another));
    }

    @Override
    public Path beforeSibling(Path another) {
        return new SingleBrowserPath((BasicPath)path.beforeSibling(another));
    }

    @Override
    public Path immediatelyBeforeSibling(Path another) {
        return new SingleBrowserPath((BasicPath)path.immediatelyBeforeSibling(another));
    }

    @Override
    public Path before(Path another) {
        return new SingleBrowserPath((BasicPath)path.before(another));
    }

    @Override
    public Path childOf(Path another) {
        return new SingleBrowserPath((BasicPath)path.childOf(another));
    }

    @Override
    public Path parentOf(Path another) {
        return new SingleBrowserPath((BasicPath)path.parentOf(another));
    }

    @Override
    public Path containing(Path another) {
        return new SingleBrowserPath((BasicPath)path.containing(another));
    }

    @Override
    public Path contains(Path another) {
        return new SingleBrowserPath((BasicPath)path.contains(another));
    }

    @Override
    public Path ancestorOf(Path another) {
        return new SingleBrowserPath((BasicPath)path.ancestorOf(another));
    }

    @Override
    public Path descendantOf(Path another) {
        return new SingleBrowserPath((BasicPath)path.descendantOf(another));
    }

    @Override
    public Path withGlobalIndex(Integer index) {
        return new SingleBrowserPath((BasicPath)path.withGlobalIndex(index));
    }

    @Override
    public Path withClass(String cssClass) {
        return new SingleBrowserPath((BasicPath)path.withClass(cssClass));
    }

    @Override
    public Path withClasses(String... cssClasses) {
        return new SingleBrowserPath((BasicPath)path.withClasses(cssClasses));
    }

    @Override
    public Path withText(String txt) {
        return new SingleBrowserPath((BasicPath)path.withText(txt));
    }

    @Override
    public Path withTextContaining(String txt) {
        return new SingleBrowserPath((BasicPath)path.withTextContaining(txt));
    }


    /////////// ACTIONS ////////////////////////////////////
    ////////////////////////////////////////////////////////

    /**
     * Find the (first) element in the browser for this path
     * @return the WebElement
     */
    public WebElement find() {
        return InBrowserSinglton.find(path);
    }

    /**
     * Find all elements in the browser with this path
     * @return a list of all WebElements with this path
     */
    public List<WebElement> findAll() {
        return InBrowserSinglton.findAll(path);
    }

    /**
     * scroll the browser until this element is visible
     * @return the WebElement that was scrolled to
     */
    public WebElement scrollTo() {
        return InBrowserSinglton.scrollTo(path);
    }

    /**
     * hover over the element with this path in the browser
     */
    public void hover() {
        InBrowserSinglton.hoverOver(path);
    }

    /**
     * right click at the location of this element
     */
    public void rightClick() {
        InBrowserSinglton.rightClick(path);
    }

    /**
     * click at the location of this element
     */
    public void click() {
        InBrowserSinglton.clickAt(path);
    }

    /**
     * doubleclick at the location of this element
     */
    public void doubleClick() {
        InBrowserSinglton.doubleClickOn(path);
    }

    /**
     * drag and drop this element, to another element or another location.
     * Examples:
     * <pre>
     * {@code
     *    element.dragAndDrop().to(anotherElement);
     *    element.dragAndDrop().to(50, 50);
     * }
     * </pre>
     * @return DragAndDrop instance. See examples for usage.
     */
    public Operations.DragAndDrop dragAndDrop() {
        return InBrowserSinglton.dragAndDrop(path);
    }

    /**
     * send keys to element
     * @param charsToSend the keys to send.
     * Examples:
     * <pre>
     * {@code
     *    input.sendKeys("abc");
     *    input.sendKeys("a", "bc");
     * }
     * </pre>
     * @throws Operations.OperationFailedException - operation failed. Includes root cause.
     */
    public void sendKeys(CharSequence... charsToSend) throws Operations.OperationFailedException {
         InBrowserSinglton.sendKeys(charsToSend).to(path);
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
