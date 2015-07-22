package info.testtools.jdollarx.singlebrowser;

import info.testtools.jdollarx.BasicPath;
import info.testtools.jdollarx.ElementProperty;
import info.testtools.jdollarx.Operations;
import info.testtools.jdollarx.Path;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

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
    public static final SingleBrowserPath html = new SingleBrowserPath(BasicPath.html);
    public static final SingleBrowserPath body = new SingleBrowserPath(BasicPath.body);
    public static final SingleBrowserPath header1 = new SingleBrowserPath(BasicPath.header1);
    public static final SingleBrowserPath header2 = new SingleBrowserPath(BasicPath.header2);
    public static final SingleBrowserPath header3 = new SingleBrowserPath(BasicPath.header3);
    public static final SingleBrowserPath header4 = new SingleBrowserPath(BasicPath.header4);
    public static final SingleBrowserPath header5 = new SingleBrowserPath(BasicPath.header5);
    public static final SingleBrowserPath header6 = new SingleBrowserPath(BasicPath.header6);
    public static final SingleBrowserPath header = new SingleBrowserPath(BasicPath.header);


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
    public Optional<String> getXPath() {
        return path.getXPath();
    }

    @Override
    public List<ElementProperty> getElementProperties() {
        return path.getElementProperties();
    }

    @Override
    public Path describedBy(String description) {
        return new SingleBrowserPath(path.describedBy(description));
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
    public Path inside(Path another) {
        return new SingleBrowserPath((BasicPath)path.inside(another));
    }

    @Override
    public Path afterSibling(Path another) {
        return new SingleBrowserPath((BasicPath)path.afterSibling(another));
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
    public Path withIndex(Integer index) {
        return new SingleBrowserPath((BasicPath)path.withIndex(index));
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

    public WebElement find() {
        return InBrowserSinglton.find(path);
    }

    public List<WebElement> findAll() {
        return InBrowserSinglton.findAll(path);
    }

    public WebElement scrollTo() {
        return InBrowserSinglton.scrollTo(path);
    }

    public void hover() {
        InBrowserSinglton.hoverOver(path);
    }

    public void click() {
        InBrowserSinglton.clickAt(path);
    }

    public void doubleClick() {
        InBrowserSinglton.doubleClickOn(path);
    }

    public Operations.DragAndDrop dragAndDrop() {
        return InBrowserSinglton.dragAndDrop(path);
    }

    public void sendKeys(CharSequence... charsToSend) {
         InBrowserSinglton.sendKeys(charsToSend).to(path);
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
