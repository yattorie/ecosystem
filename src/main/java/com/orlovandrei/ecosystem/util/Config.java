package com.orlovandrei.ecosystem.util;

import com.orlovandrei.ecosystem.annotation.Utility;
import com.orlovandrei.ecosystem.exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Utility
public class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new ConfigurationException(Messages.RESOURCE_DIRECTORY_IS_NOT_SET.getMessage());
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new ConfigurationException(Messages.UNABLE_TO_FIND_PROPERTIES.getMessage(), ex);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getDirectory() {
        String directory = getProperty("directory.path");
        if (directory == null) {
            throw new ConfigurationException(Messages.RESOURCE_DIRECTORY_IS_NOT_SET.getMessage());
        }
        return directory;
    }
}
