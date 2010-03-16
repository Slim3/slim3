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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.gwt.client.service.HogeService;
import org.slim3.gwt.server.service.HogeServiceImpl;

/**
 * @author higa
 * 
 */
public class GWTServiceServletTest {

    private GWTServiceServlet servlet = new GWTServiceServlet();

    /**
     * @throws Exception
     */
    @Test
    public void getServiceClass() throws Exception {
        assertThat(servlet
            .getServiceClass(HogeService.class.getName())
            .getName(), equalTo(HogeServiceImpl.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetClass() throws Exception {
        assertThat(servlet.getClass("I").getName(), is(int.class.getName()));
    }
}
