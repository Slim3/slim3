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
package org.slim3.gen.message;

/**
 * Represents the message code.
 * 
 * @author taedium
 * @since 1.0.0
 */
public enum MessageCode {

    /** */
    SLIM3GEN0001("Failed to process annotation[{0}]. Check a detail message displayed in the Error Log view(for Eclipse) or the console(for javac)."),
    /** */
    SLIM3GEN0002("Resource[{0}] not found."),
    /** */
    SLIM3GEN0003("slim3-gen-xxx.jar files are duplicate in the classpath."),
    /** */
    SLIM3GEN0004("Already exists. Generation Skipped. ({0}.java:0)"),
    /** */
    SLIM3GEN0005("Generated. ({0}.java:0)"),
    /** */
    SLIM3GEN0006("Already exists. Generation Skipped. ({0})"),
    /** */
    SLIM3GEN0007("Generated. ({0})"),
    /** */
    SLIM3GEN0008("The context-param 'slim3.rootPackage' is not found in web.xml."),
    /** */
    SLIM3GEN0009("The property[{0}] has already been set."),
    /** */
    SLIM3GEN0011("@com.google.gwt.user.client.rpc.RemoteServiceRelativePath is available only to an interface."),
    /** */
    SLIM3GEN0012("The second token[{0}] of the input[{1}] is illegal. It must be \"extends\"."),
    /** */
    SLIM3GEN0013("The input[{0}] consists of [{1}] tokens. The token count must be 1 or 3."),
    /** */
    SLIM3GEN1001("Unknown type[{0}] is found."),
    /** */
    SLIM3GEN1002("The class[{0}] is not supported."),
    /** */
    SLIM3GEN1004("The class[{0}] must be parametalized."),
    /** */
    SLIM3GEN1005("Specify @Attribute(lob = true) or @Attribute(persistent = false)."),
    /** */
    SLIM3GEN1007("The type annotated with @Attribute(primaryKey = true) must be com.google.appengine.api.datastore.Key."),
    /** */
    SLIM3GEN1008("The type annotated with @Attribute(version = true) must be primitive long or java.lang.Long."),
    /** */
    SLIM3GEN1009("The type annotated with @Attribute(lob = true) is not supported."),
    /** */
    SLIM3GEN1011("The getter method[{0}] is not found."),
    /** */
    SLIM3GEN1012("The setter method[{0}] is not found."),
    /** */
    SLIM3GEN1013("Multiple primary keys are not allowed."),
    /** */
    SLIM3GEN1014("Multiple version property are not allowed."),
    /** */
    SLIM3GEN1015("You should define @Attribute(primaryKey = true) to a primary key field."),
    /** */
    SLIM3GEN1016("The type parameter[{0}] is not supported."),
    /** */
    SLIM3GEN1017("The modifier must be public."),
    /** */
    SLIM3GEN1018("The public default constructor is not found."),
    /** */
    SLIM3GEN1019("The model class must be top level class."),
    /** */
    SLIM3GEN1020("The type parameter is not supported."),
    /** */
    SLIM3GEN1021("The element[{0}] and the element[{1}] can not be defined at the same time."),
    /** */
    SLIM3GEN1022("The kind element is not supported for a sub model, because the kind of sub model is identical to the super model."),
    /** */
    SLIM3GEN1023("The schemaVersionName element must not be empty."),
    /** */
    SLIM3GEN1024("The getter method[{0}] is not found. (the property[{1}] of the class[{2}])."),
    /** */
    SLIM3GEN1025("The setter method[{0}] is not found. (the property[{1}] of the class[{2}])."),
    /** */
    SLIM3GEN1026("Specify @Attribute(lob = true) or @Attribute(persistent = false). (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1027("The element[{0}] and the element[{1}] can not be defined at the same time. (the property[{2}] of the class[{3}])."),
    /** */
    SLIM3GEN1028("The type annotated with @Attribute(lob = true) is not supported. (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1029("The type annotated with @Attribute(primaryKey = true) must be com.google.appengine.api.datastore.Key. (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1030("The type annotated with @Attribute(version = true) must be primitive long or java.lang.Long. (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1031("The actual type argument of ModelRef must be a model."),
    /** */
    SLIM3GEN1032("The actual type argument of ModelRef must be a model. (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1033("The actual type argument of ModelRef is not specified."),
    /** */
    SLIM3GEN1034("The actual type argument of ModelRef is not specified. (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1035("Specify @Attribute(persistent = false). ModelRef has a key internally, so you need to persist it. But this property is read only, so you do not need to persist it."),
    /** */
    SLIM3GEN1036("Specify @Attribute(persistent = false). ModelRef has a key internally, so you need to persist it. But this property is read only, so you do not need to persist it. (the property[{0}] of the class[{1}])"),
    /** */
    SLIM3GEN1039("The setter method for the field[{0}] is not allowed. Define the field as follows:\n{1}\nThe \"xxx\" means the mapped ModelRef<{2}> property name in the class[{3}]."),
    /** */
    SLIM3GEN1040("The setter method for the field[{0}] of the class[{1}] is not allowed. Define the field as follows:\n{2}\nThe \"xxx\" means the mapped ModelRef<{3}> property name in the class[{4}]."),
    /** */
    SLIM3GEN1041("The setter method for the field[{0}] is not allowed. Define the field as follows:\n{1}"),
    /** */
    SLIM3GEN1042("The setter method for the field[{0}] of the class[{1}] is not allowed. Define the field as follows:\n{2}"),
    /** */
    SLIM3GEN1043("The property[{0}] is duplicated."),
    /** */
    SLIM3GEN1044("The property is duplicated. (the property[{0}] of the class[{1}])"),
    /** */
    SLIM3GEN1045("Specify @Attribute(unindexed = true) instead of @Attribute(lob = true)."),
    /** */
    SLIM3GEN1046("Specify @Attribute(unindexed = true) instead of @Attribute(lob = true). (the property[{0}] of the class[{1}])."),
    /** */
    SLIM3GEN1047("The property[{0}] of entity is duplicated."),
    /** */
    SLIM3GEN1048("The property[{0}] of entity is duplicated. (the property[{1}] of the class[{2}])"),
    /** */
    SLIM3GEN1049("The classHierarchyListName element must not be empty."),
    /** */
    SLIM3GEN1050("The listener[{0}] does not have a default constructor."),
    /** */
    SLIM3GEN1051("The generics parameter type of the listener[{0}] does not match the type[{1}] of the field."),
    /** */
    SLIM3GEN1052("The listener must not be an interface."),
    /** */
    SLIM3GEN1053("The type annotated with @Attribute(cipher = true) must be com.google.appengine.api.datastore.Text or java.lang.String."),
    /** */
    SLIM3GEN1054("The coder[{0}] of @Json does not have a default constructor."),
    /** */
    SLIM3GEN1055("The coder of @Json must not be an interface.");

    /** the message */
    public final String message;

    private MessageCode(String message) {
        this.message = message;
    }
}
