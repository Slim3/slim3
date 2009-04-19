/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
    protected final String packageName;
    protected final Set<String> qualifiedNameSet = new TreeSet<String>();
    protected final Set<String> simpleNameSet = new HashSet<String>();

    /**
     * Creates a new {@link ImportedNames}.
     * 
     * @param packageName
     *            the package name of class which imports classes this oject
     *            represents.
     */
    public ImportedNames(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Adds the qualified class name.
     * 
     * @param qualifiedName
     *            the qualified class name.
     * @return a qualified name if it does not belong to a package or it is not
     *         yet added, otherwise a simple name.
     */
    public String add(String qualifiedName) {
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            return qualifiedName;
        }
        String packageName = qualifiedName.substring(0, pos);
        String simpleName = qualifiedName.substring(pos + 1);
        if (!this.packageName.equals(packageName)) {
            if (!qualifiedNameSet.contains(qualifiedName)) {
                if (!simpleNameSet.contains(simpleName)) {
                    simpleNameSet.add(simpleName);
                    qualifiedNameSet.add(qualifiedName);
                } else {
                    return qualifiedName;
                }
            }
        }
        return simpleName;
    }

    @Override
    public Iterator<String> iterator() {
        final Iterator<String> internalIterator = qualifiedNameSet.iterator();
        return new Iterator<String>() {

            protected String name;

            @Override
            public boolean hasNext() {
                while (internalIterator.hasNext()) {
                    name = internalIterator.next();
                    if (name.startsWith("java.lang.")) {
                        continue;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public String next() {
                return name;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

}
