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
package org.slim3.struts.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to validate if the int value is in a range.
 * 
 * @author higa
 * @since 3.0
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Validator("intRange")
public @interface IntRange {

    /**
     * The minimum value.
     * 
     */
    int min();

    /**
     * The maximum value.
     * 
     */
    int max();

    /**
     * The message.
     * 
     */
    Msg msg() default @Msg(key = "errors.range");

    /**
     * The first argument.
     * 
     */
    Arg arg0() default @Arg(key = "");

    /**
     * The second argument.
     * 
     */
    Arg arg1() default @Arg(key = "${var:min}", resource = false);

    /**
     * The third argument.
     * 
     */
    Arg arg2() default @Arg(key = "${var:max}", resource = false);

    /**
     * Specifies the execute method names which are validation targets.
     * 
     */
    String[] targets() default {};
}