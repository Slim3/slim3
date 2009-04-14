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
package org.slim3.struts;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;

/**
 * An interface to define global constants.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public interface S3StrutsGlobals {

    /**
     * The view prefix key.
     */
    String VIEW_PREFIX_KEY = "slim3.viewPrefix";

    /**
     * The controller package key.
     */
    String CONTROLLER_PACKAGE_KEY = "slim3.controllerPackage";

    /**
     * The encoding key.
     */
    String ENCODING_KEY = "slim3.encoding";

    /**
     * The action extension.
     */
    String ACTION_EXTENSION = ".do";

    /**
     * The size exception key.
     */
    String SIZE_EXCEPTION_KEY = SizeLimitExceededException.class.getName();
}
