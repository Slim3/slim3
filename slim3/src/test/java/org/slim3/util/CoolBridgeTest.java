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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Test;
import org.slim3.util.cool.service.Bad2Service;
import org.slim3.util.cool.service.BadService;
import org.slim3.util.cool.service.GreetService;

/**
 * @author higa
 * 
 */
public class CoolBridgeTest {

    /**
     * @throws Exception
     */
    @Test
    public void create() throws Exception {
        GreetService service = CoolBridge.create(GreetService.class);
        assertThat(service, is(notNullValue()));
        assertThat(service.greet(), is("Hello"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void createWhenClassIsNotInterface() throws Exception {
        CoolBridge.create(getClass());
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void createWhenClassIsNotUnderCoolPackage() throws Exception {
        CoolBridge.create(Serializable.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void createWhenImplementationIsNotFound() throws Exception {
        CoolBridge.create(BadService.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void createWhenImplementationDoesNotImplementInterface()
            throws Exception {
        CoolBridge.create(Bad2Service.class);
    }
}