package info.testtools.jdollarx;


public interface ElementProperty {
    String toXpath() ;

    default ElementProperty or(ElementProperty p) {
        return new ElementProperties.Or(this, p);
    }

    default ElementProperty and(ElementProperty prop) {
        return new ElementProperties.And(this, prop);
    }

    default ElementProperty andNot(ElementProperty prop) { return and(new ElementProperties.Not(prop)); }
}
