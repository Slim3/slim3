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
package org.slim3.commons.exception;

import org.slim3.commons.message.MessageBuilder;

/**
 * A base runtime exception.
 * 
 * @author higa
 * @since 3.0
 */
public class SRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String messageCode;

    private Object[] args;

    /**
     * Converts the cause to a message.
     * 
     * @param cause
     *            the cause.
     * @return a message.
     */
    public static String convertCauseMessage(Throwable cause) {
        String message = cause.getMessage();
        if (message == null || message.length() == 0) {
            return "no reason";
        }
        char chars[] = message.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        message = new String(chars);
        if (message.endsWith(".")) {
            message = message.substring(0, message.length() - 1);
        }
        return message;
    }

    /**
     * Constructor.
     * 
     * @param messageCode
     *            the message code
     * @param args
     *            the arguments
     */
    public SRuntimeException(String messageCode, Object... args) {
        this(null, messageCode, args);
    }

    /**
     * Constructor.
     * 
     * @param cause
     *            the cause
     * @param messageCode
     *            the message code
     * @param args
     *            the arguments
     */
    public SRuntimeException(Throwable cause, String messageCode,
            Object... args) {

        super(MessageBuilder.getMessage(messageCode, args), cause);
        this.messageCode = messageCode;
        this.args = args;
    }

    /**
     * Returns the message code.
     * 
     * @return the message code
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Returns the arguments.
     * 
     * @return the arguments
     */
    public Object[] getArgs() {
        return args;
    }
}