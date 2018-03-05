package com.github.loyada.jdollarx;


import org.junit.Test;
import org.w3c.dom.NodeList;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.CustomElementProperties.createPropertyGenerator;
import static com.github.loyada.jdollarx.CustomElementProperties.hasProperty;
import static com.github.loyada.jdollarx.ElementProperties.hasClass;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CustomPropertiesTest extends XPathTester {
    @Test
    public void customElementPropertySingleParam() {
        Function<String, ElementProperty> role = createPropertyGenerator(
                value -> format("@role='%s'", value),
                value -> format("has role %s", value)
        );
        Path el = div.that(hasProperty(role, "foo")).and(hasClass("x"));
        NodeList nodes = findAllByXpath("<div class=\"a\">x</div><div class=\"b\">x</div><div class=\"x\" role=\"foo\">x</div><div role=\"bar\" class=\"d\">x</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), is("x"));
        assertThat(el.toString(), is(equalTo("div, that has role foo, and has class x")));
    }

    @Test
    public void customElementPropertyTwoParams() {
        BiFunction<String, Integer, ElementProperty> hasDataNum = createPropertyGenerator(
                (attr,value) -> format("@data-%s='%d'", attr, value),
                (attr, value) -> format("has data %s of %d", attr, value)
        );
        Path el = div.that(hasProperty(hasDataNum, "foo", 4)).and(hasClass("x"));
        NodeList nodes = findAllByXpath("<div data-foo=\"3\" class=\"a\">x</div><div class=\"b\">x</div><div class=\"x\" data-foo=\"4\">x</div><div role=\"bar\" class=\"d\">x</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), is("x"));
        assertThat(el.toString(), is(equalTo("div, that has data foo of 4, and has class x")));
    }
}
