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
package org.slim3.gen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Represents product information.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class ProductInfo {

    /** the path of the product properties file */
    protected static final String PATH = "/META-INF/slim3-gen.properties";

    /** the product name */
    protected static String name = "slim3-gen";

    /** the product version */
    protected static String version;

    static {
        Properties props = loadProperties();
        version = props.getProperty("version");
    }

    /**
     * Loads the product properties file.
     * 
     * @return the product properties.
     */
    protected static Properties loadProperties() {
        InputStream is = ProductInfo.class.getResourceAsStream(PATH);
        Properties props = new Properties();
        if (is != null) {
            try {
                props.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

    /**
     * Returns the product name.
     * 
     * @return the product name.
     */
    public static String getName() {
        return name;
    }

    /**
     * Returns the product version.
     * 
     * @return the product version.
     */
    public static String getVersion() {
        return version;
    }

}
