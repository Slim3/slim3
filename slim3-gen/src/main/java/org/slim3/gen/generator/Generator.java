package org.slim3.gen.generator;

import javax.lang.model.element.Element;

public interface Generator<R, E extends Element, P> {

    R generate(E e, P p);
}
