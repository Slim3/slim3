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

import org.apache.struts.util.TokenProcessor;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for transaction token.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class TokenUtil {

    private TokenUtil() {
    }

    /**
     * Saves a transaction token into session.
     * 
     */
    public static void saveToken() {
        TokenProcessor.getInstance().saveToken(WebLocator.getRequest());
    }

    /**
     * Determines if there is a transaction token in session.
     * 
     * @param reset
     *            whether token is reset after checking it
     * @return whether there is a transaction token in session.
     * 
     */
    public static boolean isTokenValid(boolean reset) {
        return TokenProcessor.getInstance().isTokenValid(
                WebLocator.getRequest(), reset);
    }
}