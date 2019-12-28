package com.github.loyada.jdollarx;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.loyada.jdollarx.PathUtils.transformXpathToCorrectAxis;

/**
 * Various constrains  on {@link Path} instances, that are used with the methods {@link Path#that}
 * and {@link Path#and}.
 */
public final class ElementProperties {

    private ElementProperties() {
    }

    /**
     * The element is the last sibling (ie: last child) of its parent.
     */
    public static final ElementProperty isLastSibling = new ElementProperty() {
        @Override
        public String toXpath() {
            return "last()";
        }

        public String toString() {
            return "is last sibling";
        }
    };

    /**
     * The element is diabled
     */
    public static final ElementProperty isDisabled = new ElementProperty() {
        @Override
        public String toXpath() {
            return "@disabled";
        }

        public String toString() {
            return "is disabled";
        }
    };

    /**
     * The element is enabled
     */
    public static final ElementProperty isEnabled = new ElementProperty() {
        @Override
        public String toXpath() {
            return "not(@disabled)";
        }

        public String toString() {
            return "is enabled";
        }
    };

    /**
     * The element is checked
     */
    public static final ElementProperty isChecked = new ElementProperty() {
        @Override
        public String toXpath() {
            return "@checked";
        }

        public String toString() {
            return "is checked";
        }
    };

    /**
     * The element is selected
     */
    public static final ElementProperty isSelected = new ElementProperty() {
        @Override
        public String toXpath() {
            return "@selected";
        }

        public String toString() {
            return "is selected";
        }
    };

    /**
     * The element has n direct children
     * @param n the number of children
     * @return a element property that can be applied with Path::that
     */
    public static ElementPropertyWithNumericalBoundaries hasNChildren(Integer n) {
        return new ElementPropertyWithNumericalBoundaries() {
            @Override
            public String toXpath() {
                return "count(./*)=" + n;
            }

            @Override
            public String toString() {
                return String.format("has %d children", n);
            }

            /**
             * The element has at least n direct children
             * @return a element property that can be applied with Path::that
             */
            @Override
            public ElementProperty orMore() {
                return new ElementProperty() {
                    @Override
                    public String toXpath() {
                        return "count(./*)>=" + n;
                    }

                    public String toString() {
                        return String.format("has at least %d children", n);
                    }
                };
            }

            /**
             * The element has at most n direct children
             * @return a element property that can be applied with Path::that
             */
            @Override
            public ElementProperty orLess() {
                return new ElementProperty() {
                    @Override
                    public String toXpath() {
                        return "count(./*)<=" + n;
                    }

                    public String toString() {
                        return String.format("has at most %d children", n);
                    }
                };
            }
        };
    }

    /**
     * The element has no children. Examples where it might be useful: an empty list, empty table, etc.
     */
    public static final ElementProperty hasNoChildren = new ElementProperty() {
        @Override
        public String toXpath() {
            return "count(./*)=0";
        }

        public String toString() {
            return "has no children";
        }
    };

    /**
     * The element has 1 or more children (the opposite from hasNoChildren). Examples where it might be useful: an non-empty list, non-empty table, etc.
     */
    public static final ElementProperty hasChildren = new ElementProperty() {
        @Override
        public String toXpath() {
            return "count(./*)>0";
        }

        public String toString() {
            return "has some children";
        }
    };

    /**
     * The element is the nth-from-last sibling. Example usage: find the element before the last one in a list.
     * @param reverseIndex - the place from last, starting at 0 for the last sibling.
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty isNthFromLastSibling(Integer reverseIndex) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return String.format("count(following-sibling::*)=%d", reverseIndex);
            }

            public String toString() {
                return String.format("is in place %d from the last sibling", reverseIndex);
            }
        };
    }

    /**
     * The element is the nth sibling. Example usage: find the 4th element in a list.
     * @param index - starting at 0 for the first one
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty isNthSibling(Integer index) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return String.format("count(preceding-sibling::*)=%d", index);
            }

            public String toString() {
                return String.format("is in place %d among its siblings", index);
            }
        };
    }

    /**
     * The element is the only direct child of its parent. It has no siblings. For example: a table with a single row.
     */
    public static final ElementProperty isOnlyChild = new ElementProperty() {
        @Override
        public String toXpath() {
            return "count(preceding-sibling::*)=0 and count(following-sibling::*)=0";
        }

        public String toString() {
            return "is only child";
        }
    };

    /**
     * The index among its siblings is between first and last parameters. For example: taking a row from a table, which we know is between row number 2 and 4.
     * @param first - lower index (inclusive, starting at 0)
     * @param last - upper index (inclusive, starting at 0)
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty withIndexInRange(int first, int last) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return String.format("position()>=%d and position()<=%d", first + 1, last + 1);
            }

            public String toString() {
                return String.format("with index from %d to %d", first, last);
            }
        };
    }

    /**
     * Custom property that allows to state the raw expath of a property, and give a string description of it.
     * Example:
     * <pre>
     * {@code
     *
     *  Path el = span.that(hasRawXpathProperty("string(.)='x'", "is awesome"), isOnlyChild);
     *  assertThat(el.getXPath().get(), equalTo("span[string(.)='x'][count(preceding-sibling::*)=0" +
     *                                                "and count(following-sibling::*)=0]"));
     *  assertThat(el.toString(), is(equalTo("span, that is awesome, and is only child")));
     * }
     * </pre>
     *
     * @param rawXpathProps - the xpath property condition string. Will be wrapped with [] in the xpath
     * @param rawExplanation - a textual readable description of this property
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasRawXpathProperty(String rawXpathProps, String rawExplanation) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return rawXpathProps;
            }

            public String toString() {
                return rawExplanation;
            }
        };
    }

    /**
     * Element has text equals to the given string parameter, ignoring case.
     * @param txt - the text to match to
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasText(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.textEquals(txt);
            }

            public String toString() {
                return "has the text \"" + txt + "\"";
            }
        };
    }

    /**
     * Element has text equals to the given string parameter.
     * The equality is case-sensitive.
     * @param txt - the text to match to (case sensitive)
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasCaseSensitiveText(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.caseSensitiveTextEquals(txt);
            }

            public String toString() {
                return "has the text \"" + txt + "\"";
            }
        };
    }

    /**
     * Element has text that starts with the given parameter
     * @param txt - the text to match to
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasTextStartingWith(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.textStartsWith(txt);
            }

            public String toString() {
                return "has text that starts with \"" + txt + "\"";
            }
        };
    }

    /**
     * Element has text that ends with the given parameter
     * @param txt - the text to match to
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasTextEndingWith(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.textEndsWith(txt);
            }

            public String toString() {
                return "has text that ends with \"" + txt + "\"";
            }
        };
    }

    /**
     * Element has ID equals to the given parameter
     * @param id - the ID to match to
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasId(String id) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.hasId(id);
            }

            public String toString() {
                return String.format("has Id \"%s\"", id);
            }
        };
    }

    /**
     * Element with a "name" attribute equal to the given parameter. Useful for input elements.
     * @param name the value of the name property
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasName(String name) {
        return hasAttribute("name", name);
    }

    /**
     * Element with a "src" attribute equal to the given parameter. Useful for images.
     * @param src the URI of the image
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasSource(String src) { return hasAttribute("src", src); }

    /**
     * Element with a "role" attribute equal to the given role.
     * @param role the value of the role property
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasRole(String role) {
        return hasAttribute("role", role);
    }

    /**
     * Element with a "ref" attribute equal to the given role.
     * @param ref the value of the role property
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasRef(String ref) {
        return hasAttribute("ref", ref);
    }

    public static ElementProperty hasAttribute(String attribute, String value) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.hasAttribute(attribute, value);
            }

            public String toString() {
                return String.format("has %s: \"%s\"", attribute, value);
            }
        };
    }

    /**
     * Element that has at least one of the classes given
     * @param cssClasses - the class names to match to
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasAnyOfClasses(String... cssClasses) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.hasAnyOfClasses(cssClasses);
            }

            public String toString() {
                String classesAsList = String.join(", ", cssClasses);
                return "has at least one of the classes [" + classesAsList + "]";
            }
        };
    }

    /**
     * Element that has all of the given classes
     * @param cssClasses - the class names to match to
     * @return a element property that can be applied with Path::that
     */
    public static ElementProperty hasClasses(String... cssClasses) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.hasClasses(cssClasses);
            }

            public String toString() {
                String classesAsList = String.join(", ", cssClasses);
                return "has classes [" + classesAsList + "]";
            }
        };
    }

    /**
     * Element that has a class with name that contain the given parameter
     * @param classSubString a string that should be contained in the class
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasClassContaining(String classSubString) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return String.format("contains(@class, '%s')", classSubString);
            }

            public String toString() {
                return String.format("has class containing '%s'", classSubString);
            }
        };
    }

    /**
     * Element that has none of the given classes
     * @param cssClasses - a list of class names, none of which is present in element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasNonOfTheClasses(String... cssClasses) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.doesNotExist(XpathUtils.hasAnyOfClasses(cssClasses));
            }

            public String toString() {
                String classesAsList = String.join(", ", cssClasses);
                return "has non of the classes [" + classesAsList + "]";
            }
        };
    }

    /**
     * Element that is the nth sibling of its parent
     * @param index - the index of the element among its sibling, starting with 0
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isWithIndex(Integer index) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return String.format("position() = %d", index + 1);
            }

            public String toString() {
                return "with index " + index;
            }
        };
    }

    /**
     * The text in the element contains the given parameter, ignoring case
     * @param txt - the substring to match to
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasTextContaining(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.textContains(txt);
            }

            public String toString() {
                return "has text containing \"" + txt + "\"";
            }
        };
    }

    /**
     * The text in the element contains the given parameter.
     * This condition is case=sensitive.
     * @param txt - the substring to match to (case sensitive)
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasCaseSensitiveTextContaining(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.caseSensitiveTextContains(txt);
            }

            public String toString() {
                return "has text containing \"" + txt + "\"";
            }
        };
    }

    /**
     * Has the class given in the parameter
     * @param className the class the element has
     * @return An element property that can be applied with Path::that
     */
    static public ElementProperty hasClass(String className) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.hasClass(className);
            }

            public String toString() {
                return "has class " + className;
            }
        };
    }

    /**
     * When aggregating all the text under this element, it equals to the given parameter.
     * This condition is not case sensitive.
     * @param txt the aggregated, case insensitive, text inside the element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAggregatedTextEqualTo(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.aggregatedTextEquals(txt);
            }

            public String toString() {
                return "with aggregated text \"" + txt + "\"";
            }
        };
    }

    /**
     * When aggregating all the text under this element, it equals to the given parameter.
     * This condition is case sensitive.
     * @param txt the aggregated, case insensitive, text inside the element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAggregatedCaseSensitiveTextEqualTo(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.aggregatedCaseSensitiveTextEquals(txt);
            }

            public String toString() {
                return "with aggregated text \"" + txt + "\"";
            }
        };
    }

    /**
     * When aggregating all the text under this element, it starts with the given substring (ignoring case)
     * @param txt the prefix of the aggregated, case insensitive, text inside the element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAggregatedTextStartingWith(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.aggregatedTextStartsWith(txt);
            }

            public String toString() {
                return "with aggregated text that starts with \"" + txt + "\"";
            }
        };
    }

    /**
     * When aggregating all the text under this element, it ends with the given substring (ignoring case)
     * @param txt the suffix of the aggregated, case insensitive, text inside the element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAggregatedTextEndingWith(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.aggregatedTextEndsWith(txt);
            }

            public String toString() {
                return "with aggregated text that ends with \"" + txt + "\"";
            }
        };
    }

    /**
     * When aggregating all the text under this element, it contains the given substring (ignoring case)
     * @param txt a substring of the aggregated, case insensitive, text inside the element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAggregatedTextContaining(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.aggregatedTextContains(txt);
            }

            public String toString() {
                return "with aggregated text containing \"" + txt + "\"";
            }
        };
    }

    /**
     * When aggregating all the text under this element, it contains the given substring.
     * This condition is case sensitive.
     * @param txt a substring of the aggregated, case sensitive, text inside the element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAggregatedCaseSensitiveTextContaining(String txt) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return XpathUtils.aggregatedcaseSensitiveTextContains(txt);
            }

            public String toString() {
                return "with aggregated text containing \"" + txt + "\"";
            }
        };
    }

    /**
     * Element that is hidden. This is limited to only examine embeded css style, so it not useful in some cases.
     */
    public static ElementProperty isHidden = new ElementProperty() {
        @Override
        public String toXpath() {
            return XpathUtils.isHidden;
        }

        public String toString() {
            return "is hidden";
        }
    };


    /**
     * Element has non-empty text
     */
    public static ElementProperty hasSomeText = new ElementProperty() {
        @Override
        public String toXpath() {
            return XpathUtils.hasSomeText;
        }

        public String toString() {
            return "has some text";
        }
    };


    ///////////////////////////////////////////////////////////
    // relationships
    private static String getRelationXpath(Path path, String relation) {
        if (path.getUnderlyingSource().isPresent() || !path.getXPath().isPresent())
            throw new IllegalArgumentException("must use a pure xpath BasicPath");
        return relation + "::" + transformXpathToCorrectAxis(path).get();
    }

    private static String rValueToString(Path path) {
        return (path.toString().trim().contains(" ")) ? "(" + path + ")" : path.toString();
    }

    /**
     * Element is direct child of the element matched by the given parameter
     * @param path - the parent of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isChildOf(Path path) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return getRelationXpath(path, "parent");
            }

            public String toString() {
                return "is child of: " + rValueToString(path);
            }
        };
    }

    /**
     * Element is direct child of the element matched by the given parameter
     * @param path - the parent of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasParent(Path path) {
        return isChildOf(path);
    }

    /**
     * Element is the parent of the given list of elements
     * @param paths - a list of elements that are children of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isParentOf(Path... paths) {
        return new RelationBetweenMultiElement("child", Arrays.asList(paths))  {
            @Override
            protected String plural(final String relation){return relation;}
            @Override
            public String toString() {
                String relation = String.format("has %s", paths.length==1 ?  "child" : "children");
                return asString(relation);
            }
        };
    }

    /**
     * Element is the parent of the given list of elements
     * @param paths - a list of elements that are children of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasChild(Path... paths) {
        return isParentOf(paths);
    }

    /**
     * The given elements in the parameters list are contained in the current element
     * @param paths - a list of elements that are descendants of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty contains(Path... paths) {
        return new RelationBetweenMultiElement("descendant", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("has descendant");
            }
        };
    }

    /**
     * The given elements in the parameters list are contained in the current element
     * @param paths - a list of elements that are descendants of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isAncestorOf(Path... paths) {
        return contains(paths);
    }

    /**
     * The given elements in the parameters list are contained in the current element
     * @param paths - a list of elements that are descendants of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasDescendant(Path... paths) {
        return contains(paths);
    }

    /**
     * Element is inside the given parameter
     * @param path the ancestor of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty hasAncesctor(Path path) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return getRelationXpath(path, "ancestor");
            }

            public String toString() {
                return "has ancestor: " + rValueToString(path);
            }
        };
    }

    /**
     * Element is inside the given parameter
     * @param path the ancestor of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isDescendantOf(Path path) {
        return hasAncesctor(path);
    }

    /**
     * Element is inside the given parameter
     * @param path the ancestor of the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isInside(Path path) {
        return hasAncesctor(path);
    }

    public static ElementProperty isContainedIn(Path path) {
        return hasAncesctor(path);
    }

    /**
     * Element appears after all the given parameters in the document
     * @param paths - elements that precede the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isAfter(Path... paths) {
        return new RelationBetweenMultiElement("preceding", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("is after");
            }
            @Override
            protected String plural(final String relation){return relation;}
        };
    }

    /**
     * Element is after at-least/at-most/exactly the given number of the given element.
     * Example use:
     * import static com.github.loyada.jdollarx.atLeast;
     *
     * input.that(isAfter(atLeast(2).occurrencesOf(div)));
     *
     * @param nPath - at-least/at-most/exactly the given number of the given element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isAfter(NPath nPath){
        return new RelationBetweenMultiElement("preceding", nPath ) {
            @Override
            public String toString() {
                return asString(String.format("is after%s%d occurrences of", RelationOperator.opAsEnglish(nPath.qualifier), nPath.n));
            }
            @Override
            protected String plural(final String relation){return relation;}
        };
    }

    /**
     * Element is before all the elements given in the parameters
     * @param paths - all the elements that appear after the current element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isBefore(Path... paths) {
        return new RelationBetweenMultiElement("following", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("is before");
            }
            @Override
            protected String plural(final String relation){return relation;}
        };
    }

    /**
     * Element is before at-least/at-most/exactly the given number of the given element.
     * Example use:
     * <pre>
     *    import static com.github.loyada.jdollarx.isBefore;
     *    input.that(isBefore(atLeast(2).occurrencesOf(div)));
     * </pre>
      *
     * @param nPath - at-least/at-most/exactly the given number of the given element
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isBefore(NPath nPath) {
        return new RelationBetweenMultiElement("following", nPath) {
            @Override
            public String toString() {
                return asString(String.format("is before%s%d occurrences of", RelationOperator.opAsEnglish(nPath.qualifier), nPath.n));
            }
            @Override
            protected String plural(final String relation){return relation;}
        };
    }

    /**
     * Element is a sibling of all the elements defined by the given paths
     * @param paths a list of paths referring to elements
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isSiblingOf(Path... paths) {
        return new RelationBetweenMultiElement("", Arrays.asList(paths)) {
            @Override
            protected String getXpathExpressionForSingle(Path path) {
                return "(" + isAfterSibling(path).toXpath() + ") or (" + isBeforeSibling(path).toXpath() + ")";
            }
            @Override
            public String toString() {
                return asString("has sibling");
            }
        };
    }

    /**
     * Element is a sibling of all the elements defined by the given paths, AND is after all those siblings
     * @param paths a list of paths referring to elements
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isAfterSibling(Path... paths) {
        return new RelationBetweenMultiElement("preceding-sibling", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("is after sibling");
            }
        };
    }

    /**
     * Element is a sibling of the at-least/at-most/exactly n elements given, and appears after them.
     * Example:
     * <pre>
     *   Path el = element.that(isAfterSibling(exactly(2).occurrencesOf(div)));
     *   assertThat(el.toString(), is(equalTo("any element, that is after 2 siblings of type: div")));
     * </pre>
     *
     * @param nPath a count of elements that are siblings appearing before current elements.
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isAfterSibling(NPath nPath) {
        return new RelationBetweenMultiElement("preceding-sibling", nPath) {
            @Override
            public String toString() {
                return asString(String.format("is after%s%d siblings of type", RelationOperator.opAsEnglish(nPath.qualifier), nPath.n));
            }
        };
    }

    /**
     * Element is a sibling of all the elements defined by the given paths, AND is before all those siblings
     * @param paths a list of paths referring to elements
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isBeforeSibling(Path... paths) {
        return new RelationBetweenMultiElement("following-sibling", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("is before sibling");
            }
        };
    }

    /**
     * Element is a sibling of the at-least/at-most/exactly n elements given, and appears before them.
     * Example:
     * <pre>
     *   Path el = element.that(isBeforeSibling(exactly(2).occurrencesOf(div)));
     *   assertThat(el.toString(), is(equalTo("any element, that is before 2 siblings of type: div")));
     * </pre>
     * @param nPath a count of elements that are siblings appearing after current elements.
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty isBeforeSibling(NPath nPath) {
        return new RelationBetweenMultiElement("following-sibling", nPath) {
            @Override
            public String toString() {
                return asString(String.format("is before%s%d siblings of type", RelationOperator.opAsEnglish(nPath.qualifier), nPath.n));
            }
        };
    }

    /**
     * Element does NOT have the given property.
     * @param prop - the property which the element must not have
     * @return An element property that can be applied with Path::that
     */
    public static ElementProperty not(ElementProperty prop) {
        return new Not(prop);
    }

    static final class Not implements ElementProperty {
        private final ElementProperty p;

        public Not(final ElementProperty p) {
            this.p = p;
        }

        public String toString() {
            if (p.toString().startsWith("is "))
                return "is not " + p.toString().substring(3);
            else if (p.toString().startsWith("has "))
                return "has no " + p.toString().substring(4);
            else return "not (" + p + ")";
        }

        @Override
        public String toXpath() {
            return XpathUtils.doesNotExist(p.toXpath());
        }
    }


    static final class And implements ElementProperty {
        private final ElementProperty p1, p2;

        public And(final ElementProperty p1, final ElementProperty p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public String toString() {
            return String.format("(%s and %s)", p1, p2);
        }

        @Override
        public String toXpath() {
            return "(" + p1.toXpath() + " and " + p2.toXpath() + ")";
        }
    }

    static final class Or implements ElementProperty {
        private final ElementProperty p1, p2;

        public Or(final ElementProperty p1, final ElementProperty p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public String toString() {
            return String.format("(%s or %s)", p1, p2);
        }

        @Override
        public String toXpath() {
            return p1.toXpath() + " or " + p2.toXpath();
        }
    }

    private static class RelationBetweenMultiElement implements ElementProperty{
        private final String relation;
        private final List<Path> paths;
        private final Optional<NPath> nPath;

        public RelationBetweenMultiElement(final String relation, final List<Path> paths) {
            this.paths = paths;
            this.relation = relation;
            this.nPath = Optional.empty();
        }

        public RelationBetweenMultiElement(final String relation, final NPath nPath) {
            this.paths = Collections.singletonList(nPath.path);
            this.relation = relation;
            this.nPath = Optional.of(nPath);
        }

        public String toXpath() {
            return getRelationXpath();
        }

        protected String asString(final String prefix) {
            String asList = paths.stream().
                    map(this::rValueToString).
                    collect(Collectors.joining(", "));
            return String.format("%s: %s", plural(prefix),
                    (paths.size() > 1) ? String.format("[%s]", asList) : asList);
        }

        protected String plural(final String relation) {
            return (paths.size() == 1) ? relation : (relation + "s");
        }

        protected String getXpathExpressionForSingle(final Path path) {
            return String.format("%s::%s", relation, transformXpathToCorrectAxis(path).get());
        }


        private String getRelationForSingleXpath(final Path path) {
            if (path.getUnderlyingSource().isPresent() || !path.getXPath().isPresent())
                throw new IllegalArgumentException("must use a pure xpath Path");
            if (path.getXPath().get().startsWith("(")) {
                throw new IllegalArgumentException("The expression not compile to a proper xpath." +
                "Please use Path methods instead to express the relation.");
            }
            String expressionForSingle = getXpathExpressionForSingle(path);
            return nPath.map(np -> addNPathQualifier(expressionForSingle)).orElse(expressionForSingle);
        }

        private String addNPathQualifier(String path) {
            return String.format("count(%s)%s%d", path, RelationOperator.opAsXpathString(nPath.get().qualifier), nPath.get().n);
        }

        private String getRelationXpath() {
            final String result = paths.stream().
                    map(this::getRelationForSingleXpath).
                    collect(Collectors.joining(") and ("));
            return (paths.size() > 1) ? String.format("(%s)", result) : result;
        }

        private String rValueToString(Path path) {
            return ((path.toString().trim().contains(" "))) ? "(" + path + ")" : path.toString();
        }
    }
}
