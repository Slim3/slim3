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
package org.slim3.struts.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.validator.Form;
import org.apache.commons.validator.FormSet;
import org.apache.commons.validator.Var;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.slim3.commons.bean.BeanDesc;
import org.slim3.commons.bean.PropertyDesc;
import org.slim3.commons.cleaner.Cleanable;
import org.slim3.commons.cleaner.Cleaner;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.exception.ClassNotFoundRuntimeException;
import org.slim3.commons.util.ClassUtil;
import org.slim3.commons.util.MethodUtil;
import org.slim3.commons.util.StringUtil;
import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.annotation.Arg;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.Msg;
import org.slim3.struts.annotation.Validator;
import org.slim3.struts.form.ActionFormWrapper;
import org.slim3.struts.form.ActionFormWrapperClass;
import org.slim3.struts.form.S3DynaProperty;
import org.slim3.struts.util.MessageResourcesUtil;
import org.slim3.struts.util.ValidatorResourcesUtil;
import org.slim3.struts.web.HotdeployClassLoader;

/**
 * {@link ModuleConfig} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3ModuleConfig extends ModuleConfigImpl {

    /**
     * The controller suffix.
     */
    public static final String CONTROLLER_SUFFIX = "Controller";

    /**
     * The controller package.
     */
    public static final String CONTROLLER_PACKAGE = "controller";

    /**
     * The form suffix.
     */
    public static final String FORM_SUFFIX = "Form";

    private static final long serialVersionUID = 1L;

    /**
     * Whether this object is initialized.
     */
    protected volatile boolean initialized;

    /**
     * The map for {@link ActionConfig}.
     */
    protected Map<String, ActionConfig> actionConfigMap = new ConcurrentHashMap<String, ActionConfig>(
            100);

    /**
     * The map for {@link FormBeanConfig}.
     */
    protected Map<String, FormBeanConfig> formBeanConfigMap = new ConcurrentHashMap<String, FormBeanConfig>(
            100);

    /**
     * The action package.
     */
    protected String actionPackage;

    /**
     * Constructor.
     * 
     * @param prefix
     *            the prefix
     */
    public S3ModuleConfig(String prefix) {
        super(prefix);
        initialize();
    }

    /**
     * Initializes this object.
     */
    protected void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                actionConfigMap.clear();
                formBeanConfigMap.clear();
                initialized = false;
            }
        });
        initialized = true;
    }

    @Override
    public ActionConfig findActionConfig(String path) {
        if (!initialized) {
            initialize();
        }
        ActionConfig ac = actionConfigMap.get(path);
        if (ac != null) {
            return ac;
        }
        S3ActionMapping ac2 = createActionConfig(path);
        if (ac2 == null) {
            return null;
        }
        addActionConfig(ac2);
        FormBeanConfig formBeanConfig = createFormBeanConfig(ac2);
        if (formBeanConfig != null) {
            addFormBeanConfig(formBeanConfig);
        }
        setupValidator(ac2);
        return ac2;
    }

    @Override
    public ActionConfig[] findActionConfigs() {
        return actionConfigMap.values().toArray(
                new ActionConfig[actionConfigMap.size()]);
    }

    @Override
    public void addActionConfig(ActionConfig config) {
        actionConfigMap.put(config.getPath(), config);
    }

    @Override
    public void removeActionConfig(ActionConfig actionConfig) {
        actionConfigMap.remove(actionConfig.getPath());
    }

    @Override
    public FormBeanConfig findFormBeanConfig(String name) {
        return formBeanConfigMap.get(name);
    }

    @Override
    public FormBeanConfig[] findFormBeanConfigs() {
        return formBeanConfigMap.values().toArray(
                new FormBeanConfig[formBeanConfigMap.size()]);
    }

    @Override
    public void addFormBeanConfig(FormBeanConfig formBeanConfig) {
        formBeanConfigMap.put(formBeanConfig.getName(), formBeanConfig);
    }

    @Override
    public void removeFormBeanConfig(FormBeanConfig formBeanConfig) {
        formBeanConfigMap.remove(formBeanConfig.getName());
    }

    @Override
    public void freeze() {
    }

    /**
     * Creates an action configuration.
     * 
     * @param path
     *            the path
     * @return an action configuration
     * @throws IllegalStateException
     *             if the controller is not marked by @Controller
     */
    protected S3ActionMapping createActionConfig(String path)
            throws IllegalStateException {
        String controllerName = toControllerName(path);
        Class<?> controllerClass = toControllerClass(controllerName);
        if (controllerClass == null) {
            return null;
        }
        if (controllerClass.getAnnotation(Controller.class) == null) {
            throw new IllegalStateException("The controller("
                    + controllerClass.getName()
                    + ") is not marked by @Controller.");
        }
        S3ActionMapping actionMapping = new S3ActionMapping();
        actionMapping.setPath(path);
        actionMapping.setName(controllerName + FORM_SUFFIX);
        actionMapping.setControllerName(controllerName);
        actionMapping.setControllerClass(controllerClass);
        setupExecuteMethod(actionMapping, controllerClass);
        return actionMapping;
    }

    /**
     * Converts the path to the controller name.
     * 
     * @param path
     *            the path
     * @return the controller name
     */
    protected String toControllerName(String path) {
        int index = path.lastIndexOf('/');
        if (index < 0) {
            throw new IllegalArgumentException("The path(" + path
                    + ") does not contain slash(/).");
        }
        if (index == 0) {
            return path.substring(1) + CONTROLLER_SUFFIX;
        }
        return path.substring(1, index).replace('/', '.') + "."
                + path.substring(index + 1) + CONTROLLER_SUFFIX;
    }

    /**
     * Converts the controller name into the controller class.
     * 
     * @param name
     *            the controller name.
     * @return the controller class.
     */
    protected Class<?> toControllerClass(String name) {
        int index = name.lastIndexOf('.');
        if (index > 0) {
            name = name.substring(0, index + 1)
                    + StringUtil.capitalize(name.substring(index + 1));
        } else {
            name = StringUtil.capitalize(name);
        }
        String className = Configuration.getInstance().getValue(
                S3StrutsGlobals.CONTROLLER_PACKAGE_KEY)
                + "." + name;
        ClassLoader loader = HotdeployClassLoader.getCurrentInstance();
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        try {
            return ClassUtil.forName(className, loader);
        } catch (ClassNotFoundRuntimeException ignore) {
        }
        return null;
    }

    /**
     * Sets up execute methods.
     * 
     * @param actionMapping
     *            the action mapping
     * @param controllerClass
     *            the controller class
     */
    protected void setupExecuteMethod(S3ActionMapping actionMapping,
            Class<?> controllerClass) {
        for (Class<?> clazz = controllerClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (Method m : clazz.getDeclaredMethods()) {
                if (!Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                Execute execute = m.getAnnotation(Execute.class);
                if (execute == null) {
                    if (m.getParameterTypes().length == 0
                            && m.getReturnType() == ActionForward.class) {
                        throw new IllegalStateException("Define @Execute at "
                                + controllerClass.getName() + "#" + m.getName()
                                + "().");
                    }
                    continue;
                }
                if (actionMapping.getExecuteConfig(m.getName()) != null) {
                    continue;
                }
                if (m.getParameterTypes().length > 0
                        || m.getReturnType() != ActionForward.class) {
                    throw new IllegalStateException("The signature, \""
                            + m.toString() + "\", must be \"ActionForward "
                            + m.getName() + "()\"");
                }
                S3ExecuteConfig executeConfig = new S3ExecuteConfig(m);
                boolean validate = execute.validate();
                executeConfig.setValidate(validate);
                String input = execute.input();
                if (input.length() > 0) {
                    executeConfig.setInput(input);
                }
                if (validate && executeConfig.getInput() == null) {
                    throw new IllegalStateException(
                            "If you want to validate "
                                    + controllerClass.getName()
                                    + "#"
                                    + m.getName()
                                    + "(), define @Execute(input = \"path when validation errors have occurred\"). If you do not, define @Execute(validate = false).");
                }
                executeConfig.setRoles(execute.roles());
                String reset = execute.reset();
                if (!StringUtil.isEmpty(reset)) {
                    try {
                        Method resetMethod = controllerClass.getMethod(reset);
                        executeConfig.setResetMethod(resetMethod);
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("The reset method("
                                + reset + ") is not found in the action("
                                + controllerClass.getName() + ").");
                    }
                }
                actionMapping.addExecuteConfig(executeConfig);
            }
        }
        if (actionMapping.getExecuteConfigSize() == 0) {
            throw new IllegalStateException("The action("
                    + controllerClass.getName()
                    + ") does not have any execute methods.");
        }
    }

    /**
     * Creates a {@link FormBeanConfig}.
     * 
     * @param actionMapping
     *            the action mapping
     * @return a {@link FormBeanConfig}
     */
    protected FormBeanConfig createFormBeanConfig(S3ActionMapping actionMapping) {
        S3FormBeanConfig formBeanConfig = new S3FormBeanConfig();
        formBeanConfig.setType(ActionFormWrapper.class.getName());
        formBeanConfig.setName(actionMapping.getName());
        ActionFormWrapperClass dynaClass = new ActionFormWrapperClass(
                actionMapping.getName());
        BeanDesc beanDesc = actionMapping.getBeanDesc();
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            dynaClass.addDynaProperty(new S3DynaProperty(propertyDesc));
        }
        formBeanConfig.setDynaClass(dynaClass);
        return formBeanConfig;
    }

    /**
     * Sets up the validators.
     * 
     * @param actionMapping
     *            the action mapping
     */
    protected void setupValidator(S3ActionMapping actionMapping) {
        Map<String, Form> forms = new HashMap<String, Form>();
        for (String methodName : actionMapping.getExecuteMethodNames()) {
            Form form = new Form();
            form.setName(actionMapping.getName() + "_" + methodName);
            forms.put(methodName, form);
        }
        for (Class<?> clazz = actionMapping.getControllerClass(); clazz != null
                && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                for (Annotation anno : field.getDeclaredAnnotations()) {
                    processValidatorAnnotation(field.getName(), anno, forms);
                }
            }
        }
        FormSet formSet = new FormSet();
        for (Iterator<Form> i = forms.values().iterator(); i.hasNext();) {
            formSet.addForm(i.next());
        }
        ValidatorResourcesUtil.getValidatorResources().addFormSet(formSet);
    }

    /**
     * Processes the validator annotation.
     * 
     * @param propertyName
     *            the property name
     * @param annotation
     *            the validator annotation
     * @param forms
     *            the forms
     */
    protected void processValidatorAnnotation(String propertyName,
            Annotation annotation, Map<String, Form> forms) {
        Class<? extends Annotation> annotationType = annotation
                .annotationType();
        Validator metaAnnotation = annotationType
                .getAnnotation(Validator.class);
        if (metaAnnotation == null) {
            return;
        }
        String validatorName = metaAnnotation.value();
        Map<String, Object> props = getProperties(annotation);
        registerValidatorField(propertyName, validatorName, props, forms);
    }

    /**
     * Returns the properties of the annotation.
     * 
     * @param annotation
     *            the annotation
     * @return the properties of the annotation
     */
    protected Map<String, Object> getProperties(Annotation annotation) {
        Map<String, Object> properties = new HashMap<String, Object>();
        Class<?> clazz = annotation.annotationType();
        for (Method m : clazz.getMethods()) {
            if (m.getDeclaringClass() == Object.class
                    || m.getDeclaringClass() == Annotation.class
                    || m.getParameterTypes().length > 0
                    || m.getReturnType() == void.class) {
                continue;
            }
            Object value = MethodUtil.invoke(m, annotation);
            properties.put(m.getName(), value);
        }
        return properties;
    }

    /**
     * Registers a validator field.
     * 
     * @param propertyName
     *            the property name
     * @param validatorName
     *            the validator name
     * @param props
     *            the annotation properties
     * @param forms
     *            the foms
     */
    protected void registerValidatorField(String propertyName,
            String validatorName, Map<String, Object> props,
            Map<String, Form> forms) {
        org.apache.commons.validator.Field field = createValidatorField(
                propertyName, validatorName, props);
        for (Iterator<String> i = forms.keySet().iterator(); i.hasNext();) {
            String methodName = i.next();
            if (!isValidationTarget(methodName, (String[]) props.get("targets"))) {
                continue;
            }
            Form form = forms.get(methodName);
            form.addField(field);
        }
    }

    /**
     * Creates a validator field
     * 
     * @param propertyName
     *            the property name
     * @param validatorName
     *            the validator name
     * @param properties
     *            the annotation properties
     * @return a validator field
     */
    protected org.apache.commons.validator.Field createValidatorField(
            String propertyName, String validatorName,
            Map<String, Object> properties) {
        org.apache.commons.validator.Field field = new org.apache.commons.validator.Field();
        field.setDepends(validatorName);
        field.setProperty(propertyName);
        Msg msg = (Msg) properties.remove("msg");
        if (msg != null) {
            org.apache.commons.validator.Msg m = new org.apache.commons.validator.Msg();
            m.setName(validatorName);
            m.setKey(msg.key());
            String bundle = msg.bundle();
            if (!StringUtil.isEmpty(bundle)) {
                m.setBundle(bundle);
            }
            m.setResource(msg.resource());
            field.addMsg(m);
        }
        Arg[] args = (Arg[]) properties.remove("args");
        if (args != null) {
            for (Arg arg : args) {
                org.apache.commons.validator.Arg a = new org.apache.commons.validator.Arg();
                a.setKey(resolveKey(arg.key(), arg.resource(), properties));
                String bundle = arg.bundle();
                if (!StringUtil.isEmpty(bundle)) {
                    a.setBundle(bundle);
                }
                a.setResource(arg.resource());
                a.setPosition(arg.position());
                field.addArg(a);
            }
        }
        for (int i = 0; i < 5; i++) {
            Arg arg = (Arg) properties.remove("arg" + i);
            if (arg != null) {
                if (!StringUtil.isEmpty(arg.key())) {

                    org.apache.commons.validator.Arg a = new org.apache.commons.validator.Arg();
                    a.setKey(resolveKey(arg.key(), arg.resource(), properties));
                    String bundle = arg.bundle();
                    if (!StringUtil.isEmpty(bundle)) {
                        a.setBundle(bundle);
                    }
                    a.setResource(arg.resource());
                    a.setPosition(i);
                    field.addArg(a);
                } else if (i == 0) {
                    org.apache.commons.validator.Arg a = new org.apache.commons.validator.Arg();
                    String key = "labels." + propertyName;
                    String message = MessageResourcesUtil.getMessage(null, key);
                    if (!StringUtil.isEmpty(message)) {
                        a.setKey(key);
                    } else {
                        a.setKey(propertyName);
                        a.setResource(false);
                    }
                    a.setPosition(0);
                    field.addArg(a);
                }
            }
        }

        for (Iterator<String> i = properties.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            if (name.equals("targets")) {
                continue;
            }
            Object value = properties.get(name);
            String jsType = Var.JSTYPE_STRING;
            if (value instanceof Number) {
                jsType = Var.JSTYPE_INT;
            } else if (name.equals("mask")) {
                jsType = Var.JSTYPE_REGEXP;
            }
            field.addVar(name, value.toString(), jsType);
        }
        return field;
    }

    /**
     * Determines if the method is a validation target.
     * 
     * @param methodName
     *            the method name
     * @param targets
     *            the array of targets
     * @return whether the method is a validation target
     */
    protected boolean isValidationTarget(String methodName, String[] targets) {
        if (targets.length == 0) {
            return true;
        }
        for (String target : targets) {
            if (methodName.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolves the key.
     * 
     * @param key
     *            the key
     * @param resource
     *            resource flag
     * @param properties
     *            the annotation propertes
     * @return the result
     */
    protected String resolveKey(String key, boolean resource,
            Map<String, Object> properties) {
        if (resource) {
            return key;
        }
        if (key.startsWith("${") && key.endsWith("}")) {
            String s = key.substring(2, key.length() - 1);
            if (s.startsWith("var:")) {
                s = s.substring(4);
                return String.valueOf(properties.get(s));
            }
        }
        return key;
    }
}