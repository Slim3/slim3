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
package org.slim3.gen.message;

/**
 * Represents the message code.
 * 
 * @author taedium
 * @since 3.0
 */
public enum MessageCode {

    /** */
    SILM3GEN0001("Failed to process annotation[{0}]. Check a detail message displayed in the Error Log view(for Eclipse) or the console(for javac)."),
    /** */
    SILM3GEN0004("Already exists. Generation Skipped. ({0}.java:0)"),
    /** */
    SILM3GEN0005("Generated. ({0}.java:0)"),
    /** */
    SILM3GEN0006("Already exists. Generation Skipped. ({0})"),
    /** */
    SILM3GEN0007("Generated. ({0})"),
    /** */
    SILM3GEN0008("The context-param 'slim3.rootPackage' is not found in web.xml."),
    /** */
    SILM3GEN0009("The property[{0}] has already been set."),
    /** */
    SILM3GEN0011("@com.google.gwt.user.client.rpc.RemoteServiceRelativePath is available only to an interface."),
    /** */
    SILM3GEN0012("The second token[{0}] of the input[{1}] is illegal. It must be \"extends\"."),
    /** */
    SILM3GEN0013("The input[{0}] consists of [{1}] tokens. The token count must be 1 or 3."),
    /** */
    SILM3GEN1001("Unknown type[{0}] is found."),
    /** */
    SILM3GEN1002("The class[{0}] is not supported."),
    /** */
    SILM3GEN1004("The class[{0}] must be parametalized."),
    /** */
    SILM3GEN1005("Specify @Attribute(lob = true) or @Attribute(persistent = false)."),
    /** */
    SILM3GEN1007("The type annotated with @Attribute(primaryKey = true) must be com.google.appengine.api.datastore.Key."),
    /** */
    SILM3GEN1008("The type annotated with @Attribute(version = true) must be primitive long or java.lang.Long."),
    /** */
    SILM3GEN1009("The type annotated with @Attribute(lob = true) is not supported."),
    /** */
    SILM3GEN1011("The getter method is not found."),
    /** */
    SILM3GEN1012("The setter method is not found."),
    /** */
    SILM3GEN1013("Multiple primary keys are not allowed."),
    /** */
    SILM3GEN1014("Multiple version property are not allowed."),
    /** */
    SILM3GEN1015("You should define @Attribute(primaryKey = true) to a primary key field."),
    /** */
    SILM3GEN1016("The type parameter[{0}] is not supported."),
    /** */
    SILM3GEN1017("The modifier must be public."),
    /** */
    SILM3GEN1018("The public default constructor is not found."),
    /** */
    SILM3GEN1019("The model class must be top level class."),
    /** */
    SILM3GEN1020("The type parameter is not supported."),
    /** */
    SILM3GEN1021("The element[{0}] and the element[{1}] can not be defined at the same time."),
    /** */
    SILM3GEN1022("The kind element is not supported for a sub model, because the kind of sub model is identical to the super model."),
    /** */
    SILM3GEN1024("The getter method is not found. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1025("The setter method is not found. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1026("Specify @Attribute(lob = true) or @Attribute(persistent = false). (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1027("The element[{0}] and the element[{1}] can not be defined at the same time. (the property[{2}] of the class[{3}])."),
    /** */
    SILM3GEN1028("The type annotated with @Attribute(lob = true) is not supported. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1029("The type annotated with @Attribute(primaryKey = true) must be com.google.appengine.api.datastore.Key. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1030("The type annotated with @Attribute(version = true) must be primitive long or java.lang.Long. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1031("The actual type argument of ModelRef must be a model."),
    /** */
    SILM3GEN1032("The actual type argument of ModelRef must be a model. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1033("The actual type argument of ModelRef is not specified."),
    /** */
    SILM3GEN1034("The actual type argument of ModelRef is not specified. (the property[{0}] of the class[{1}])."),
    /** */
    SILM3GEN1035("Specify @Attribute(persistent = false) for the org.slim3.datastore.InverseModelRef property."),
    /** */
    SILM3GEN1036("Specify @Attribute(persistent = false) for the org.slim3.datastore.InverseModelRef property. (the property[{0}] of the class[{1}])"),
    /** */
    SILM3GEN1037("Specify @Attribute(persistent = false) for the org.slim3.datastore.InverseModelListRef property."),
    /** */
    SILM3GEN1038("Specify @Attribute(persistent = false) for the org.slim3.datastore.InverseModelListRef property. (the property[{0}] of the class[{1}])"),
    /** */
    SILM3GEN1039("The setter method for the property[{0}] is not allowed. Define the property field as follows:\n{1}\nThe ''xxx'' means the mapped property name."),
    /** */
    SILM3GEN1040("The setter method for the property[{0}] of the class[{1}] is not allowed. Define the property as follows:\n{2}\nThe ''xxx'' means the mapped property name."),
    /** */
    SILM3GEN1041("The setter method is not allowed. Define the property as follows:\n{0}"),
    /** */
    SILM3GEN1042("The setter method for the property[{0}] of the class[{1}] is not allowed. Define the property as follows:\n{2}"),
    /** */
    SILM3GEN1043("Specify @Attribute(persistent = false) for the org.slim3.datastore.ModelRef property."),
    /** */
    SILM3GEN1044("Specify @Attribute(persistent = false) for the org.slim3.datastore.ModelRef property. (the property[{0}] of the class[{1}])");

    /** the message */
    public final String message;

    private MessageCode(String message) {
        this.message = message;
    }
}
