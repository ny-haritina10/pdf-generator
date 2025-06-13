package mg.bici.htmltojrxml.config;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Manages font mappings from CSS to JasperReports fonts.
 */
public class FontMappingConfig {
    
    private final Map<String, String> fontMappings;

    public FontMappingConfig() {
        fontMappings = new HashMap<>();
        loadFontMappings();
    }

    /**
     * Loads font mappings from properties file.
     */
    public void loadFontMappings() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config/font-mappings.properties"));
            for (String cssFont : properties.stringPropertyNames()) {
                fontMappings.put(cssFont.toLowerCase(), properties.getProperty(cssFont));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font-mappings.properties", e);
        }
    }

    /**
     * Maps a CSS font to a JasperReports font.
     */
    public String mapCssFontToJasper(String cssFont) {
        if (cssFont == null) {
            return "Arial"; // Default fallback
        }
        return fontMappings.getOrDefault(cssFont.toLowerCase(), "Arial");
    }

    /**
     * Gets available fonts.
     */
    public Map<String, String> getAvailableFonts() {
        return Collections.unmodifiableMap(fontMappings);
    }
}