package com.github.loyada.dollarx;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.NPath.*;


public class PathExamples {
    public static void main(String[] argc) {
        System.out.println(input.that(hasAttribute("name", "goo"), isAfter(atLeast(2).occurrencesOf(div))));
    }

}
