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
package org.slim3.struts.util;

import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for {@link S3ExecuteConfig}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class S3ExecuteConfigUtil {

    private static final String KEY = S3ExecuteConfigUtil.class.getName();

    private S3ExecuteConfigUtil() {
    }

    /**
     * Returns the execute configuration.
     * 
     * @return the execute configuration
     */
    public static S3ExecuteConfig getExecuteConfig() {
        return (S3ExecuteConfig) WebLocator.getRequest().getAttribute(KEY);
    }

    /**
     * Sets the execute configuration.
     * 
     * @param executeConfig
     *            the execute configuration
     */
    public static void setExecuteConfig(S3ExecuteConfig executeConfig) {
        WebLocator.getRequest().setAttribute(KEY, executeConfig);
    }
}