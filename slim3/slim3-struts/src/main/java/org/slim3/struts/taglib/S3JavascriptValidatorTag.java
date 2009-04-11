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
package org.slim3.struts.taglib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.Var;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.JavascriptValidatorTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;
import org.apache.struts.validator.ValidatorPlugIn;
import org.slim3.struts.config.S3ActionMapping;
import org.slim3.struts.util.S3ActionMappingUtil;

/**
 * {@link JavascriptValidatorTag} of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3JavascriptValidatorTag extends JavascriptValidatorTag {

    private static final long serialVersionUID = 1L;

    /**
     * The comparator of {@link ValidatorAction}.
     */
    protected static final Comparator<ValidatorAction> actionComparator = new Comparator<ValidatorAction>() {
        public int compare(ValidatorAction va1, ValidatorAction va2) {
            if ((va1.getDepends() == null || va1.getDepends().length() == 0)
                    && (va2.getDepends() == null || va2.getDepends().length() == 0)) {
                return 0;
            } else if ((va1.getDepends() != null && va1.getDepends().length() > 0)
                    && (va2.getDepends() == null || va2.getDepends().length() == 0)) {
                return 1;
            } else if ((va1.getDepends() == null || va1.getDepends().length() == 0)
                    && (va2.getDepends() != null && va2.getDepends().length() > 0)) {
                return -1;
            } else {
                return va1.getDependencyList().size()
                        - va2.getDependencyList().size();
            }
        }
    };

    @Override
    protected String renderJavascript() throws JspException {
        StringBuffer results = new StringBuffer();
        ModuleConfig config = TagUtils.getInstance().getModuleConfig(
                pageContext);
        ValidatorResources resources = (ValidatorResources) pageContext
                .getAttribute(ValidatorPlugIn.VALIDATOR_KEY
                        + config.getPrefix(), PageContext.APPLICATION_SCOPE);
        if (resources == null) {
            throw new JspException(
                    "ValidatorResources not found in application scope under key \""
                            + ValidatorPlugIn.VALIDATOR_KEY
                            + config.getPrefix() + "\"");
        }
        Locale locale = TagUtils.getInstance().getUserLocale(this.pageContext,
                null);
        if (formName == null) {
            S3ActionMapping actionMapping = S3ActionMappingUtil
                    .getActionMapping();
            if (actionMapping != null && methodName != null) {
                formName = actionMapping.getName() + "_" + methodName;
            }
        }
        Form form = resources.getForm(locale, formName);

        if ("true".equalsIgnoreCase(dynamicJavascript) && form == null) {
            throw new JspException("No form found under '" + formName
                    + "' in locale '" + locale + "'");
        }

        if (form != null) {
            if ("true".equalsIgnoreCase(dynamicJavascript)) {
                results.append(this.createDynamicJavascript(config, resources,
                        locale, form));

            } else if ("true".equalsIgnoreCase(staticJavascript)) {
                results.append(this.renderStartElement());
                if ("true".equalsIgnoreCase(htmlComment)) {
                    results.append(HTML_BEGIN_COMMENT);
                }
            }
        }

        if ("true".equalsIgnoreCase(staticJavascript)) {
            results.append(getJavascriptStaticMethods(resources));
        }

        if (form != null
                && ("true".equalsIgnoreCase(dynamicJavascript) || "true"
                        .equalsIgnoreCase(staticJavascript))) {

            results.append(getJavascriptEnd());
        }

        return results.toString();
    }

    /**
     * Generates the dynamic JavaScript for the form.
     * 
     * @param config
     *            the module configuration
     * @param resources
     *            the validator resources
     * @param locale
     *            the locale
     * @param form
     *            the validator form
     * @return created dynamic javascript
     * @throws JspException
     *             if an exception occurs
     */
    @SuppressWarnings("unchecked")
    protected String createDynamicJavascript(ModuleConfig config,
            ValidatorResources resources, Locale locale, Form form)
            throws JspException {
        StringBuffer results = new StringBuffer();
        MessageResources messages = TagUtils.getInstance()
                .retrieveMessageResources(pageContext, bundle, true);
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();
        ServletContext application = pageContext.getServletContext();
        List<ValidatorAction> actions = createActionList(resources, form);
        String methods = createMethods(actions, stopOnError(config));
        String formName = form.getName();
        jsFormName = formName;
        if (jsFormName.charAt(0) == '/') {
            String mappingName = TagUtils.getInstance().getActionMappingName(
                    jsFormName);
            ActionMapping mapping = (ActionMapping) config
                    .findActionConfig(mappingName);
            if (mapping == null) {
                JspException e = new JspException(messages.getMessage(
                        "formTag.mapping", mappingName));
                pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                        PageContext.REQUEST_SCOPE);
                throw e;
            }
            jsFormName = mapping.getAttribute();
        }
        results.append(getJavascriptBegin(methods));
        for (Iterator<ValidatorAction> i = actions.iterator(); i.hasNext();) {
            ValidatorAction va = i.next();
            int jscriptVar = 0;
            String functionName = null;

            if (va.getJsFunctionName() != null
                    && va.getJsFunctionName().length() > 0) {
                functionName = va.getJsFunctionName();
            } else {
                functionName = va.getName();
            }
            results.append("    function " + jsFormName + "_" + functionName
                    + " () { \n");
            for (Iterator<Field> x = form.getFields().iterator(); x.hasNext();) {
                Field field = x.next();

                // Skip indexed fields for now until there is a good way to
                // handle
                // error messages (and the length of the list (could retrieve
                // from scope?))
                if (field.isIndexed() || field.getPage() != page
                        || !field.isDependency(va.getName())) {

                    continue;
                }

                String message = Resources.getMessage(application, request,
                        messages, locale, va, field);

                message = (message != null) ? message : "";

                // prefix variable with 'a' to make it a legal identifier
                results.append("     this.a" + jscriptVar++ + " = new Array(\""
                        + field.getKey() + "\", \"" + escapeQuotes(message)
                        + "\", ");

                results.append("new Function (\"varName\", \"");

                Map<String, Var> vars = field.getVars();
                // Loop through the field's variables.
                Iterator<String> varsIterator = vars.keySet().iterator();
                while (varsIterator.hasNext()) {
                    String varName = varsIterator.next();
                    Var var = vars.get(varName);
                    String varValue = var.getValue();
                    String jsType = var.getJsType();

                    // skip requiredif variables field, fieldIndexed, fieldTest,
                    // fieldValue
                    if (varName.startsWith("field")) {
                        continue;
                    }

                    String varValueEscaped = escapeJavascript(varValue);

                    if (Var.JSTYPE_INT.equalsIgnoreCase(jsType)) {
                        results.append("this." + varName + "="
                                + varValueEscaped + "; ");
                    } else if (Var.JSTYPE_REGEXP.equalsIgnoreCase(jsType)) {
                        results.append("this." + varName + "=/"
                                + varValueEscaped + "/; ");
                    } else if (Var.JSTYPE_STRING.equalsIgnoreCase(jsType)) {
                        results.append("this." + varName + "='"
                                + varValueEscaped + "'; ");
                        // So everyone using the latest format doesn't need to
                        // change their xml files immediately.
                    } else if ("mask".equalsIgnoreCase(varName)) {
                        results.append("this." + varName + "=/"
                                + varValueEscaped + "/; ");
                    } else {
                        results.append("this." + varName + "='"
                                + varValueEscaped + "'; ");
                    }
                }

                results.append(" return this[varName];\"));\n");
            }
            results.append("    } \n\n");
        }
        return results.toString();
    }

    /**
     * Creates List of actions for the given Form.
     * 
     * @param resources
     *            the validator resources
     * @param form
     *            the validator form
     * @return A sorted List of ValidatorAction objects.
     */
    @SuppressWarnings("unchecked")
    protected List<ValidatorAction> createActionList(
            ValidatorResources resources, Form form) {
        List<String> actionMethods = new ArrayList<String>();
        Iterator<Field> iterator = form.getFields().iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            for (Iterator<String> x = field.getDependencyList().iterator(); x
                    .hasNext();) {
                String o = x.next();
                if (o != null && !actionMethods.contains(o)) {
                    actionMethods.add(o);
                }
            }
        }
        List<ValidatorAction> actions = new ArrayList<ValidatorAction>();
        Iterator<String> iterator2 = actionMethods.iterator();
        while (iterator2.hasNext()) {
            String depends = iterator2.next();
            ValidatorAction va = resources.getValidatorAction(depends);
            if (va == null) {
                throw new NullPointerException("Depends string \"" + depends
                        + "\" was not found in validator-rules.xml.");
            }
            if (va.getJavascript() != null && va.getJavascript().length() > 0) {
                actions.add(va);
            } else {
                iterator2.remove();
            }
        }
        Collections.sort(actions, actionComparator);
        return actions;
    }

    /**
     * Creates the JavaScript methods list from the given actions.
     * 
     * @param actions
     *            A List of ValidatorAction objects.
     * @param stopOnError
     *            If true, behaves like released version of struts 1.1 and stops
     *            after first error. If false, evaluates all validations.
     * @return JavaScript methods.
     */
    protected String createMethods(List<ValidatorAction> actions,
            boolean stopOnError) {
        StringBuilder methods = new StringBuilder();
        final String methodOperator = stopOnError ? " && " : " & ";
        Iterator<ValidatorAction> iter = actions.iterator();
        while (iter.hasNext()) {
            ValidatorAction va = iter.next();
            if (methods.length() > 0) {
                methods.append(methodOperator);
            }
            methods.append(va.getMethod()).append("(form)");
        }
        return methods.toString();
    }

    /**
     * Determines if validations should stop on an error.
     * 
     * @param config
     *            The <code>ModuleConfig</code> used to lookup the stopOnError
     *            setting.
     * @return <code>true</code> if validations should stop on errors.
     */
    protected boolean stopOnError(ModuleConfig config) {
        Object stopOnErrorObj = pageContext.getAttribute(
                ValidatorPlugIn.STOP_ON_ERROR_KEY + '.' + config.getPrefix(),
                PageContext.APPLICATION_SCOPE);
        boolean stopOnError = true;
        if (stopOnErrorObj instanceof Boolean) {
            stopOnError = ((Boolean) stopOnErrorObj).booleanValue();
        }
        return stopOnError;
    }

    /**
     * Escapes quotes.
     * 
     * @param input
     *            the input value
     * @return escaped value
     */
    protected String escapeQuotes(String input) {
        if (input == null || input.indexOf("\"") == -1) {
            return input;
        }
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(input, "\"", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals("\"")) {
                buffer.append("\\");
            }
            buffer.append(token);
        }
        return buffer.toString();
    }

    /**
     * <p>
     * Backslash-escapes the following characters from the input string: &quot;,
     * &apos;, \, \r, \n.
     * </p>
     * 
     * <p>
     * This method escapes characters that will result in an invalid Javascript
     * statement within the validator Javascript.
     * </p>
     * 
     * @param str
     *            The string to escape.
     * @return The string <code>s</code> with each instance of a double quote,
     *         single quote, backslash, carriage-return, or line feed escaped
     *         with a leading backslash.
     */
    protected String escapeJavascript(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return str;
        }

        // guess at how many chars we'll be adding...
        StringBuffer out = new StringBuffer(length + 4);
        // run through the string escaping sensitive chars
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c == '"' || c == '\'' || c == '\\' || c == '\n' || c == '\r') {
                out.append('\\');
            }
            out.append(c);
        }
        return out.toString();
    }

    /**
     * Returns the opening script element and some initial javascript.
     */
    @Override
    protected String getJavascriptBegin(String methods) {
        StringBuffer sb = new StringBuffer();
        String name = jsFormName.replace('/', '_'); // remove any '/' characters
        name = jsFormName.substring(0, 1).toUpperCase()
                + jsFormName.substring(1, jsFormName.length());

        sb.append(renderStartElement());

        if (isXhtml() && "true".equalsIgnoreCase(this.cdata)) {
            sb.append("//<![CDATA[\r\n");
        }

        if (!isXhtml() && "true".equals(htmlComment)) {
            sb.append(HTML_BEGIN_COMMENT);
        }
        sb.append("\n    var bCancel = false; \n\n");

        sb.append("    function validate" + name + "(form) { \n");

        sb.append("        if (bCancel) { \n");
        sb.append("            return true; \n");
        sb.append("        } else { \n");

        // Always return true if there aren't any Javascript validation methods
        if ((methods == null) || (methods.length() == 0)) {
            sb.append("            return true; \n");
        } else {
            sb.append("            var formValidationResult; \n");
            sb.append("            formValidationResult = " + methods + "; \n");
            if (methods.indexOf("&&") >= 0) {
                sb.append("            return (formValidationResult); \n");
            } else {
                // Making Sure that Bitwise operator works:
                sb.append("            return (formValidationResult == 1); \n");
            }
        }
        sb.append("        } \n");
        sb.append("    } \n\n");

        return sb.toString();
    }

    /**
     * Returns true if this is an xhtml page.
     * 
     * @return whether this is an xhtml page
     */
    protected boolean isXhtml() {
        return TagUtils.getInstance().isXhtml(this.pageContext);
    }
}