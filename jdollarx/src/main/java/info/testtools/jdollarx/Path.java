package info.testtools.jdollarx;


import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public interface Path {
    Optional<WebElement> getUnderlyingSource();

    Optional<String> getXpathExplanation();

    Optional<String> getDescribedBy();

    Optional<String> getXPath();

    List<ElementProperty> getElementProperties();

    Path describedBy(String description);

    Path or(Path path);

    Path that(ElementProperty... prop);

    Path withText(String txt);

    Path inside(Path path);

    Path afterSibling(Path path);

    Path after(Path path);

    Path beforeSibling(Path path);

    Path before(Path path);

    Path childOf(Path path);

    Path parentOf(Path path);

    Path containing(Path path);

    Path contains(Path path);

    Path ancestorOf(Path path);

    Path descendantOf(Path path);

    Path withIndex(Integer index);

    Path withClass(String cssClass);

    Path withClasses(String... cssClasses);

    Path withTextContaining(String txt);
}
