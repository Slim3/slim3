/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.gae.jdo;

/**
 * A meta data for attribute.
 * 
 * @author higa
 * @param <T>
 *            the attribute type
 * @since 3.0
 * 
 */
public class AttributeMeta<T> {

    /**
     * The name.
     */
    protected String name;

    /**
     * Constructor.
     * 
     * @param name
     *            the name
     * @throws NullPointerException
     *             the name parameter is null
     */
    public AttributeMeta(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        this.name = name;
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the "equal" filter criterion.
     * 
     * @param parameter
     * @return the "equal" filter criterion
     */
    public EqCriterion<T> eq(T parameter) {
        if (parameter == null) {
            return null;
        }
        return new EqCriterion<T>(name, name + "Param", parameter);
    }

    /**
     * Returns the "less than" filter criterion.
     * 
     * @param parameter
     * @return the "less than" filter criterion
     */
    public LtCriterion<T> lt(T parameter) {
        if (parameter == null) {
            return null;
        }
        return new LtCriterion<T>(name, name + "LtParam", parameter);
    }

    /**
     * Returns the "less equal" filter criterion.
     * 
     * @param parameter
     * @return the "less equals" filter criterion
     */
    public LeCriterion<T> le(T parameter) {
        if (parameter == null) {
            return null;
        }
        return new LeCriterion<T>(name, name + "LeParam", parameter);
    }

    /**
     * Returns the "greater than" filter criterion.
     * 
     * @param parameter
     * @return the "greater than" filter criterion
     */
    public GtCriterion<T> gt(T parameter) {
        if (parameter == null) {
            return null;
        }
        return new GtCriterion<T>(name, name + "GtParam", parameter);
    }

    /**
     * Returns the "greater equal" filter criterion.
     * 
     * @param parameter
     * @return the "greater equal" filter criterion
     */
    public GeCriterion<T> ge(T parameter) {
        if (parameter == null) {
            return null;
        }
        return new GeCriterion<T>(name, name + "GeParam", parameter);
    }

    /**
     * Returns the "ascending" order criterion.
     * 
     * @return the "ascending" order criterion
     */
    public AscCriterion asc() {
        return new AscCriterion(name);
    }

    /**
     * Returns the "descending" order criterion.
     * 
     * @return the "descending" order criterion
     */
    public DescCriterion desc() {
        return new DescCriterion(name);
    }
}
