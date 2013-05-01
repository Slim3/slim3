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
package org.slim3.memcache;

import com.google.appengine.api.memcache.ConsistentErrorHandler;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.apphosting.api.ApiProxy.CapabilityDisabledException;

/**
 * @author higa
 * @since 1.0.1
 * 
 */
public class S3ErrorHandler implements ConsistentErrorHandler {

    /**
     * Constructor.
     */
    public S3ErrorHandler() {
    }

    public void handleDeserializationError(InvalidValueException thrown) {
        throw thrown;
    }

    public void handleServiceError(MemcacheServiceException thrown) {
        Throwable cause = thrown.getCause();
        if (cause instanceof CapabilityDisabledException) {
            throw (CapabilityDisabledException) cause;
        }
        throw thrown;
    }
}