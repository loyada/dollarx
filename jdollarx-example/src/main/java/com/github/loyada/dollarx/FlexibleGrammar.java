package com.github.loyada.dollarx;

import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;

public class FlexibleGrammar {

    public static void main(String[] argc){
        Path contactsTable = div.withClasses("ag-table", "contacts");
        Path row = div.withClass("ag-row");

        Path contact = row.inside(table).describedBy("contact");

        System.out.println(contact.that(hasAggregatedTextContaining("john smith")));
        Path dialog = div.that(hasClass("ui-dialog"));
        row = element.withClass("condition").inside(dialog);
        // any element, that has class condition, inside (div, that has class ui-dialog)
        // or....
        row = element.inside(dialog).withClass("condition");
        row = element.inside(dialog).that(hasClass("condition"));
        row = element.inside(dialog).and(hasClass("condition"));

        row = element.that(hasClass("condition"), isInside(dialog));
        row = element.that(hasClass("condition").and(isInside(dialog)));
        row = element.that(hasClass("condition")).and(isInside(dialog));
        // any element, that [has class condition, has ancestor: (div, that has class ui-dialog)]
        row = element.inside(dialog).that(hasClass("condition"));

        row = element.withClass("condition").and(isInside(dialog));
        row = element.withClass("condition").and(isContainedIn(dialog));
        row = element.that(hasAncesctor(dialog)).and(hasClass("condition"));
        // any element, that [has ancestor: (div, that has class ui-dialog), has class condition]

        // if you prefer to break the definition to two steps:
        Path condition = element.withClass("condition");
        row = condition.inside(dialog);
        System.out.println(row);
    }

}
