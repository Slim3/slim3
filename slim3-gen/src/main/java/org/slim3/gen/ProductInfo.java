package org.slim3.gen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProductInfo {

    protected static final String PATH = "META-INF/product.properties";

    protected static String name = "Slim3-Gen";

    protected static String version;

    static {
        Properties props = loadProperties();
        version = props.getProperty("version");
    }

    protected static Properties loadProperties() {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(PATH);
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

    public static String getName() {
        return name;
    }

    public static String getVersion() {
        return version;
    }

}
