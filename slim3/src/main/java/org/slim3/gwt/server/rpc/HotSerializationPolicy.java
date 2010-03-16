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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.TypeNameObfuscator;

/**
 * The serialization policy for HOT reloading.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotSerializationPolicy extends SerializationPolicy implements
        TypeNameObfuscator {

    private final Map<String, Set<String>> clientFields;
    private final Map<String, Boolean> deserializationWhitelist;
    private final Map<String, Boolean> serializationWhitelist;
    private final Map<String, String> typeIds;
    private final Map<String, String> typeIdsToClasses =
        new HashMap<String, String>();

    /**
     * Field serializable types are primitives and types on the specified
     * whitelist.
     */
    private static boolean isFieldSerializable(Class<?> clazz,
            Map<String, Boolean> whitelist) {
        if (clazz.isPrimitive()) {
            return true;
        }
        return whitelist.containsKey(clazz.getName());
    }

    /**
     * Instantiable types are primitives and types on the specified whitelist
     * which can be instantiated.
     */
    private static boolean isInstantiable(Class<?> clazz,
            Map<String, Boolean> whitelist) {
        if (clazz.isPrimitive()) {
            return true;
        }
        Boolean instantiable = whitelist.get(clazz.getName());
        return (instantiable != null && instantiable);
    }

    /**
     * Constructs a {@link SerializationPolicy} from several {@link Map}s.
     * 
     * @param serializationWhitelist
     *            the serialization white list
     * @param deserializationWhitelist
     *            the deserialization white list
     * @param obfuscatedTypeIds
     *            the obfuscated type identifiers
     * @param clientFields
     *            the client fields
     */
    public HotSerializationPolicy(Map<String, Boolean> serializationWhitelist,
            Map<String, Boolean> deserializationWhitelist,
            Map<String, String> obfuscatedTypeIds,
            Map<String, Set<String>> clientFields) {
        if (serializationWhitelist == null || deserializationWhitelist == null) {
            throw new NullPointerException("whitelist");
        }
        this.serializationWhitelist = serializationWhitelist;
        this.deserializationWhitelist = deserializationWhitelist;
        this.typeIds = obfuscatedTypeIds;
        this.clientFields = clientFields;
        for (String key : obfuscatedTypeIds.keySet()) {
            String value = obfuscatedTypeIds.get(key);
            assert key != null : "null key";
            assert value != null : "null value for " + key;
            assert !typeIdsToClasses.containsKey(value) : "Duplicate type id "
                + value;
            typeIdsToClasses.put(value, key);
        }
    }

    public final String getClassNameForTypeId(String id)
            throws SerializationException {
        return typeIdsToClasses.get(id);
    }

    @Override
    public Set<String> getClientFieldNamesForEnhancedClass(Class<?> clazz) {
        if (clientFields == null) {
            return null;
        }
        Set<String> fieldNames = clientFields.get(clazz.getName());
        return fieldNames == null ? null : Collections
            .unmodifiableSet(fieldNames);
    }

    public final String getTypeIdForClass(Class<?> clazz)
            throws SerializationException {
        return typeIds.get(clazz.getName());
    }

    @Override
    public boolean shouldDeserializeFields(Class<?> clazz) {
        return isFieldSerializable(clazz, deserializationWhitelist);
    }

    @Override
    public boolean shouldSerializeFields(Class<?> clazz) {
        return isFieldSerializable(clazz, serializationWhitelist);
    }

    @Override
    public void validateDeserialize(Class<?> clazz)
            throws SerializationException {
        if (!isInstantiable(clazz, deserializationWhitelist)) {
            throw new SerializationException(
                "Type '"
                    + clazz.getName()
                    + "' was not included in the set of types which can be deserialized by this SerializationPolicy or its Class object could not be loaded. For security purposes, this type will not be deserialized.");
        }
    }

    @Override
    public void validateSerialize(Class<?> clazz) throws SerializationException {
        if (!isInstantiable(clazz, serializationWhitelist)) {
            throw new SerializationException(
                "Type '"
                    + clazz.getName()
                    + "' was not included in the set of types which can be serialized by this SerializationPolicy or its Class object could not be loaded. For security purposes, this type will not be serialized.");
        }
    }
}