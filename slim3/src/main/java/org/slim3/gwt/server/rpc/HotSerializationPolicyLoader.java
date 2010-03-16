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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.TypeNameObfuscator;

/**
 * A loader for {@link HotSerializationPolicy}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotSerializationPolicyLoader {

    /**
     * Keyword for listing the serializable fields of an enchanced class that
     * are visible to client code.
     */
    public static final String CLIENT_FIELDS_KEYWORD = "@ClientFields";

    /**
     * Default encoding for serialization policy files.
     */
    public static final String SERIALIZATION_POLICY_FILE_ENCODING = "UTF-8";

    private static final String FORMAT_ERROR_MESSAGE =
        "Expected: className, "
            + "[true | false], [true | false], [true | false], [true | false], typeId, signature";

    /**
     * Returns the serialization policy file name from the serialization policy
     * strong name.
     * 
     * @param serializationPolicyStrongName
     *            the serialization policy strong name
     * @return the serialization policy file name from the serialization policy
     *         strong name
     */
    public static String getSerializationPolicyFileName(
            String serializationPolicyStrongName) {
        return serializationPolicyStrongName + ".gwt.rpc";
    }

    /**
     * Loads a SerializationPolicy from an input stream and optionally record
     * any {@link ClassNotFoundException}s.
     * 
     * @param inputStream
     *            stream to load the SerializationPolicy from.
     * @param classNotFoundExceptions
     *            if not <code>null</code>, all of the
     *            {@link ClassNotFoundException}s thrown while loading this
     *            serialization policy will be added to this list
     * @return a {@link SerializationPolicy} loaded from the input stream.
     * 
     * @throws IOException
     *             if an error occurs while reading the stream
     * @throws ParseException
     *             if the input stream is not properly formatted
     */
    public static SerializationPolicy loadFromStream(InputStream inputStream,
            List<ClassNotFoundException> classNotFoundExceptions)
            throws IOException, ParseException {

        if (inputStream == null) {
            throw new NullPointerException("inputStream");
        }

        Map<String, Boolean> whitelistSer = new HashMap<String, Boolean>();
        Map<String, Boolean> whitelistDeser = new HashMap<String, Boolean>();
        Map<String, String> typeIds = new HashMap<String, String>();
        Map<String, Set<String>> clientFields =
            new HashMap<String, Set<String>>();

        ClassLoader contextClassLoader =
            Thread.currentThread().getContextClassLoader();

        InputStreamReader isr =
            new InputStreamReader(
                inputStream,
                SERIALIZATION_POLICY_FILE_ENCODING);
        BufferedReader br = new BufferedReader(isr);

        String line = br.readLine();
        int lineNum = 1;
        while (line != null) {
            line = line.trim();
            if (line.length() > 0) {
                String[] components = line.split(",");

                if (components[0].equals(CLIENT_FIELDS_KEYWORD)) {
                    /*
                     * Lines starting with '@ClientFields' list potentially
                     * serializable fields known to client code for classes that
                     * may be enhanced with additional fields on the server. If
                     * additional server fields are found, they will be
                     * serizalized separately from the normal RPC process and
                     * transmitted to the client as an opaque blob of data
                     * stored in a WeakMapping associated with the object
                     * instance.
                     */
                    String binaryTypeName = components[1].trim();
                    Class<?> clazz;
                    try {
                        clazz =
                            Class.forName(
                                binaryTypeName,
                                false,
                                contextClassLoader);
                        HashSet<String> fieldNames = new HashSet<String>();
                        for (int i = 2; i < components.length; i++) {
                            fieldNames.add(components[i]);
                        }
                        clientFields.put(clazz.getName(), fieldNames);
                    } catch (ClassNotFoundException ex) {
                        // Ignore the error, but add it to the list of errors if
                        // one was
                        // provided.
                        if (classNotFoundExceptions != null) {
                            classNotFoundExceptions.add(ex);
                        }
                    }
                } else {
                    if (components.length != 2 && components.length != 7) {
                        throw new ParseException(FORMAT_ERROR_MESSAGE, lineNum);
                    }

                    for (int i = 0; i < components.length; i++) {
                        components[i] = components[i].trim();
                        if (components[i].length() == 0) {
                            throw new ParseException(
                                FORMAT_ERROR_MESSAGE,
                                lineNum);
                        }
                    }

                    String binaryTypeName = components[0].trim();
                    boolean fieldSer;
                    boolean instantSer;
                    boolean fieldDeser;
                    boolean instantDeser;
                    String typeId;

                    if (components.length == 2) {
                        fieldSer = fieldDeser = true;
                        instantSer =
                            instantDeser = Boolean.valueOf(components[1]);
                        typeId = binaryTypeName;
                    } else {
                        int idx = 1;
                        // TODO: Validate the instantiable string better.
                        fieldSer = Boolean.valueOf(components[idx++]);
                        instantSer = Boolean.valueOf(components[idx++]);
                        fieldDeser = Boolean.valueOf(components[idx++]);
                        instantDeser = Boolean.valueOf(components[idx++]);
                        typeId = components[idx++];

                        if (!fieldSer
                            && !fieldDeser
                            && !TypeNameObfuscator.SERVICE_INTERFACE_ID
                                .equals(typeId)) {
                            throw new ParseException(
                                "Type "
                                    + binaryTypeName
                                    + " is neither field serializable, field deserializable "
                                    + "nor the service interface",
                                lineNum);
                        }
                    }

                    try {
                        Class<?> clazz =
                            Class.forName(
                                binaryTypeName,
                                false,
                                contextClassLoader);
                        if (fieldSer) {
                            whitelistSer.put(clazz.getName(), instantSer);
                        }
                        if (fieldDeser) {
                            whitelistDeser.put(clazz.getName(), instantDeser);
                        }
                        typeIds.put(clazz.getName(), typeId);
                    } catch (ClassNotFoundException ex) {
                        // Ignore the error, but add it to the list of errors if
                        // one was
                        // provided.
                        if (classNotFoundExceptions != null) {
                            classNotFoundExceptions.add(ex);
                        }
                    }
                }
            }
            line = br.readLine();
            lineNum++;
        }
        return new HotSerializationPolicy(
            whitelistSer,
            whitelistDeser,
            typeIds,
            clientFields);
    }

    private HotSerializationPolicyLoader() {
    }
}
