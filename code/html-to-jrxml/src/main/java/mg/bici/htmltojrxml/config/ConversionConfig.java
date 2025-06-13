package mg.bici.htmltojrxml.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Manages conversion configuration settings loaded from properties file.
 */
public class ConversionConfig {
    
    private final Properties properties;

    public ConversionConfig() {
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config/conversion-config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load conversion-config.properties", e);
        }
    }

    /**
     * Gets page dimensions in points (width, height).
     */
    public int[] getPageDimensions() {
        int width = Integer.parseInt(properties.getProperty("page.width", "595"));
        int height = Integer.parseInt(properties.getProperty("page.height", "842"));
        return new int[]{width, height};
    }

    /**
     * Gets default font settings.
     */
    public String getDefaultFont() {
        return properties.getProperty("font.default.family", "Arial");
    }

    /**
     * Gets page margins in points (top, right, bottom, left).
     */
    public int[] getMargins() {
        int top = Integer.parseInt(properties.getProperty("page.margin.top", "36"));
        int right = Integer.parseInt(properties.getProperty("page.margin.right", "36"));
        int bottom = Integer.parseInt(properties.getProperty("page.margin.bottom", "36"));
        int left = Integer.parseInt(properties.getProperty("page.margin.left", "36"));
        return new int[]{top, right, bottom, left};
    }

    /**
     * Loads configuration (called during initialization).
     */
    public void loadConfiguration() {
        // Already loaded in constructor; can be extended for reloading if needed
    }
}