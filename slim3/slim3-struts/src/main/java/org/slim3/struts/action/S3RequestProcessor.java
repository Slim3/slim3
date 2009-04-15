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
package org.slim3.struts.action;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.slim3.commons.bean.BeanDesc;
import org.slim3.commons.bean.BeanUtil;
import org.slim3.commons.bean.ParameterizedClassDesc;
import org.slim3.commons.bean.PropertyDesc;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.util.ClassUtil;
import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.annotation.SessionScope;
import org.slim3.struts.config.S3ActionMapping;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.util.ControllerUtil;
import org.slim3.struts.util.S3ActionMappingUtil;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.web.RoutingFilter;
import org.slim3.struts.web.WebLocator;

/**
 * {@link RequestProcessor} for Slim3.
 * 
 * @author higa
 * @since 3.0
 */
public class S3RequestProcessor extends RequestProcessor {

    /**
     * The nested delimiter.
     */
    protected static final char NESTED_DELIM = '.';

    /**
     * The indexed start delimiter.
     */
    protected static final char INDEXED_DELIM = '[';

    /**
     * The indexed end delimiter.
     */
    protected static final char INDEXED_DELIM2 = ']';

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request = processMultipart(request);
        String path = processPath(request, response);
        if (path == null) {
            return;
        }
        processLocale(request, response);
        processContent(request, response);
        processNoCache(request, response);
        if (!processPreprocess(request, response)) {
            return;
        }
        processCachedMessages(request, response);
        ActionMapping mapping = processMapping(request, response, path);
        if (mapping == null) {
            return;
        }
        ActionForm form = processActionForm(request, response, mapping);
        processPopulate(request, response, form, mapping);
        if (!processRoles(request, response, mapping)) {
            return;
        }
        try {
            if (!processValidate(request, response, form, mapping)) {
                return;
            }
        } catch (InvalidCancelException e) {
            ActionForward forward = processException(request, response, e,
                    form, mapping);
            processForwardConfig(request, response, forward);
            return;
        } catch (IOException e) {
            throw e;
        } catch (ServletException e) {
            throw e;
        }
        if (!processForward(request, response, mapping)) {
            return;
        }
        if (!processInclude(request, response, mapping)) {
            return;
        }
        Action action = processActionCreate(request, response, mapping);
        if (action == null) {
            return;
        }
        ActionForward forward = processActionPerform(request, response, action,
                form, mapping);
        processForwardConfig(request, response, forward);
    }

    @Override
    protected HttpServletRequest processMultipart(HttpServletRequest request) {
        HttpServletRequest result = super.processMultipart(request);
        WebLocator.setRequest(result);
        return result;
    }

    @Override
    protected ActionMapping processMapping(HttpServletRequest request,
            HttpServletResponse response, String path) throws IOException {
        S3ActionMapping mapping = (S3ActionMapping) moduleConfig
                .findActionConfig(path);
        if (mapping != null) {
            request.setAttribute(Globals.MAPPING_KEY, mapping);
            Object controller = createController(mapping);
            BeanDesc beanDesc = mapping.getBeanDesc();
            for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
                PropertyDesc pd = beanDesc.getPropertyDesc(i);
                if (!pd.isWritable() || pd.getField() == null) {
                    continue;
                }
                if (pd.getField().getAnnotation(SessionScope.class) != null) {
                    pd.setValue(controller, request.getSession().getAttribute(
                            pd.getPropertyName()));
                }
            }
            ControllerUtil.setController(controller);
            return mapping;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, path
                + " not found.");
        return null;
    }

    /**
     * Creates a new controller.
     * 
     * @param mapping
     *            the action mapping
     * @return a new controller
     */
    protected Object createController(S3ActionMapping mapping) {
        return ClassUtil.newInstance(mapping.getControllerClass());
    }

    /**
     * Sets execute configuration.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param mapping
     *            the action mapping
     * @throws IllegalStateException
     *             if execute method is not specified
     */
    protected void processExecuteConfig(HttpServletRequest request,
            HttpServletResponse response, ActionMapping mapping)
            throws IllegalStateException {
        if (S3ExecuteConfigUtil.getExecuteConfig() == null) {
            S3ExecuteConfig executeConfig = ((S3ActionMapping) mapping)
                    .findExecuteConfig(request);
            if (executeConfig == null) {
                throw new IllegalStateException(
                        "Execute method is not specified.");
            }
            S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        }
    }

    @Override
    protected boolean processRoles(HttpServletRequest request,
            HttpServletResponse response, ActionMapping mapping)
            throws IOException, ServletException {
        S3ExecuteConfig executeConfig = S3ExecuteConfigUtil.getExecuteConfig();
        if (executeConfig == null) {
            return true;
        }
        String roles[] = executeConfig.getRoles();
        if (roles.length == 0) {
            return true;
        }
        for (int i = 0; i < roles.length; i++) {
            if (request.isUserInRole(roles[i])) {
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User("
                + request.getRemoteUser()
                + ") is not authorized to access the path(" + mapping.getPath()
                + "/" + executeConfig.getMethod().getName() + ").");
        return false;
    }

    @Override
    protected ActionForm processActionForm(HttpServletRequest request,
            HttpServletResponse response, ActionMapping mapping) {
        String name = mapping.getName();
        if (name == null) {
            return null;
        }
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(name);
        if (formConfig == null) {
            return null;
        }
        try {
            ActionForm actionForm = formConfig.createActionForm(servlet);
            request.setAttribute(mapping.getAttribute(), actionForm);
            return actionForm;
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Action processActionCreate(HttpServletRequest request,
            HttpServletResponse response, ActionMapping mapping)
            throws IOException {

        Action action = null;
        try {
            action = createActionWrapper(ControllerUtil.getController());
        } catch (Exception e) {
            log.error(getInternal().getMessage("actionCreate",
                    mapping.getPath()), e);
            response
                    .sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            getInternal().getMessage("actionCreate",
                                    mapping.getPath()));
            return null;
        }
        action.setServlet(servlet);
        return action;
    }

    @Override
    protected ActionForward processActionPerform(HttpServletRequest request,
            HttpServletResponse response, Action action, ActionForm form,
            ActionMapping mapping) throws IOException, ServletException {

        try {
            return (action.execute(mapping, form, request, response));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return (processException(request, response, e, form, mapping));
        }

    }

    /**
     * Creates {@link ActionWrapper}.
     * 
     * @param controller
     *            the controller
     * @return a action wrapper
     */
    protected ActionWrapper createActionWrapper(Object controller) {
        return new ActionWrapper(ControllerUtil.getController());
    }

    @Override
    protected void processPopulate(HttpServletRequest request,
            HttpServletResponse response, ActionForm form, ActionMapping mapping)
            throws ServletException {

        if (form == null) {
            return;
        }
        form.setServlet(servlet);
        form.setMultipartRequestHandler(null);
        MultipartRequestHandler multipartHandler = null;
        if (ServletFileUpload.isMultipartContent(request)) {
            multipartHandler = getMultipartHandler(mapping.getMultipartClass());
            if (multipartHandler != null) {
                multipartHandler.setServlet(servlet);
                multipartHandler.setMapping(mapping);
                multipartHandler.handleRequest(request);
                Boolean maxLengthExceeded = (Boolean) request
                        .getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
                if ((maxLengthExceeded != null)
                        && (maxLengthExceeded.booleanValue())) {
                    form.setMultipartRequestHandler(multipartHandler);
                    processExecuteConfig(request, response, mapping);
                    return;
                }
            }
        }
        processExecuteConfig(request, response, mapping);
        form.reset(mapping, request);
        Map<String, Object> params = getAllParameters(request, multipartHandler);
        for (Iterator<String> i = params.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            setProperty(ControllerUtil.getController(), name, params.get(name));
        }
    }

    /**
     * Returns the multipart request handler.
     * 
     * @param multipartClass
     *            the name of multipart request handler
     * @return the multipart request handler
     * @throws ServletException
     *             if an exception is encountered.
     */
    protected MultipartRequestHandler getMultipartHandler(String multipartClass)
            throws ServletException {

        MultipartRequestHandler multipartHandler = null;
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) ClassUtil
                        .newInstance(multipartClass, Thread.currentThread()
                                .getContextClassLoader());
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                throw new ServletException(t.getMessage(), t);
            }
            if (multipartHandler != null) {
                return multipartHandler;
            }
        }
        multipartClass = moduleConfig.getControllerConfig().getMultipartClass();
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) ClassUtil
                        .newInstance(multipartClass, Thread.currentThread()
                                .getContextClassLoader());
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                throw new ServletException(t.getMessage(), t);
            }
            if (multipartHandler != null) {
                return multipartHandler;
            }
        }
        return null;
    }

    /**
     * Returns all request parameters.
     * 
     * @param request
     *            the request
     * @param multipartHandler
     *            the multipart request handler
     * @return all request parameters
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> getAllParameters(HttpServletRequest request,
            MultipartRequestHandler multipartHandler) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper) request).getRequest();
        }
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            params.put(name, request.getParameterValues(name));
        }
        if (multipartHandler != null) {
            Hashtable elements = multipartHandler.getAllElements();
            params.putAll(elements);
        }
        return params;
    }

    /**
     * Forward or redirect to the specified destination.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param forward
     *            the forward configuration
     * 
     * @throws IOException
     *             if {@link IOException} error occurs
     * @throws ServletException
     *             if {@link ServletException} occurs
     */
    @Override
    protected void processForwardConfig(HttpServletRequest request,
            HttpServletResponse response, ForwardConfig forward)
            throws IOException, ServletException {

        if (forward == null) {
            return;
        }
        String path = forward.getPath();
        if (path.indexOf(':') < 0) {
            if (!path.startsWith("/")) {
                path = ControllerUtil.getPath() + path; // uri
            }
            if (path.indexOf('.') > 0) {
                if (forward.getRedirect()) {
                    throw new IllegalStateException(
                            "In case of redirect, you must not specify view path("
                                    + path
                                    + ") including extension("
                                    + path.substring(path.indexOf('.'), path
                                            .length()) + ").");
                }
                path = Configuration.getInstance().getValue(
                        S3StrutsGlobals.VIEW_PREFIX_KEY)
                        + path;
            }
        } else {
            if (!forward.getRedirect()) {
                throw new IllegalStateException(
                        "In case of forward, you must not specify view path("
                                + path + ") including scheme("
                                + path.substring(0, path.indexOf(':')) + ").");
            }
        }
        if (forward.getRedirect()) {
            if (path.startsWith("/")) {
                path = request.getContextPath() + path;
            }
            exportProperties(request, response, S3ActionMappingUtil
                    .getActionMapping().getBeanDesc(), true);
            response.sendRedirect(response.encodeRedirectURL(path));
        } else {
            path = RoutingFilter.assembleActionPath(path);
            exportProperties(request, response, S3ActionMappingUtil
                    .getActionMapping().getBeanDesc(), false);
            doForward(path, request, response);
        }
    }

    /**
     * Exports action properties to request, session, cookie.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param beanDesc
     *            the bean descriptor
     * @param redirect
     *            redirect flag
     * 
     */
    protected void exportProperties(HttpServletRequest request,
            HttpServletResponse response, BeanDesc beanDesc, boolean redirect) {
        Object controller = ControllerUtil.getController();
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (pd.isReadable()) {
                Object value = pd.getValue(controller);
                Field field = pd.getField();
                if (field != null) {
                    if (field.getAnnotation(SessionScope.class) != null) {
                        if (value != null) {
                            if (!(value instanceof Serializable)) {
                                throw new IllegalStateException("The field("
                                        + field.getName() + ") of class("
                                        + field.getDeclaringClass().getName()
                                        + ") is not serializable.");
                            }
                            request.getSession().setAttribute(
                                    pd.getPropertyName(), value);
                        } else {
                            request.getSession().removeAttribute(
                                    pd.getPropertyName());
                        }
                    } else if (!redirect) {
                        if (value != null) {
                            request.setAttribute(pd.getPropertyName(), value);
                        } else {
                            request.removeAttribute(pd.getPropertyName());
                        }
                    }
                }

            }
        }
    }

    /**
     * Sets request parameter to the property of the action.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void setProperty(Object bean, String name, Object value) {
        int nestedIndex = name.indexOf(NESTED_DELIM);
        int indexedIndex = name.indexOf(INDEXED_DELIM);
        if (nestedIndex < 0 && indexedIndex < 0) {
            setSimpleProperty(bean, name, value);
        } else if (nestedIndex >= 0 && indexedIndex >= 0) {
            if (nestedIndex < indexedIndex) {
                setProperty(getSimpleProperty(bean, name.substring(0,
                        nestedIndex)), name.substring(nestedIndex + 1), value);
            } else {
                IndexParsedResult result = parseIndex(name
                        .substring(indexedIndex + 1));
                bean = getIndexedProperty(bean,
                        name.substring(0, indexedIndex), result.indexes);
                setProperty(bean, result.name, value);
            }
        } else if (nestedIndex >= 0) {
            Object o = getSimpleProperty(bean, name.substring(0, nestedIndex));
            if (o == null) {
                return;
            }
            setProperty(o, name.substring(nestedIndex + 1), value);
        } else {
            IndexParsedResult result = parseIndex(name
                    .substring(indexedIndex + 1));
            setIndexedProperty(bean, name.substring(0, indexedIndex),
                    result.indexes, value);
        }
    }

    /**
     * Returns simple property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the property name
     * @return the property value
     */
    protected Object getSimpleProperty(Object bean, String name) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return null;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isReadable()) {
            return null;
        }
        Object value = pd.getValue(bean);
        if (value == null) {
            if (!Modifier.isAbstract(pd.getPropertyClass().getModifiers())) {
                value = ClassUtil.newInstance(pd.getPropertyClass());
                if (pd.isWritable()) {
                    pd.setValue(bean, value);
                }
            } else if (Map.class.isAssignableFrom(pd.getPropertyClass())) {
                value = new HashMap<String, Object>();
                if (pd.isWritable()) {
                    pd.setValue(bean, value);
                }
            }
        }
        return value;
    }

    /**
     * Sets simple property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    @SuppressWarnings("unchecked")
    protected void setSimpleProperty(Object bean, String name, Object value) {
        if (bean instanceof Map) {
            Map m = (Map) bean;
            if (value instanceof String[]) {
                String[] values = (String[]) value;
                m.put(name, values.length > 0 ? values[0] : null);
            } else {
                m.put(name, value);
            }
            return;
        }
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isWritable()) {
            return;
        }
        if (pd.getPropertyClass().isArray()) {
            pd.setValue(bean, value);
        } else if (List.class.isAssignableFrom(pd.getPropertyClass())) {
            List<String> list = Modifier.isAbstract(pd.getPropertyClass()
                    .getModifiers()) ? new ArrayList<String>()
                    : (List<String>) ClassUtil.newInstance(pd
                            .getPropertyClass());
            list.addAll(Arrays.asList((String[]) value));
            pd.setValue(bean, list);
        } else if (value == null) {
            pd.setValue(bean, null);
        } else if (value instanceof String[]) {
            String[] values = (String[]) value;
            pd.setValue(bean, values.length > 0 ? values[0] : null);
        } else {
            pd.setValue(bean, value);
        }
    }

    /**
     * Returns the indexed property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the property name
     * @param indexes
     *            The array of indexes
     * @return the value
     * 
     */
    @SuppressWarnings("unchecked")
    protected Object getIndexedProperty(Object bean, String name, int[] indexes) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return null;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isReadable()) {
            return null;
        }
        if (pd.getPropertyClass().isArray()) {
            Object array = pd.getValue(bean);
            Class<?> elementType = getArrayElementClass(pd.getPropertyClass(),
                    indexes.length);
            if (array == null) {
                int[] newIndexes = new int[indexes.length];
                newIndexes[0] = indexes[0] + 1;
                array = Array.newInstance(elementType, newIndexes);
            }
            array = expandArray(array, indexes, elementType);
            pd.setValue(bean, array);
            return getArrayValue(array, indexes, elementType);
        } else if (List.class.isAssignableFrom(pd.getPropertyClass())) {
            List list = (List) pd.getValue(bean);
            if (list == null) {
                list = new ArrayList(Math.max(50, indexes[0]));
                pd.setValue(bean, list);
            }
            ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
            for (int i = 0; i < indexes.length; i++) {
                if (pcd == null || !pcd.isParameterized()
                        || !List.class.isAssignableFrom(pcd.getRawClass())) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j <= i; j++) {
                        sb.append("[").append(indexes[j]).append("]");
                    }
                    throw new IllegalStateException("The property("
                            + pd.getPropertyName() + sb + ") of class("
                            + beanDesc.getBeanClass().getName()
                            + ") is not parametized.");
                }
                int size = list.size();
                pcd = pcd.getArguments()[0];
                for (int j = size; j <= indexes[i]; j++) {
                    if (i == indexes.length - 1) {
                        list.add(ClassUtil.newInstance(convertConcreteClass(pcd
                                .getRawClass())));
                    } else {
                        list.add(new ArrayList());
                    }
                }
                if (i < indexes.length - 1) {
                    list = (List) list.get(indexes[i]);
                }
            }
            return list.get(indexes[indexes.length - 1]);
        } else {
            throw new IllegalStateException("The property("
                    + pd.getPropertyName() + ") of class("
                    + beanDesc.getBeanClass().getName()
                    + ") is not list and array.");
        }
    }

    /**
     * Sets indexed property.
     * 
     * @param bean
     *            the bean
     * @param name
     *            the property name
     * @param indexes
     *            the array of indexes
     * @param value
     *            the value
     */
    @SuppressWarnings("unchecked")
    protected void setIndexedProperty(Object bean, String name, int[] indexes,
            Object value) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
        if (!beanDesc.hasPropertyDesc(name)) {
            return;
        }
        PropertyDesc pd = beanDesc.getPropertyDesc(name);
        if (!pd.isWritable()) {
            return;
        }
        if (value.getClass().isArray() && Array.getLength(value) > 0) {
            value = Array.get(value, 0);
        }
        if (pd.getPropertyClass().isArray()) {
            Object array = pd.getValue(bean);
            Class<?> elementType = getArrayElementClass(pd.getPropertyClass(),
                    indexes.length);
            if (array == null) {
                int[] newIndexes = new int[indexes.length];
                newIndexes[0] = indexes[0] + 1;
                array = Array.newInstance(elementType, newIndexes);
            }
            array = expandArray(array, indexes, elementType);
            pd.setValue(bean, array);
            setArrayValue(array, indexes, value);
        } else if (List.class.isAssignableFrom(pd.getPropertyClass())) {
            List list = (List) pd.getValue(bean);
            if (list == null) {
                list = new ArrayList(Math.max(50, indexes[0]));
                pd.setValue(bean, list);
            }
            ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
            for (int i = 0; i < indexes.length; i++) {
                if (pcd == null || !pcd.isParameterized()
                        || !List.class.isAssignableFrom(pcd.getRawClass())) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j <= i; j++) {
                        sb.append("[").append(indexes[j]).append("]");
                    }
                    throw new IllegalStateException("The property("
                            + pd.getPropertyName() + sb + ") of class("
                            + beanDesc.getBeanClass().getName()
                            + ") is not List<T>.");
                }
                int size = list.size();
                pcd = pcd.getArguments()[0];
                for (int j = size; j <= indexes[i]; j++) {
                    if (i == indexes.length - 1) {
                        list.add(ClassUtil.newInstance(convertConcreteClass(pcd
                                .getRawClass())));
                    } else {
                        list.add(new ArrayList());
                    }
                }
                if (i < indexes.length - 1) {
                    list = (List) list.get(indexes[i]);
                }
            }
            list.set(indexes[indexes.length - 1], value);
        } else {
            throw new IllegalStateException("The property("
                    + pd.getPropertyName() + ") of class("
                    + beanDesc.getBeanClass().getName()
                    + ") is not list and array.");
        }
    }

    /**
     * Returns the element class of the array.
     * 
     * @param clazz
     *            the class of the array
     * @param depth
     *            the array depth
     * @return the element class of the array
     */
    protected Class<?> getArrayElementClass(Class<?> clazz, int depth) {
        for (int i = 0; i < depth; i++) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }

    /**
     * Expands the array.
     * 
     * @param array
     *            the array
     * @param indexes
     *            the array of indexes
     * @param elementClass
     *            the element class of the array
     * @return expanded array
     */
    protected Object expandArray(Object array, int[] indexes,
            Class<?> elementClass) {
        int length = Array.getLength(array);
        if (length <= indexes[0]) {
            int[] newIndexes = new int[indexes.length];
            newIndexes[0] = indexes[0] + 1;
            Object newArray = Array.newInstance(elementClass, newIndexes);
            System.arraycopy(array, 0, newArray, 0, length);
            array = newArray;
        }
        if (indexes.length > 1) {
            int[] newIndexes = new int[indexes.length - 1];
            for (int i = 1; i < indexes.length; i++) {
                newIndexes[i - 1] = indexes[i];
            }
            Array.set(array, indexes[0], expandArray(Array.get(array,
                    indexes[0]), newIndexes, elementClass));
        }
        return array;
    }

    /**
     * Adds the int value to the array.
     * 
     * @param array
     *            the array
     * @param value
     *            the int value
     * @return added array.
     */
    protected int[] addArray(int[] array, int value) {
        if (array == null) {
            throw new NullPointerException("The array parameter is null.");
        }
        int[] newArray = (int[]) Array.newInstance(int.class, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = value;
        return newArray;
    }

    /**
     * Returns the value of the array.
     * 
     * @param array
     *            the array
     * @param indexes
     *            the array of indexes
     * @param elementClass
     *            the element class of the array
     * @return the value of the array
     */
    protected Object getArrayValue(Object array, int[] indexes,
            Class<?> elementClass) {
        Object value = array;
        elementClass = convertConcreteClass(elementClass);
        for (int i = 0; i < indexes.length; i++) {
            Object value2 = Array.get(value, indexes[i]);
            if (i == indexes.length - 1 && value2 == null) {
                value2 = ClassUtil.newInstance(elementClass);
                Array.set(value, indexes[i], value2);
            }
            value = value2;
        }
        return value;
    }

    /**
     * Sets the value of the array.
     * 
     * @param array
     *            the array
     * @param indexes
     *            the array of indexes
     * @param value
     *            the value of the array
     */
    protected void setArrayValue(Object array, int[] indexes, Object value) {
        for (int i = 0; i < indexes.length - 1; i++) {
            array = Array.get(array, indexes[i]);
        }
        Array.set(array, indexes[indexes.length - 1], value);
    }

    /**
     * Converts the abstract class to the concrete class.
     * 
     * @param clazz
     *            the class
     * @return converted class
     */
    protected Class<?> convertConcreteClass(Class<?> clazz) {
        if (!clazz.isPrimitive() && Modifier.isAbstract(clazz.getModifiers())) {
            if (Map.class.isAssignableFrom(clazz)) {
                return HashMap.class;
            } else if (List.class.isAssignableFrom(clazz)) {
                return ArrayList.class;
            } else {
                throw new IllegalArgumentException("The class("
                        + clazz.getName()
                        + ") can not be converted to concrete class.");
            }
        }
        return clazz;
    }

    /**
     * Parses the indexed property name.
     * 
     * @param name
     *            the property name
     * @return the parsed result.
     */
    protected IndexParsedResult parseIndex(String name) {
        IndexParsedResult result = new IndexParsedResult();
        while (true) {
            int index = name.indexOf(INDEXED_DELIM2);
            if (index < 0) {
                throw new IllegalArgumentException(INDEXED_DELIM2
                        + " is not found in (" + name + ").");
            }
            result.indexes = addArray(result.indexes, Integer.valueOf(
                    name.substring(0, index)).intValue());
            name = name.substring(index + 1);
            if (name.length() == 0) {
                break;
            } else if (name.charAt(0) == INDEXED_DELIM) {
                name = name.substring(1);
            } else if (name.charAt(0) == NESTED_DELIM) {
                name = name.substring(1);
                break;
            } else {
                throw new IllegalArgumentException(name);
            }
        }
        result.name = name;
        return result;
    }

    /**
     * 
     */
    protected static class IndexParsedResult {
        /**
         * The array of indexes.
         */
        public int[] indexes = new int[0];

        /**
         * The name except index part.
         */
        public String name;
    }
}