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
package org.slim3.gen.generator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents imported class names.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ImportedNames implements Iterable<String> {

    /** the package name of class which imports classes this oject represents */
    protected final String packageName;

    /** the set of qualified class names */
    protected final Set<String> qualifiedNameSet = new HashSet<String>();

    /** the set of simple class names */
    protected final Set<String> simpleNameSet = new HashSet<String>();

    /**
     * Creates a new {@link ImportedNames}.
     * 
     * @param packageName
     *            the package name of class which imports classes this oject
     *            represents. an empty name is acceptable for an unnamed
     *            package.
     */
    public ImportedNames(String packageName) {
        if (packageName == null) {
            throw new NullPointerException("The pacakgeName parameter is null.");
        }
        this.packageName = packageName;
    }

    /**
     * Adds the qualified class name.
     * 
     * @param qualifiedName
     *            the qualified class name.
     * @return the qualified class name if the simple class name has been
     *         already added.
     */
    public String add(String qualifiedName) {
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            String simpleName = qualifiedName;
            simpleNameSet.add(simpleName);
            return simpleName;
        }
        String simpleName = qualifiedName.substring(pos + 1);
        if (qualifiedNameSet.contains(qualifiedName)) {
            return simpleName;
        } else if (simpleNameSet.contains(simpleName)) {
            return qualifiedName;
        }
        simpleNameSet.add(simpleName);
        qualifiedNameSet.add(qualifiedName);
        return simpleName;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> set = new TreeSet<String>();
        for (String name : qualifiedNameSet) {
            int pos = name.lastIndexOf('.');
            if (pos > -1) {
                String packageName = name.substring(0, pos);
                if (packageName.equals("java.lang")
                        || this.packageName.equals(packageName)) {
                    continue;
                }
            }
            set.add(name);
        }
        return set.iterator();
    }

}
