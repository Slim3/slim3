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
package org.slim3.tester;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.model.Bbb;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServicePb.MailMessage;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskQueuePb.TaskQueueAddRequest;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest;

/**
 * @author higa
 * 
 */
public class AppEngineTesterTest {

    private AppEngineTester tester = new AppEngineTester();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        tester.setUp();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        tester.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getLibDir() throws Exception {
        File libDir = AppEngineTester.getLibDir();
        System.out.println(libDir);
        assertThat(libDir.exists(), is(true));
        assertThat(libDir.isDirectory(), is(true));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getLocalRuntimeJar() throws Exception {
        File libDir = AppEngineTester.getLibDir();
        File implDir = new File(libDir, AppEngineTester.IMPL_DIR_NAME);
        if (implDir.exists()) {
            URL url =
                AppEngineTester.getLibraryURL(
                    implDir,
                    AppEngineTester.LOCAL_RUNTIME_LIB_NAME);
            assertThat(url, is(notNullValue()));
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getApiStubsJar() throws Exception {
        File libDir = AppEngineTester.getLibDir();
        File implDir = new File(libDir, AppEngineTester.IMPL_DIR_NAME);
        if (implDir.exists()) {
            URL url =
                AppEngineTester.getLibraryURL(
                    implDir,
                    AppEngineTester.API_STUBS_LIB_NAME);
            System.out.println(url);
            assertThat(url, is(notNullValue()));
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getCount() throws Exception {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(new Entity("Hoge"));
        assertThat(tester.count("Hoge"), is(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getCountForPolyModel() throws Exception {
        Datastore.put(new Bbb());
        assertThat(tester.count(Bbb.class), is(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void environment() throws Exception {
        assertThat(tester.environment, is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void mail() throws Exception {
        Message message = new Message();
        String to = "foo@bar.com";
        String sender = "hoge@fuga.com";
        String subject = "subject";
        String body = "body";
        message.setTo(to);
        message.setSender(sender);
        message.setSubject(subject);
        message.setTextBody(body);
        MailServiceFactory.getMailService().sendToAdmins(message);
        assertThat(tester.mailMessages.size(), is(1));
        MailMessage mes = tester.mailMessages.get(0);
        assertThat(mes.getTo(0), is(to));
        assertThat(mes.getSender(), is(sender));
        assertThat(mes.getSubject(), is(subject));
        assertThat(mes.getTextBody(), is(body));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void taskQueueForDefaultQueue() throws Exception {
        Queue queue = QueueFactory.getDefaultQueue();
        queue
            .add(TaskOptions.Builder.withUrl("/tqHandler").param("key", "aaa"));
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getUrl(), is("/tqHandler"));
        assertThat(task.getBody(), is("key=aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void taskQueueForNamedQueue() throws Exception {
        Queue queue = QueueFactory.getQueue("test-queue");
        queue
            .add(TaskOptions.Builder.withUrl("/tqHandler").param("key", "aaa"));
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getUrl(), is("/tqHandler"));
        assertThat(task.getBody(), is("key=aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void urlFetch() throws Exception {
        URLFetchService service = URLFetchServiceFactory.getURLFetchService();
        HTTPRequest httpRequest = new HTTPRequest(new URL("http://hoge"));
        String queryString = "aaa=111";
        httpRequest.setPayload(queryString.getBytes("utf-8"));
        tester.setUrlFetchHandler(new URLFetchHandler() {

            public int getStatusCode(URLFetchRequest request)
                    throws IOException {
                return 200;
            }

            public byte[] getContent(URLFetchRequest request)
                    throws IOException {
                return "hello".getBytes();
            }
        });
        HTTPResponse httpResponse = service.fetch(httpRequest);
        assertThat(httpResponse.getResponseCode(), is(200));
        assertThat(new String(httpResponse.getContent()), is("hello"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void urlFetchAsync() throws Exception {
        URLFetchService service = URLFetchServiceFactory.getURLFetchService();
        HTTPRequest httpRequest = new HTTPRequest(new URL("http://hoge"));
        String queryString = "aaa=111";
        httpRequest.setPayload(queryString.getBytes("utf-8"));
        tester.setUrlFetchHandler(new URLFetchHandler() {

            public int getStatusCode(URLFetchRequest request)
                    throws IOException {
                return 200;
            }

            public byte[] getContent(URLFetchRequest request)
                    throws IOException {
                return "hello".getBytes();
            }
        });
        Future<HTTPResponse> future = service.fetchAsync(httpRequest);
        HTTPResponse httpResponse = future.get();
        assertThat(httpResponse.getResponseCode(), is(200));
        assertThat(new String(httpResponse.getContent()), is("hello"));
    }
}