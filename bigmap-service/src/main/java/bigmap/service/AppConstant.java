package bigmap.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConstant {

    private static Properties prop = null;

    public static String get(String key) {
        try {
            if (prop == null) {
                InputStream in = ClassLoader.getSystemResourceAsStream("application.properties");
                prop = new Properties();
                prop.load(in);
            }

            return prop.getProperty(key);
        } catch (IOException e) {

        }
        return null;

    }

}
