package com.github.loyada.jdollarx;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ElementProperties {

    private ElementProperties() {
    }

    public static final ElementProperty isLastSibling = new ElementProperty() {
        @Override
        public String toXpath() {
            return "last()";
        }

        public String toString() {
            return "is last sibling";
        }
    };

    public static ElementProperty hasNChildren(Integer n) {
        return new ElementProperty() {
            @Override
            public String toXpath() {
                return "count(./*)=" + n;
            }

            public String toString() {
                return String.format("has %d children", n);
            }
        };
    }

    public static final ElementProperty hasNoChildren = new ElementProperty() {
        @Override
        public String toXpath() {
            return "count(./*)=0";
        }

        public String toString() {
            return "has no children";
        }
    };

    public static final ElementProperty hasChildren = new ElementProperty() {
        @Override
        public String toXpath() {
            return "count(./*)>0";
        }

        public String toString() {
            return "has some children";
        }
    };

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

    public static final ElementProperty isOnlyChild = new ElementProperty() {
        @Override
        public String toXpath() {
            return "count(preceding-sibling::*)=0 and count(following-sibling::*)=0";
        }

        public String toString() {
            return "is only child";
        }
    };

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

    public static ElementProperty rawXpathProperty(String rawXpathProps, String rawExplanation) {
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

    public static ElementProperty hasName(String name) {
        return hasAttribute("name", name);
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

    public static ElementProperty withIndex(Integer index) {
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

    public static ElementProperty isHidden = new ElementProperty() {
        @Override
        public String toXpath() {
            return XpathUtils.isHidden;
        }

        public String toString() {
            return "is hidden";
        }
    };


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
        return relation + "::" + path.getXPath().get();
    }

    private static String rValueToString(Path path) {
        return (path.toString().trim().contains(" ")) ? "(" + path + ")" : path.toString();
    }

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

    public static ElementProperty hasParent(Path path) {
        return isChildOf(path);
    }

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

    public static ElementProperty hasChild(Path... paths) {
        return isParentOf(paths);
    }

    public static ElementProperty contains(Path... paths) {
        return new RelationBetweenMultiElement("descendant", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("has descendant");
            }
        };
    }

    public static ElementProperty isAncestorOf(Path... webElements) {
        return contains(webElements);
    }

    public static ElementProperty hasDescendant(Path... path) {
        return contains(path);
    }

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

    public static ElementProperty isDescendantOf(Path path) {
        return hasAncesctor(path);
    }

    public static ElementProperty isInside(Path path) {
        return hasAncesctor(path);
    }

    public static ElementProperty isContainedIn(Path path) {
        return hasAncesctor(path);
    }

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


    public static ElementProperty isAfterSibling(Path... paths) {
        return new RelationBetweenMultiElement("preceding-sibling", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("is after sibling");
            }
        };
    }

    public static ElementProperty isBeforeSibling(Path... paths) {
        return new RelationBetweenMultiElement("following-sibling", Arrays.asList(paths)) {
            @Override
            public String toString() {
                return asString("is before sibling");
            }
        };
    }

    public static ElementProperty not(ElementProperty prop) {
        return new Not(prop);
    }

    static final class Not implements ElementProperty {
        private final ElementProperty p;

        public Not(final ElementProperty p) {
            this.p = p;
        }

        public String toString() {
            return "not (" + p + ")";
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

        public RelationBetweenMultiElement(final String relation, final List<Path> paths) {
            this.paths = paths;
            this.relation = relation;
        }

        public String toXpath() {
            return getRelationXpath(relation);
        }

        protected String asString(final String prefix) {
            String asList = paths.stream().
                    map(path -> rValueToString(path)).
                    collect(Collectors.joining(", "));
            return String.format("%s: %s", plural(prefix),
                    (paths.size() > 1) ? String.format("[%s]", asList) : asList);
        }

        protected String plural(final String relation) {
            return (paths.size() == 1) ? relation : (relation + "s");
        }

        protected String getXpathExpressionForSingle(final Path path) {
            return String.format("%s::%s", relation, path.getXPath().get());
        }


        private String getRelationForSingleXpath(final Path path, final String relation) {
            if (path.getUnderlyingSource().isPresent() || !path.getXPath().isPresent())
                throw new IllegalArgumentException("must use a pure xpath Path");
            return getXpathExpressionForSingle(path);
        }

        protected String getRelationXpath(final String relation) {
            final String result = paths.stream().
                    map(path -> {
                return getRelationForSingleXpath(path, relation);
            }).collect(Collectors.joining(") and ("));
            return (paths.size() > 1) ? String.format("(%s)", result) : result.toString();
        }

        private String rValueToString(Path path) {
            return ((path.toString().trim().contains(" "))) ? "(" + path + ")" : path.toString();
        }
    }
}
