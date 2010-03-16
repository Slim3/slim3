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
package org.slim3.gwt.server.rpc;

import com.google.gwt.user.server.rpc.RPCRequest;

/**
 * The RPC request for Slim3.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class S3RPCRequest {

    /**
     * The service.
     */
    protected Object service;

    /**
     * The original request.
     */
    protected RPCRequest originalRequest;

    /**
     * Constructor.
     * 
     * @param service
     *            the service
     * @param originalRequest
     *            the original request
     */
    public S3RPCRequest(Object service, RPCRequest originalRequest) {
        if (service == null) {
            throw new NullPointerException("The service parameter is null.");
        }
        if (originalRequest == null) {
            throw new NullPointerException(
                "The originalRequest parameter is null.");
        }
        this.service = service;
        this.originalRequest = originalRequest;
    }

    /**
     * Returns the service.
     * 
     * @return the service
     */
    public Object getService() {
        return service;
    }

    /**
     * Returns the original request.
     * 
     * @return the original request
     */
    public RPCRequest getOriginalRequest() {
        return originalRequest;
    }
}