package tests.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties PROPERTIES = new Properties();
    static{
        try(FileInputStream input = new FileInputStream("src/test/resources/config.properties")) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

}
