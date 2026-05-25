package com.example.portfolio_simple_spring_mvc;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public final class Utf8ResourceBundle {
    
    private Utf8ResourceBundle() {
        // インスタンス化防止
    }

    /**
     * UTF-8対応のResourceBundleを取得
     *
     * @param baseName ベース名（例: "messages"）
     * @return ResourceBundle
     */
    public static ResourceBundle getBundle(String baseName) {
        return ResourceBundle.getBundle(baseName, Locale.getDefault(), new UTF8Control());
    }

    /**
     * UTF-8対応のResourceBundle.Control実装
     */
    private static class UTF8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(
                String baseName, 
                Locale locale, 
                String format, 
                ClassLoader loader, 
                boolean reload
        ) throws java.io.IOException {

            String resourceName = toResourceName(toBundleName(baseName, locale), "properties");

            try (var stream = loader.getResourceAsStream(resourceName)) {
                if (stream == null) return null;

                Properties props = new Properties();
                props.load(new InputStreamReader(stream, StandardCharsets.UTF_8));

                return new ResourceBundle() {
                    @Override
                    protected Object handleGetObject(String key) {
                        return props.getProperty(key);
                    }

                    @Override
                    public Enumeration<String> getKeys() {
                        return Collections.enumeration(props.stringPropertyNames());
                    }
                };
            }
        }
    }
}
