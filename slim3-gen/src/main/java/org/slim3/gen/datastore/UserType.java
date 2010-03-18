/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.gen.datastore;

import org.slim3.gen.ClassConstants;

/**
 * Represents {@code com.google.appengine.api.users.User} type.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class UserType extends CoreReferenceType {

    /**
     * Creates a new {@link UserType}.
     */
    public UserType() {
        super(ClassConstants.User);
    }

    @Override
    public <R, P, TH extends RuntimeException> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitUserType(this, p);
    }

}
