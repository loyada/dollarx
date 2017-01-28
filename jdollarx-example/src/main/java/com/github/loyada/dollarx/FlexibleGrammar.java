package com.github.loyada.dollarx;

import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;

public class FlexibleGrammar {

    public static void main(String[] argc){
        Path dialog = div.that(hasClass("ui-dialog"));
        Path row = element.withClass("condition").inside(dialog);

        // or....
        element.inside(dialog).withClass("condition");
        element.inside(dialog).that(hasClass("condition"));
        element.inside(dialog).and(hasClass("condition"));

        element.that(hasClass("condition"), isInside(dialog));
        element.that(hasClass("condition").and(isInside(dialog)));
        element.that(hasClass("condition")).and(isInside(dialog));
        element.inside(dialog).that(hasClass("condition"));

        element.withClass("condition").and(isInside(dialog));
        element.withClass("condition").and(isContainedIn(dialog));
        element.that(hasAncesctor(dialog)).and(hasClass("condition"));

        // if you prefer to break the definition to two steps:
        Path condition = element.withClass("condition");
        condition.inside(dialog);

        System.out.println(row);
    }

}
