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
package org.slim3.gen.processor;

import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.util.SourcePosition;

/**
 * Thrown when annotation processing is failed.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public class AptException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /** the message code */
    protected final MessageCode messageCode;

    /** the source position */
    protected final SourcePosition sourcePosition;

    /**
     * Creates a new {@link AptException}.
     * 
     * @param messageCode
     *            the message code
     * @param env
     *            the environment
     * @param sourcePosition
     *            the send target
     * @param args
     *            arguments
     */
    public AptException(MessageCode messageCode,
            AnnotationProcessorEnvironment env, SourcePosition sourcePosition,
            Object... args) {
        this(messageCode, env, sourcePosition, null, args);
    }

    /**
     * Creates a new {@link AptException}.
     * 
     * @param messageCode
     *            the message code
     * @param env
     *            the environment
     * @param sourcePosition
     *            the send target
     * @param cause
     *            the cause
     * @param args
     *            arguments
     */
    public AptException(MessageCode messageCode,
            AnnotationProcessorEnvironment env, SourcePosition sourcePosition,
            Throwable cause, Object... args) {
        super(MessageFormatter.getMessage(messageCode, args), cause);
        this.env = env;
        this.messageCode = messageCode;
        this.sourcePosition = sourcePosition;
    }

    /**
     * Sends error message.
     */
    public void sendError() {
        Logger.error(env, sourcePosition, getMessage());
    }
}
