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
package org.slim3.mvc.controller;

import org.slim3.mvc.MvcConstants;

/**
 * The locator for {@link Controller}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ControllerLocator {

    /**
     * Returns the controller from the current request.
     * 
     * @return the controller
     */
    public static Controller getController() {
        return (Controller) RequestLocator.getRequest().getAttribute(
            MvcConstants.CONTROLLER_KEY);
    }

    /**
     * Sets the controller to the current request.
     * 
     * @param controller
     *            the controller
     */
    public static void setController(Controller controller) {
        RequestLocator.getRequest().setAttribute(
            MvcConstants.CONTROLLER_KEY,
            controller);
    }

    private ControllerLocator() {
    }
}