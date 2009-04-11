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

import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;
import org.slim3.struts.config.S3ModuleConfig;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for {@link ModuleConfig}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class S3ModuleConfigUtil {

    /**
     * Returns the module configuration.
     * 
     * @return the module configuration
     */
    public static S3ModuleConfig getModuleConfig() {
        return (S3ModuleConfig) WebLocator.getServletContext().getAttribute(
                Globals.MODULE_KEY);
    }

    private S3ModuleConfigUtil() {
    }
}