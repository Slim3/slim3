package org.slim3.gen.generator;

import javax.lang.model.element.Element;

public interface Generator<T extends Element> {

    public void generate(T element);
}
