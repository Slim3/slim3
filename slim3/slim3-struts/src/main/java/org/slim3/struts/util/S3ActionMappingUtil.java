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
import org.apache.struts.action.ActionMapping;
import org.slim3.struts.config.S3ActionMapping;
import org.slim3.struts.web.WebLocator;

/**
 * A utility of {@link ActionMapping}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class S3ActionMappingUtil {

    private S3ActionMappingUtil() {
    }

    /**
     * Returns the action mapping
     * 
     * @return the action mapping
     */
    public static S3ActionMapping getActionMapping() {
        return (S3ActionMapping) WebLocator.getRequest().getAttribute(
                Globals.MAPPING_KEY);
    }

    /**
     * Finds the action mapping
     * 
     * @param path
     *            the path
     * @return action mapping
     */
    public static S3ActionMapping findActionMapping(String path) {
        return (S3ActionMapping) S3ModuleConfigUtil.getModuleConfig()
                .findActionConfig(path);
    }
}